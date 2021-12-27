package com.mapisso.test;

import java.io.File;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.mapisso.shape.ReadShapeFile;
import com.mapisso.utils.CoordinateUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class transformUtm {
	
	private static ReadShapeFile readShapeFile = new ReadShapeFile();
	private static CoordinateUtils cUtils = new CoordinateUtils();
	
	public static Coordinate transformToUtm(Coordinate source) throws FactoryException, TransformException {
		
		CRSAuthorityFactory   factory = CRS.getAuthorityFactory(true);
		CoordinateReferenceSystem destSRC = factory.createCoordinateReferenceSystem("EPSG:32637");
		CoordinateReferenceSystem srcCRS = DefaultGeocentricCRS.CARTESIAN;
		boolean lenient = true; // allow for some error due to different datums
		MathTransform transform = CRS.findMathTransform(srcCRS, destSRC, lenient);
		return JTS.transform(source, null, transform);
	}
	
	
	public static Coordinate transformToUtm2(Coordinate source) throws FactoryException, TransformException {
		
		CRSAuthorityFactory   factory = CRS.getAuthorityFactory(true);
		CoordinateReferenceSystem destSRC = factory.createCoordinateReferenceSystem("EPSG:32637");
		CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
		boolean lenient = true; // allow for some error due to different datums
		MathTransform transform = CRS.findMathTransform(srcCRS, destSRC, lenient);
		return JTS.transform(source, null, transform);
	}

	public static void main(String[] args) throws FactoryException, TransformException {
		
		
		File fileDirectory = new File("/Users/ziya/Documents/test/2bina.shp");
		
		SimpleFeatureCollection sfc = readShapeFile.getSFC(fileDirectory);
		
		SimpleFeatureIterator itr = sfc.features();
		
		while( itr.hasNext()){
			
			SimpleFeature f = itr.next();
			
			Geometry  g = (Geometry) f.getDefaultGeometry();
			
			Coordinate[] coords = g.getCoordinates();
			
			
			
			
			for (int i = 0; i<coords.length; i++) {
				
				Coordinate c = cUtils.transformToPlanar(coords[i]);
				
				System.out.println("geocentric "+c.z);
				
				Coordinate c1 = transformToUtm2(coords[i]);
				
				System.out.println("utm "+c1.z);
				
				
			}
			
			
			
			
		}
		
	
		// TODO Auto-generated method stub

	}

}
