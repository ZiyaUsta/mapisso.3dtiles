package com.mapisso.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mapisso.model.gltf.v2.Center;
import com.mapisso.model.obj.Obj;
import com.mapisso.model.obj.Triangle;
import com.vividsolutions.jts.geom.Coordinate;

public class GLTFUtils {
	
	private ByteUtils byteUtils = new ByteUtils();
	
	public Map<String, Object> createRTCCenter(List<Obj> objList) {

		Map<String, Object> extensions = new LinkedHashMap<String, Object>();

		Double centerX = 0.0d, centerY = 0.0d, centerZ = 0.0d;

		List<Double> centerCoord = new ArrayList<Double>();

		for (int i = 0; i < objList.size(); i++) {

			Obj tempObj = objList.get(i);

			centerX = centerX + (Double) tempObj.getCenter()[0];
			centerY = centerY + (Double) tempObj.getCenter()[1];
			centerZ = centerZ + (Double) tempObj.getCenter()[2];

		}

		centerX = centerX / objList.size();
		centerY = centerY / objList.size();
		centerZ = centerZ / objList.size();

		centerCoord.add(centerX);
		centerCoord.add(centerY);
		centerCoord.add(centerZ);

		Center center = new Center();
		center.setCenter(centerCoord);

		extensions.put("CESIUM_RTC", center);

		return extensions;
	}
	
	public byte[] normalVectorBufferArray(List<Triangle> triangleList, Map<Triangle, Float[]> normals) {
		
		byte[] normalArray = new byte[triangleList.size() * 9 * 4];
		
		int n = 0;
		for (int i = 0; i < triangleList.size(); i++) {
			
			Triangle triangle = triangleList.get(i);
			
			Float[] normal = normals.get(triangle);
			
			byte[] x1Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[0]);
			byte[] y1Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[1]);
			byte[] z1Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[2]);

			byte[] x2Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[0]);
			byte[] y2Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[1]);
			byte[] z2Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[2]);

			byte[] x3Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[0]);
			byte[] y3Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[1]);
			byte[] z3Byte = byteUtils.floatToByteArrayLittleEndianOrder(normal[2]);
			
			normalArray[n] = x1Byte[0];
			normalArray[n + 1] = x1Byte[1];
			normalArray[n + 2] = x1Byte[2];
			normalArray[n + 3] = x1Byte[3];

			normalArray[n + 4] = y1Byte[0];
			normalArray[n + 5] = y1Byte[1];
			normalArray[n + 6] = y1Byte[2];
			normalArray[n + 7] = y1Byte[3];

			normalArray[n + 8] = z1Byte[0];
			normalArray[n + 9] = z1Byte[1];
			normalArray[n + 10] = z1Byte[2];
			normalArray[n + 11] = z1Byte[3];

			normalArray[n + 12] = x2Byte[0];
			normalArray[n + 13] = x2Byte[1];
			normalArray[n + 14] = x2Byte[2];
			normalArray[n + 15] = x2Byte[3];

			normalArray[n + 16] = y2Byte[0];
			normalArray[n + 17] = y2Byte[1];
			normalArray[n + 18] = y2Byte[2];
			normalArray[n + 19] = y2Byte[3];

			normalArray[n + 20] = z2Byte[0];
			normalArray[n + 21] = z2Byte[1];
			normalArray[n + 22] = z2Byte[2];
			normalArray[n + 23] = z2Byte[3];

			normalArray[n + 24] = x3Byte[0];
			normalArray[n + 25] = x3Byte[1];
			normalArray[n + 26] = x3Byte[2];
			normalArray[n + 27] = x3Byte[3];

			normalArray[n + 28] = y3Byte[0];
			normalArray[n + 29] = y3Byte[1];
			normalArray[n + 30] = y3Byte[2];
			normalArray[n + 31] = y3Byte[3];

			normalArray[n + 32] = z3Byte[0];
			normalArray[n + 33] = z3Byte[1];
			normalArray[n + 34] = z3Byte[2];
			normalArray[n + 35] = z3Byte[3];

			n = n + 36;
			
		}
		
		
		return normalArray;
	}
	
/*	private double setPrecisionDouble(double input) {
		
		Double output = BigDecimal.valueOf(input)
			    .setScale(5, RoundingMode.HALF_UP)
			    .doubleValue();
		return output;
	}*/
		

	public byte[] coordinateBufferArray(List<Triangle> triangleList, List<Coordinate> coordinateList) {
		
		byte[] coordinateArray = new byte[triangleList.size() * 9 * 4];

		int n = 0;

		for (int j = 0; j < triangleList.size(); j++) {

			Triangle indicesTriangle = triangleList.get(j);

			Integer t1 = indicesTriangle.getT1();
			Integer t2 = indicesTriangle.getT2();
			Integer t3 = indicesTriangle.getT3();

			float x1 = (float) coordinateList.get(t1).x;
			float y1 = (float) coordinateList.get(t1).y;
			float z1 = (float) coordinateList.get(t1).z;

			float x2 = (float) coordinateList.get(t2).x;
			float y2 = (float) coordinateList.get(t2).y;
			float z2 = (float) coordinateList.get(t2).z;

			float x3 = (float) coordinateList.get(t3).x;
			float y3 = (float) coordinateList.get(t3).y;
			float z3 = (float) coordinateList.get(t3).z;

			// System.out.println("t1: " + t1.shortValue() + " t2: " + t2.shortValue() + "
			// t3: " + t3.shortValue());

			byte[] x1Byte = byteUtils.floatToByteArrayLittleEndianOrder(x1);
			byte[] y1Byte = byteUtils.floatToByteArrayLittleEndianOrder(y1);
			byte[] z1Byte = byteUtils.floatToByteArrayLittleEndianOrder(z1);

			byte[] x2Byte = byteUtils.floatToByteArrayLittleEndianOrder(x2);
			byte[] y2Byte = byteUtils.floatToByteArrayLittleEndianOrder(y2);
			byte[] z2Byte = byteUtils.floatToByteArrayLittleEndianOrder(z2);

			byte[] x3Byte = byteUtils.floatToByteArrayLittleEndianOrder(x3);
			byte[] y3Byte = byteUtils.floatToByteArrayLittleEndianOrder(y3);
			byte[] z3Byte = byteUtils.floatToByteArrayLittleEndianOrder(z3);

			coordinateArray[n] = x1Byte[0];
			coordinateArray[n + 1] = x1Byte[1];
			coordinateArray[n + 2] = x1Byte[2];
			coordinateArray[n + 3] = x1Byte[3];

			coordinateArray[n + 4] = y1Byte[0];
			coordinateArray[n + 5] = y1Byte[1];
			coordinateArray[n + 6] = y1Byte[2];
			coordinateArray[n + 7] = y1Byte[3];

			coordinateArray[n + 8] = z1Byte[0];
			coordinateArray[n + 9] = z1Byte[1];
			coordinateArray[n + 10] = z1Byte[2];
			coordinateArray[n + 11] = z1Byte[3];

			coordinateArray[n + 12] = x2Byte[0];
			coordinateArray[n + 13] = x2Byte[1];
			coordinateArray[n + 14] = x2Byte[2];
			coordinateArray[n + 15] = x2Byte[3];

			coordinateArray[n + 16] = y2Byte[0];
			coordinateArray[n + 17] = y2Byte[1];
			coordinateArray[n + 18] = y2Byte[2];
			coordinateArray[n + 19] = y2Byte[3];

			coordinateArray[n + 20] = z2Byte[0];
			coordinateArray[n + 21] = z2Byte[1];
			coordinateArray[n + 22] = z2Byte[2];
			coordinateArray[n + 23] = z2Byte[3];

			coordinateArray[n + 24] = x3Byte[0];
			coordinateArray[n + 25] = x3Byte[1];
			coordinateArray[n + 26] = x3Byte[2];
			coordinateArray[n + 27] = x3Byte[3];

			coordinateArray[n + 28] = y3Byte[0];
			coordinateArray[n + 29] = y3Byte[1];
			coordinateArray[n + 30] = y3Byte[2];
			coordinateArray[n + 31] = y3Byte[3];

			coordinateArray[n + 32] = z3Byte[0];
			coordinateArray[n + 33] = z3Byte[1];
			coordinateArray[n + 34] = z3Byte[2];
			coordinateArray[n + 35] = z3Byte[3];

			n = n + 36;

		}

		return coordinateArray;

	}
	
}
