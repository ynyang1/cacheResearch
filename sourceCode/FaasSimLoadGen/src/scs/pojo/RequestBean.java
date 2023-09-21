package scs.pojo;

import java.io.Serializable;

public class RequestBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long arrivalTime;
	private int tryTimes;
	private FuncMetadataBean funcMetadata;
	private int extraLatency;
	
	public RequestBean(String funcName, long arrivalTime, int memoryConsume, int startUpTime, int executionTime) {
		super(); 
		this.arrivalTime = arrivalTime;
		this.funcMetadata = new FuncMetadataBean(funcName, memoryConsume, startUpTime, executionTime,0);
	}
	 
	public long getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	public int getTryTimes() {
		return tryTimes;
	}

	public void setTryTimes(int tryTimes) {
		this.tryTimes = tryTimes;
	}

	public FuncMetadataBean getFuncMetadata(){
		return funcMetadata;
	}

	public int getExtraLatency() {
		return extraLatency;
	}

	public void setExtraLatency(int extraLatency) {
		this.extraLatency = extraLatency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestBean [arrivalTime=");
		builder.append(arrivalTime);
		builder.append(", funcMetadata=");
		builder.append(funcMetadata);
		builder.append("]");
		return builder.toString();
	}
 

}
