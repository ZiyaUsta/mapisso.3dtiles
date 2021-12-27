package com.mapisso.model.b3dm.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeatureTable {
	
	private Integer BATCH_LENGTH;

	public Integer getBATCH_LENGTH() {
		return BATCH_LENGTH;
	}
	
	@JsonProperty("BATCH_LENGTH")
	public void setBATCH_LENGTH(Integer bATCH_LENGTH) {
		BATCH_LENGTH = bATCH_LENGTH;
	}
	
	
	

}
