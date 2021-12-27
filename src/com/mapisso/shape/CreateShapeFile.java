package com.mapisso.shape;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.mapisso.model.tiles3D.Building;
import com.vividsolutions.jts.geom.MultiPolygon;

public class CreateShapeFile {
	
	private WriteShapeFile writeShapeFile = new WriteShapeFile();

	public void create(List<Building> buildingObjectList, File file, CoordinateReferenceSystem inputCRS) {

		DefaultFeatureCollection collection = new DefaultFeatureCollection();
		SimpleFeatureType buildingTYPE = buildingObjectList.get(0).getFeatureType();
		
		for (int i = 0; i < buildingObjectList.size(); i++) {

			Building buildingObjectTemp = buildingObjectList.get(i);
			
			MultiPolygon buildingGeom = buildingObjectTemp.getBuildingGeom();
			
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(buildingTYPE);
			
			featureBuilder.add(buildingGeom);
			
			Map<String, Object>  attributeValues = buildingObjectTemp.getAttributes();
			
			for (Map.Entry<String, Object> entry : attributeValues.entrySet()) {
				
				featureBuilder.add(entry.getValue());
			}
			
			SimpleFeature feature = featureBuilder.buildFeature(null);
			collection.add(feature);

			try {
				writeShapeFile.write(file, collection, buildingTYPE, inputCRS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
