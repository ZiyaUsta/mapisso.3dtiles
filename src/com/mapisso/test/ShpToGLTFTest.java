package com.mapisso.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapisso.gltf.v2.GLTFEngine;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.model.obj.Obj;
import com.mapisso.obj.ObjEngine;

public class ShpToGLTFTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File fileDirectory = new File("/Users/ziya/Documents/test/bina1.shp");

		ObjEngine objEngine = new ObjEngine("shape");
		List<Obj> objList = objEngine.start(fileDirectory, "height");
		
		GLTFEngine gltfEngine = new GLTFEngine(false, false);

		if (objList != null) {

			GLTF gltf = gltfEngine.createGLTF(objList);

			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			try {
				mapper.writeValue(new File("/Users/ziya/Documents/Applications/shadowMapping/bina.gltf"), gltf);

				// String jsonString = mapper.writeValueAsString(gltf);
				// System.out.println("JSON STRING: " + jsonString);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
