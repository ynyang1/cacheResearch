package scs.pojo;

import java.io.Serializable;
import java.util.Map;

public class StatisticsBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String agentName;
	private int memorySize;
	private int availMemorySize;
	private int deployedFunCount;
	private int deployedInstanceCount;
	private int totalEvictionCount;
	private int totalReleaseCount;
	private int totalRequestCount;
	private int totalColdstartCount;
	private int totalWarmstartCount;
	private int totalRedirectedCount;
	private int totalDropCount; 
	
	private Map<String, FuncInstBean> funcLevelStatistics;
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public int getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}
	public int getAvailMemorySize() {
		return availMemorySize;
	}
	public void setAvailMemorySize(int availMemorySize) {
		this.availMemorySize = availMemorySize;
	}
	public int getDeployedFunCount() {
		return deployedFunCount;
	}
	public void setDeployedFunCount(int deployedFunCount) {
		this.deployedFunCount = deployedFunCount;
	}
	public int getDeployedInstanceCount() {
		return deployedInstanceCount;
	}
	public void setDeployedInstanceCount(int deployedInstanceCount) {
		this.deployedInstanceCount = deployedInstanceCount;
	}
	public int getTotalEvictionCount() {
		return totalEvictionCount;
	}
	public void setTotalEvictionCount(int totalEvictionCount) {
		this.totalEvictionCount = totalEvictionCount;
	}
	public int getTotalReleaseCount() {
		return totalReleaseCount;
	}
	public void setTotalReleaseCount(int totalReleaseCount) {
		this.totalReleaseCount = totalReleaseCount;
	}
	public int getTotalRequestCount() {
		return totalRequestCount;
	}
	public void setTotalRequestCount(int totalRequestCount) {
		this.totalRequestCount = totalRequestCount;
	}
	public int getTotalColdstartCount() {
		return totalColdstartCount;
	}
	public void setTotalColdstartCount(int totalColdstartCount) {
		this.totalColdstartCount = totalColdstartCount;
	}
	public int getTotalWarmstartCount() {
		return totalWarmstartCount;
	}
	public void setTotalWarmstartCount(int totalWarmstartCount) {
		this.totalWarmstartCount = totalWarmstartCount;
	}
	public int getTotalDropCount() {
		return totalDropCount;
	}
	public void setTotalDropCount(int totalDropCount) {
		this.totalDropCount = totalDropCount;
	}
	public int getTotalRedirectedCount() {
		return totalRedirectedCount;
	}
	public void setTotalRedirectedCount(int totalRedirectedCount) {
		this.totalRedirectedCount = totalRedirectedCount;
	}
	
	public float getSloViolationRate() {
		return 1-1.0f*totalWarmstartCount/(totalColdstartCount+totalWarmstartCount+totalDropCount);
	}
	public float getMemoryUsageRate() {
		return 1-1.0f*availMemorySize/memorySize;
	}
	
	public Map<String, FuncInstBean> getFuncLevelStatistics() {
		return funcLevelStatistics;
	}
	public void setFuncLevelStatistics(Map<String, FuncInstBean> funcLevelStatistics) {
		this.funcLevelStatistics=funcLevelStatistics;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatisticsBean [agentName=");
		builder.append(agentName);
		builder.append(", memorySize=");
		builder.append(memorySize);
		builder.append(", availMemorySize=");
		builder.append(availMemorySize);
		builder.append(", deployedFunCount=");
		builder.append(deployedFunCount);
		builder.append(", deployedInstanceCount=");
		builder.append(deployedInstanceCount);
		builder.append(", totalEvictionCount=");
		builder.append(totalEvictionCount);
		builder.append(", totalReleaseCount=");
		builder.append(totalReleaseCount);
		builder.append(", totalRequestCount=");
		builder.append(totalRequestCount);
		builder.append(", totalColdstartCount=");
		builder.append(totalColdstartCount);
		builder.append(", totalWarmstartCount=");
		builder.append(totalWarmstartCount);
		builder.append(", totalRedirectedCount=");
		builder.append(totalRedirectedCount);
		builder.append(", totalDropCount=");
		builder.append(totalDropCount);
		builder.append(", funcLevelStatistics=");
		builder.append(funcLevelStatistics);
		builder.append("]");
		return builder.toString();
	}
}
