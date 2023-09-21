package scs.pojo;

import java.io.Serializable;

public class ServerStatusBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String agentName;
	private int availMemorySize;
	private boolean instanceExist;
	private int hotspotScore;
	private TwoTuple<String,Integer> funcInvocFreq;
	
	public ServerStatusBean() {}
	
	public ServerStatusBean(String agentName, int availMemorySize, boolean instanceExist, int hotspotScore) {
		super();
		this.agentName = agentName;
		this.availMemorySize = availMemorySize;
		this.instanceExist = instanceExist;
		this.hotspotScore = hotspotScore;
	}
	
	public String getAgentName() {
		return agentName;
	}
	public int getAvailMemorySize() {
		return availMemorySize;
	}
	public int getHotspotScore() {
		return hotspotScore;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public void setAvailMemorySize(int availMemorySize) {
		this.availMemorySize = availMemorySize;
	}
	public void setHotspotScore(int hotspotScore) {
		this.hotspotScore = hotspotScore;
	}
	
	public boolean getInstanceExist() {
		return instanceExist;
	}

	public void setInstanceExist(boolean instanceExist) {
		this.instanceExist = instanceExist;
	}

	public TwoTuple<String, Integer> getFuncInvocFreq() {
		return funcInvocFreq;
	}

	public void setFuncInvocFreq(TwoTuple<String, Integer> funcInvocFreq) {
		this.funcInvocFreq = funcInvocFreq;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServerStatusBean [agentName=");
		builder.append(agentName);
		builder.append(", availMemorySize=");
		builder.append(availMemorySize);
		builder.append(", instanceExist=");
		builder.append(instanceExist);
		builder.append(", hotspotScore=");
		builder.append(hotspotScore);
		builder.append(", funcInvocFreq=");
		builder.append(funcInvocFreq.toString());
		builder.append("]");
		return builder.toString();
	}

}
