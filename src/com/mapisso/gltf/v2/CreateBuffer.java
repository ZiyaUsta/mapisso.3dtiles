package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.mapisso.model.gltf.v2.Buffer;
import com.mapisso.model.obj.Obj;
import com.mapisso.model.obj.Triangle;
import com.mapisso.utils.ByteUtils;
import com.mapisso.utils.GLTFUtils;
import com.vividsolutions.jts.geom.Coordinate;

public class CreateBuffer {

	private GLTFUtils gltfUtils = new GLTFUtils();
	private ByteUtils byteUtils = new ByteUtils();

	public List<Buffer> create(List<Obj> objList) {

		List<Buffer> bufferList = new ArrayList<Buffer>();

		Buffer buffer = new Buffer();

		Integer totalByteSize = 0;

		String bufferEncoded = "";

		for (int i = 0; i < objList.size(); i++) {

			Obj objTemp = objList.get(i);

			List<Coordinate> coordinateList = objTemp.getV();
			List<Triangle> triangleList = objTemp.getTriangles();
			
			byte[] indicesByteArray = new byte[triangleList.size() * 3 * 2];
			byte[] batchidsByteArray = new byte[triangleList.size() * 3 * 2];
			byte[] coordinateArray = gltfUtils.coordinateBufferArray(triangleList, coordinateList);
			byte[] normalArray = gltfUtils.normalVectorBufferArray(triangleList, objTemp.getNormals());
			
			int k = 0;
			Integer e = 0;
			for (int j = 0; j < triangleList.size(); j++) {
				
				byte[] shortValue = byteUtils.shortToByteArrayLittleEndianOrder(e);
				indicesByteArray[k] = shortValue[0];
				indicesByteArray[k+1] = shortValue[1];
				
				e = e + 1;
				shortValue = byteUtils.shortToByteArrayLittleEndianOrder(e);
				indicesByteArray[k + 2] = shortValue[0];
				indicesByteArray[k + 3] = shortValue[1];
				
				e = e + 1;
				shortValue = byteUtils.shortToByteArrayLittleEndianOrder(e);
				indicesByteArray[k + 4] = shortValue[0];
				indicesByteArray[k + 5] = shortValue[1];
				
				e = e + 1;
				
				k = k + 6;
			}

			k = 0;
			for (int j = 0; j < triangleList.size(); j++) {

				byte[] shortValue = byteUtils.shortToByteArrayLittleEndianOrder(i);
				batchidsByteArray[k] = shortValue[0];
				batchidsByteArray[k+1] = shortValue[1];
				
				shortValue = byteUtils.shortToByteArrayLittleEndianOrder(i);
				batchidsByteArray[k + 2] = shortValue[0];
				batchidsByteArray[k + 3] = shortValue[1];
				
				shortValue = byteUtils.shortToByteArrayLittleEndianOrder(i);
				batchidsByteArray[k + 4] = shortValue[0];
				batchidsByteArray[k + 5] = shortValue[1];
				
				k = k + 6;
			}

			totalByteSize = totalByteSize + coordinateArray.length + normalArray.length + indicesByteArray.length + batchidsByteArray.length;
			bufferEncoded += Base64.encodeBase64String(coordinateArray) + Base64.encodeBase64String(normalArray) + Base64.encodeBase64String(batchidsByteArray) +
					Base64.encodeBase64String(indicesByteArray) ;

			buffer.setName("input");
			buffer.setByteLength(totalByteSize);

		}

		buffer.setUri("data:application/octet-stream;base64," + bufferEncoded);

		bufferList.add(buffer);

		return bufferList;

	}

}
