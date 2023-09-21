package scs.repository;

import java.io.IOException; 
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map; 
import java.util.Set;

import scs.pojo.CacheAreaBean;
import scs.pojo.FuncInstBean;
import scs.pojo.FuncMetadataBean;
import scs.pojo.InstanceBean;
import scs.pojo.RequestBean;
import scs.pojo.ServerBean;
import scs.pojo.StatisticsBean;
import scs.pojo.TwoTuple;
import scs.util.rmi.AgentInterface;
import scs.util.rmi.RmiService;
import scs.util.tool.FileOperation;

public class Repository {

	private static ServerBean serverBean;
	public static AgentInterface cacheManager;
	private static int keep_alive_time=30*4*1000;
	private final static int rps_monitor_time_window=5*60*1000;
	private final static int dispatch_latency=5;

	private final static String outputFilePath="D:\\cacheResearch\\output\\";  //window
	//private final static String outputFilePath="/home/tank/1_yanan/cacheResearch/output/";  //linux

	private final static long STATUS_UPDATE_INTERVAL_TIME_MS=15*1000; //15秒检测一次 释放没用的实例

	private static Map<String, FuncMetadataBean> funcMetadataMap=new HashMap<String, FuncMetadataBean>();
	private static Map<String,TwoTuple<Integer,Boolean>> hotspotFuncMap=new HashMap<String,TwoTuple<Integer,Boolean>>(); //TwoTuple的first是热点，second是热点标记
	private static int receivedRequestCount=0; //只是用来统计的,没有实际意义
	private static int receivedColdstartCount=0; //cold+warm+drop为实际该节点处理的
	private static int receivedWarmstartCount=0;
	private static int receivedEvictionCount=0;
	private static int receivedReleaseCount=0;
	private static int receivedDropCount=0;
	private static int redirectedCount=0;
	private static int receivedInstanceCount=0;

	private final static String cacheManagerIp="localhost";
	private final static int cacheManagerPort=33330;

	private static long lastArrivalTime=0;

	public static void init(String agentName, int memorySize, String ip, int port, int keepaliveTime){
		serverBean=new ServerBean(agentName, ip, port, memorySize, memorySize, new HashMap<String, FuncInstBean>(), 0);
		keep_alive_time=keepaliveTime;
		//System.out.println("Repository.init(): init "+serverBean.toString());
		//System.out.println("Repository.init(): init outputFilePath="+outputFilePath);
		//System.out.println("Repository.init(): init keep_alive_time="+keep_alive_time+"ms, rps_monitor_time_window="+rps_monitor_time_window+"ms, STATUS_UPDATE_INTERVAL_TIME_MS="+STATUS_UPDATE_INTERVAL_TIME_MS+"ms");
		//System.out.println("Repository.init(): init dispatch_latency="+dispatch_latency+"ms");

		RmiService.getInstance().service(ip, port);//start the RMI service
		cacheManager=RmiService.getInstance().setupCacheManagerRmiConnection(cacheManagerIp,cacheManagerPort,"first");
	}

	public static int receiveRequest(RequestBean request){
		receivedRequestCount++; 
		lastArrivalTime=request.getArrivalTime();

		//System.out.println("Repository.reciveRequest(): "+request.toString());
		registerFunc(request.getFuncMetadata());
		checkReleaseInstance(request.getArrivalTime());

		int latency=executeRequest(request);
		return latency+request.getExtraLatency();
	}

	private static void registerFunc(FuncMetadataBean metadataBean){
		if(!funcMetadataMap.containsKey(metadataBean.getFuncName())){
			funcMetadataMap.put(metadataBean.getFuncName(), metadataBean);
			serverBean.getFuncInstMap().put(metadataBean.getFuncName(), new FuncInstBean(new ArrayList<InstanceBean>(), 0, new LinkedList<Long>())); // init one function
			//System.out.println("Repository.registerFunc(): register function "+metadataBean.getFuncName());
		}else{
			//System.out.println("Repository.registerFunc(): no need to register function "+metadataBean.getFuncName());
		}
	}

	private static long lastUpdateTime=0;

	/**
	 * 浪费资源是可耻的，我们的设计中应该用尽可能少的资源去更有效得缓存实例
	 * 因此要做到agent里的动态缩减机制
	 * @param arrivalTime
	 */
	private static void checkReleaseInstance(long arrivalTime){

		if(arrivalTime-lastUpdateTime>STATUS_UPDATE_INTERVAL_TIME_MS){
			//System.out.println("Repository.checkReleaseInstance(): check ------------------------------------------------------"+arrivalTime);
			/**
			 * 定时检查器1：检查keepalive到期的函数实例并释放
			 */
			Set<String> keySet=serverBean.getFuncInstMap().keySet();
			InstanceBean temp=null;
			for(String funcName:keySet){
				ArrayList<InstanceBean> instanceList=serverBean.getFuncInstMap().get(funcName).getInstanceList();
				if(instanceList.size()>0){
					for(Iterator<InstanceBean> item=instanceList.iterator();item.hasNext();){
						temp=item.next();
						if((temp.getExpellable()==true)&&(temp.getLastActiveTimeEnd()>0)&&((arrivalTime-temp.getLastActiveTimeEnd())>keep_alive_time)){
							item.remove(); //删除keepalive过期的实例
							//System.out.println("Repository.checkReleaseInstance(): release temp instance"+temp.getFuncName()+" before memory="+serverBean.getAvailMemorySize()+" after that memory="+(serverBean.getAvailMemorySize()+funcMetadataMap.get(temp.getFuncName()).getMemoryConsume()));
							serverBean.setAvailMemorySize(serverBean.getAvailMemorySize()+funcMetadataMap.get(temp.getFuncName()).getMemoryConsume());
							receivedReleaseCount++;
							{
								float memoryTimeCost=(lastArrivalTime-temp.getCreateTime())/1000.0f*funcMetadataMap.get(temp.getFuncName()).getMemoryConsume();
								serverBean.getFuncInstMap().get(funcName).addMemoryTimespanCost(memoryTimeCost);
								serverBean.getFuncInstMap().get(funcName).addReleaseCount();
							}
						}
					}
				}
			}

			/**
			 * 定时检查器2：检查函数的到达率与当前的实例，进行缩容
			 */
			for(String funcName:keySet){
				float avgRps=serverBean.getFuncInstMap().get(funcName).getReqArrivalRate(rps_monitor_time_window);
				int execTime=funcMetadataMap.get(funcName).getExecutionTime();
				int instanceExpectedCount=(int) Math.ceil(1.2f*avgRps/(1000.0f/execTime));
				ArrayList<InstanceBean> instanceList=serverBean.getFuncInstMap().get(funcName).getInstanceList();
				if(instanceList.size()>instanceExpectedCount){
					//System.out.println("Repository.checkReleaseInstance(): func="+funcName+" avgRps="+avgRps+" execTime="+execTime+" instanceExpectedCount="+instanceExpectedCount);
					//System.out.println("Repository.checkReleaseInstance(): func="+funcName+" instanceList.size()="+instanceList.size()+" instanceExpectedCount"+instanceExpectedCount);
					for(Iterator<InstanceBean> item=instanceList.iterator();item.hasNext();){
						temp=item.next();
						if(temp.getExpellable()==false&&temp.getLastActiveTimeEnd()<arrivalTime){
							//if(temp.getExpellable()==false){
							item.remove(); //删除keepalive过期的实例
							//System.out.println("Repository.checkReleaseInstance(): release cached instance"+temp.getFuncName()+" before memory="+serverBean.getAvailMemorySize()+" after that memory="+(serverBean.getAvailMemorySize()+funcMetadataMap.get(temp.getFuncName()).getMemoryConsume()));
							serverBean.setAvailMemorySize(serverBean.getAvailMemorySize()+funcMetadataMap.get(temp.getFuncName()).getMemoryConsume());
							receivedReleaseCount++;

							{
								float memoryTimeCost=(lastArrivalTime-temp.getCreateTime())/1000.0f*funcMetadataMap.get(temp.getFuncName()).getMemoryConsume();
								serverBean.getFuncInstMap().get(funcName).addMemoryTimespanCost(memoryTimeCost);
								serverBean.getFuncInstMap().get(funcName).addReleaseCount();
							}

							if(instanceList.size()<=instanceExpectedCount){
								//System.out.println("Repository.checkReleaseInstance(): instanceList.size()="+instanceList.size()+"<=instanceExpectedCount"+instanceExpectedCount+" break");
								break;
							}
						}
					}
				}
			}

			//show();
			lastUpdateTime=arrivalTime;
		}

	}

	/*private static int[] show1(){
		int tempAreaInstCount=0;
		int tempAreaMem=0;
		int consistAreaInstCount=0;
		int consistAreaMem=0;
		int deployedFuncCount=0;
		int hotspotFuncCount=0;
		int nonhotspotFuncCount=0;
		Set<String> keySet=serverBean.getFuncInstMap().keySet();
		for(String funcName:keySet){

			ArrayList<InstanceBean> list=serverBean.getFuncInstMap().get(funcName).getInstanceList();
			if(list.size()>0){
				deployedFuncCount++;
				if(hotspotFuncMap.containsKey(funcName)&&hotspotFuncMap.get(funcName).second==true){
					hotspotFuncCount++;
				}else{
					nonhotspotFuncCount++;
				}
			}

			for(InstanceBean item:list){
				if(item.getExpellable()==true){
					tempAreaInstCount++;
					tempAreaMem+=funcMetadataMap.get(funcName).getMemoryConsume();
				}else{
					consistAreaInstCount++                                                                                                                                                                                                                        ;
					consistAreaMem+=funcMetadataMap.get(funcName).getMemoryConsume();
				}
			}
		}
		int[] result=new int[4];
		result[0]=tempAreaInstCount;
		result[1]=tempAreaMem;
		result[2]=consistAreaInstCount;
		result[3]=consistAreaMem;
		//"deployedFuncCount,hotspotFuncCount,nonhotspotFuncCount,tempAreaInstCount,tempAreaMem,consistAreaInstCount,consistAreaMem"
		//System.out.println(deployedFuncCount+","+hotspotFuncCount+","+nonhotspotFuncCount+","+result[0]+","+result[1]+","+result[2]+","+result[3]);
		return result;
	}*/

	//先判断节点是否有可用实例，没有可用实例的话，触发调度：
	//                     （1）看本地是否有足够空闲资源(不驱逐)，
	//                           如果有则创建。如果本地没有足够空闲资源，则（2）尝试全局查找有空闲资源的得分最大节点调度（不驱逐）。
	//		                                                                               如果找到节点则创建。否则，（3）本地尝试创建（可驱逐）,如果有足够资源则创建；否则（4）全局搜索（可驱逐）

	private static int executeRequest(RequestBean request){
		int latency=0;
		FuncMetadataBean metadataBean=request.getFuncMetadata();
		String funcName=metadataBean.getFuncName();
		long arrivalTime=request.getArrivalTime();
		if(serverBean.getFuncInstMap().containsKey(funcName)){ // function exists
			if(!checkAvailInstance(metadataBean, arrivalTime)){ 
				//System.out.println("Repository.checkCache(): cache miss for function "+metadataBean.getFuncName()+", request arrival time="+arrivalTime);
				if(remoteCreateInstance(request, funcName)){
					latency=-1; // successfully find another node for launch instance
				}else{
					if(localCreateInstance(metadataBean,arrivalTime)){
						if(checkAvailInstance(metadataBean, arrivalTime)){
							receivedColdstartCount++;
							serverBean.getFuncInstMap().get(funcName).addColdStartCount();
							latency=metadataBean.getExecutionTime()+metadataBean.getStartUpTime();
							//System.out.println("Repository.execRequest(): cold start for function "+metadataBean.getFuncName());
						}else{
							//这个是系统错误，创建出来实例缺没法执行，做为丢弃处理
							receivedDropCount++;
							serverBean.getFuncInstMap().get(funcName).addDropCount();
							latency=99999;
							//System.out.println("Repository.execRequest(): createInstance successfully but checkAvailInstance failed for function "+metadataBean.toString()+" arrival time"+arrivalTime);
						}
					}else{
						if(request.getTryTimes()>0){
							request.setTryTimes(request.getTryTimes()-1);
							redirectedCount++;
							serverBean.getFuncInstMap().get(funcName).addRedirectedCount();
							request.setExtraLatency(request.getExtraLatency()+dispatch_latency);
							if(RmiService.getInstance().cacheManagerRedirectRequestRmiCall(cacheManager, request, true)) {
								latency=-1; // successfully find another node for launch instance
							}else{
								receivedDropCount++;
								serverBean.getFuncInstMap().get(funcName).addDropCount();
								latency=99999;
							}
							//System.out.println("Repository.execRequest(): redirect function "+request.getFuncMetadata().getFuncName());
						}else{
							//转发次数达到上限，丢弃处理
							receivedDropCount++;
							serverBean.getFuncInstMap().get(funcName).addDropCount();
							latency=99999;
							//System.out.println("Repository.execRequest(): redirect times=0 drop request for function "+metadataBean.getFuncName());
						}
					}
				}
			}else{
				//System.out.println("Repository.checkCache(): cache hit for function"+metadataBean.getFuncName()+", request arrval time="+arrivalTime);
				receivedWarmstartCount++;
				serverBean.getFuncInstMap().get(funcName).addWarmStartCount();
				latency=metadataBean.getExecutionTime();
				//System.out.println("Repository.execRequest(): warm start for function "+metadataBean.getFuncName());
			}
		}else{
			//System.out.println("Repository.executeRequest(): error, serverBean.getFuncInstMap().containsKey(funcName)=false");
		}


		// update frequency
		serverBean.getFuncInstMap().get(funcName).addReceivedReqCount(); // update frequency of function
		serverBean.getFuncInstMap().get(funcName).addArrivalReqRecord(arrivalTime,rps_monitor_time_window);
		//System.out.println("Repository.execRequest(): update frequecy for function "+metadataBean.toString()+" before="+(serverBean.getFuncInstMap().get(metadataBean.getFuncName()).getReceivedReqCount()-1)+" after="+(serverBean.getFuncInstMap().get(metadataBean.getFuncName()).getReceivedReqCount()));

		return latency;
	}

	private static boolean checkAvailInstance(FuncMetadataBean functionBean, long arrivalTime){
		boolean cacheHit=false;
		ArrayList<InstanceBean> instanceList=serverBean.getFuncInstMap().get(functionBean.getFuncName()).getInstanceList();

		/**
		 * 检查是否有可用的已缓存实例
		 */
		for(InstanceBean instanceItem: instanceList){
			if(instanceItem.getExpellable()==false&&instanceItem.getLastActiveTimeEnd()<=arrivalTime){
				instanceItem.setLastActiveTimeStart(arrivalTime);
				instanceItem.setLastActiveTimeEnd(arrivalTime+functionBean.getExecutionTime());
				cacheHit=true;
				//System.out.println("Repository.checkAvailInstance(): find available cached instance");
				break;
			}
		}
		/**
		 * 没有可用的已缓存实例，检查是否有可用的临时实例
		 */
		if(!cacheHit){
			for(InstanceBean instanceItem: instanceList){
				if(instanceItem.getExpellable()==true&&instanceItem.getLastActiveTimeEnd()<=arrivalTime){
					instanceItem.setLastActiveTimeStart(arrivalTime);
					instanceItem.setLastActiveTimeEnd(arrivalTime+functionBean.getExecutionTime());
					cacheHit=true;
					//System.out.println("Repository.checkAvailInstance(): find available temporary instance");
					break;
				}
			}
		}
		return cacheHit;
	}

	/**
	 * 更新热点函数
	 * @param hotspotFuncMap
	 */
	public static void updateHotspotFunc(Map<String,TwoTuple<Integer,Boolean>> map){
		Repository.hotspotFuncMap=map;
		//System.out.println("Repository.updateHotspotFunc: update hotspot map size="+hotspotFuncMap.size()+"----------");
		//Set<String> keyset=Repository.hotspotFuncMap.keySet();
		/*for(String key:keyset){
			//System.out.println(key);
		}*/
		//System.out.println("Repository.updateHotspotFunc: ----------");
		for(Map.Entry<String, FuncInstBean> funcItem: serverBean.getFuncInstMap().entrySet()){
			ArrayList<InstanceBean> instanceList=funcItem.getValue().getInstanceList();
			/*if(instanceList.size()>0){
				//System.out.println("Repository.updateHotspotFunc: for() funcName="+funcItem.getKey()+" list size="+instanceList.size());
			}*/
			for(InstanceBean instanceItem:instanceList){
				//System.out.println("Repository.updateHotspotFunc: for() funcName="+funcItem.getKey()+"instanceItem="+instanceItem.toString());
				if(hotspotFuncMap.containsKey(funcItem.getKey())&&hotspotFuncMap.get(funcItem.getKey()).second==false&&instanceItem.getExpellable()==false){ 
					//System.out.println("Repository.updateHotspotFunc: update instanceItem type ="+instanceItem.getExpellable()+"------------> setExpellable=true, instanceItem="+instanceItem.getFuncName());
					instanceItem.setExpellable(true); //不属于热点函数但是先前被标记为不可驱逐
				}

				if(hotspotFuncMap.containsKey(funcItem.getKey())&&hotspotFuncMap.get(funcItem.getKey()).second==true&&instanceItem.getExpellable()==true){
					//System.out.println("Repository.updateHotspotFunc: update instanceItem type ="+instanceItem.getExpellable()+"------------> setExpellable=false, instanceItem="+instanceItem.getFuncName());
					instanceItem.setExpellable(false); //属于热点函数但是先前被标记为可驱逐
				}
			}
		}
	}
	private static boolean remoteCreateInstance(RequestBean request, String funcName){
		//System.out.println("Repository.remoteCreateInstance: start ");
		boolean result=false;
		if(serverBean.getAvailMemorySize()<request.getFuncMetadata().getMemoryConsume()){
			redirectedCount++;
			serverBean.getFuncInstMap().get(funcName).addRedirectedCount();
			request.setExtraLatency(request.getExtraLatency()+dispatch_latency);
			result=RmiService.getInstance().cacheManagerRedirectRequestRmiCall(cacheManager, request, false);
			//System.out.println("Repository.remoteCreateInstance: result="+result);
		}
		return result;
	}
	/**
	 * 创建函数实例
	 * @param newFuncMetadataBean
	 * @param arrivalTime
	 * @return
	 * @throws RemoteException 
	 */
	private static boolean localCreateInstance(FuncMetadataBean newFuncMetadataBean, long arrivalTime){
		InstanceBean candidateInstance=null;
		while(serverBean.getAvailMemorySize()<newFuncMetadataBean.getMemoryConsume()){
			candidateInstance=localEviction(newFuncMetadataBean.getMemoryConsume(), arrivalTime);
			if(candidateInstance==null){
				//System.out.println("Repository.localCreateInstance(): local eviction failed");
				break; // local eviction failed
			}
		}

		boolean createFlag=false;
		if(serverBean.getAvailMemorySize()>=newFuncMetadataBean.getMemoryConsume()){
			//boolean needCached=RmiService.getInstance().CacheManagerRmiCall(cacheManager, newFuncMetadataBean.getFuncName());
			//boolean flag=isExpellable(newFuncMetadataBean.getFuncName(), 0);
			boolean expellableFlag=true;
			if(hotspotFuncMap.containsKey(newFuncMetadataBean.getFuncName())&&hotspotFuncMap.get(newFuncMetadataBean.getFuncName()).second==true){
				expellableFlag=false;
			}
			InstanceBean instanceBean=new InstanceBean(newFuncMetadataBean.getFuncName(), arrivalTime, 0L, 0L, expellableFlag);
			serverBean.getFuncInstMap().get(instanceBean.getFuncName()).getInstanceList().add(instanceBean);
			serverBean.setAvailMemorySize(serverBean.getAvailMemorySize()-newFuncMetadataBean.getMemoryConsume());
			//System.out.println("Repository.localCreateInstance(): create new instance "+instanceBean.toString()+" before memory="+(serverBean.getAvailMemorySize()+newFuncMetadataBean.getMemoryConsume())+" after that memory="+serverBean.getAvailMemorySize());
			createFlag=true;
		}
		return createFlag;
	}

	/**
	 * 
	 * @param newInstanceMemory
	 * @param arrivalTime
	 * @return
	 */
	//private static float UsedMemoryTime=0;
	//private static float NeverUsedMemoryTime=0;
	private static InstanceBean localEviction(int newInstanceMemory, long arrivalTime){
		long maxInterval=Integer.MIN_VALUE;
		InstanceBean currentInstance=null;
		for(Map.Entry<String, FuncInstBean> funcItem: serverBean.getFuncInstMap().entrySet()){
			ArrayList<InstanceBean> instanceList=funcItem.getValue().getInstanceList();
			for(InstanceBean instanceItem:instanceList){
				if(instanceItem.getExpellable()==false){
					//System.out.println("Repository.localEviction(): instanceItem is cached isntance cannot be evicated, instance="+instanceItem.toString());
					continue; // 永久区的实例不可驱逐
				}
				long interval=arrivalTime-instanceItem.getLastActiveTimeEnd();
				if(interval>0&&interval>maxInterval){ //找最久空闲的实例
					maxInterval=interval;
					currentInstance=instanceItem;
					//System.out.println("Repository.localEviction(): eviction of function "+funcItem.getKey()+"'s instance (createTime="+instanceItem.getCreateTime()+" is idle) ");
				}
			}
		}

		/**
		 * 如果找到最久空闲的实例，释放该实例，不然返回null
		 */
		if(currentInstance!=null){
			//System.out.println("Repository.localEviction(): release instance"+currentInstance.toString()+" before memory="+serverBean.getAvailMemorySize()+" after that memory="+(serverBean.getAvailMemorySize()+funcMetadataMap.get(currentInstance.getFuncName()).getMemoryConsume()));
			serverBean.setAvailMemorySize(serverBean.getAvailMemorySize()+funcMetadataMap.get(currentInstance.getFuncName()).getMemoryConsume());
			serverBean.getFuncInstMap().get(currentInstance.getFuncName()).getInstanceList().remove(currentInstance);

			{
				float memoryTimeCost=(lastArrivalTime-currentInstance.getCreateTime())/1000.0f*funcMetadataMap.get(currentInstance.getFuncName()).getMemoryConsume();
				serverBean.getFuncInstMap().get(currentInstance.getFuncName()).addMemoryTimespanCost(memoryTimeCost);
				serverBean.getFuncInstMap().get(currentInstance.getFuncName()).addEvictionCount();
			}
			receivedEvictionCount++;
		}
		return currentInstance;
	}

	/**
	 * 返回节点的热度以及可用内存大小
	 * @return TwoTuple<Float,Integer> <热度和可用内存大小>
	 */
	public static TwoTuple<Integer,Float> calServerHotDegree(long arrivalTime, boolean evictionEnableFlag){
		TwoTuple<Integer,Float> result=new TwoTuple<Integer,Float>();

		int hotDegreeSum=0;
		int availMemory=0;
		for(Map.Entry<String, FuncInstBean> funcItem: serverBean.getFuncInstMap().entrySet()){
			ArrayList<InstanceBean> instanceList=funcItem.getValue().getInstanceList();
			if(instanceList.size()>0){
				/**
				 * 算热度
				 */
				if(hotspotFuncMap.containsKey(funcItem.getKey())&&hotspotFuncMap.get(funcItem.getKey()).second==true){ //只计算已缓存的热点函数的热度和
					hotDegreeSum+=hotspotFuncMap.get(funcItem.getKey()).first; //first是热度，second是热点标记
					//System.out.println("Repository.calServerHotDegree(): function "+funcItem.getKey()+" is hotspot, hotspot degree="+hotspotFuncMap.get(funcItem.getKey()).first+" hotDegreeSum="+(hotDegreeSum-hotspotFuncMap.get(funcItem.getKey()).first)+"--->"+hotDegreeSum);
				}

				if(evictionEnableFlag==true){
					/**
					 * 算空闲的内存，非热点函数的空闲实例也算到可用内存里去
					 */
					for(InstanceBean instItem:instanceList){
						if(instItem.getExpellable()==true&&arrivalTime>instItem.getLastActiveTimeEnd()){ //非缓存的函数 可驱逐且空闲
							availMemory+=funcMetadataMap.get(instItem.getFuncName()).getMemoryConsume();
							//System.out.println("Repository.calServerHotDegree(): function "+instItem.getFuncName()+" is not hotspot, availMemory="+(availMemory-funcMetadataMap.get(instItem.getFuncName()).getMemoryConsume())+"--->"+availMemory);
						}
					}
				}

			}
		}
		availMemory+=serverBean.getAvailMemorySize();//加上系统空闲的
		//System.out.println("Repository.calServerHotDegree(): server's availMemory="+(availMemory-serverBean.getAvailMemorySize())+"--->"+availMemory);

		result.first=availMemory;
		if(availMemory!=0){
			if(hotDegreeSum==0){
				result.second=availMemory*1.0f;
			}else{
				result.second=availMemory*1.0f/hotDegreeSum; // the larger, the better
			}
		}

		return result;
	}

	/**
	 * 返回节点的热度以及可用内存大小
	 * @return TwoTuple<Float,Integer> <热度和可用内存大小>
	 */
	public static int calServerAvailMemory(){
		return serverBean.getAvailMemorySize();//加上系统空闲的
	}

	/**
	 * 统计数据
	 * @return
	 */
	public static StatisticsBean display(){
		/**
		 * function level的统计
		 */
		StringBuilder result=new StringBuilder();
		result.append("funcName,funcReceivedReqCount,funcWarmCount,funcColdCount,funcDropCount,funcEvictionCount,funcReleaseCount,funcRedirectedCount,memoryTimeCost(evication),totalMemoryCost,uselessRate\n");
		Set<String> keySet=serverBean.getFuncInstMap().keySet();
		for(String funcName:keySet){
			result.append(funcName).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getReceivedReqCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getWarmStartCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getColdStartCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getDropCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getEvictionCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getReleaseCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getRedirectedCount()).append(",");
			result.append(serverBean.getFuncInstMap().get(funcName).getMemoryTimespanCost()).append(","); //被驱逐实例的memoryTimeCost
			float temp=serverBean.getFuncInstMap().get(funcName).getMemoryTimespanCost();
			for(InstanceBean item: serverBean.getFuncInstMap().get(funcName).getInstanceList()){
				float memoryTimeCost=(lastArrivalTime-item.getCreateTime())/1000.0f*funcMetadataMap.get(item.getFuncName()).getMemoryConsume();
				serverBean.getFuncInstMap().get(funcName).addMemoryTimespanCost(memoryTimeCost);
			}
			result.append(serverBean.getFuncInstMap().get(funcName).getMemoryTimespanCost()).append(","); //驱逐+未被驱逐实例的memoryTimeCost
			result.append(temp/serverBean.getFuncInstMap().get(funcName).getMemoryTimespanCost()*100).append("%");// 那些被被驱逐的实例的内存开销比例
			result.append("\n");
		}  
		try {
			FileOperation.outputFile(result.toString(),outputFilePath+serverBean.getAgentName()+"_func_level_.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}


		/**
		 * server-level的统计
		 */
		receivedInstanceCount=0;
		keySet=serverBean.getFuncInstMap().keySet();
		for(String funcName:keySet){
			if(serverBean.getFuncInstMap().get(funcName).getInstanceList()==null){
			}else{
				receivedInstanceCount+=serverBean.getFuncInstMap().get(funcName).getInstanceList().size();
			}
		} 

		StatisticsBean statisBean=new StatisticsBean();
		statisBean.setAgentName(serverBean.getAgentName());
		statisBean.setMemorySize(serverBean.getMemorySize());
		statisBean.setAvailMemorySize(serverBean.getAvailMemorySize());
		statisBean.setDeployedFunCount(serverBean.getFuncInstMap().size());
		statisBean.setDeployedInstanceCount(receivedInstanceCount);
		statisBean.setTotalRequestCount(receivedRequestCount);
		statisBean.setTotalColdstartCount(receivedColdstartCount);
		statisBean.setTotalWarmstartCount(receivedWarmstartCount);
		statisBean.setTotalEvictionCount(receivedEvictionCount);
		statisBean.setTotalReleaseCount(receivedReleaseCount);
		statisBean.setTotalRedirectedCount(redirectedCount);
		statisBean.setTotalDropCount(receivedDropCount);

		statisBean.setFuncLevelStatistics(serverBean.getFuncInstMap());
		//System.out.println(statisBean.getFuncLevelStatistics().toString());

		//System.out.println();
		//System.out.println();
		//System.out.println("tempAreaInstCount,consistAreaInstCount,,tempAreaMem,consistAreaMem,serverBean.getAvailMemorySize(),,tempAreaMem*100.0f/serverBean.getMemorySize()%,consistAreaMem*100.0f/serverBean.getMemorySize()%");
		return statisBean;
	}


	/*static StringBuilder str=new StringBuilder();
	public static void statistics(){
		if(receivedRequestCount%1000==0){
			//System.out.println("yes "+receivedRequestCount%1000);
			int tempAreaMem=0;
			int tempAreaInstCount=0;
			int consistAreaMem=0;
			int consistAreaInstCount=0;

			Set<String> keySet=serverBean.getFuncInstMap().keySet();
			for(String funcName:keySet){
				ArrayList<InstanceBean> list=serverBean.getFuncInstMap().get(funcName).getInstanceList();
				for(InstanceBean item:list){
					if(item.getExpellable()==true){
						tempAreaInstCount++;
						tempAreaMem+=funcMetadataMap.get(funcName).getMemoryConsume();
					}else{
						consistAreaInstCount++;
						consistAreaMem+=funcMetadataMap.get(funcName).getMemoryConsume();
					}
				}
			}
			str.append(tempAreaInstCount+","+consistAreaInstCount+",,"+tempAreaMem+","+consistAreaMem+","+serverBean.getAvailMemorySize()+",,"+tempAreaMem*100.0f/serverBean.getMemorySize()+"%,"+consistAreaMem*100.0f/serverBean.getMemorySize()+"%"+serverBean.getMemorySize()+"\n");
			//System.out.println(tempAreaInstCount+","+consistAreaInstCount+",,"+tempAreaMem+","+consistAreaMem+",,"+serverBean.getAvailMemorySize()+",,"+tempAreaMem*100.0f/serverBean.getMemorySize()+"%,"+consistAreaMem*100.0f/serverBean.getMemorySize()+"%");


		}else{
			//System.out.println("no "+receivedRequestCount%1000);
		}

	}*/

	public static CacheAreaBean statistics(int i){
		int tempAreaMem=0;
		int tempAreaInstCount=0;
		int consistAreaMem=0;
		int consistAreaInstCount=0;
		if(true){
			Set<String> keySet=serverBean.getFuncInstMap().keySet();
			for(String funcName:keySet){
				ArrayList<InstanceBean> list=serverBean.getFuncInstMap().get(funcName).getInstanceList();
				for(InstanceBean item:list){
					if(item.getExpellable()==true){
						tempAreaInstCount++;
						tempAreaMem+=funcMetadataMap.get(funcName).getMemoryConsume();
					}else{
						consistAreaInstCount++;
						consistAreaMem+=funcMetadataMap.get(funcName).getMemoryConsume();
					}

				}
			} 
		}
		System.out.println(tempAreaInstCount+","+consistAreaInstCount+",,"+tempAreaMem+","+consistAreaMem+",,"+serverBean.getAvailMemorySize()+",,"+tempAreaMem*100.0f/serverBean.getMemorySize()+"%,"+consistAreaMem*100.0f/serverBean.getMemorySize()+"%");
		CacheAreaBean bean=new CacheAreaBean(tempAreaMem, tempAreaInstCount, consistAreaMem, consistAreaInstCount,serverBean.getAvailMemorySize());
		return bean;
	}

	/**
	 * rehash check cache
	 * @param funcName
	 * @param arrivalTime
	 * @return
	 */
	public static boolean probe(RequestBean request){
		boolean redirectFlag=true;

		String funcName=request.getFuncMetadata().getFuncName();
		long arrivalTime=request.getArrivalTime();
		int funcMemory=request.getFuncMetadata().getMemoryConsume();

		/**
		 * 先检查cache是否需要命中
		 */
		boolean cacheHit=false;
		if(serverBean.getFuncInstMap().containsKey(funcName)){
			ArrayList<InstanceBean> instanceList=serverBean.getFuncInstMap().get(funcName).getInstanceList();
			for(InstanceBean instanceItem: instanceList){
				if(instanceItem.getLastActiveTimeEnd()<=arrivalTime){ //这里不需要判断是临时函数还是缓存函数
					cacheHit=true;
					//System.out.println("Repository.probe(): find available cached(temp) instance, cacheHit="+cacheHit);
					break;
				}
			}
		}

		/**
		 * cache miss 检查是否能创建实例
		 */
		if(cacheHit==false){
			/**
			 * 不会执行具体的删除和实例修改操作
			 */ 
			ArrayList<Integer> simDeletedList=new ArrayList<Integer>();
			int serverAvailMemorySize=serverBean.getAvailMemorySize();
			float maxInterval=0; //declear
			InstanceBean currentInstance; //declear
			while(serverAvailMemorySize<funcMemory){
				{
					maxInterval=Integer.MIN_VALUE; //init
					currentInstance=null; //init
					for(Map.Entry<String, FuncInstBean> funcItem: serverBean.getFuncInstMap().entrySet()){
						ArrayList<InstanceBean> instanceList=funcItem.getValue().getInstanceList();
						for(InstanceBean instanceItem:instanceList){
							// 已经模拟删除的实例不计入
							if(simDeletedList.contains(instanceItem.hashCode())){
								//System.out.println("repository.probe(): instanceItem forbidden hashcode="+instanceItem.hashCode()+" forbiddenList size="+forbiddenList.size());
								continue;
							}
							if(instanceItem.getExpellable()==false){
								//System.out.println("Repository.probe(): instanceItem is cached isntance cannot be evicated, instance="+instanceItem.toString());
								continue; // 永久区的实例不可驱逐
							}
							long interval=arrivalTime-instanceItem.getLastActiveTimeEnd();
							if(interval>0&&interval>maxInterval){ //找最久空闲的实例
								maxInterval=interval;
								currentInstance=instanceItem;
								//System.out.println("Repository.probe(): eviction of function "+funcItem.getKey()+"'s instance (createTime="+instanceItem.getCreateTime()+" is idle) ");
							}
						}
					}
					if(currentInstance!=null){
						//System.out.println("repository.probe(): found instance"+currentInstance.toString()+" before memory="+serverAvailMemorySize+" after that memory="+(serverAvailMemorySize+funcMetadataMap.get(currentInstance.getFuncName()).getMemoryConsume()));
						serverAvailMemorySize=serverAvailMemorySize+funcMetadataMap.get(currentInstance.getFuncName()).getMemoryConsume();
						simDeletedList.add(currentInstance.hashCode());
					}else{
						break; // there is no instance can be deleted
					}
				}
			}

			if(serverAvailMemorySize>=funcMemory){
				redirectFlag=false; //cache miss but availMemory
			}
		}else{
			redirectFlag=false; //cache hit
		}

		if(redirectFlag==true){
			redirectedCount++;
			registerFunc(request.getFuncMetadata());
			if(!serverBean.getFuncInstMap().containsKey(funcName)){
				serverBean.getFuncInstMap().put(funcName, new FuncInstBean(new ArrayList<InstanceBean>(), 0, new LinkedList<Long>()));
			}
			serverBean.getFuncInstMap().get(funcName).addRedirectedCount();
		}

		return redirectFlag;
	}


	public static Map<String,TwoTuple<Integer,Float>> getServerStatus(RequestBean request){
		//System.out.println("Repository.getServerStatus(111): start----------");
		long arrivalTime=request.getArrivalTime();
		Map<String,TwoTuple<Integer,Float>> map=new HashMap<String,TwoTuple<Integer,Float>>();
		int idleInstCount=0;
		for(Map.Entry<String, FuncInstBean> funcItem: serverBean.getFuncInstMap().entrySet()){
			ArrayList<InstanceBean> instanceList=funcItem.getValue().getInstanceList();
			if(instanceList.size()>0){
				idleInstCount=0;
				for(InstanceBean instanceItem: instanceList){
					if(instanceItem.getLastActiveTimeEnd()<=arrivalTime){ //这里不需要判断是临时函数还是缓存函数
						idleInstCount++;
					}
				}
				TwoTuple<Integer,Float> tuple=new TwoTuple<Integer,Float>(idleInstCount,idleInstCount*1.0f/instanceList.size());
				//System.out.println("Repository.getServerStatus(): find available cached(temp) instance,"+funcItem.getKey()+" availCounter="+idleInstCount+", instanceSize="+instanceList.size()+", idleScore="+(idleInstCount*1.0f/instanceList.size()));
				map.put(funcItem.getKey(), tuple); // <funcName,(idleInstCount,idleRate)>
			}
		}
		return map;
	}

}

