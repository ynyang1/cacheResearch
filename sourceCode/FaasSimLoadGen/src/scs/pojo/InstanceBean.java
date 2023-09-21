package scs.pojo;

import java.io.Serializable;

public class InstanceBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String funcName;
	private long createTime;
	private long lastActiveTimeStart;
	private long lastActiveTimeEnd;
	private boolean expellable;
	
	public InstanceBean(String funcName, long createTime, long lastActiveTimeStart, long lastActiveTimeEnd, boolean expellable) {
		this.funcName = funcName;
		this.createTime = createTime;
		this.lastActiveTimeStart = lastActiveTimeStart;
		this.lastActiveTimeEnd = lastActiveTimeEnd;
		this.expellable = expellable;
	}
	
	public String getFuncName() {
		return funcName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public long getLastActiveTimeStart() {
		return lastActiveTimeStart;
	}
	public long getLastActiveTimeEnd() {
		return lastActiveTimeEnd;
	}
	public boolean getExpellable() {
		return expellable;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public void setLastActiveTimeStart(long lastActiveTimeStart) {
		this.lastActiveTimeStart = lastActiveTimeStart;
	}
	public void setLastActiveTimeEnd(long lastActiveTimeEnd) {
		this.lastActiveTimeEnd = lastActiveTimeEnd;
	}
	public void setExpellable(boolean expellable) {
		this.expellable = expellable;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InstanceBean [funcName=");
		builder.append(funcName);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", lastActiveTimeStart=");
		builder.append(lastActiveTimeStart);
		builder.append(", lastActiveTimeEnd=");
		builder.append(lastActiveTimeEnd);
		builder.append(", expellable=");
		builder.append(expellable);
		builder.append("]");
		return builder.toString();
	}
	
	 
	
}
