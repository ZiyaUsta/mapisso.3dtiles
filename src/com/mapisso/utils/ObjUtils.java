package com.mapisso.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;

import com.mapisso.model.obj.Obj;
import com.mapisso.model.obj.Triangle;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class ObjUtils {
	
	public String transformToV(List<Coordinate> coordinates) {

		String v = "";
		// koordinatları ve extruded koordinatları alt alta string e çevirir
		for (int i = 0; i < coordinates.size(); i++) {

			Coordinate c = coordinates.get(i);
			v = v + new String("v " + c.x + " " + c.y + " " + c.z) + "\n";

		}
		return v;
	}

	public String transformToVN(List<Coordinate> normals) {

		String v = "";
		// koordinatları ve extruded koordinatları alt alta string e çevirir
		for (int i = 0; i < normals.size(); i++) {

			Coordinate c = normals.get(i);
			v = v + new String("vn " + c.x + " " + c.y + " " + c.z) + "\n";

		}
		return v;
	}

	public String transformToF(List<List<Integer>> faces) {

		String f = "";
		for (int i = 0; i < faces.size(); i++) {

			f = f + new String("f ");
			List<Integer> tempFaces = faces.get(i);
			for (int j = 0; j < tempFaces.size(); j++) {

				f = f + tempFaces.get(j) + " ";

			}

			f = f + "\n";

		}

		return f;
	}

	public Integer getMaxIndice(List<List<Integer>> list) {

		List<Integer> allIndices = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {

			List<Integer> objFaceList = list.get(i);
			for (int j = 0; j < objFaceList.size(); j++) {
				allIndices.add(objFaceList.get(j));
			}

		}

		return Collections.max(allIndices);
	}

	public Integer getMinIndice(List<List<Integer>> list) {

		List<Integer> allIndices = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {

			List<Integer> objFaceList = list.get(i);
			for (int j = 0; j < objFaceList.size(); j++) {
				allIndices.add(objFaceList.get(j));
			}

		}

		return Collections.min(allIndices);
	}

	public Double getMaxCoord(List<Coordinate> list, String parameter) {

		List<Double> coordinateList = new ArrayList<Double>();
		for (int i = 0; i < list.size(); i++) {

			if (parameter.equals("y")) {
				Double y = list.get(i).y;
				coordinateList.add(y);
			} else if (parameter.equals("x")) {
				Double x = list.get(i).x;
				coordinateList.add(x);
			} else if (parameter.equals("z")) {
				Double z = list.get(i).z;
				coordinateList.add(z);
			}

		}

		return Collections.max(coordinateList);
	}

	public Double getMinCoord(List<Coordinate> list, String parameter) {

		List<Double> coordinateList = new ArrayList<Double>();
		for (int i = 0; i < list.size(); i++) {

			if (parameter.equals("y")) {
				Double y = list.get(i).y;
				coordinateList.add(y);
			} else if (parameter.equals("x")) {
				Double x = list.get(i).x;
				coordinateList.add(x);
			} else if (parameter.equals("z")) {
				Double z = list.get(i).z;
				coordinateList.add(z);
			}

		}

		return Collections.min(coordinateList);
	}

	public Obj updateObjMaxMinValues(Obj obj) {

		List<Coordinate> coordinateList = obj.getV();
		List<List<Integer>> facesList = obj.getF();

		Integer minIndice = getMinIndice(facesList);
		Integer maxIndice = getMaxIndice(facesList);

		Double minX = getMinCoord(coordinateList, "x");
		Double minY = getMinCoord(coordinateList, "y");
		Double minZ = getMinCoord(coordinateList, "z");

		Double maxX = getMaxCoord(coordinateList, "x");
		Double maxY = getMaxCoord(coordinateList, "y");
		Double maxZ = getMaxCoord(coordinateList, "z");

		Double[] minArray = new Double[] { minX, minY, minZ };
		Double[] maxArray = new Double[] { maxX, maxY, maxZ };

		obj.setMinCoord(minArray);
		obj.setMaxCoord(maxArray);

		obj.setMaxIndice(maxIndice - minIndice);
		obj.setMinIndice(minIndice - minIndice);

		return obj;

	}

	public Obj addCenterValueToObj(Obj obj) {

		Double[] centerArray = new Double[] { 0.0d, 0.0d, 0.0d };

		Double[] minCoords =  (Double[]) obj.getMinCoord();
		Double[] maxCoords =  (Double[]) obj.getMaxCoord();

		centerArray[0] = minCoords[0] + ((maxCoords[0] - minCoords[0]) / 2);
		centerArray[1] = minCoords[1] + ((maxCoords[1] - minCoords[1]) / 2);
		centerArray[2] = minCoords[2] + ((maxCoords[2] - minCoords[2]) / 2);
		
		obj.setCenter(centerArray);

		return obj;

	}

	public List<List<Integer>> createFaces(Coordinate[] coords, int vIndex) {
		
//		logger.info("OBJ create faces method started.");

		List<List<Integer>> faceList = new ArrayList<List<Integer>>();
		List<Integer> footprintFaceList = new ArrayList<Integer>();
		List<Integer> roofprintFaceList = new ArrayList<Integer>();

		int forVIndex = vIndex;
		for (int j = forVIndex; j < coords.length + forVIndex; j++) {

			footprintFaceList.add(j);
			roofprintFaceList.add(j + coords.length);

			List<Integer> verticalFaceList = new ArrayList<Integer>();
			if (j != coords.length + forVIndex - 1) {

				verticalFaceList.add(j);
				verticalFaceList.add(j + 1);
				verticalFaceList.add(j + 1 + coords.length);
				verticalFaceList.add(j + coords.length);

				faceList.add(verticalFaceList);

			}

			vIndex = vIndex + 1;

		}

		List<Integer> lastVerticalFaces = new ArrayList<Integer>();
		lastVerticalFaces.add(vIndex - 1);
		lastVerticalFaces.add(forVIndex);
		lastVerticalFaces.add(vIndex);
		lastVerticalFaces.add(vIndex + coords.length - 1);

		faceList.add(lastVerticalFaces);
		faceList.add(footprintFaceList);
		faceList.add(roofprintFaceList);
		
//		logger.info("OBJ create faces method finished. Face count: " + faceList.size());

		return faceList;

	}

	public List<Triangle> createTriangleList(List<List<Integer>> fList, List<Coordinate> coordinateList) {
		
//		logger.info("OBJ createTriangleList method started");
		
		List<Triangle> triangleList = new ArrayList<Triangle>();

		int lastIndiceNumber = 0;

		for (int i = 0; i < fList.size(); i++) {

			if (fList.get(i).size() > 4) {

				List<Integer> fTemp = fList.get(i);

				// System.out.println("fTemp.size(): " + fTemp.size());
				double[] coordinateArray = new double[fTemp.size() * 2];

				int startingIndex = fTemp.get(0);
				// System.out.println("startingIndex: " + startingIndex);
				for (int j = 0; j < fTemp.size(); j++) {
					int coordIndex = (fTemp.get(j) - startingIndex) + lastIndiceNumber;
					coordinateArray[j * 2] = coordinateList.get(coordIndex).x;
					coordinateArray[(j * 2) + 1] = coordinateList.get(coordIndex).y;
				}

				List<Integer> triangles = EarcutUtils.earcut(coordinateArray, null, 2);

				for (int j = 0; j < triangles.size(); j++) {
					triangles.set(j, triangles.get(j) + lastIndiceNumber);
				}
				
				lastIndiceNumber = fTemp.size();
				
				Float[] tempNormal = null;
				for (int k = 0; k < triangles.size(); k += 3) {
					
					Triangle triangle = new Triangle(triangles.get(k), triangles.get(k + 1),
							triangles.get(k + 2));
					
					if(k == 0) {
						tempNormal = calculateNormalVectorOfTriangle(triangle, coordinateList);
					}
					
					triangle.setNormal(tempNormal);
					
					triangleList.add(triangle);
				}

			} else {

				Integer minIndice = getMinIndice(fList);

				List<Integer> tempF = fList.get(i);

				Integer ucgenSayisi = (tempF.size() - 2);

				Float[] tempNormal = null;
				for (int j = 0; j < ucgenSayisi; j++) {

					Triangle triangle = new Triangle(tempF.get(0) - minIndice, tempF.get(j + 1) - minIndice,
							tempF.get(j + 2) - minIndice);
					
					if(j == 0) {
						tempNormal = calculateNormalVectorOfTriangle(triangle, coordinateList);
					}
					
					triangle.setNormal(tempNormal);

					triangleList.add(triangle);

				}
			}
		}

//		logger.info("OBJ createTriangleList method finished. Triangle List Size: " + triangleList.size());

		return triangleList;

	}

	public Map<Triangle, Float[]> createVertexNormals(List<Triangle> triangleList, List<Coordinate> coordinateList) {
		
//		logger.info("OBJ createVertexNormals method started.");
		
		Map<Triangle, Float[]> normalMap = new HashMap<Triangle, Float[]>();

		for (int j = 0; j < triangleList.size(); j++) {

			Triangle tempTriangle = triangleList.get(j);

			normalMap.put(tempTriangle, tempTriangle.getNormal());

		}
		
//		logger.info("OBJ createVertexNormals method finished.");

		return normalMap;
	}

	public Float[] getMinNormalVector(Map<Triangle, Float[]> normals) {

//		logger.info("OBJ getMinNormalVector method started.");
		
		int i = 0;
		Float min0 = 0.0f, min1 = 0.0f, min2 = 0.0f;
		for (Map.Entry<Triangle, Float[]> entry : normals.entrySet()) {

			Float[] tNormal = entry.getValue();

			if (i == 0) {
				min0 = tNormal[0];
				min1 = tNormal[1];
				min2 = tNormal[2];
			} else {
				if (tNormal[0] < min0) {
					min0 = tNormal[0];
				}

				if (tNormal[1] < min1) {
					min1 = tNormal[1];
				}

				if (tNormal[2] < min2) {
					min2 = tNormal[2];
				}
			}

			i += 1;

		}
		
//		logger.info("OBJ getMinNormalVector method finished.");

		return new Float[] { min0, min1, min2 };
	}

	public Float[] getMaxNormalVector(Map<Triangle, Float[]> normals) {
		
//		logger.info("OBJ getMaxNormalVector method started.");

		int i = 0;
		Float max0 = 0.0f, max1 = 0.0f, max2 = 0.0f;
		for (Map.Entry<Triangle, Float[]> entry : normals.entrySet()) {

			Float[] tNormal = entry.getValue();

			if (i == 0) {
				max0 = tNormal[0];
				max1 = tNormal[1];
				max2 = tNormal[2];
			} else {
				if (tNormal[0] > max0) {
					max0 = tNormal[0];
				}

				if (tNormal[1] > max1) {
					max1 = tNormal[1];
				}

				if (tNormal[2] > max2) {
					max2 = tNormal[2];
				}
			}

			i += 1;

		}
		
//		logger.info("OBJ getMaxNormalVector method finished.");
		
		return new Float[] { max0, max1, max2 };
	}

	public Float[] calculateNormalVectorOfTriangle(Triangle triangle, List<Coordinate> coordinateList) {
		
//		logger.info("OBJ calculateNormalVectorOfTriangle method started.");

		Integer firstPointIndez = triangle.getT1();
		Integer secondPointIndex = triangle.getT2();
		Integer thirdPointIndex = triangle.getT3();

		Coordinate coordA = coordinateList.get(firstPointIndez);
		Coordinate coordB = coordinateList.get(secondPointIndex);
		Coordinate coordC = coordinateList.get(thirdPointIndex);
		
		if(checkCCW(coordA, coordB, coordC) == -1) {
			
			coordA = coordinateList.get(thirdPointIndex);
			coordB = coordinateList.get(secondPointIndex);
			coordC = coordinateList.get(firstPointIndez);
		}
		
		// bbox tan 2 adet vector oluşturuyoruz
		Coordinate vec1 = new Coordinate((coordB.x - coordA.x), (coordB.y - coordA.y), (coordB.z - coordA.z));
		Coordinate vec2 = new Coordinate((coordC.x - coordA.x), (coordC.y - coordA.y), (coordC.z - coordA.z));

		// bu iki vektörü kullanarak yüzey normalini hesaplıyoruz
		Double[] normal = { ((vec1.y * vec2.z) - (vec1.z * vec2.y)), ((vec1.z * vec2.x - vec1.x - vec2.z)),
				((vec1.x * vec2.y) - (vec1.y * vec2.x)) };

		// yüzey normalini, normalin boyutuna bölerek birim vektör haline getiriyoruz
		Double length = Math.sqrt((Math.pow(normal[0], 2) + Math.pow(normal[1], 2) + Math.pow(normal[2], 2)));
		
		Double normal0 = normal[0];
		Double normal1 = normal[1];
		Double normal2 = normal[2];
		
		Float normalize0 = (float) (normal0.floatValue() / length.doubleValue());
		Float normalize1 = (float) (normal1.floatValue() / length.doubleValue());
		Float normalize2 = (float) (normal2.floatValue() / length.doubleValue());
		
		Float[] normalized = {normalize0 , normalize1, normalize2 };
		
//		logger.info("OBJ calculateNormalVectorOfTriangle method finished.");
		
		return normalized;
	}

	public double[] calculateSurfaceNormal(Coordinate[] coordArray) {

		Coordinate coordA = coordArray[0];
		Coordinate coordB = coordArray[1];
		Coordinate coordC = coordArray[2];

		// bbox tan 2 adet vectör oluşturuyoruz
		Coordinate vec1 = new Coordinate((coordB.x - coordA.x), (coordB.y - coordA.y), (coordB.z - coordA.z));
		Coordinate vec2 = new Coordinate((coordC.x - coordA.x), (coordC.y - coordA.y), (coordC.z - coordA.z));

		// bu iki vektörü kullanarak yüzey normalini hesaplıyoruz
		double[] normal = { ((vec1.y * vec2.z) - (vec1.z * vec2.y)), ((vec1.z * vec2.x - vec1.x - vec2.z)),
				((vec1.x * vec2.y) - (vec1.y * vec2.x)) };

		// yüzey normalini, normalin boyutuna bölerek birim vektör haline getiriyoruz
		double lenght = Math.sqrt((Math.pow(normal[0], 2) + Math.pow(normal[1], 2) + Math.pow(normal[2], 2)));
		double[] normalized = { normal[0] / lenght, normal[1] / lenght, normal[2] / lenght };

		return normalized;
	}

	public double[] calculateSurfaceNormalv2(Coordinate[] coordArray) {

		Coordinate A = coordArray[0];
		Coordinate B = coordArray[1];
		Coordinate C = coordArray[coordArray.length - 1];

		System.out.println("A: " + A.toString());

		// let vector1 = [(pointB.x-pointA.x), (pointB.y-pointA.y),
		// (pointB.z-pointA.z)];
		// let vector2 = [(pointC.x-pointA.x), (pointC.y-pointA.y),
		// (pointC.z-pointA.z)];

		double[] vector1 = new double[] { B.x - A.x, B.y - A.y, B.z - A.z };
		double[] vector2 = new double[] { C.x - A.x, C.y - A.y, C.z - A.z };

		System.out.println("vector 1: " + Arrays.toString(vector1));
		System.out.println("vector 2: " + Arrays.toString(vector2));

		// let normal = [(vector1[1]*vector2[2]-vector1[2]*vector2[1]),
		// (vector1[2]*vector2[0]-vector1[0]*vector2[2]),
		// (vector1[0]*vector2[1]-vector1[1]*vector2[0])];

		double[] normal = new double[] { (vector1[1] * vector2[2] - vector1[2] * vector2[1]),
				(vector1[2] * vector2[0] - vector1[0] * vector2[2]),
				(vector1[0] * vector2[1] - vector1[1] * vector2[0]) };

		// let n1 = normal[0]/(Math.sqrt(((Math.pow(normal[0], 2)) +
		// (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));
		// let n2 = normal[1]/(Math.sqrt(((Math.pow(normal[0], 2)) +
		// (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));
		// let n3 = normal[2]/(Math.sqrt(((Math.pow(normal[0], 2)) +
		// (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));

		double n1 = normal[0]
				/ (Math.sqrt(((Math.pow(normal[0], 2)) + (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));
		double n2 = normal[1]
				/ (Math.sqrt(((Math.pow(normal[0], 2)) + (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));
		double n3 = normal[2]
				/ (Math.sqrt(((Math.pow(normal[0], 2)) + (Math.pow(normal[1], 2)) + (Math.pow(normal[2], 2)))));

		return new double[] { Math.abs(n1), Math.abs(n2), Math.abs(n3) };

	}

	public List<Obj> updateObjWithRtcCenter(List<Obj> objList, List<Double> rtcCenter) {

		Double centerX = rtcCenter.get(0);
		Double centerY = rtcCenter.get(1);
		Double centerZ = rtcCenter.get(2);
		
		for (int i = 0; i < objList.size(); i++) {

			Obj obj = objList.get(i);

			Double[] minCoords = (Double[]) obj.getMinCoord();
			Double[] maxCoords = (Double[]) obj.getMaxCoord();

			Double diffMinX = minCoords[0] - centerX;
			Double diffMinY = minCoords[1] - centerY;
			Double diffMinZ = minCoords[2] - centerZ;

			Double diffMaxX = maxCoords[0] - centerX;
			Double diffMaxY = maxCoords[1] - centerY;
			Double diffMaxZ = maxCoords[2] - centerZ;

			Double[] minTempCoords = new Double[] { diffMinX, diffMinY, diffMinZ };
			Double[] maxTempCoords = new Double[] { diffMaxX, diffMaxY, diffMaxZ };

			obj.setMaxCoord(maxTempCoords);
			obj.setMinCoord(minTempCoords);

			List<Coordinate> coordinates = obj.getV();

			for (int j = 0; j < coordinates.size(); j++) {

				Double diffX = coordinates.get(j).x - centerX;
				Double diffY = coordinates.get(j).y - centerY;
				Double diffZ = coordinates.get(j).z - centerZ;

				Coordinate objCoordinates = new Coordinate(diffX, diffY, diffZ);

				coordinates.remove(j);
				coordinates.add(j, objCoordinates);

			}

			obj.setV(coordinates);

			objList.remove(i);
			objList.add(i, obj);

		}

		return objList;

	}
	
	public int checkCCW(Coordinate a, Coordinate b, Coordinate c) {
		
		double dx1 = b.x - a.x;
		double dx2 = c.x - a.x;
		double dy1 = b.y - a.y;
		double dy2 = c.y - a.y;
		
		if(dx1 * dy2 > dx2 * dy1) {
			return 1;
		}else {
			return -1;
		}
		
	}

	public ReferencedEnvelope3D merge(ReferencedEnvelope3D bbox1, ReferencedEnvelope3D bbox2) {

		double minX = Math.min(bbox2.getMinX(), bbox1.getMinX());
		double maxX = Math.max(bbox2.getMaxX(), bbox1.getMaxX());

		double minY = Math.min(bbox2.getMinY(), bbox1.getMinY());
		double maxY = Math.max(bbox2.getMaxY(), bbox1.getMaxY());

		double minZ = Math.min(bbox2.getMinZ(), bbox1.getMinZ());
		double maxZ = Math.max(bbox2.getMaxZ(), bbox1.getMaxZ());

		Coordinate minPoint = new Coordinate(minX, minY, minZ);
		Coordinate maxPoint = new Coordinate(maxX, maxY, maxZ);

		ReferencedEnvelope envelope = ReferencedEnvelope.reference(new Envelope(minPoint, maxPoint));

		ReferencedEnvelope3D mergedEnvelope = new ReferencedEnvelope3D(envelope);

		return mergedEnvelope;
	}

}
