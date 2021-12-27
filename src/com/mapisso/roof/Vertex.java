package com.mapisso.roof;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.lang3.ArrayUtils;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

public class Vertex {
	
	Coordinate position;
	Coordinate prevEdge;
	Coordinate nextEdge;
	Coordinate bisector;

	
public static Coordinate getAngularBiSector(Coordinate c1, Coordinate c2) {
	
	Coordinate v1 =  new Coordinate((c1.x+c2.x), (c1.y+c2.y), (c1.z+c2.z));
	
	double X2 = Math.pow(v1.x, 2);
	double Y2 = Math.pow(v1.y, 2);
	double Z2 = Math.pow(v1.z, 2);
	
	double d = Math.sqrt((X2+Y2+Z2));
	
	Coordinate normalized = new Coordinate(v1.x/d, v1.y/d, v1.z/d);
	
	return normalized;
	
	
}


public static List<Vertex> getVertices(SimpleFeature feature) {
	Geometry g = (Geometry) feature.getDefaultGeometry();
	Coordinate[] coords = ArrayUtils.remove(g.getCoordinates(), g.getCoordinates().length - 1);
	Vertex vertice = new Vertex();
	Edge edge = new Edge();
	List<Edge> edges = edge.getLinesOfPolygon(feature);
	List<Vertex> vertices = new ArrayList<Vertex>();
	
	for (int i =0; i<coords.length; i++) {
		
		vertice.position = coords[i];
		vertice.prevEdge = edges.get(i).edge;
		vertice.nextEdge = edges.get(i).nextEdge;
		vertice.bisector = vertice.getAngularBiSector(vertice.prevEdge, vertice.nextEdge);
		vertices.add(vertice);
	}
	return vertices;
	
	}

// poligonun diagon uzunluğunu hesaplayan fonksiyon
public static double getDiagonal(SimpleFeature feature) {
	
	Geometry g = (Geometry) feature.getDefaultGeometry();
	Geometry bbox = g.getEnvelope();
	
	Coordinate [] coords = bbox.getCoordinates();
	
	
	// bbox u oluşturan vertice leri değişkenlere atıyorum, saat yönünün tersine doğru sol alttan 1 2 3 4 şeklinde
	
	Coordinate v1 = coords[0];
	Coordinate v2 = coords[1];
	Coordinate v3 = coords[2];
	Coordinate v4 = coords[3];
	
	double deltaX3 = Math.pow((v3.x-v1.x), 2);
	double deltaY3 = Math.pow((v3.y-v1.y), 2);
	
	double distance = Math.sqrt((deltaX3+deltaY3));
	
	return distance;
	
}

// iki doğrultuyu (vektörü) line a çevirip kesişim noktasını double array olarak döndüren fonksiyon

public static double[] calculateIntersectionOfBisectors(double[] vec1, double[] vec2, double distance) {
	
	Coordinate startCoord1 = new Coordinate(vec1[0], vec1[1]);	
	double angle1 = Math.atan(vec1[1]/vec1[0]);
	Coordinate endCoord1 = new Coordinate(startCoord1.x+(distance*Math.sin(angle1)), startCoord1.y+(distance*Math.cos(angle1)));
	
	Coordinate startCoord2 = new Coordinate(vec2[0], vec2[1]);
	double angle2 = Math.atan(vec2[1]/vec2[0]);
	Coordinate endCoord2 = new Coordinate(startCoord2.x+(distance*Math.sin(angle2)), startCoord2.y+(distance*Math.cos(angle2)));
	
	Coordinate[] coords1 = {startCoord1, endCoord1};
	Coordinate[] coords2 = {startCoord2, endCoord2};
	
	GeometryFactory gf = new GeometryFactory();
	
	LineString line1 = gf.createLineString(coords1);
	LineString line2 = gf.createLineString(coords2);
	
	Geometry intersectionPoint = line1.intersection(line2);
	
	double[] point = {intersectionPoint.getCoordinate().x, intersectionPoint.getCoordinate().y}; 
	
	return point;
}

public static PriorityQueue<Vertex> calculateSkeleton (SimpleFeature feature){
	
	PriorityQueue<Vertex> nodes = new PriorityQueue();
	
	Vertex vertice = new Vertex();
	List<Vertex> vertices = vertice.getVertices(feature);
	
	double distance = vertice.getDiagonal(feature);
	
	for (int i=0; i<vertices.size(); i++) {
		Coordinate startPoint = vertices.get(i).bisector;
		
	}
	
	return null;
	
}
}

