package scs.pojo;

import scs.util.rmi.CacheletInterface;

public class AgentBean {
	private String agentName;
	private String ip;
	private int port;
	private CacheletInterface rmiInterface;
	
	public AgentBean(String agentName, String ip, int port) {
		super();
		this.agentName = agentName;
		this.ip = ip;
		this.port = port;
	}
	public AgentBean() {
	}
	
	public String getAgentName() {
		return agentName;
	}
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public CacheletInterface getRmiInterface() {
		return rmiInterface;
	}
	public void setRmiInterface(CacheletInterface rmiInterface) {
		this.rmiInterface = rmiInterface;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgentBean [agentName=");
		builder.append(agentName);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", port=");
		builder.append(port);
		builder.append("]");
		return builder.toString();
	}
	
}
