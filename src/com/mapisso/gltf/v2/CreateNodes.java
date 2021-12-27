package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;

import com.mapisso.model.gltf.v2.Node;
import com.mapisso.model.obj.Obj;

public class CreateNodes {
	
	public List<Node> createNodeList(List<Obj> objList) {
		
		List<Node> nodeList = new ArrayList<Node>();
		
		for (int i = 0; i < objList.size(); i++) {

			Node node = new Node();

			Obj objTemp = objList.get(i);
			node.setName(objTemp.getO());
//			node.setMatrix(new float[]{1,0,0,0,0,0,-1,0,0,1,0,0,0,0,0,1});
			node.setMesh(i);

			nodeList.add(node);
		}
		
		return nodeList;
	}

}
