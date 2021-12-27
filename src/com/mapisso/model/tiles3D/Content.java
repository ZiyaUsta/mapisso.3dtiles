package com.mapisso.model.tiles3D;

public class Content {
	
	private String uri;
	
	private Region boundingVolume;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Region getBoundingVolume() {
		return boundingVolume;
	}

	public void setBoundingVolume(Region boundingVolume) {
		this.boundingVolume = boundingVolume;
	}
	
}
