package com.mapisso.sensorpoints;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class SensorPoints {

public static List<Coordinate> generatePointsOnRoof (Polygon poly){
		
	
		Geometry bbox = poly.getEnvelope();
		GeometryFactory gf = new GeometryFactory();
		
		Coordinate [] coords = bbox.getCoordinates();
		
		
		// bbox u oluşturan vertice leri değişkenlere atıyorum, saat yönünün tersine doğru sol alttan 1 2 3 4 şeklinde
		
		Coordinate v1 = coords[0];
		Coordinate v2 = coords[1];
		Coordinate v3 = coords[2];
		Coordinate v4 = coords[3];
		
	
		double deltaX1 = Math.pow((v2.x-v1.x), 2);
		double deltaY1 = Math.pow((v2.y-v1.y), 2);
		
		
		// dikdörtgenin bir kenarı
		
		double d1 = Math.sqrt((deltaX1+deltaY1));
		
		
		double dt = 0.00; 
		
		List<Coordinate> pointList1 = new ArrayList<Coordinate>();
		List<Coordinate> pointList2 = new ArrayList<Coordinate>();
		List<Coordinate> pointListP = new ArrayList<Coordinate>();
		List<Coordinate> pointListToplam = new ArrayList<Coordinate>();
		
		while (dt<d1) {
		
		double t = dt/d1;
		
		// 1.çizginin noktalarını Coordinate tipinde oluşturma (şimdilik  zler sıfır attribute dan gelecek değer olmalı)
		Coordinate vt1 = new Coordinate(((1-t)*v1.x+t*v2.x), (((1-t)*v1.y+t*v2.y)), v1.z);
		
		pointList1.add(vt1);
		
		// 2. çizginin noktalarını Coordinate tipinde oluşturma
		Coordinate vt2 = new Coordinate(((1-t)*v4.x+t*v3.x), (((1-t)*v4.y+t*v3.y)), v3.z);
		pointList2.add(vt2);
		
		dt = dt+2;
		}
		
		// 1 ve 2. çizgiler arasında ara noktalar oluşturma
		
		for (int i = 0; i<pointList1.size(); i++){
			
			Coordinate sP = pointList1.get(i); 
			Coordinate eP = pointList2.get(i);
			
			double deltaXp = Math.pow((eP.x-sP.x), 2);
			double deltaYp = Math.pow((eP.y-sP.y), 2);
			
			
			double dp = Math.sqrt((deltaXp+deltaYp));
			
			double dtp = 0.00;
			
			while (dtp<dp) {
				
				double t = dtp/dp;
				
				Coordinate vtp = new Coordinate(((1-t)*sP.x+t*eP.x), ((1-t)*sP.y+t*eP.y), v3.z);
				
				pointListP.add(vtp);
				
				
				
				dtp = dtp+2;
				}
			
		}
		
		// oluşturulan tüm noktaları tek bir listeye ekleyip döndürme
		
		pointListToplam.addAll(pointList1);
		pointListToplam.addAll(pointList2);
		pointListToplam.addAll(pointListP);
		
		return pointListToplam;
		
	}
	
	
	public static List<Point> generatePointsOnFacades(SimpleFeature feature){
		
		Geometry g = (Geometry) feature.getDefaultGeometry();
		Geometry bbox = g.getEnvelope();
		GeometryFactory gf = new GeometryFactory();
		
		Coordinate [] coords = bbox.getCoordinates();
		
		
		// bbox u oluşturan vertice leri değişkenlere atıyorum, saat yönünün tersine doğru sol alttan 1 2 3 4 şeklinde
		
		Coordinate v1 = coords[0];
		Coordinate v2 = coords[1];
		Coordinate v3 = coords[2];
		Coordinate v4 = coords[3];
		
	
		double deltaX1 = Math.pow((v2.x-v1.x), 2);
		double deltaY1 = Math.pow((v2.y-v1.y), 2);
		
		
		// dikdörtgenin bir kenarı
		
		double d1 = Math.sqrt((deltaX1+deltaY1));
		
		
		double dt = 0.00; 
		
		List<Point> pointList1 = new ArrayList<Point>();
		List<Point> pointList2 = new ArrayList<Point>();
		List<Point> pointListToplam = new ArrayList<Point>();
		
		while (dt<d1) {
		
		double t = dt/d1;
		
		// 1.çizginin noktalarını Coordinate tipinde oluşturma (şimdilik  zler sıfır attribute dan gelecek değer olmalı)
		Coordinate vt1 = new Coordinate(((1-t)*v1.x+t*v2.x), (((1-t)*v1.y+t*v2.y)), 0.00);
		
		pointList1.add(gf.createPoint(vt1));
		
		// 2. çizginin noktalarını Coordinate tipinde oluşturma
		Coordinate vt2 = new Coordinate(((1-t)*v4.x+t*v3.x), (((1-t)*v4.y+t*v3.y)), 0.00);
		pointList2.add(gf.createPoint(vt2));
		
		dt = dt+2;
		}
		
		pointListToplam.addAll(pointList1);
		pointListToplam.addAll(pointList2);
		
		return pointListToplam;
		
		
	}
	

	
	public static double [] calculateRoofSurfaceNormal(SimpleFeature feature) {
		
		Geometry g = (Geometry) feature.getDefaultGeometry();
		Geometry bbox = g.getEnvelope();
		Coordinate[] coords = bbox.getCoordinates();
		
		Coordinate coordA = coords[0];
		
		System.out.println(coordA);
		Coordinate coordB = coords[1];
		Coordinate coordC = coords[3];
		
		// bbox tan 2 adet vectör oluşturuyoruz
		Coordinate vec1 = new Coordinate((coordB.x-coordA.x), (coordB.y-coordA.y), (coordB.z-coordA.z));
		Coordinate vec2 = new Coordinate((coordC.x-coordA.x), (coordC.y-coordA.y), (coordC.z-coordA.z));
		
		// bu iki vektörü kullanarak yüzey normalini hesaplıyoruz
		double [] normal = {((vec1.y*vec2.z)-(vec1.z*vec2.y)), ((vec1.z*vec2.x-vec1.x-vec2.z)), ((vec1.x*vec2.y)-(vec1.y*vec2.x))};
		
		// yüzey normalini, normalin boyutuna bölerek birim vektör haline getiriyoruz
		double lenght = Math.sqrt((Math.pow(normal[0], 2) + Math.pow(normal[1], 2) + Math.pow(normal[2], 2)));
		double [] normalized = {normal[0]/lenght, normal[1]/lenght, normal[2]/lenght};
    
	
		return normalized;
		
	}
	
	
  
	// çizgi üzerindeki noktalardan düşeyde yeni noktalar oluşturma
	public static List<Point> extrudePointsOnLines(List<Point> points, Double height){
		List<Point> extrudedPoints = new ArrayList<Point>();
		GeometryFactory gf = new GeometryFactory();
		Double z = points.get(0).getCoordinate().z;
		while (z<height) {
			for (int i = 0; i<points.size(); i++) {
				Double x = points.get(i).getX();
				Double y = points.get(i).getY();
				
				Coordinate vtz = new Coordinate (x,y,z);
				Point p = gf.createPoint(vtz);
				extrudedPoints.add(p);
				z = z+2;
			}
		}
		return extrudedPoints;
	}
	
	public Coordinate transformCartesian2D(Coordinate source) throws FactoryException, TransformException {
		CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
		CoordinateReferenceSystem destSRC = CRS.decode("EPSG:32637");
		boolean lenient = true; // allow for some error due to different datums
		MathTransform transform = CRS.findMathTransform(srcCRS, destSRC, lenient);
		return JTS.transform(source, null, transform);
	}
}

