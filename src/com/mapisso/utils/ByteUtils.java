package com.mapisso.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class ByteUtils {
	
	public String readBufferString(byte[] array) {
		return new String(array);
	}

	public int readBufferInt(byte[] array) {

		int response = 0;

		ArrayUtils.reverse(array);
		ByteBuffer buffer = ByteBuffer.wrap(array);

		while (buffer.hasRemaining()) {
			response = buffer.getInt();
		}

		return response;

	}

	public byte[] writeIntoByteArray(byte[] source, byte[] willCopy, int start, int end) {

		// System.out.println("willCopySize: " + willCopy.length + " start: " + start +
		// " end: " + end);
		int k = 0;
		for (int i = start; i < end; i++) {
			source[i] = willCopy[k];
			k += 1;
		}

		return source;

	}

	public byte[] allocateByteList(List<byte[]> byteList) {

		int totalByteArraySize = 0;

		for (int i = 0; i < byteList.size(); i++) {
			totalByteArraySize = totalByteArraySize + byteList.get(i).length;
		}

		byte[] allocatedByteArray = new byte[totalByteArraySize];

		int k = 0;
		for (int i = 0; i < byteList.size(); i++) {

			byte[] tempByteArray = byteList.get(i);

			for (int j = 0; j < tempByteArray.length; j++) {
				allocatedByteArray[k + j] = tempByteArray[j];
			}

			k = k + tempByteArray.length;

		}

		return allocatedByteArray;

	}
	
	public String readCharFromByteArray(Integer start, byte[] array) {

		byte[] b3dmString = Arrays.copyOfRange(array, start, start + 4);

		String stringB3dm = new String(b3dmString);

		return stringB3dm;

	}
	
	public Integer readIntegerFromByteArray(Integer start, byte[] array) {

		byte[] versionByte = Arrays.copyOfRange(array, start, start + 4);

		ArrayUtils.reverse(versionByte);

		ByteBuffer versionBuffer = ByteBuffer.wrap(versionByte);
		Integer version = 0;

		while (versionBuffer.hasRemaining()) {
			version = versionBuffer.getInt();
		}

		return version;

	}
	
	public byte[] alignByteArray(byte[] buffer, Integer byteOffset) {
		// The glb may not be aligned to an 8-byte boundary within the tile, causing
		// gltf-pipeline operations to fail.
		// If unaligned, copy the glb to a new buffer.
		if (byteOffset % 8 == 0) {
			return buffer;
		}

		return Arrays.copyOf(buffer, buffer.length);
	}
	
	public byte[] getJsonBufferPadded(String gtlfJson) {
		
		byte[] jsonChunkArray = gtlfJson.getBytes();
		
		int boundary = 4;
		int byteLength = jsonChunkArray.length;
		int reminder = byteLength % boundary;
		
		if(reminder == 0) {
			return jsonChunkArray;
		}
		
		int padding = boundary - reminder;
		String whitespace = "";
		for (int i = 0; i < padding; i++) {
			whitespace += ' ';
		}
		
		gtlfJson +=  whitespace;
		
		return gtlfJson.getBytes();
	}
	
	public byte[] integerToByteArrayLittleEndianOrder(int value) {

		byte[] response = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();

		return response;

	}

	public byte[] shortToByteArrayLittleEndianOrder(Integer value) {

		byte[] response = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value.shortValue()).array();

		return response;

	}

	public byte[] floatToByteArrayLittleEndianOrder(float value) {

		byte[] response = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();

		return response;

	}

}
