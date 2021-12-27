package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mapisso.model.gltf.v2.Mesh;
import com.mapisso.model.gltf.v2.MeshPrimitive;
import com.mapisso.model.obj.Obj;

public class CreateMeshs {

	public List<Mesh> createMeshList(List<Obj> objList) {

		List<Mesh> meshList = new ArrayList<Mesh>();
		
		Integer position = 0;

		for (int i = 0; i < objList.size(); i++) {

			Mesh mesh = new Mesh();

			Obj objTemp = objList.get(i);

			mesh.setName(objTemp.getO() + "-Mesh");

			MeshPrimitive meshPrimitive = new MeshPrimitive();
			Map<String, Integer> attributes = new LinkedHashMap<String, Integer>();

			if (i == 0) {
				attributes.put("POSITION", 0);
				attributes.put("NORMAL", 1);
				attributes.put("_BATCHID", 2);
				
			} else {
				position = position + 4;
				attributes.put("POSITION", position);
				attributes.put("NORMAL", position + 1);
				attributes.put("_BATCHID", position + 2);
			}
			
			meshPrimitive.setAttributes(attributes);
			meshPrimitive.setIndices(position + 3);
			meshPrimitive.setMaterial(0);
			meshPrimitive.setMode(4);

			List<MeshPrimitive> meshPrimitiveList = new ArrayList<MeshPrimitive>();
			meshPrimitiveList.add(meshPrimitive);

			mesh.setPrimitives(meshPrimitiveList);

			meshList.add(mesh);
			
		}
		
		return meshList;
		
	}
		

}
