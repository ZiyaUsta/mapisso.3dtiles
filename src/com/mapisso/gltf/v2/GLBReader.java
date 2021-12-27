package com.mapisso.gltf.v2;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.utils.GLBUtils;

public class GLBReader {
	
	private GLBUtils glbUtils = new GLBUtils(); 
	
	public GLTF read(byte[] glb) {
		
		byte[] type = Arrays.copyOfRange(glb, 0, 4);
		System.out.println("TYPE: " + glbUtils.readBufferString(type));

		byte[] version = Arrays.copyOfRange(glb, 4, 8);
		System.out.println("VERSION: " + glbUtils.readBufferInt(version));
		
		byte[] lengthValue = Arrays.copyOfRange(glb, 8, 12);
		System.out.println("GLB LENGTH: " + glbUtils.readBufferInt(lengthValue));
		
		byte[] jsonBufferLengthArray = Arrays.copyOfRange(glb, 12, 16);
		int  jsonBufferLengthArraySize = glbUtils.readBufferInt(jsonBufferLengthArray);
		System.out.println("JSON BUFFER LENGTH: " + jsonBufferLengthArraySize);
		
		byte[] jsonBufferTypeArray = Arrays.copyOfRange(glb, 16, 20);
		System.out.println("JSON TYPE: " + glbUtils.readBufferString(jsonBufferTypeArray));
		
		byte[] jsonChunkStringArray = Arrays.copyOfRange(glb, 20, 20 + jsonBufferLengthArraySize);
		System.out.println("JSON : " + glbUtils.readBufferString(jsonChunkStringArray));
		
		String jsonString = glbUtils.readBufferString(jsonChunkStringArray);
		
		ObjectMapper mapper = new ObjectMapper();
		GLTF gltf = null;
		
		try {
			gltf = mapper.readValue(jsonString, GLTF.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gltf;
		
	}

}
