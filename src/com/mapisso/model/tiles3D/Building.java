package com.mapisso.model.tiles3D;

import java.util.Map;

import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.MultiPolygon;

public class Building {
	
	private MultiPolygon buildingGeom;
	
	private Map<String, Object> attributes;
	
	private Envelope envelope;
	
	private SimpleFeatureType featureType;
	
	public Building() {
		
	}
	
	public Building(MultiPolygon buildingGeom, Map<String, Object> attributes, Envelope envelope, SimpleFeatureType featureType) {
		this.buildingGeom = buildingGeom;
		this.attributes = attributes;
		this.envelope = envelope;
		this.featureType = featureType;
	}

	public MultiPolygon getBuildingGeom() {
		return buildingGeom;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public Envelope getEnvelope() {
		return envelope;
	}

	public SimpleFeatureType getFeatureType() {
		return featureType;
	}
	
}
