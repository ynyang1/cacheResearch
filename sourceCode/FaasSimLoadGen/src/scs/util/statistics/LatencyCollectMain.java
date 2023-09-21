package scs.util.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import scs.util.tool.FileOperation;

//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import scs.repository.Repository;
//import scs.util.tool.ExecutorDriverThread;
//import scs.util.tool.LoadDriver;
//
class TestBean{
	private String trace;
	private int agentMemorySize;
	private String pattern;
	private String requestCount;
	private String baseline;
	private int cacheletCount;
	private float hotspotThreshold;
	private boolean cacheAwareDispatch;
	private int keepaliveTime;

	/*public TestBean(String trace, int agentMemorySize, String pattern, String requestCount, String baseline) {
		super();
		this.trace = trace;
		this.agentMemorySize = agentMemorySize;
		this.pattern = pattern;
		this.requestCount = requestCount;
		this.baseline = baseline;
	}*/

	public TestBean(String trace, int agentMemorySize, String pattern, String requestCount, String baseline,float threshold, int cacheletCount, boolean cacheAwareDispatch, int keepaliveTime) {
		super();
		this.trace = trace;
		this.agentMemorySize = agentMemorySize;
		this.pattern = pattern;
		this.requestCount = requestCount;
		this.baseline = baseline;
		this.hotspotThreshold=threshold;
		this.cacheletCount=cacheletCount;
		this.cacheAwareDispatch=cacheAwareDispatch;
		this.keepaliveTime=keepaliveTime;
	}

	public String getTrace() {
		return trace;
	}
	public int getAgentMemorySize() {
		return agentMemorySize;
	}
	public String getPattern() {
		return pattern;
	}
	public String getRequestCount() {
		return requestCount;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public void setAgentMemorySize(int agentMemorySize) {
		this.agentMemorySize = agentMemorySize;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public void setRequestCount(String requestCount) {
		this.requestCount = requestCount;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getBaseline() {
		return baseline;
	}
	public void setHotspotThreshold(float hotspotThreshold) {
		this.hotspotThreshold = hotspotThreshold;
	}

	public float getHotspotThreshold() {
		return hotspotThreshold;
	}

	public int getCacheletCount() {
		return cacheletCount;
	}

	public boolean isCacheAwareDispatch() {
		return cacheAwareDispatch;
	}

	public void setCacheletCount(int cacheletCount) {
		this.cacheletCount = cacheletCount;
	}

	public void setCacheAwareDispatch(boolean cacheAwareDispatch) {
		this.cacheAwareDispatch = cacheAwareDispatch;
	}

	public int getKeepaliveTime() {
		return keepaliveTime;
	}

	public void setKeepaliveTime(int keepaliveTime) {
		this.keepaliveTime = keepaliveTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestBean [trace=");
		builder.append(trace);
		builder.append(", agentMemorySize=");
		builder.append(agentMemorySize);
		builder.append(", pattern=");
		builder.append(pattern);
		builder.append(", requestCount=");
		builder.append(requestCount);
		builder.append(", baseline=");
		builder.append(baseline);
		builder.append("]");
		return builder.toString();
	}
}
public class LatencyCollectMain {
	static int thres=200000;
	
	/**
	 * 统计各个方法的延迟
	 * @param args
	 */
	public static void main(String[] args){
		ArrayList<TestBean> testList=new ArrayList<TestBean>();

//		testList.add(new TestBean("a", 1906, "round", "full","faascache", 0.5f, 1, true, 300000));
//		testList.add(new TestBean("b", 1144, "round", "full","faascache", 0.5f, 1, true, 300000));
		
//		testList.add(new TestBean("d", 636, "round", "full","faascache", 0.5f, 1, true, 300000));
//		testList.add(new TestBean("e", 3430, "round", "full","faascache", 0.5f, 1, true, 300000));
//		testList.add(new TestBean("f", 5970, "round", "full","faascache", 0.5f, 1, true, 300000));
//		testList.add(new TestBean("g", 9018, "round", "full","faascache", 0.5f, 1, true, 300000));
//		testList.add(new TestBean("h", 1652, "round", "full","faascache", 0.5f, 1, true, 300000));
//		testList.add(new TestBean("h", 12288, "CHRLU", "full","none",0.9f, 8, true, 300000)); 
//		testList.add(new TestBean("g", 14336, "CHRLU", "full","none",0.9f, 8, true, 300000));
//		testList.add(new TestBean("f", 12288, "CHRLU", "full","none",0.9f, 8, true, 300000));
//		testList.add(new TestBean("e", 8192, "CHRLU", "full","none",0.9f, 8, true, 300000));
//		testList.add(new TestBean("d", 1792, "CHRLU", "full","none",0.9f, 8, true, 300000));
//		testList.add(new TestBean("c", 5632, "CHRLU", "full","none",0.9f, 8, true, 300000));
//		testList.add(new TestBean("b", 4096, "CHRLU", "full","none",0.9f, 8, true, 300000));
//		testList.add(new TestBean("a", 10542, "CHRLU", "full","none",0.9f, 8, true, 300000));
	  
		testList.add(new TestBean("c", 4446, "round", "full","faascache", 0.5f, 1, true, 300000));
		LatencyCollectMain latencyCollectMain=new LatencyCollectMain();
		String policy="rehash";
		try {
			latencyCollectMain.FaasCache(testList, policy);
			latencyCollectMain.Keepalive(testList, policy);
			latencyCollectMain.Flame(testList, policy);
			latencyCollectMain.CHRLU(testList, policy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void FaasCache(ArrayList<TestBean> testList, String policy) throws IOException {
		String method="faascache";
		String path="D:\\cacheResearch\\coldstartRate\\temp\\"+method+"\\"+policy+"\\";
		FileWriter writer=new FileWriter("D:\\cacheResearch\\latency\\trace-c\\"+method+"_"+policy+"_overall_latency.csv");
		for(TestBean item:testList){
			List<Integer> tempList= new FileOperation().readIntFile(path+method+"_"+policy+"_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()+"_full\\"+
		"latency_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()+"_8_full.csv");
			int max=0;
			int min=0;
			for(int i:tempList){
				if(i>thres||i==0){
					continue;
				}
				writer.write(i+"\n");
				if(i%1000==0){
					writer.flush();
				}
			}
			System.out.println(tempList.size()+" max="+max+" min="+min);
		}
		writer.flush();
		writer.close();
	}
	public static void Keepalive(ArrayList<TestBean> testList, String policy) throws IOException {
		String method="keepalive";
		String path="D:\\cacheResearch\\coldstartRate\\temp\\"+method+"\\"+policy+"\\";
		FileWriter writer=new FileWriter("D:\\cacheResearch\\latency\\trace-c\\"+method+"_"+policy+"_overall_latency.csv");
		for(TestBean item:testList){
			List<Integer> tempList= new FileOperation().readIntFile(path+method+"_"+policy+"_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()+"_full\\"+
		"latency_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()+"_8_full.csv");
			int max=0;
			int min=0;
			for(int i:tempList){
				if(i>thres||i==0){
					continue;
				}
				writer.write(i+"\n");
				if(i%1000==0){
					writer.flush();
				}
			}
			System.out.println(tempList.size()+" max="+max+" min="+min);
		}
		writer.flush();
		writer.close();
	}

	
//	//CHRLU Latency
		public static void CHRLU(ArrayList<TestBean> testList, String policy) throws IOException {
	    String method="CHRLU";
		String path="D:\\cacheResearch\\coldstartRate\\temp\\"+method+"\\";
		FileWriter writer=new FileWriter("D:\\cacheResearch\\latency\\trace-c\\"+method+"_"+policy+"_overall_latency.csv");
		for(TestBean item:testList){
			List<Integer> tempList= new FileOperation().readIntFile(path+"faascache_"+method+"_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()
			+"_full\\latency_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()+"_8_full.csv");
			int max=0;
			int min=0;
			for(int i:tempList){
				if(i>thres||i==0){
					continue;
				}
				writer.write(i+"\n");
				if(i%1000==0){
					writer.flush();
				}
			}
			System.out.println(tempList.size()+" max="+max+" min="+min);
		}
		writer.flush();
		writer.close();
	}

	
	
	// Flame Latency
	public static void Flame(ArrayList<TestBean> testList, String policy) throws IOException {
	    String method="Flame";
		String path="D:\\cacheResearch\\coldstartRate\\temp\\baseline-faascache\\"+policy+"\\fixed\\";
		FileWriter writer=new FileWriter("D:\\cacheResearch\\latency\\trace-c\\"+method+"_"+policy+"_overall_latency.csv");
		for(TestBean item:testList){
			List<Integer> tempList= new FileOperation().readIntFile(path+"baseline_faascache_cachelet_"+policy+"_trace_"+item.getTrace()+"_8nodes_"+item.getAgentMemorySize()+
					"MBs_300000ms_fullreqs_0.5_true\\latency_trace_"+item.getTrace()+"_"+item.getAgentMemorySize()+"_8_full.csv");
			int max=0;
			int min=0;
			for(int i:tempList){
				if(i>thres||i==0){
					continue;
				}
				writer.write(i+"\n");
				if(i%1000==0){
					writer.flush();
				}
			}
			System.out.println(tempList.size()+" max="+max+" min="+min);
		}
		writer.flush();
		writer.close();
	}



// TODO Auto-generated method stub
//		if(args.length==0){
//			System.out.println("java -jar xxx.jar agent1_192.168.1.109_20001#agent2_192.168.1.109_20002");
//			System.exit(0);
//		}
//String parm=args[0];

//		/*ArrayList<Integer> serverMemoryList=new ArrayList<Integer>();
//		serverMemoryList.add(512);
//		serverMemoryList.add(1024);
//		serverMemoryList.add(1536);
//		serverMemoryList.add(2048);
//		serverMemoryList.add(2560);
//		serverMemoryList.add(3072);
//		serverMemoryList.add(4096);
//		serverMemoryList.add(5120);
//		serverMemoryList.add(8192);
//		serverMemoryList.add(10240);
//		serverMemoryList.add(12800);
//		serverMemoryList.add(16384);*/
//
//		ArrayList<String> traceList=new ArrayList<String>();
//		traceList.add("h");
//		traceList.add("g");
//		traceList.add("f");
//		traceList.add("e");
//		traceList.add("d");
//		traceList.add("c");
//		traceList.add("b");
//		traceList.add("a");
//		
//		ArrayList<String> patternList=new ArrayList<String>();
//		patternList.add("roundBobin");
//
//		ArrayList<String> workloadCountList=new ArrayList<String>();
//		workloadCountList.add("full");
//		//workloadCountList.add("1000000");
//
//		float threshold=0.9f;
//		boolean recordLatency=false;
//
//
//		int MaxMem=0;
//		int minMem=0;
//		int tempMem=0;
//
//		float sloViolationRate=1.0f;
//		//float overMemUsageRate=1.0f;
//
//
//		Map<String,Float> recordMap=new HashMap<String,Float>();
//
//		for(String trace:traceList){
//			for(String workloadCount:workloadCountList){
//				for(String pattern:patternList){
//					/**
//					 * 检查最大内存下的冷启动率率，如果无法满足则无需尝试
//					 */
//					String key=trace+"_"+workloadCount+"_"+pattern;
//					System.out.println("Main.main(): "+key);
//					if(!recordMap.containsKey(key)){
//						MaxMem=16384; //init
//						minMem=128; //init
//						tempMem=0; //int
//						try {
//							float[] res = new Main().evaluateFaasCacheAutoLinux(MaxMem,threshold,pattern,trace,workloadCount,recordLatency);
//							sloViolationRate=res[0];
//							recordMap.put(key,sloViolationRate);
//							if(sloViolationRate>0.01){
//								System.out.println("Main.main(): stop exploring sloViolationRate="+sloViolationRate+",trace="+trace+",workloadCount="+workloadCount+",pattern="+pattern+",maxServerMem="+MaxMem+",");
//								continue;
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//					/**
//					 * 2分法探索可用内存
//					 */
//					while(MaxMem-minMem>=256){
//						try {
//							tempMem=(int)((MaxMem+minMem)*1.0f/2);
//							float[] res = new Main().evaluateFaasCacheAutoLinux(tempMem,threshold,pattern,trace,workloadCount,recordLatency);
//							sloViolationRate=res[0];
//
//							if(sloViolationRate>=0.01f){
//								System.out.println("Main.main() find sloViolationRate="+sloViolationRate+",trace="+trace+",workloadCount="+workloadCount+",pattern="+pattern+",minMem="+minMem+",");
//								minMem=tempMem;
//							}else{
//								System.out.println("Main.main() find sloViolationRate="+sloViolationRate+",trace="+trace+",workloadCount="+workloadCount+",pattern="+pattern+",MaxMem="+MaxMem+",");
//								MaxMem=tempMem;
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//
//
//		/**
//		 * keepalive
//		 */
//		/*recordMap.clear();
//
//		for(String trace:traceList){
//			for(String workloadCount:workloadCountList){
//				for(String pattern:patternList){
//
//					 // 检查最大内存下的冷启动率率，如果无法满足则无需尝试
//
//					String key=trace+"_"+workloadCount+"_"+pattern;
//					System.out.println("Main.main(): "+key);
//					if(!recordMap.containsKey(key)){
//						MaxMem=16384; //init
//						minMem=128; //init
//						tempMem=0; //int
//						try {
//							float[] res = new Main().evaluateFaasKeepaliveAuto(MaxMem,threshold,pattern,trace,workloadCount,recordLatency);
//							sloViolationRate=res[0];
//							recordMap.put(key,sloViolationRate);
//							if(sloViolationRate>0.01){
//								System.out.println("Main.main(): stop exploring sloViolationRate="+sloViolationRate+",trace="+trace+",workloadCount="+workloadCount+",pattern="+pattern+",maxServerMem="+MaxMem+",");
//								continue;
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//
//					 // 2分法探索可用内存
//
//					while(MaxMem-minMem>=256){
//						try {
//							tempMem=(int)((MaxMem+minMem)*1.0f/2);
//							float[] res = new Main().evaluateFaasKeepaliveAuto(tempMem,threshold,pattern,trace,workloadCount,recordLatency);
//							sloViolationRate=res[0];
//
//							if(sloViolationRate>=0.01f){
//								System.out.println("Main.main() find sloViolationRate="+sloViolationRate+",trace="+trace+",workloadCount="+workloadCount+",pattern="+pattern+",minMem="+minMem+",");
//								minMem=tempMem;
//							}else{
//								System.out.println("Main.main() find sloViolationRate="+sloViolationRate+",trace="+trace+",workloadCount="+workloadCount+",pattern="+pattern+",MaxMem="+MaxMem+",");
//								MaxMem=tempMem;
//							}
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//
//
//					}
//				}
//			}
//		}*/
//
//
//
//
//	}
//	private void evaluateFaasCacheManual(String[] args) throws InterruptedException, IOException {
//		long start=System.currentTimeMillis();
//		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
//		Repository.init(parm);
//
//		String pattern="hash";
//		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
//		new LoadDriver().readFile("F:\\cacheTrace\\new_trace\\e.csv",outputPathPrefix+"trace_a_2048_8_1000000_latency.csv", pattern,"", 1000000, false);
//		Repository.display(outputPathPrefix+"trace_e_20480_8_1000000.csv",1);
//		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
//	}
//
//	private float[] evaluateFaasCacheAutoLinux(int serverMemory, float threshold, String pattern, String trace, String workloadCount, boolean recordLatency) throws InterruptedException, IOException {
//
//		String workspacePath="/home/tank/1_yanan/cacheResearch/";
//
//		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
//		stopJavaThread.run();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		ExecutorDriverThread startFaasCacheThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/startJava-faascache.sh "+serverMemory);
//		startFaasCacheThread.start();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		/**
//		 * evaluate
//		 */
//
//		long start=System.currentTimeMillis();
//		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
//		Repository.init(parm);
//
//		String outputPathPrefix=workspacePath+"coldstartRate/temp/";
//		int requestCount=-1;
//		if(workloadCount.equals("full")){
//			requestCount=Integer.MAX_VALUE;
//		}else{
//			requestCount=Integer.parseInt(workloadCount);
//		}
//
//		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
//		new LoadDriver().readFile(workspacePath+"cacheTrace/new_trace/"+trace+".csv",outputPathPrefix+"latency_"+fileName,pattern, "", requestCount, recordLatency);
//		float[] res=Repository.display(outputPathPrefix+fileName,1);
//		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
//		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
//
//
//		/**
//		 * 结果数据汇总
//		 */
//		String resultFold="faascache_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
//		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh "+workspacePath+"workspace/resultHandler.sh "+resultFold);
//		resultHandlerThread.run();
//
//		ExecutorDriverThread stopJavaThread2=new ExecutorDriverThread("sh "+workspacePath+"workspace/stopJava.sh");
//		stopJavaThread2.run();
//
//		return res;
//	}
//
//	private void evaluateFaasKeepaliveManual(String[] args) throws InterruptedException, IOException {
//		long start=System.currentTimeMillis();
//		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
//		Repository.init(parm);
//
//		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
//		String pattern="hash";
//		boolean recordLatency=false;
//		int requestCount=1000000;
//		new LoadDriver().readFile("F:\\cacheTrace\\new_trace\\e.csv", outputPathPrefix+"trace_a_2048_8_1000000_latency.csv", pattern,"", requestCount, recordLatency);
//		Repository.display(outputPathPrefix+"trace_e_20480_8_1000000.csv",1);
//		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
//	}
//
//
//	private float[] evaluateFaasKeepaliveAuto(int serverMemory, float threshold, String pattern, String trace, String workloadCount, boolean recordLatency) throws InterruptedException, IOException {
//		ExecutorDriverThread startKeepAliveThread=new ExecutorDriverThread("sh D:\\cacheResearch\\workspace\\startJava-keepalive.sh "+serverMemory);
//		startKeepAliveThread.start();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		/**
//		 * evaluate
//		 */
//
//		long start=System.currentTimeMillis();
//		String parm="agent1_localhost_22221#agent2_localhost_22222#agent3_localhost_22223#agent4_localhost_22224#agent5_localhost_22225#agent6_localhost_22226#agent7_localhost_22227#agent8_localhost_22228";
//		Repository.init(parm);
//
//		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
//		int requestCount=0;
//		if(workloadCount.equals("full")){
//			requestCount=Integer.MAX_VALUE;
//		}else{
//			requestCount=Integer.parseInt(workloadCount);
//		}
//
//		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
//		new LoadDriver().readFile("F:\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName, pattern,"", requestCount, recordLatency);
//		float[] res=Repository.display(outputPathPrefix+fileName,1);
//		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
//		//System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
//
//
//		/**
//		 * 结果数据汇总
//		 */
//		String resultFold="keepalive_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
//		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh D:\\cacheResearch\\workspace\\resultHandler.sh "+resultFold);
//		resultHandlerThread.run();
//
//		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread("D:\\cacheResearch\\workspace\\stopJava.bat");
//		stopJavaThread.run();
//
//		return res;
//	}
//
//	public void evaluateOurworkManual(String[] args) throws InterruptedException, IOException {
//		long start=System.currentTimeMillis();
//		String parm="cacheManager_localhost_22220";
//		Repository.init(parm);
//
//		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
//		String pattern="";
//		boolean recordLatency=false;
//		int requestCount=100000;
//		String trace="e";
//		String fileName="trace_"+trace+"_15360_8_";                                                                                                                                                                                                                                                                                                                          
//		new LoadDriver().readFile("F:\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+fileName+"1000000_latency.csv","", pattern, requestCount, recordLatency);
//		Repository.display(outputPathPrefix+fileName+"1000000.csv",8);
//		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
//		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
//	}
//
//	private float[] evaluateOurworkAuto(int serverMemory, float threshold, String pattern, String trace, String workloadCount, boolean recordLatency) throws InterruptedException, IOException {
//		ExecutorDriverThread startCacheletThread=new ExecutorDriverThread("sh D:\\cacheResearch\\workspace\\startJava-cachelet.sh "+serverMemory);
//		startCacheletThread.start();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		ExecutorDriverThread startCacheManagerThread=new ExecutorDriverThread("sh D:\\cacheResearch\\workspace\\startJava-cacheManager.sh "+threshold);
//		startCacheManagerThread.start();
//
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		/**
//		 * evaluate
//		 */
//
//		long start=System.currentTimeMillis();
//		String parm="cacheManager_localhost_22220";
//		Repository.init(parm);
//
//		String outputPathPrefix="D:\\cacheResearch\\coldstartRate\\temp\\";
//		int requestCount=100000;
//		if(workloadCount.equals("full")){
//			requestCount=Integer.MAX_VALUE;
//		}else{
//			requestCount=Integer.parseInt(workloadCount);
//		}
//
//		String fileName="trace_"+trace+"_"+serverMemory+"_8_"+workloadCount+".csv";                                                                                                                                                                                                                                                                                                                          
//		new LoadDriver().readFile("F:\\cacheTrace\\new_trace\\"+trace+".csv",outputPathPrefix+"latency_"+fileName,"", pattern, requestCount, recordLatency);
//		float[] res=Repository.display(outputPathPrefix+fileName,8);
//		//168,52,0,4096,0.99975586,10000,0.0086,0.0052,0.9862,0.9914
//		System.out.println((System.currentTimeMillis()-start)/1000.0f+"s "+pattern);
//
//
//		String resultFold="cachelet_"+pattern+"_trace_"+trace+"_"+serverMemory+"_"+workloadCount;
//		ExecutorDriverThread resultHandlerThread=new ExecutorDriverThread("sh D:\\cacheResearch\\workspace\\resultHandler.sh "+resultFold);
//		resultHandlerThread.run();
//
//		ExecutorDriverThread stopJavaThread=new ExecutorDriverThread("D:\\cacheResearch\\workspace\\stopJava.bat");
//		stopJavaThread.run();
//
//		return res;
//	}
//
//}

}