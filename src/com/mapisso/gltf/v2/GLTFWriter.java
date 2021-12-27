package com.mapisso.gltf.v2;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapisso.model.gltf.v2.GLTF;

public class GLTFWriter {
	
	public void write(GLTF gltf, File fileLocation) {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		try {
			mapper.writeValue(fileLocation, gltf);

			// String jsonString = mapper.writeValueAsString(gltf);
			// System.out.println("JSON STRING: " + jsonString);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
