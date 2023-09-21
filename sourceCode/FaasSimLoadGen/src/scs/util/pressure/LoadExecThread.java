package scs.util.pressure;

import java.io.FileWriter;
import java.util.concurrent.CountDownLatch;

import scs.repository.Repository;

/**
 * 请求发送线程,发送请求并记录时间
 * @author yanan
 *
 */
public class LoadExecThread extends Thread{
	private CountDownLatch begin;
	private int funcMemory;
	private long arrivalTime;
	private boolean evictionEnableFlag;
	private FileWriter writer;
	private int sleepUnit;
	/**
	 * 线程构造方法
	 * @param httpclient httpclient对象
	 * @param url 要访问的链接 
	 */
	public LoadExecThread(CountDownLatch begin, int funcMemory, long arrivalTime, boolean evictionEnableFlag, FileWriter writer,int sleepUnit){
		this.funcMemory=funcMemory;
		this.begin=begin;
		this.arrivalTime=arrivalTime;
		this.evictionEnableFlag=evictionEnableFlag;
		this.writer=writer;
		this.sleepUnit=sleepUnit;
	}

	@Override
	public void run(){
		try{
			begin.await();//
			Thread.sleep(sleepUnit);
			long start=System.nanoTime();
			Repository.cacheManagerSchedulerInterfaceCall(funcMemory, arrivalTime, evictionEnableFlag);
			synchronized (writer) {
				writer.write((System.nanoTime()-start)+"\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}



}
