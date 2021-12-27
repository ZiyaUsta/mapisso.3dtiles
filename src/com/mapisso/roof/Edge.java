package com.mapisso.roof;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class Edge {
	
	Coordinate edge;
	Coordinate nextEdge;
	
	public static List<Edge> getLinesOfPolygon(SimpleFeature feature) {
		List<Edge> edges = new ArrayList<Edge>();
		Geometry g = (Geometry) feature.getDefaultGeometry();
		Coordinate[] coords = ArrayUtils.remove(g.getCoordinates(), g.getCoordinates().length - 1);
			
			for (int i = 2; i<=coords.length+1; i++) {
				
			Edge edge = new Edge();
			edge.edge = new Coordinate(coords[i-1].x-coords[i-2].x, coords[i-1].y-coords[i-2].y, coords[i-1].z-coords[i-2].z);
			edge.nextEdge = new Coordinate(coords[i].x-coords[i-1].x, coords[i].y-coords[i-1].y, coords[i].z-coords[i-1].z);
			edges.add(edge);
			
			// Edge listesini circular liste olarak oluştur out of bounds hatasından kurtar
			
			}
			return edges;	
		}
	
}

