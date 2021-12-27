package com.mapisso.model.tiles3D;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Envelope;

public class Node {
	
	private Region boundingVolume;
	
	private Double geometricError;
	
	private String refine;
	
	private Content content;
	
	private List<Node> children;
	
	@JsonIgnore
	private List<Building> data;
	
	@JsonIgnore
	private Envelope envelope;
	
	@JsonIgnore
	private Node parent;
	
	public Region getBoundingVolume() {
		return boundingVolume;
	}

	public void setBoundingVolume(Region boundingVolume) {
		this.boundingVolume = boundingVolume;
	}

	public Double getGeometricError() {
		return geometricError;
	}

	public void setGeometricError(Double geometricError) {
		this.geometricError = geometricError;
	}

	public String getRefine() {
		return refine;
	}

	public void setRefine(String refine) {
		this.refine = refine;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public List<Building> getData() {
		return data;
	}

	public void setData(List<Building> data) {
		this.data = data;
	}

	public Envelope getEnvelope() {
		return envelope;
	}

	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

}
