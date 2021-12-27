package com.mapisso.gltf.v2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.mapisso.constants.GLTFConstants;
import com.mapisso.utils.ByteUtils;

public class GLBWriter {

	private ByteUtils byteUtils = new ByteUtils();

	public byte[] write(String gtlfJson, byte[] byteBufferEncoded) {

		byte[] jsonBuffer = byteUtils.getJsonBufferPadded(gtlfJson);

//		byte[] binaryChunkArray = byteOperations.allocateByteList(allocatedByteList);
		byte[] binaryChunkArray = byteBufferEncoded;

		// Allocate buffer (Global header) + (JSON chunk header) + (JSON chunk) +
		// (Binary chunk header) + (Binary chunk)
		int glbLength = 12 + 8 + jsonBuffer.length + 8 +  binaryChunkArray.length;

		byte[] glb = ByteBuffer.allocate(glbLength).order(ByteOrder.LITTLE_ENDIAN).array();

		// Write binary glTF header (magic, version, length)
		int byteOffset = 0;
		byte[] magicGlb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(GLTFConstants.GLTF_MAGIC).array();
		glb = byteUtils.writeIntoByteArray(glb, magicGlb, byteOffset, byteOffset + 4);
		byteOffset += 4;

		byte[] versionGlb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(GLTFConstants.GLTF_VERSION_GLB).array();
		glb = byteUtils.writeIntoByteArray(glb, versionGlb, byteOffset, byteOffset + versionGlb.length);
		byteOffset += 4;

		byte[] glbLengthByte = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(glbLength).array();
		glb = byteUtils.writeIntoByteArray(glb, glbLengthByte, byteOffset, byteOffset + glbLengthByte.length);
		byteOffset += 4;

		// Write JSON Chunk header (length, type)
		byte[] jsonBufferLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(jsonBuffer.length).array();
		glb = byteUtils.writeIntoByteArray(glb, jsonBufferLength, byteOffset, byteOffset + jsonBufferLength.length);
		byteOffset += 4;

		byte[] chunkTypeJson = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(GLTFConstants.JSON_TYPE).array();
		glb = byteUtils.writeIntoByteArray(glb, chunkTypeJson, byteOffset, byteOffset + chunkTypeJson.length);
		byteOffset += 4;

		// Write JSON Chunk
		glb = byteUtils.writeIntoByteArray(glb, jsonBuffer, byteOffset, byteOffset + jsonBuffer.length);
		byteOffset += jsonBuffer.length;

		// Write Binary Chunk header (length, type)
		byte[] chunkBinaryBufferLength = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(binaryChunkArray.length).array();
		glb = byteUtils.writeIntoByteArray(glb, chunkBinaryBufferLength, byteOffset, byteOffset + chunkBinaryBufferLength.length);
		byteOffset += 4;

		byte[] chunkTypeBinary = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(GLTFConstants.BIN_TYPE).array();
		glb = byteUtils.writeIntoByteArray(glb, chunkTypeBinary, byteOffset, byteOffset + chunkTypeBinary.length);
		byteOffset += 4;
		
		// Write Binary Chunk
		glb = byteUtils.writeIntoByteArray(glb, binaryChunkArray, byteOffset, byteOffset + binaryChunkArray.length);

		return glb;

	}

}
