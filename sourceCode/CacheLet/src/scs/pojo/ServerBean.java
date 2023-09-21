package scs.pojo;

import java.util.Map;

public class ServerBean {
	private String agentName;
	private String ip;
	private int port;
	private int memorySize;
	private int availMemorySize;
	private Map<String, FuncInstBean> funcInstMap; //<key, value> = <funcName, funcInstBean>

	public ServerBean(String agentName, String ip, int port, int memorySize, int availMemorySize,
			Map<String, FuncInstBean> funcInstMap, float logicalClock) {
		super();
		this.agentName = agentName;
		this.ip = ip;
		this.port = port;
		this.memorySize = memorySize;
		this.availMemorySize = availMemorySize;
		this.funcInstMap = funcInstMap; 
	}

	public String getAgentName() {
		return agentName;
	}
	public int getMemorySize() {
		return memorySize;
	}
	public int getAvailMemorySize() {
		return availMemorySize;
	}
	public Map<String, FuncInstBean> getFuncInstMap() {
		return funcInstMap;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}
	public void setAvailMemorySize(int availMemorySize) {
		this.availMemorySize = availMemorySize;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
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
		builder.append(", memorySize=");
		builder.append(memorySize);
		builder.append(", availMemorySize=");
		builder.append(availMemorySize);
		builder.append(", funcInstMap=");
		builder.append(funcInstMap); 
		builder.append("]");
		return builder.toString();
	}


}
