package com.mapisso.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;

import com.mapisso.model.tiles3D.Building;
import com.mapisso.model.tiles3D.Node;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;

public class RtreeUtils {
	
	private CoordinateUtils coordUtils = new CoordinateUtils();

	public List<Building> createBuildingList(SimpleFeatureCollection featureCollection) {

		List<Building> buildingList = new ArrayList<Building>();

		SimpleFeatureType buildingType = featureCollection.getSchema();

		List<AttributeType> attributeList = buildingType.getTypes();

		SimpleFeatureIterator featureIterator = (SimpleFeatureIterator) featureCollection.features();
		try {
			while (featureIterator.hasNext()) {

				SimpleFeature feature = featureIterator.next();
				MultiPolygon polygon = (MultiPolygon) feature.getDefaultGeometry();

				Map<String, Object> attributes = new HashMap<String, Object>();

				for (int i = 0; i < attributeList.size(); i++) {

					AttributeType attribute = attributeList.get(i);
//					System.out.println("attribute type: " + attribute.getName().toString());
					if (!"MultiPolygon".equals(attribute.getName().toString())) {
						attributes.put(attribute.getName().toString(), feature.getAttribute(attribute.getName()));
					}

				}

				if(polygon != null) {
					Building building = new Building(polygon, attributes, polygon.getEnvelopeInternal(), buildingType);

					buildingList.add(building);
				}
				

			}
		} finally {
			featureIterator.close();
		}

		return buildingList;
	}

	public Number calculateMinimumHeightValue(List<Building> buildingList) {

		Number minimumHeight = null;

		for (int i = 0; i < buildingList.size(); i++) {

			Building building = buildingList.get(i);

			MultiPolygon polygon = building.getBuildingGeom();
			Coordinate[] coordinates = polygon.getCoordinates();
			for (int j = 0; j < coordinates.length; j++) {
				Coordinate tempCoord = coordinates[j];
				
				Double tempZ = tempCoord.z;

				if (tempZ.isNaN()) {
					minimumHeight = 0;
				} else {
					if (minimumHeight == null) {
						minimumHeight = tempZ;
					} else {
						if (tempZ.doubleValue() < minimumHeight.doubleValue()) {
							minimumHeight = tempZ;
						}
					}
				}

			}

		}

		return minimumHeight;

	}

	public Number calculateMaximumHeightValue(List<Building> buildingList) {

		Number maximumHeight = null;
		for (int i = 0; i < buildingList.size(); i++) {

			Building building = buildingList.get(i);
			MultiPolygon polygon = building.getBuildingGeom();
			
			Coordinate[] coordinates = polygon.getCoordinates();
			
			for (int j = 0; j < coordinates.length; j++) {
				Coordinate tempCoord = coordinates[j];
				Double z = tempCoord.z;
				
				Object height = building.getAttributes().get("height");
				
				if (z.isNaN()) {
					
					if(height instanceof Number) {
						z = ((Number) height).doubleValue();
					}else {
						throw new NumberFormatException("height value is not instance of number");
					}
					
				} else {
					
					if(height instanceof Number) {
						Double tempZ = ((Number) height).doubleValue();
						z = tempZ;
					}else {
						throw new NumberFormatException("height value is not instance of number");
					}
				}
				
				System.out.println("z: " + z);
				
				if (maximumHeight == null) {
					maximumHeight = z;
				} else {
					if (z > maximumHeight.doubleValue()) {
						maximumHeight = z;
					}
				}

			}

		}

		return maximumHeight;
	}
	
	public Double calculateGeometricError(Node node) {

		Double geometricError = null;
		
		Building maxBuilding = null;

		List<Building> list = node.getData();
		
		for (int i = 0; i < list.size(); i++) {

			Building buildingObject = list.get(i);

			Double tempArea = buildingObject.getBuildingGeom().getArea();
			
			if (geometricError == null) {
				maxBuilding = buildingObject;
			} else {
				if (geometricError < tempArea) {
					maxBuilding = buildingObject;
				}
			}

		}
		
		Coordinate[] coords = maxBuilding.getBuildingGeom().getCoordinates();
		
		
		geometricError = coordUtils.calculateDistanceDegree(coords[0].x, coords[0].y, coords[1].x, coords[1].y);
		
		return geometricError * 250;
	}

}
