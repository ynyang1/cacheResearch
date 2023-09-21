package scs.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class FuncInstBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private ArrayList<InstanceBean> instanceList;
	private int receivedReqCount;
	private int evictionCount;
	private int releaseCount;
	private int coldStartCount;
	private int dropCount;
	private int warmStartCount;
	private int redirectedCount;
	private float memoryTimespanCost;
	private LinkedList<Long> arrivalReqList; //Long表示到达时间

	public FuncInstBean(ArrayList<InstanceBean> instanceList, int receivedReqCount, LinkedList<Long> arrivalReqList) {
		super();
		this.instanceList = instanceList;
		this.receivedReqCount = receivedReqCount;
		this.arrivalReqList = arrivalReqList;
	}
	public void setInstanceList(ArrayList<InstanceBean> instanceList) {
		this.instanceList = instanceList;
	}

	public ArrayList<InstanceBean> getInstanceList() {
		return instanceList;
	}

	public void addEvictionCount() {
		this.evictionCount++;
	}
	public void addEvictionCount(int evictionCount){
		this.evictionCount+=evictionCount;
	}

	public int getEvictionCount() {
		return evictionCount;
	}
	public void addReleaseCount() {
		this.releaseCount++;
	}
	public void addReleaseCount(int releaseCount){
		this.releaseCount+=releaseCount;
	}

	public int getReleaseCount() {
		return releaseCount;
	}

	public void addReceivedReqCount() {
		this.receivedReqCount++;
	}
	
	public void addReceivedReqCount(int receivedReqCount) {
		this.receivedReqCount+=receivedReqCount;
	}
 
	public int getReceivedReqCount() {
		return receivedReqCount;
	}

	public void addColdStartCount() {
		this.coldStartCount++;
	}

	public void addColdStartCount(int coldStartCount) {
		this.coldStartCount+=coldStartCount;
	}

	public int getColdStartCount() {
		return coldStartCount;
	}

	public void addWarmStartCount() {
		this.warmStartCount++;
	}

	public void addWarmStartCount(int warmStartCount) {
		this.warmStartCount+=warmStartCount;
	}
	
	public int getWarmStartCount() {
		return warmStartCount;
	}
	
	public void addRedirectedCount() {
		this.redirectedCount++;
	}

	public void addRedirectedCount(int redirectedCount) {
		this.redirectedCount+=redirectedCount;
	}

	public int getRedirectedCount() {
		return redirectedCount;
	}

	public void addDropCount() {
		this.dropCount++;
	}

	public void addDropCount(int dropCount) {
		this.dropCount+=dropCount;
	}

	public int getDropCount() {
		return dropCount;
	}

	public void addMemoryTimespanCost(float memoryTimeCost){
		this.memoryTimespanCost+=memoryTimeCost;
	}
	
	public float getMemoryTimespanCost(){
		return memoryTimespanCost;
	}
	
	public void addArrivalReqRecord(long reqArrivalTime, long timeWindowLength) {
		Long temp;
		for(Iterator<Long> item=arrivalReqList.iterator();item.hasNext();){
			temp=item.next();
			if(reqArrivalTime-temp>timeWindowLength){
				item.remove(); //删除keepalive过期的实例
				//System.out.println("FuncInstBean.addArrivalReqRecord(): remove old item, interval="+(reqArrivalTime-temp)+">timeWindowLength"+timeWindowLength);
			}
		}
		arrivalReqList.add(reqArrivalTime);
	} 
	
	public float getReqArrivalRate(long timeWindowLength){
		long currentTime=System.currentTimeMillis();
		int sum=0;
		for(Long item:arrivalReqList){
			if(currentTime-item<timeWindowLength){
				sum++;
			}
		}
		float avgRps=sum*1000.0f/timeWindowLength;
		
		return avgRps;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FuncInstBean [instanceList=");
		builder.append(instanceList);
		builder.append(", receivedReqCount=");
		builder.append(receivedReqCount);
		builder.append(", evictionCount=");
		builder.append(evictionCount);
		builder.append(", releaseCount=");
		builder.append(releaseCount);
		builder.append(", coldStartCount=");
		builder.append(coldStartCount);
		builder.append(", warmStartCount=");
		builder.append(warmStartCount);
		builder.append(", redirectedCount=");
		builder.append(redirectedCount);
		builder.append(", dropCount=");
		builder.append(dropCount);
		builder.append(", memoryTimespanCost=");
		builder.append(memoryTimespanCost);
		builder.append("]");
		return builder.toString();
	}


}

