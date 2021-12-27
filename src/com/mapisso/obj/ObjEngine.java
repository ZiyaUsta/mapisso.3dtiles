package com.mapisso.obj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mapisso.model.obj.Obj;
import com.mapisso.model.obj.Triangle;
import com.mapisso.shape.ReadShapeFile;
import com.mapisso.utils.CoordinateUtils;
import com.mapisso.utils.ObjUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPolygon;

public class ObjEngine {
	
	private static Logger logger = LoggerFactory.getLogger(ObjEngine.class);
	
	private ReadShapeFile readShapeFile = new ReadShapeFile();
	private CoordinateUtils coordinateUtils = new CoordinateUtils();
	private ObjUtils objUtils = new ObjUtils();
	
	private String fileType;
	private List<Obj> objList = null;
	
	public ObjEngine(String fileType) {
		this.fileType = fileType;
	}
	
	public List<Obj> start(File file, String heightAttributeName){
		
		logger.info("OBJ ENGINE: transformation process started.");
		long methodStartTime = System.currentTimeMillis();
		
		objList = new ArrayList<Obj>();
		
		if(this.fileType.equals("shape")) {
			logger.info("OBJ ENGINE: input data format is shape file.");
			objList = createObjListFromShapeFile(readShapeFile.getSFC(file), heightAttributeName);
		}
		
		long methodFinishedTime = System.currentTimeMillis();
		logger.info("OBJ ENGINE: transformation process finished. elapsed time: " + (methodFinishedTime - methodStartTime) + " ms.");
		
		return objList;
	}
	
	private List<Obj> createObjListFromShapeFile(SimpleFeatureCollection featureCollection, String heightAttributeName){
		
		logger.info("OBJ ENGINE: createObjListFromShapeFile method started.");
		
		List<Obj> resultObjList = new ArrayList<Obj>();
		
		SimpleFeatureIterator iterator = (SimpleFeatureIterator) featureCollection.features();
		
		Integer vIndex = 1;
		
		while (iterator.hasNext()) {
			
			SimpleFeature feature = iterator.next();
			
			//o value
			String o = feature.getID();
			
			MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
			Coordinate[] coords = ArrayUtils.remove(geom.getCoordinates(), geom.getCoordinates().length - 1);
			
			String heightStringAttribute = feature.getAttribute(heightAttributeName).toString();
			Double height = Double.valueOf(heightStringAttribute);
			
			//v value list 
			List<Coordinate> vCoordinates = coordinateUtils.extrudedCoordinate(coords,height);
			//f value list
			List<List<Integer>> faceList = objUtils.createFaces(coords, vIndex);
			//triangle list
			List<Triangle> triangleList = objUtils.createTriangleList(faceList, vCoordinates);
			//normal map
			Map<Triangle, Float[]> normalMap = objUtils.createVertexNormals(triangleList, vCoordinates);
			
			Float[] minNormals = objUtils.getMinNormalVector(normalMap);
			Float[] maxNormals = objUtils.getMaxNormalVector(normalMap);
			
			Obj tempObj = new Obj(o, vCoordinates, faceList);
			
			tempObj = objUtils.updateObjMaxMinValues(tempObj);
			tempObj = objUtils.addCenterValueToObj(tempObj);
			
			tempObj.setTriangles(triangleList);
			tempObj.setNormals(normalMap);
			tempObj.setMinNormal(minNormals);
			tempObj.setMaxNormal(maxNormals);
			
			resultObjList.add(tempObj);
			vIndex += (coords.length *2);
			
		}
		
		logger.info("OBJ ENGINE: createObjListFromShapeFile method finished. Total OBJ building count is " + resultObjList.size() + ".");
		
		return resultObjList;
		
	}
	

}
