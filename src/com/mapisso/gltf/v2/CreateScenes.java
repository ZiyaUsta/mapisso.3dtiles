package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;

import com.mapisso.model.gltf.v2.Scene;
import com.mapisso.model.obj.Obj;

public class CreateScenes {
	
	public List<Scene> createSceneList(List<Obj> objList) {
		
		List<Scene> sceneList = new ArrayList<Scene>();

		Scene scene = new Scene();

		List<Integer> nodes = new ArrayList<Integer>();
		
		for (int i = 0; i < objList.size(); i++) {
			
			nodes.add(i);
		}

		scene.setNodes(nodes);
		sceneList.add(scene);
		
		return sceneList;
	}

}
