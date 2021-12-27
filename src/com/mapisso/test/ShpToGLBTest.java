package com.mapisso.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapisso.gltf.v2.GLBWriter;
import com.mapisso.gltf.v2.GLTFEngine;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.model.obj.Obj;
import com.mapisso.obj.ObjEngine;

public class ShpToGLBTest {
	
	private static GLBWriter glbWriter = new GLBWriter();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		String jsonGLTF = "";

		File fileDirectory = new File("/Users/mapisso/Desktop/SolarProject/mapisso/shp/binagrup.shp");
		
		ObjEngine objEngine = new ObjEngine("shape");
		List<Obj> objList = objEngine.start(fileDirectory, "height");

		if (objList != null) {
			
			GLTFEngine gltfEngine = new GLTFEngine(true, true);
			GLTF gltf = gltfEngine.createGLTF(objList);
			
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
			
			byte[] glb = glbWriter.write(jsonGLTF, byteBufferEncoded);
			
			String targetFile = "/Users/mapisso/Desktop/SolarProject/mapisso/mapisso.glb";

			//Create a new file and override when already exists:
			try (OutputStream output = openFile(targetFile)) {
				output.write(glb);
			}

		}

	}

	private static BufferedOutputStream openFile(String fileName) throws IOException {
		return openFile(fileName, false);
	}

	public static BufferedOutputStream openFile(String fileName, boolean append) throws IOException {
		// Don't forget to add buffering to have better performance!
		return new BufferedOutputStream(new FileOutputStream(fileName, append));
	}

}
