package com.mapisso.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import com.mapisso.shape.ReadShapeFile;
import com.mapisso.shape.WriteShapeFile;
import com.mapisso.utils.CoordinateUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.mapisso.model.tiles3D.Building;
import com.mapisso.sensorpoints.SensorPoints;
import com.mapisso.shape.CreateShapeFile;


public class SensorPointsTest {
	
	private static ReadShapeFile readShapeFile = new ReadShapeFile();
	private static WriteShapeFile writeShapeFile = new WriteShapeFile();
	private static SensorPoints sp = new SensorPoints();
	private static CoordinateUtils coordinateUtils = new CoordinateUtils();
		

	public static void main(String[] args) throws FactoryException, TransformException, IOException {
		
		
		File fileDirectory = new File("/Users/ziya/Documents/test/binadenemepoints84.shp");
		
		SimpleFeatureCollection sfc = readShapeFile.getSFC(fileDirectory);
		
		Geometry geom = (Geometry) sfc.features().next().getDefaultGeometry();
		

		
		
		
		
		// son noktası atılmamış koordinatlar poligon üretmek için
		Coordinate[] coords = geom.getCoordinates();
		
		// son noktası atılmış koordinatlar extrude için
		Coordinate[] coords1 = ArrayUtils.remove(geom.getCoordinates(), geom.getCoordinates().length-1);
		
		int n = coords.length;
		
		System.out.println(n);
		
		List<Coordinate> cartesian = new ArrayList<Coordinate>();
		
		for (int j = 0; j<n; j++) {
		Coordinate	coord = sp.transformCartesian2D(coords[j]);
		cartesian.add(coord);
			
		}
		
		
		
		Double height = Double.valueOf(sfc.features().next().getAttribute("height").toString());
		

		// Create a GeometryFactory if you don't have one already
		GeometryFactory geometryFactory = new GeometryFactory();

		// Simply pass an array of Coordinate or a CoordinateSequence to its method
		Polygon polygonFromCoordinates = geometryFactory.createPolygon((Coordinate[]) cartesian.toArray(new Coordinate[] {}));
		
		System.out.println(polygonFromCoordinates.getCoordinates()[5]);

		
		List<Coordinate> coordinateList = new ArrayList<Coordinate>(Arrays.asList(coords1));
		
		System.out.println(coordinateList.size());

		List<Coordinate> extCoordinateList = new ArrayList<Coordinate>();
		
		
		
		double[] normal = sp.calculateRoofSurfaceNormal(sfc.features().next());
		
		double[] nH = {normal[0]*height, normal[1]*height, normal[2]*height};
		
		extCoordinateList = (ArrayList<Coordinate>) coordinateUtils.extrudedCoordinate(coords1, height);
		
		
		System.out.println(extCoordinateList.size());
	
		
		
	//	List<Coordinate> extrudedOnly = extCoordinateList.subList(n, extCoordinateList.size());
	//	System.out.println(extrudedOnly.size());
		
		
	//	Coordinate ext = extrudedOnly.get(0);
		
	//	extrudedOnly.add(ext);
		
	
	
	//	Polygon poly = geometryFactory.createPolygon((Coordinate[]) extrudedOnly.toArray(new Coordinate[] {}));
		
	//	System.out.println(extrudedOnly.size());
		
	
    List<Coordinate> bboxPoly = sp.generatePointsOnRoof(polygonFromCoordinates);
    
    System.out.println(bboxPoly.get(20));
    
    List<Coordinate> bboxPolyZ = new ArrayList<Coordinate>();
    
    for (int m = 0; m<bboxPoly.size(); m++) {
    	bboxPoly.get(m).z = height;
    }

    System.out.println(bboxPoly.get(20));
    System.out.println(normal[0]);
    
    String result = "";
    
    BufferedWriter writer = null;
    
    writer = new BufferedWriter(new FileWriter("/Users/ziya/Documents/test/sensor.pts"));
    
    for (int n1 = 0; n1<bboxPoly.size(); n1++) {
    	result = result+bboxPoly.get(n1).x+" "+bboxPoly.get(n1).y+" "+bboxPoly.get(n1).z+" "+normal[0]+" "+normal[1]+" "+normal[2]+"\n";
    }
    
    
    
    
	try {
		
		writer.write(result);

	} catch (IOException e) {
	} finally {
		try {
			if (writer != null)
				writer.close();
		} catch (IOException e) {
		}
	}
    
    
	}

}
