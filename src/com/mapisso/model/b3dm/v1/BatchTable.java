package com.mapisso.model.b3dm.v1;

import java.util.List;

public class BatchTable {

	private List<Integer> batchId;
	
	private List<String> name;

	public List<Integer> getBatchId() {
		return batchId;
	}

	public void setBatchId(List<Integer> batchId) {
		this.batchId = batchId;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}
	
	

}
