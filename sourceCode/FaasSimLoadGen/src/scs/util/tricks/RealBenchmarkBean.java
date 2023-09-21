package scs.util.tricks;

import java.awt.datatransfer.FlavorTable;

public class RealBenchmarkBean {
	private String funcName;
	private int memory;
	private float execTime;
	private float coldstartTime;
	
	public String getFuncName() {
		return funcName;
	}
	public int getMemory() {
		return memory;
	}
	public float getExecTime() {
		return execTime;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	public void setExecTime(float execTime) {
		this.execTime = execTime;
	}
	public float getColdstartTime() {
		return coldstartTime;
	}
	public void setColdstartTime(float coldstartTime) {
		this.coldstartTime = coldstartTime;
	}
	public RealBenchmarkBean() {};
	public RealBenchmarkBean(String funcName, int memory, float execTime, float coldstartTime) {
		super();
		this.funcName = funcName;
		this.memory = memory;
		this.execTime = execTime;
		this.coldstartTime=coldstartTime;
	}
	
	@Override
	public String toString() {
		return String.format("RealBenchmarkBean [funcName=%s, memory=%s, execTime=%s, coldstartTime=%s]", funcName, memory, execTime, coldstartTime);
	}
}
