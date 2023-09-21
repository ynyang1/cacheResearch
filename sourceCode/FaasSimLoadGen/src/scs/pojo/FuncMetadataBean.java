package scs.pojo;

import java.io.Serializable;

public class FuncMetadataBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String funcName;
	private int memoryConsume;
	private int startUpTime;
	private int executionTime;
	private int totalInvocation;
	
	public FuncMetadataBean(String funcName, int memoryConsume, int startUpTime, int executionTime, int totalInvocation) {
		super();
		this.funcName = funcName;
		this.memoryConsume = memoryConsume;
		this.startUpTime = startUpTime;
		this.executionTime = executionTime; 
		this.totalInvocation = totalInvocation;
	}

	public String getFuncName() {
		return funcName;
	}

	public int getMemoryConsume() {
		return memoryConsume;
	}

	public int getStartUpTime() {
		return startUpTime;
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public void setMemoryConsume(int memoryConsume) {
		this.memoryConsume = memoryConsume;
	}

	public void setStartUpTime(int startUpTime) {
		this.startUpTime = startUpTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}
	
//	public void addTotalInvocation(){
//		this.totalInvocation++;
//	}
//	public int getTotalInvocation(){
//		return totalInvocation;
//	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FuncMetadataBean [funcName=");
		builder.append(funcName);
		builder.append(", memoryConsume=");
		builder.append(memoryConsume);
		builder.append(", startUpTime=");
		builder.append(startUpTime);
		builder.append(", executionTime=");
		builder.append(executionTime);
		builder.append(", totalInvocation=");
		builder.append(totalInvocation);
		builder.append("]");
		return builder.toString();
	}
	 
	
	 
}
