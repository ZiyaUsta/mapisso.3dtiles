package com.mapisso.model.tiles3D;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Envelope;

public class Region {
	
	private List<Double> region;
	
	@JsonIgnore
	private CoordinateReferenceSystem sourceCRS;
	
	public Region (Envelope envelope, Height height, CoordinateReferenceSystem sourceCRS) {
		
		List<Double> region = new ArrayList<Double>();
		
		CoordinateReferenceSystem targetCRS = null;
		
		CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
		
		try {
			targetCRS = factory.createCoordinateReferenceSystem("EPSG:4326");
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MathTransform transform = null;
		try {
			transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			envelope = JTS.transform(envelope, transform);
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		region.add(Math.toRadians(envelope.getMinX()));
		region.add(Math.toRadians(envelope.getMinY()));
		region.add(Math.toRadians(envelope.getMaxX()));
		region.add(Math.toRadians(envelope.getMaxY()));
		
		region.add(height.getMinimum());
		region.add(height.getMaximum());
		
		this.region = region;
		this.sourceCRS = sourceCRS;
		
	}

	public List<Double> getRegion() {
		return region;
	}

	public CoordinateReferenceSystem getSourceCRS() {
		return sourceCRS;
	}
	
}
