package com.mapisso.model.b3dm.v1;

public class B3DM {
	
	private Header header;
	
	private FeatureTable featureTable;
	
	private BatchTable batchTable;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public FeatureTable getFeatureTable() {
		return featureTable;
	}

	public void setFeatureTable(FeatureTable featureTable) {
		this.featureTable = featureTable;
	}

	public BatchTable getBatchTable() {
		return batchTable;
	}

	public void setBatchTable(BatchTable batchTable) {
		this.batchTable = batchTable;
	}
	
	

}
