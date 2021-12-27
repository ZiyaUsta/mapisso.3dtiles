package com.mapisso.b3dm.v1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapisso.constants.B3DMConstants;
import com.mapisso.gltf.v2.GLBWriter;
import com.mapisso.gltf.v2.GLTFEngine;
import com.mapisso.model.b3dm.v1.BatchTable;
import com.mapisso.model.b3dm.v1.FeatureTable;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.model.gltf.v2.Node;
import com.mapisso.model.obj.Obj;
import com.mapisso.utils.ByteUtils;

public class B3dmEngine {
	
	private static Logger logger = LoggerFactory.getLogger(B3dmEngine.class);

	private GLBWriter glbWriter = new GLBWriter();
	private ByteUtils byteUtils = new ByteUtils();

	public byte[] createB3dm(List<Obj> objList) {
		
		logger.info("B3DM ENGINE: transformation process started.");
		
		GLTF gltf = null;
		byte[] glb = null;
		String jsonGLTF = "";

		if (objList != null) {

			GLTFEngine gltfEngine = new GLTFEngine(true, true);
			gltf = gltfEngine.createGLTF(objList);

			String bufferEncoded = gltf.getBuffers().get(0).getUri().replaceAll("data:application/octet-stream;base64,", "");
			byte[] byteBufferEncoded = Base64.decodeBase64(bufferEncoded);

			gltf = gltf.setNullBufferUri(gltf);

			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			try {
				jsonGLTF = mapper.writeValueAsString(gltf);
			} catch (IOException e) {
				// TODO Auto-generated catch block

			}

			glb = glbWriter.write(jsonGLTF, byteBufferEncoded);
			
		}
		
		List<Node> nodeListesi = gltf.getNodes();

		List<Integer> batchID = new ArrayList<Integer>();
		List<String> name = new ArrayList<String>();
		for (int i = 0; i < nodeListesi.size(); i++) {

			Node node = nodeListesi.get(i);

			batchID.add(node.getMesh());
			name.add(node.getName());

		}

		BatchTable batchTable = new BatchTable();
		batchTable.setBatchId(batchID);
		batchTable.setName(name);

		FeatureTable featureTable = new FeatureTable();
		featureTable.setBATCH_LENGTH(nodeListesi.size());

		ObjectMapper mapper = new ObjectMapper();

		String batchTableJsonString = "";
		String featureTableJsonString = "";

		try {
			batchTableJsonString = mapper.writeValueAsString(batchTable);
			featureTableJsonString = mapper.writeValueAsString(featureTable);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] b3dmByte = B3DMConstants.MAGIC.getBytes();
		byte[] versionByte = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(B3DMConstants.VERSION)
				.array();

		byte[] featureTableJsonByte = featureTableJsonString.getBytes();
		byte[] featureTableJsonByteLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
				.putInt(featureTableJsonByte.length).array();
		byte[] featureTableBinaryLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(0).array();

		byte[] batchTableJsonByte = batchTableJsonString.getBytes();
		byte[] batchTableJsonByteLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
				.putInt(batchTableJsonByte.length).array();
		byte[] batchTableBinaryLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(0).array();

		Integer byteLength = B3DMConstants.HEADER_SIZE + featureTableJsonByte.length + batchTableJsonByte.length
				+ glb.length;
		byte[] byteLengthArray = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(byteLength).array();

		byte[] b3dm = new byte[byteLength];

		List<byte[]> byteList = new ArrayList<byte[]>();

		byteList.add(b3dmByte);
		byteList.add(versionByte);
		byteList.add(byteLengthArray);
		byteList.add(featureTableJsonByteLength);
		byteList.add(featureTableBinaryLength);
		byteList.add(batchTableJsonByteLength);
		byteList.add(batchTableBinaryLength);
		byteList.add(featureTableJsonByte);
		byteList.add(batchTableJsonByte);
		byteList.add(glb);

		b3dm = byteUtils.allocateByteList(byteList);
		logger.info("B3DM ENGINE: b3dm file byte length: " + b3dm.length);

		String magic = byteUtils.readCharFromByteArray(0, b3dm);
		logger.info("B3DM ENGINE: b3dm file magic: " + magic);

		Integer version = byteUtils.readIntegerFromByteArray(4, b3dm);
		logger.info("B3DM ENGINE: b3dm file version: " + version);

		Integer byteLengthResult = byteUtils.readIntegerFromByteArray(8, b3dm);
		logger.info("B3DM ENGINE: b3dm file byte length: " + byteLengthResult);

		Integer featureTableJSONByteLength = byteUtils.readIntegerFromByteArray(12, b3dm);
		logger.info("B3DM ENGINE: b3dm file feature table JSON byte length: " + featureTableJSONByteLength);

		Integer featureTableBinaryLengthValue = byteUtils.readIntegerFromByteArray(16, b3dm);
		logger.info("B3DM ENGINE: b3dm file feature table Binary byte length: " + featureTableBinaryLengthValue);

		Integer batchTableJsonByteLengthValue = byteUtils.readIntegerFromByteArray(20, b3dm);
		logger.info("B3DM ENGINE: b3dm file batch table Json byte length: " + batchTableJsonByteLengthValue);
		
		logger.info("B3DM ENGINE: transformation process finished.");

		return b3dm;
	}

}
