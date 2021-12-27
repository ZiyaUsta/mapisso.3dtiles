package com.mapisso.model.tiles3D;

public class Tile {
	
	private Asset asset;
	
	private Properties properties;
	
	private Double geometricError;
	
	private Node root;

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Double getGeometricError() {
		return geometricError;
	}

	public void setGeometricError(Double geometricError) {
		this.geometricError = geometricError;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	
}
