package com.mapisso.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

public class CoordinateUtils {
	
	private static Logger logger = LoggerFactory.getLogger(CoordinateUtils.class);
	
	/**
	 * 
	 * Takes coordinate array which is building base coordinates and height value of building and
	 * extrude building base coordinates using height value.
	 * @param coordinates {@link com.vividsolutions.jts.geom.Coordinate} 
	 * @param height {@link Double}
	 * @return List<{@link com.vividsolutions.jts.geom.Coordinate}>
	 * @throws TransformException 
	 * @throws FactoryException 
	 */
	public List<Coordinate> extrudedCoordinate(Coordinate[] coordinates, Double height)  {
		
//		logger.info("extrudedCoordinate method started.");
		
		Coordinate[] coords = new Coordinate[coordinates.length];
		
		for (int i = 0; i < coordinates.length; i++) {
			try {
				coords[i] = transformToPlanar(coordinates[i]);
			} catch (FactoryException | TransformException e) {
				logger.error("planar coordinates transformation error: ", e);
			}
		}
		
		ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>(Arrays.asList(coords));
		
		Coordinate A = null;
		Coordinate B = null;
		Coordinate C = null;
		
		try {
			A = transformToPlanar(coordinates[0]);
			B = transformToPlanar(coordinates[1]);
			C = transformToPlanar(coordinates[2]);
		} catch (FactoryException | TransformException e) {
			logger.error("planar coordinates transformation error: ", e);
		}
		
		double[] vector1 = new double[]{B.x - A.x, B.y - A.y, B.z - A.z};
		double[] vector2 = new double[]{C.x - A.x, C.y - A.y, C.z - A.z};
		
		double[] normal = new double[] {(vector1[1]*vector2[2]-vector1[2]*vector2[1]), (vector1[2]*vector2[0]-vector1[0]*vector2[2]), (vector1[0]*vector2[1]-vector1[1]*vector2[0])};
		
		double n1 = normal[0]/(Math.sqrt(((Math.pow(normal[0], 2)) + (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));
		double n2 = normal[1]/(Math.sqrt(((Math.pow(normal[0], 2)) + (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2))))); 
		double n3 = normal[2]/(Math.sqrt(((Math.pow(normal[0], 2)) + (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));
		
		double[] nH = new double[] {Math.abs(n1*height), Math.abs(n2*height),Math.abs(n3*height)};
		
		for (int i = 0; i < coordinates.length; i++) {
			Double z = 0.0;
			if(!Double.isNaN(coords[i].z)) {
				z = coords[i].z;
			}
			Coordinate coord = new Coordinate(coords[i].x + nH[0], coords[i].y + nH[1], z + nH[2]);
			coordinateList.add(coord);
			
		}
		
//		logger.info("extrudedCoordinate method finished.");
		System.out.println(coordinates.length);
		System.out.println(coordinateList.size());
		
		return coordinateList;

	}
	
	public Coordinate transformToPlanar(Coordinate source) throws FactoryException, TransformException {
		CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
		CoordinateReferenceSystem destSRC = DefaultGeocentricCRS.CARTESIAN;
		boolean lenient = true; // allow for some error due to different datums
		MathTransform transform = CRS.findMathTransform(srcCRS, destSRC, lenient);
		return JTS.transform(source, null, transform);
	}
	

	
	
	public double calculateDistanceDegree(double lat1, double lon1, double lat2, double lon2) {

		String unit = "K";

		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
					+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;

			if (unit == "K") {
				dist = dist * 1.609344;
			} else if (unit == "N") {
				dist = dist * 0.8684;
			}
			return (dist * 1000);
		}
	}

	public double calculateDistance(double x1, double y1, double x2, double y2) {

		Double dist = 0.0;
		if ((x1 == x2) && (y1 == y2)) {
			return dist;
		} else {
			dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		}

		return dist;

	}
	
}
