package scs.pojo;

import java.io.Serializable;

public class CacheAreaBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int tempAreaMem=0;
	private int tempAreaInstCount=0;
	private int consistAreaMem=0;
	private int consistAreaInstCount=0;
	private int availMemory=0;

	public CacheAreaBean(int tempAreaMem, int tempAreaInstCount, int consistAreaMem, int consistAreaInstCount, int availMemory) {
		super();
		this.tempAreaMem = tempAreaMem;
		this.tempAreaInstCount = tempAreaInstCount;
		this.consistAreaMem = consistAreaMem;
		this.consistAreaInstCount = consistAreaInstCount;
		this.availMemory = availMemory;
	}

	public int getTempAreaMem() {
		return tempAreaMem;
	}

	public int getTempAreaInstCount() {
		return tempAreaInstCount;
	}

	public int getConsistAreaMem() {
		return consistAreaMem;
	}

	public int getConsistAreaInstCount() {
		return consistAreaInstCount;
	}

	public int getAvailMemory() {
		return availMemory;
	}

	public void setTempAreaMem(int tempAreaMem) {
		this.tempAreaMem = tempAreaMem;
	}

	public void setTempAreaInstCount(int tempAreaInstCount) {
		this.tempAreaInstCount = tempAreaInstCount;
	}

	public void setConsistAreaMem(int consistAreaMem) {
		this.consistAreaMem = consistAreaMem;
	}

	public void setConsistAreaInstCount(int consistAreaInstCount) {
		this.consistAreaInstCount = consistAreaInstCount;
	}

	public void setAvailMemory(int availMemory) {
		this.availMemory = availMemory;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CacheAreaBean [tempAreaMem=");
		builder.append(tempAreaMem);
		builder.append(", tempAreaInstCount=");
		builder.append(tempAreaInstCount);
		builder.append(", consistAreaMem=");
		builder.append(consistAreaMem);
		builder.append(", consistAreaInstCount=");
		builder.append(consistAreaInstCount);
		builder.append(", availMemory=");
		builder.append(availMemory);
		builder.append("]");
		return builder.toString();
	}
 


}	
