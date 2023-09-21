package scs.repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import scs.pojo.AgentBean;
import scs.pojo.FuncInstBean;
import scs.pojo.RequestBean;
import scs.pojo.StatisticsBean;
import scs.pojo.TwoTuple;
import scs.util.rmi.AgentInterface; 

public class Repository {
	private Repository(){}
	public static HashMap<String,Integer> reqCountMap=new HashMap<>();
	private static String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";

	//private final static String funLevelOutputFile="/home/tank/1_yanan/cacheResearch/output/overall_func_level.csv";  //linux
	//public static ArrayList<AgentInterface> cacheAgentRmiList1=new ArrayList<AgentInterface>();
	public static ArrayList<AgentBean> agentList=new ArrayList<AgentBean>();
	private static int totalRequestNum=0;
	private static int[] stepSizes=new int[4];

	static FileWriter fileWriter=null;
	public static void init(String agentStr, String outputFilePath){
		agentList.clear(); // init
		totalRequestNum=0; // init

		String[] nodeSplits=agentStr.split("#");
		for(String item:nodeSplits){
			String[] splits=item.split("_");
			if(splits.length==3){
				AgentBean agent=new AgentBean();
				agent.setAgentName(splits[0]);
				agent.setIp(splits[1]);
				agent.setPort(Integer.parseInt(splits[2]));
				agentList.add(agent);
				System.out.println("Repository.init(): agent found "+agent.toString());
			}
		} 
		System.out.println("Repository.init(): agentList size="+agentList.size());

		stepSizes[0]=1;
		stepSizes[1]=3;
		stepSizes[2]=5;
		stepSizes[3]=7;

		for(AgentBean agent: agentList){
			AgentInterface agentInterface=setupCacheAgentRmiConnection(agent.getIp(), agent.getPort(), "first");
			if(agentInterface!=null){
				agent.setRmiInterface(agentInterface);
			}
		}

		funLevelOutputFile=outputFilePath;
		System.out.println("Repository.init(): funLevelOutputFile= "+funLevelOutputFile);
		
		try {
			 fileWriter=new FileWriter("D:\\cacheResearch\\workspace\\faascache_log_overall_memory.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		reqCountMap.put("a",3111827);
		reqCountMap.put("b",11363701);
		reqCountMap.put("c",12411923);
		reqCountMap.put("d",24774632);
		reqCountMap.put("e",3462726);
		reqCountMap.put("f",2735329);
		reqCountMap.put("g",3665540);
		reqCountMap.put("h",2183501);
	}

	/**
	 * 转发请求
	 * @param request
	 * @param pattern
	 * @return [agentIndex, latency]
	 * @throws RemoteException
	 */
	public static int[] sendRequest(RequestBean request, String pattern, String childPattern) throws IOException{
		totalRequestNum++;
		
		int agentIndex=-1;
		if(pattern!=null&&pattern.equals("round")){
			agentIndex=roundBobinDispatcher(request);
		}else if(pattern!=null&&pattern.equals("rehash")){
			agentIndex=rehashDispatcher(request);
		}else if(pattern!=null&&pattern.equals("random")){
			agentIndex=randomDispatcher(request);
		}else if(pattern!=null&&pattern.equals("CHRLU")){
			agentIndex=CH_RLU_Dispatcher(request);
		}else{
			agentIndex=0;
		}
		 
		
		if(totalRequestNum%10000==0){
			
			runtimeDisplay(); //faascache and icebreaker memory size runtime
			
		}
 
		int latency=cacheAgentInterfaceCall(agentIndex,request,childPattern);
		int[] result=new int[2];
		result[0]=agentIndex;
		result[1]=latency;

		return result;
	}
	
	private static void runtimeDisplay() throws IOException{
		//System.out.println(totalRequestNum+" "+totalRequestNum/reqCountMap.get(trace)*100.0f+"%");
		for(int i=0;i<agentList.size();i++){
				int memory=agentList.get(i).getRmiInterface().serverMemDisplay();
				fileWriter.write(memory+",");
				//System.out.println(bean.toString());
			}
			fileWriter.write("\n");
			fileWriter.flush();
	}
	/***
	 * 
	 * @param filePath 输出文件路径
	 * @param childAgentCount 子节点的数量 our work是中心式 为8
	 * @return [sloViolationRate,overallMemoryUsageRate]
	 * @throws IOException
	 */
	public static float[] display(String filePath, int childAgentCount) throws IOException{
		float sloViolationRate=1.0f;
		float overallMemoryUsage=0;
		ArrayList<Float> agentMemoryUsageList=new ArrayList<Float>();

		List<StatisticsBean> agentResultList=new ArrayList<StatisticsBean>();
		for(int i=0;i<agentList.size();i++){
			for(int j=0;j<childAgentCount;j++){
				StatisticsBean bean=agentList.get(i).getRmiInterface().display(j);
				agentResultList.add(bean);
				//System.out.println(bean.toString());
			}
		}


		/**
		 * func level (per function)
		 */
		FileWriter file2=null;
		if(filePath!=null&&!filePath.equals("")){
			file2=new FileWriter(funLevelOutputFile);
		}
		Map<String, FuncInstBean> map=new HashMap<String, FuncInstBean>();
		for(StatisticsBean bean:agentResultList){
			float tempAgentMemUsage=0;
			Map<String, FuncInstBean> tempMap=bean.getFuncLevelStatistics();
			Set<String> keyset=tempMap.keySet();
			for(String key:keyset){
				if(!map.containsKey(key)){
					map.put(key, tempMap.get(key));
				}else{
					map.get(key).addReceivedReqCount(tempMap.get(key).getReceivedReqCount());// =warm+cold+drop
					map.get(key).addWarmStartCount(tempMap.get(key).getWarmStartCount());
					map.get(key).addColdStartCount(tempMap.get(key).getColdStartCount());
					map.get(key).addDropCount(tempMap.get(key).getDropCount());
					map.get(key).addRedirectedCount(tempMap.get(key).getRedirectedCount());
					map.get(key).addEvictionCount(tempMap.get(key).getEvictionCount());
					map.get(key).addReleaseCount(tempMap.get(key).getReleaseCount());
					map.get(key).addMemoryTimespanCost(tempMap.get(key).getMemoryTimespanCost());
				}
				tempAgentMemUsage+=tempMap.get(key).getMemoryTimespanCost();
			}
			agentMemoryUsageList.add(tempAgentMemUsage);
		}

		/**
		 * memoryTime
		 */
		//System.out.println();
		//System.out.println();
		//System.out.println("function level statistics");
		StringBuilder result=new StringBuilder();
		result.append("funcName,receivedReqCount,funcWarmCount,funcColdCount,funcDropCount,funcRedirectedCount,funcEvictionCount,funcReleaseCount,totalMemoryCost\n");
		Set<String> keySet=map.keySet();
		for(String funcName:keySet){
			result.append(funcName).append(",");
			result.append(map.get(funcName).getReceivedReqCount()).append(",");
			result.append(map.get(funcName).getWarmStartCount()).append(",");
			result.append(map.get(funcName).getColdStartCount()).append(",");
			result.append(map.get(funcName).getDropCount()).append(",");
			result.append(map.get(funcName).getRedirectedCount()).append(",");
			result.append(map.get(funcName).getEvictionCount()).append(",");
			result.append(map.get(funcName).getReleaseCount()).append(",");
			result.append(map.get(funcName).getMemoryTimespanCost()); 
			overallMemoryUsage+=map.get(funcName).getMemoryTimespanCost();
			result.append("\n");
		}  
		//System.out.println(result.toString());
		file2.write(result.toString());
		file2.flush();
		file2.close();


		/**
		 * overall level (per node)
		 */
		FileWriter file=null;
		if(filePath!=null&&!filePath.equals("")){
			file=new FileWriter(filePath);
		}

		//System.out.println();
		String title="serverName"+
				","+"FinalDeployedFunCount"+
				","+"FinalDeployedInstanceCount"+
				","+"TotalEvictionCount"+
				","+"TotalReleaseCount"+
				","+"MemorySize"+
				","+"MemoryUsageRate"+
				","+"ReceivedRequestCount"+ //=warm+cold+drop+redirected
				","+"RedirectedCount"+
				","+"WarmStartCount"+
				","+"ColdStartCount"+
				","+"DropCount"+
				","+"SloViolationRate";
		//System.out.println(title);
		if(file!=null){
			file.write(title+"\r\n");
		}
		int totalDeployedFuncCount=0;
		int totalDeployedInstanceCount=0;
		int totalColdstartCount=0;
		int totalWarmstartCount=0;
		int totalEvictionCount=0;
		int totalReleaseCount=0;
		int totalReceivedRequestCount=0; 
		int totalRedirectedCount=0;
		int totalDropCount=0;
		int totalAvailMemoryUsage=0;
		int totalMemorySize=0;
		int i=0;
		for(StatisticsBean bean:agentResultList){
			totalDeployedFuncCount+=bean.getDeployedFunCount();
			totalDeployedInstanceCount+=bean.getDeployedInstanceCount();
			totalEvictionCount+=bean.getTotalEvictionCount();
			totalReleaseCount+=bean.getTotalReleaseCount();
			totalWarmstartCount+=bean.getTotalWarmstartCount();
			totalColdstartCount+=bean.getTotalColdstartCount();
			totalReceivedRequestCount+=bean.getTotalRequestCount();
			totalRedirectedCount+=bean.getTotalRedirectedCount();
			totalDropCount+=bean.getTotalDropCount();
			//totalAvailMemoryUsage+=bean.getAvailMemorySize();
			totalMemorySize+=bean.getMemorySize();
			String row=
					bean.getAgentName()+
					","+bean.getDeployedFunCount()+
					","+bean.getDeployedInstanceCount()+
					","+bean.getTotalEvictionCount()+
					","+bean.getTotalReleaseCount()+
					","+bean.getMemorySize()+
					//","+bean.getMemoryUsageRate()+
					","+agentMemoryUsageList.get(i)+
					","+bean.getTotalRequestCount()+
					","+bean.getTotalRedirectedCount()+
					","+bean.getTotalWarmstartCount()+
					","+bean.getTotalColdstartCount()+
					","+bean.getTotalDropCount()+
					","+bean.getSloViolationRate();
			i++;
			//System.out.println(row);
			if(file!=null){
				file.write(row+"\r\n");
			}
		}

		sloViolationRate=1-1.0f*totalWarmstartCount/(totalWarmstartCount+totalColdstartCount+totalDropCount);

		String bottom=
				"overall"+
						","+totalDeployedFuncCount+
						","+totalDeployedInstanceCount+
						","+totalEvictionCount+
						","+totalReleaseCount+
						","+totalMemorySize+
						","+overallMemoryUsage+
						","+totalReceivedRequestCount+ 
						","+totalRedirectedCount+
						","+totalWarmstartCount+
						","+totalColdstartCount+
						","+totalDropCount+
						","+sloViolationRate;

		//System.out.println(bottom);
		if(file!=null){
			file.write(bottom+"\r\n");
			file.flush();
			file.close();
		}

		float[] res=new float[2];
		res[0]=sloViolationRate;
		res[1]=overallMemoryUsage;
		return res;
	}



	/*private static int roundDispatcher(){
		int agentIndex = 0;
		if(!agentList.isEmpty()){
			agentIndex=totalRequestNum%agentList.size();
		}else{
			System.out.println("repository.roundDispatcher(): reqDispatch error");
		}
		totalRequestNum++;
		return agentIndex;
	}

	private static int hashDispatcher(String funcName){
		int agentIndex = 0;
		if(!agentList.isEmpty()){
			//System.out.println((funcName.hashCode()&Integer.MAX_VALUE)%arraySize);
			agentIndex=(funcName.hashCode()&Integer.MAX_VALUE)%agentList.size();
		}else{
			System.out.println("repository.hashDispatcher(): reqDispatch error");
		}
		totalRequestNum++;
		return agentIndex;
	}*/
	private static Random random=new Random(189984971L);
	private static int randomDispatcher(RequestBean request) throws RemoteException{
		int agentIndex = -1;
		int tryTimes=3;

		if(!agentList.isEmpty()){
			int offset=1;
			agentIndex=random.nextInt(agentList.size());
			boolean redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
			//System.out.println("repository.reRoundDispatcher(): funcName="+funcName+" firstAgentIndex="+agentIndex+" redirectFlag="+redirectFlag+"  ----------------------------");
			while(redirectFlag&&tryTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
				tryTimes--;
				//System.out.println("repository.reRoundDispatcher(): tryTimes="+tryTimes+" redirectFlag="+redirectFlag+" new agentIndex="+agentIndex);
			}
			/*if(tryTimes==0){
				System.out.println("repository.reRoundDispatcher(): tryTimes out ++++++++++++++++");
			}else{
				System.out.println("repository.reRoundDispatcher(): fount it ++++++++++++++++");
			}*/

		}else{
			System.out.println("repository.randomDispatcher(): reqDispatch rehash error");
		}
		return agentIndex;
	}
	private static int roundBobinDispatcher(RequestBean request) throws RemoteException{
		int agentIndex = -1;
		int tryTimes=3;
		
		if(!agentList.isEmpty()){
			int offset=1;
			agentIndex=totalRequestNum%agentList.size();

			boolean redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
			//System.out.println("repository.reRoundDispatcher(): funcName="+funcName+" firstAgentIndex="+agentIndex+" redirectFlag="+redirectFlag+"  ----------------------------");
			while(redirectFlag&&tryTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
				tryTimes--;
				//System.out.println("repository.reRoundDispatcher(): tryTimes="+tryTimes+" redirectFlag="+redirectFlag+" new agentIndex="+agentIndex);
			}
			/*if(tryTimes==0){
				System.out.println("repository.reRoundDispatcher(): tryTimes out ++++++++++++++++");
			}else{
				System.out.println("repository.reRoundDispatcher(): fount it ++++++++++++++++");
			}*/

		}else{
			System.out.println("repository.reRoundDispatcher(): reqDispatch rehash error");
		}
		return agentIndex;
	}

	private static int rehashDispatcher(RequestBean request) throws RemoteException{
		int agentIndex = -1;
		int tryTimes=3;

		String funcName=request.getFuncMetadata().getFuncName();

		if(!agentList.isEmpty()){
			//System.out.println("stepSizes.length="+stepSizes.length);
			int hashIndex=(funcName.hashCode()&Integer.MAX_VALUE)%stepSizes.length;
			//System.out.println("hashIndex "+hashIndex);
			int offset=stepSizes[hashIndex]; //each function has an offset

			agentIndex=(funcName.hashCode()&Integer.MAX_VALUE)%agentList.size();
			boolean redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
			//System.out.println("repository.rehashDispatcher(): funcName="+funcName+" firstAgentIndex="+agentIndex+" redirectFlag="+redirectFlag+"  ----------------------------");
			while(redirectFlag&&tryTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				redirectFlag=Repository.agentList.get(agentIndex).getRmiInterface().probe(request);
				tryTimes--;
				//System.out.println("repository.rehashDispatcher(): tryTimes="+tryTimes+" redirectFlag="+redirectFlag+" new agentIndex="+agentIndex);
			}
			/*if(tryTimes==0){
				System.out.println("repository.rehashDispatcher(): tryTimes out ++++++++++++++++");
			}else{
				System.out.println("repository.rehashDispatcher(): fount it ++++++++++++++++");
			}*/

		}else{
			System.out.println("repository.rehashDispatcher(): reqDispatch rehash error");
		}

		return agentIndex;
	}

	final static float load_upper_bound=0.6f;
	final static float max_load_upper_bound=0.9f;
	final static int max_chain_len=3;
	private static int CH_RLU_Dispatcher(RequestBean request) throws RemoteException {
		int agentIndex = -1;
		boolean redirectFlag=true;
		int tryTimes=max_chain_len;
		
		int minServerLoadAgentIndex=-1;
		float minServerLoad=1.0f;

		String funcName=request.getFuncMetadata().getFuncName();
		if(!agentList.isEmpty()){
			int offset=1;
			agentIndex=(funcName.hashCode()&Integer.MAX_VALUE)%agentList.size();
			//System.out.println(agentIndex);
			TwoTuple<Integer,Float> serverLoad=Repository.agentList.get(agentIndex).getRmiInterface().calServerLoad(request.getArrivalTime());
			float coldstartPenalty=1+request.getFuncMetadata().getStartUpTime()*1.0f/request.getFuncMetadata().getExecutionTime();
			float minThreshold=coldstartPenalty*load_upper_bound<max_load_upper_bound?coldstartPenalty*load_upper_bound:max_load_upper_bound;
			if(serverLoad.second<minThreshold){
				redirectFlag=false;
			}else{
				if(serverLoad.second<minServerLoad){
					minServerLoad=serverLoad.second;
					minServerLoadAgentIndex=agentIndex;
				}
			}
			
			while(redirectFlag&&tryTimes>0){
				agentIndex=(agentIndex+offset)%agentList.size();
				serverLoad=Repository.agentList.get(agentIndex).getRmiInterface().calServerLoad(request.getArrivalTime());
				if(serverLoad.second<minThreshold){
					redirectFlag=false;
				}else{
					if(serverLoad.second<minServerLoad){
						minServerLoad=serverLoad.second;
						minServerLoadAgentIndex=agentIndex;
					}
					tryTimes--;
				}
			}
		}else{
			System.out.println("repository.CHRLU-Dispatcher(): reqDispatch error, agentList is null");
		}
		agentIndex=agentIndex==-1?minServerLoadAgentIndex:agentIndex; // return least-loaded server
		return agentIndex;
	} 

	public static int cacheAgentInterfaceCall(int agentIndex, RequestBean request,String pattern){
		int latency=-1;
		AgentBean agent=agentList.get(agentIndex);
		try {
			latency=agent.getRmiInterface().receiveRequest(request,pattern);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Repository.cacheAgentInterfaceCall(): cacheAgent reconnect...");
			agent.setRmiInterface(setupCacheAgentRmiConnection(agent.getIp(), agent.getPort(),"re"));
		}
		return latency;
	}
	
	public static int cacheManagerSchedulerInterfaceCall(int funcMemory, long arrivalTime, boolean evictionEnableFlag){
		int latency=-1;
		AgentBean agent=agentList.get(0);
		try {
			agent.getRmiInterface().testSchedule(funcMemory, arrivalTime, evictionEnableFlag);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Repository.cacheAgentInterfaceCall(): cacheAgent reconnect...");
			agent.setRmiInterface(setupCacheAgentRmiConnection(agent.getIp(), agent.getPort(),"re"));
		}
		return latency;
	}

	private static AgentInterface setupCacheAgentRmiConnection(String ip, int port, String status){
		AgentInterface agent=null;
		try {
			agent=(AgentInterface) Naming.lookup("rmi://"+ip+":"+port+"/agent");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		if(agent!=null){
			System.out.println("rmi://"+ip+":"+port+"/agent " + status +"connection build successfully");
		}else{
			System.out.println("rmi://"+ip+":"+port+"/agent " + status +"connection build unsuccessfully");
		}
		return agent;
	}
}

