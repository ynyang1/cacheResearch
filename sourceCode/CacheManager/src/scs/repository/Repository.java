package scs.repository;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import scs.pojo.AgentBean;
import scs.pojo.CacheAreaBean;
import scs.pojo.RequestBean;
import scs.pojo.StatisticsBean;
import scs.pojo.TwoTuple;
import scs.util.rmi.CacheletInterface;
import scs.util.rmi.RmiService;

public class Repository {

	private static RmiService rmiService=RmiService.getInstance();
	private static ArrayList<AgentBean> agentList=new ArrayList<AgentBean>();

	private static Map<String,ArrayList<Integer>> funcInvocCountTimerMap=new HashMap<String,ArrayList<Integer>>();
	private static LinkedList<TwoTuple<String,Integer>> hotspotScoreSortedList=new LinkedList<TwoTuple<String, Integer>>();
	private static Map<String,TwoTuple<Integer,Boolean>> hotspotFuncMap=new HashMap<String, TwoTuple<Integer,Boolean>>(); 

	private final static long HOTSPOT_STATISTICS_INTERVAL=3600*1000;//半小时
	private static float hotspotThreshold=1.0f;
	private static int totalRequestCount=0;
	private static int hotspotTimer=1;

	private final static long LTB_CACHE_REFRESH_TIME_INTERVAL=5*1000;//5秒
	private static boolean LTB_AWARE_DISPATCHER_FLAG=false;

	private final static int dispatch_latency=5;//5秒
	
	private final static String cacheManagerIp="localhost";
	private final static int cacheManagerPort=33330;

	private static int[] stepSizes=new int[4];

	public static void init(String agentStr, float threshold, boolean awareDispatcher){
		String[] nodeSplits=agentStr.split("#");
		for(String item:nodeSplits){
			String[] splits=item.split("_");

			if(splits.length==3){
				AgentBean cacheletAgent=new AgentBean();
				cacheletAgent.setAgentName(splits[0]);
				cacheletAgent.setIp(splits[1]);
				cacheletAgent.setPort(Integer.parseInt(splits[2]));
				CacheletInterface cacheletInterface=rmiService.setupCacheAgentRmiConnection(cacheletAgent.getIp(), cacheletAgent.getPort(), "first");
				if(cacheletInterface!=null){
					cacheletAgent.setRmiInterface(cacheletInterface);
					//System.out.println("Repository.init(): cachelet found "+cacheletAgent.toString());
				}
				agentList.add(cacheletAgent);
			}
		}

		hotspotThreshold=threshold;
		LTB_AWARE_DISPATCHER_FLAG=awareDispatcher;
		//System.out.println("Repository.init(): init cacheletList size="+agentList.size()+" HOTSPOT_STATISTICS_INTERVAL="+HOTSPOT_STATISTICS_INTERVAL+"ms, hotspotThreshold="+hotspotThreshold);
		//System.out.println("Repository.init(): init LTB_AWARE_DISPATCHER_FLAG="+LTB_AWARE_DISPATCHER_FLAG+", LTB_CACHE_REFRESH_TIME_INTERVAL="+LTB_CACHE_REFRESH_TIME_INTERVAL+"ms");
		//System.out.println("Repository.init(): init dispatch_latency="+dispatch_latency+"ms");
		
		stepSizes[0]=1;
		stepSizes[1]=3;
		stepSizes[2]=5;
		stepSizes[3]=7;

		for(int i=0;i<agentList.size();i++){
			LTBCacheList.add(new HashMap<String,TwoTuple<Integer,Float>>());
		}

		RmiService.getInstance().service(cacheManagerIp, cacheManagerPort);
	}

	/**
	 * 接受请求 入口函数
	 * @param request
	 * @return
	 */
	public static int receiveRequest(RequestBean request,String pattern){
		totalRequestCount++;
		statistics();

		String funcName=request.getFuncMetadata().getFuncName();
		registerFunc(funcName);

		checkHotspotCounterUpdate(request.getArrivalTime());

		int result;
		try {
			result = dispatchRequest(request,pattern);
		} catch (RemoteException e) {
			e.printStackTrace();
			return -1;
		}
		return result;
	}

	/***
	 * hotspot计数器注册函数
	 * @param funcName
	 */
	private static void registerFunc(String funcName){
		if(!funcInvocCountTimerMap.containsKey(funcName)){
			ArrayList<Integer> list=new ArrayList<>();
			for(int i=0;i<hotspotTimer;i++){
				list.add(0);
			}
			funcInvocCountTimerMap.put(funcName,list);
		}
		addLastHotspotCounter(funcInvocCountTimerMap.get(funcName));
	}

	private static void addLastHotspotCounter(ArrayList<Integer> list){
		int size=list.size();
		if(size==0){
			System.err.println("Repository.updateLastHotspotCounter(): error---------list is null");
			System.exit(0);
		}else{
			list.set(size-1,list.get(size-1)+1);
		}
	}

	private static long firstReqArrivalTime=-1;
	private static long offset=HOTSPOT_STATISTICS_INTERVAL*hotspotTimer;
	private static void checkHotspotCounterUpdate(long arrivalTime){
		/**
		 * 检查更新计数器时间范围
		 */
		if(firstReqArrivalTime==-1){
			firstReqArrivalTime=arrivalTime;
		}else if(arrivalTime-firstReqArrivalTime>=offset){
			addNewHotspotCounter(funcInvocCountTimerMap);
			hotspotTimer++;
			offset=HOTSPOT_STATISTICS_INTERVAL*hotspotTimer;

			//long start=System.nanoTime();
			calculateHotspot(); //计算热点函数得分 这里面会更新hotspotFuncMap的元素
			//System.out.println("calculateHotspot()="+(System.nanoTime()-start));
			for(AgentBean agent:agentList){
				//start=System.nanoTime();
				rmiService.updateHotspotFuncCall(agent,hotspotFuncMap); //更改已经缓存实例的标签，如果非热点函数，则取消缓存标记
				//System.out.println("updateHotspotFuncCall()="+(System.nanoTime()-start));
			}
		}
	}

	/**
	 * 检查Hotspot计数器是否需要新增一个计数器
	 * @param arrivalTime
	 */ 
	private static void addNewHotspotCounter(Map<String,ArrayList<Integer>> map){
		Set<String> keySet=map.keySet();
		for(String key:keySet){
			map.get(key).add(0);
		}
	}

	/**
	 * 转发请求
	 * @param request
	 * @param pattern
	 * @return [agentIndex, latency]
	 * @throws RemoteException
	 */
	private static int dispatchRequest(RequestBean request, String pattern) throws RemoteException{
		int[] result=new int[2];
		result[0]=0;
		result[1]=0;
		if(pattern!=null&&pattern.equals("round")){
			result=roundDispatcher(request);
		}else if(pattern!=null&&pattern.equals("rehash")){
			result=rehashDispatcher(request);
		}else if(pattern!=null&&pattern.equals("random")){
			result=randomDispatcher(request);
		}
		//System.out.println("Repositoyr.dispatchRequest(): "+result[0]+" "+request.getFuncMetadata().getFuncName());
		request.setExtraLatency(result[1]);
		int latency=rmiService.reciveRequestCall(request,agentList.get(result[0]));
		
		return latency;
	}


	/**
	 * 单个节点资源受限的时候，重新选择一个新节点进行实例的创建
	 * @param request
	 * @throws RemoteException
	 */
	public static boolean redirectRequest(RequestBean request, boolean evictionEnableFlag) throws RemoteException{
		boolean result=false;
		AgentBean cachelet=scheduleFunc(request.getFuncMetadata().getMemoryConsume(),request.getArrivalTime(), evictionEnableFlag);
		if(cachelet!=null){
			//System.out.println("Repository.redirectRequest(): find scheduled server "+cachelet.getAgentName()+" for request "+request.toString());
			request.setExtraLatency(request.getExtraLatency()+dispatch_latency);
			cachelet.getRmiInterface().reciveRequest(request);
			result=true;
		}
		return result;
	}

	/**
	 * 热度最小原则进行调度
	 * @param funcMemory
	 * @param arrivalTime
	 * @return
	 */
	private static AgentBean scheduleFunc(int funcMemory, long arrivalTime, boolean evictionEnableFlag){
		float maxScore=-1;
		AgentBean candidator=null;
		for(AgentBean agent:agentList){
			TwoTuple<Integer,Float> result=rmiService.calServerHotDegreeCall(agent,arrivalTime, evictionEnableFlag); 
			//System.out.println("Repository.scheduleFunc(): agent "+agent.getAgentName()+" availMem="+result.first+" score="+result.second);

			if(funcMemory<=result.first&&result.second>maxScore){ //result.second=availMemory*1.0f/hotDegreeSum;  the larger, the better
				maxScore=result.second;
				candidator=agent;
			}
		}
		/*if(candidator==null){
			//System.out.println("Repository.scheduleFunc(): candidator is null, funcMemory="+funcMemory+" score="+maxScore);
		}else{
			//System.out.println("Repository.scheduleFunc(): candidator="+candidator.getAgentName()+" funcMemory="+funcMemory+" score="+maxScore);
		}*/
		return candidator;
	}
	
	public static AgentBean test_scheduleFunc(int funcMemory, long arrivalTime, boolean evictionEnableFlag){
		float maxScore=-1;
		AgentBean candidator=null;
		
		for(AgentBean agent:agentList){
			TwoTuple<Integer,Float> result=rmiService.calServerHotDegreeCall(agent,arrivalTime, evictionEnableFlag); 
			//System.out.println("Repository.scheduleFunc(): agent "+agent.getAgentName()+" availMem="+result.first+" score="+result.second);

			if(funcMemory<=result.first&&result.second>maxScore){ //result.second=availMemory*1.0f/hotDegreeSum;  the larger, the better
				maxScore=result.second;
				candidator=agent;
			}
		}
		/*if(candidator==null){
			//System.out.println("Repository.scheduleFunc(): candidator is null, funcMemory="+funcMemory+" score="+maxScore);
		}else{
			//System.out.println("Repository.scheduleFunc(): candidator="+candidator.getAgentName()+" funcMemory="+funcMemory+" score="+maxScore);
		}*/
		return candidator;
	}


	private static boolean displayFlag=false;
	public static StatisticsBean display(int agentIndex) throws RemoteException{
		if(displayFlag==false){
			System.out.println(str.toString());	 //保证只输出一次
			displayFlag=true;
		}
		return agentList.get(agentIndex).getRmiInterface().display();
	}


	static StringBuilder str=new StringBuilder();
	public static void statistics(){

		if(totalRequestCount%10000==0){
			int tempAreaMem=0;
			int tempAreaInstCount=0;
			int consistAreaMem=0;
			int consistAreaInstCount=0;
			int availMem=0;
			for(AgentBean cachelet:agentList){
				try {
					CacheAreaBean bean=cachelet.getRmiInterface().statistics(0);
					tempAreaMem+=bean.getTempAreaMem();
					tempAreaInstCount+=bean.getTempAreaInstCount();
					consistAreaMem+=bean.getConsistAreaMem();
					consistAreaInstCount+=bean.getConsistAreaInstCount();
					availMem+=bean.getAvailMemory();

				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			str.append(tempAreaInstCount+","+consistAreaInstCount+",,"+tempAreaMem+","+consistAreaMem+","+availMem+"\n");
		}

	}

	/**
	 * 计算热点函数计数器得分,采用衰减计数方法进行计算 func-level
	 */
	private static void calculateHotspot(){
		//System.out.println("Repository.calculateHotspot(): start------------------");

		hotspotScoreSortedList.clear(); //初始化
		hotspotScoreSortedList.add(new TwoTuple<String,Integer>("virtual", -1));//添加一个虚拟元素
		int sum=0;
		/**
		 * 衰减计数法 计算热点得分
		 */
		Set<String> funcNameSet=funcInvocCountTimerMap.keySet();
		for(String funcName:funcNameSet){
			int score=0;
			//System.out.println("Repository.calculateHotspot(): funcName="+funcName);
			int size=funcInvocCountTimerMap.get(funcName).size();
			for(int item:funcInvocCountTimerMap.get(funcName)){
				size--;
				score=score+(item>>size);
				//System.out.println("Repository.calculateHotspot(): size="+size+" item="+item+" score="+score);
			}
			/**
			 * score排序并插入到linkedList
			 */
			sum+=score;
			for(int i=0;i<hotspotScoreSortedList.size();i++){
				if(score>=hotspotScoreSortedList.get(i).second){ //第一个元素是虚拟元素，一定比真实得分小
					hotspotScoreSortedList.add(i, new TwoTuple<String,Integer>(funcName,score)); //从大到小排列 插入元素
					break;
				}
			}
		}

		/**
		 * 从高到底排序
		 */
		hotspotFuncMap.clear();
		int threshold=(int)(sum*1.0f*hotspotThreshold);
		sum=0;
		for(TwoTuple<String,Integer> item:hotspotScoreSortedList){
			hotspotFuncMap.put(item.first,new TwoTuple<Integer,Boolean>(item.second,true)); //true代表是热点函数<funcName,<score,true/false>>
			sum+=item.second;
			if(sum<=threshold){ //函数按照调用量从大到小，累积贡献请求数超过总体的x%,这个列表里的函数就定义为热点函数
				//System.out.println("Repository.calculateHotspot(): "+item.first+","+item.second+",true"+"++++++++++");
				continue;
			}else{
				break;
			}
		}
		/**
		 * 处理剩余的
		 */
		for(TwoTuple<String,Integer> item:hotspotScoreSortedList){
			if(!hotspotFuncMap.containsKey(item.first)){
				hotspotFuncMap.put(item.first,new TwoTuple<Integer,Boolean>(item.second,false)); //false代表不是热点函数
				//System.out.println("Repository.calculateHotspot(): "+item.first+","+item.second+",false"+"--------");
			}
		}
	}

	private static int[] roundDispatcher(RequestBean request) throws RemoteException{
		int[] result=new int[2];
		int agentIndex=-1;
		int extraLatency=0;
		int rehashTimes=3;
		if(!agentList.isEmpty()){
			int offset=1;
			agentIndex=totalRequestCount%agentList.size();
			extraLatency+=dispatch_latency;
			boolean redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);//false代表不需要转发，找到可用空闲实例的节点了
			//System.out.println("repository.rehashDispatcher(): funcName="+request.getFuncMetadata().getFuncName()+" firstAgentIndex="+agentIndex+" redirectFlag="+redirectFlag+"  ----------------------------");
			while(redirectFlag&&rehashTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				extraLatency+=dispatch_latency;
				redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
				rehashTimes--;
				//System.out.println("repository.rehashDispatcher(): tryTimes="+rehashTimes+" redirectFlag="+redirectFlag+" new agentIndex="+agentIndex);
			}
			if(rehashTimes==0&&redirectFlag==true){ //3次probe失败，次数耗尽，且没有找到可用空闲实例的节点
				if(LTB_AWARE_DISPATCHER_FLAG==true){
					extraLatency+=dispatch_latency;
					int awareAgentIndex=cacheAwareDispatcher(request);
					if(awareAgentIndex!=-1){
						agentIndex=awareAgentIndex;
					}
				}
			}
		}else{
			//System.out.println("repository.rehashDispatcher(): error cacheletList is null");
		}
		result[0]=agentIndex;
		result[1]=extraLatency;
		return result;
	}
	private static Random random=new Random(189984971L);
	private static int[] randomDispatcher(RequestBean request) throws RemoteException{
		int[] result=new int[2];
		int agentIndex=-1;
		int extraLatency=0;
		int rehashTimes=3;
		if(!agentList.isEmpty()){
			int offset=1;
			agentIndex=random.nextInt(agentList.size());
			extraLatency+=dispatch_latency;
			boolean redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);//false代表不需要转发，找到可用空闲实例的节点了
			//System.out.println("repository.rehashDispatcher(): funcName="+request.getFuncMetadata().getFuncName()+" firstAgentIndex="+agentIndex+" redirectFlag="+redirectFlag+"  ----------------------------");
			while(redirectFlag&&rehashTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				extraLatency+=dispatch_latency;
				redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
				rehashTimes--;
				//System.out.println("repository.rehashDispatcher(): tryTimes="+rehashTimes+" redirectFlag="+redirectFlag+" new agentIndex="+agentIndex);
			}
			if(rehashTimes==0&&redirectFlag==true){ //3次probe失败，次数耗尽，且没有找到可用空闲实例的节点
				if(LTB_AWARE_DISPATCHER_FLAG==true){
					extraLatency+=dispatch_latency;
					int awareAgentIndex=cacheAwareDispatcher(request);
					if(awareAgentIndex!=-1){
						agentIndex=awareAgentIndex;
					}
				}
			}
		}else{
			//System.out.println("repository.randomDispatcher(): error cacheletList is null");
		}
		result[0]=agentIndex;
		result[1]=extraLatency;
		return result;
	}

	private static int[] rehashDispatcher(RequestBean request) throws RemoteException{
		int[] result=new int[2];
		int agentIndex=-1;
		int extraLatency=0;
		int rehashTimes=2;
		String funcName=request.getFuncMetadata().getFuncName();
		if(!agentList.isEmpty()){
			//System.out.println("stepSizes.length="+stepSizes.length);
			int hashIndex=(funcName.hashCode()&Integer.MAX_VALUE)%stepSizes.length;
			//System.out.println("hashIndex "+hashIndex);
			int offset=stepSizes[hashIndex];

			agentIndex=(funcName.hashCode()&Integer.MAX_VALUE)%agentList.size();
			extraLatency+=dispatch_latency;
			boolean redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);//false代表不需要转发，找到可用空闲实例的节点了
			//System.out.println("repository.rehashDispatcher(): funcName="+funcName+" firstAgentIndex="+agentIndex+" redirectFlag="+redirectFlag+"  ----------------------------");
			while(redirectFlag&&rehashTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				extraLatency+=dispatch_latency;
				redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
				rehashTimes--;
				//System.out.println("repository.rehashDispatcher(): tryTimes="+rehashTimes+" redirectFlag="+redirectFlag+" new agentIndex="+agentIndex);
			}
			if(rehashTimes==0&&redirectFlag==true){ //3次probe失败，次数耗尽，且没有找到可用空闲实例的节点
				if(LTB_AWARE_DISPATCHER_FLAG==true){
					extraLatency+=dispatch_latency;
					int awareAgentIndex=cacheAwareDispatcher(request);
					if(awareAgentIndex!=-1){
						agentIndex=awareAgentIndex;
					}
				}
			}
		}else{
			//System.out.println("repository.rehashDispatcher(): error cacheletList is null");
		}
		result[0]=agentIndex;
		result[1]=extraLatency;
		return result;
	}

	/**
	 * 给定一个函数的请求, 遍历所有的节点, 查询各个节点上该函数空闲实例的数量和空闲率,如果有空闲,取空闲率最大的节点
	 * 由于查询比较费时,所以设定了一个5秒的cache,来存储各个节点上的空闲实例状态,每个节点都有一个map,所以是一个List数据结构. 在实际场景中,这个过程可以采用异步进行.
	 */
	private static long lastUpdateTime=0;
	private static ArrayList<Map<String,TwoTuple<Integer,Float>>> LTBCacheList=new ArrayList<Map<String,TwoTuple<Integer,Float>>>();
	private static int cacheAwareDispatcher(RequestBean request) throws RemoteException {
		String funcName=request.getFuncMetadata().getFuncName();
		long arrivalTime=request.getArrivalTime();
		float maxIdleRate=-1f;
		int agentIndex=-1;

		/**
		 * check LTB cache, 可以异步执行
		 */
		if(arrivalTime-lastUpdateTime>LTB_CACHE_REFRESH_TIME_INTERVAL){
			for(int i=0;i<agentList.size();i++){
				Map<String,TwoTuple<Integer,Float>> map=agentList.get(i).getRmiInterface().getServerStatus(request); //<funcName,(idleInstCount,idleRate)>
				LTBCacheList.set(i,map);
			}
			lastUpdateTime=arrivalTime;
		}

		for(int i=0;i<LTBCacheList.size();i++){
			Map<String,TwoTuple<Integer,Float>> map=LTBCacheList.get(i);
			//System.out.println("Repository.cacheAwareDispatchRequest(11): agentIndex="+i+",map.size="+map.size());
			if(map.containsKey(funcName)){
				TwoTuple<Integer,Float> tuple=map.get(funcName);
				//System.out.println("Repository.cacheAwareDispatchRequest(11): tuple.first="+tuple.first+",tuple.second="+tuple.second);
				if(tuple.first>0){ // has idle instance
					if(tuple.second>maxIdleRate){ // the larger idleRate, the better
						maxIdleRate=tuple.second;
						agentIndex=i;
					}
				}
			}
		}

		//System.out.println("Repository.cacheAwareDispatchRequest(): agentIndex="+agentIndex +",maxIdleRate="+maxIdleRate);
		/*if(agentIndex==-1){ //没有任何一个节点 存在该实例，哪怕是不空闲的实例也没有
			agentIndex=hashDispatcher(request.getFuncMetadata().getFuncName());
			//System.out.println("Repository.cacheAwareDispatchRequest(): hash agent index="+agentIndex);
		}*/
		return agentIndex;
	}

 
}

