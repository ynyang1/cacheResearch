package scs.util.pressure;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.omg.CORBA.SystemException;

import scs.pojo.AgentBean;
import scs.pojo.TwoTuple;
import scs.repository.Repository;


public class Test {

	public static void main(String[] args){

		try {
			new Test().evaluateOurworkManual(args);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void evaluateOurworkManual(String[] args) throws InterruptedException, IOException {
		long start=System.currentTimeMillis();
		int concurrency=100;
		FileWriter writer=new FileWriter("D:\\cacheResearch\\workspace\\concurrency\\"+concurrency+".csv");
		String parm="cacheManager-dev_localhost_33330";
		String funLevelOutputFile="D:\\cacheResearch\\output\\overall_func_level.csv";
		Repository.init(parm,funLevelOutputFile);

		new Test().test(concurrency,writer);

	}

	private void test(int concurrency, FileWriter writer){
		ExecutorService executor = Executors.newCachedThreadPool();
		Random random=new Random();
		CountDownLatch begin=new CountDownLatch(1);
		for (int i=0;i<concurrency;i++){
			int funcMemory=random.nextInt(512);
			long arrivalTime=System.currentTimeMillis();
			boolean evictionEnableFlag=random.nextBoolean();
			int sleepUnit=1000/concurrency;
			funcMemory=256;
			evictionEnableFlag=true;
			executor.execute(new LoadExecThread(begin, funcMemory, arrivalTime, evictionEnableFlag, writer,sleepUnit));
		}
		begin.countDown();
		
		executor.shutdown();
		while(!executor.isTerminated()){
			 
		}
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static Random random=new Random();
	public static AgentBean test_scheduleFunc(int funcMemory, long arrivalTime, boolean evictionEnableFlag) throws InterruptedException{
		long start=System.nanoTime();
		float maxScore=-1;
		AgentBean candidator=null;
		
		for(int i=0;i<1;i++){
			Thread.sleep(2);
			TwoTuple<Integer,Float> result=new TwoTuple<>(random.nextInt(25600),random.nextFloat());
					//rmiService.test_calServerHotDegreeCall(agent,arrivalTime, evictionEnableFlag); 
			//System.out.println("Repository.scheduleFunc(): agent "+agent.getAgentName()+" availMem="+result.first+" score="+result.second);

			if(funcMemory<=result.first&&result.second>maxScore){ //result.second=availMemory*1.0f/hotDegreeSum;  the larger, the better
				maxScore=result.second;
			}
		}
		/*if(candidator==null){
			//System.out.println("Repository.scheduleFunc(): candidator is null, funcMemory="+funcMemory+" score="+maxScore);
		}else{
			//System.out.println("Repository.scheduleFunc(): candidator="+candidator.getAgentName()+" funcMemory="+funcMemory+" score="+maxScore);
		}*/
		System.out.println((System.nanoTime()-start));
		return candidator;
	}
}
