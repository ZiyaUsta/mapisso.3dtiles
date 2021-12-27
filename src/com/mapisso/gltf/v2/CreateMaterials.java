package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;

import com.mapisso.model.gltf.v2.Material;
import com.mapisso.model.gltf.v2.MaterialPbrMetallicRoughness;

public class CreateMaterials {
	
	public List<Material> create() {
		
		List<Material> materialList = new ArrayList<Material>();

		Material material = new Material();
		material.setName("default");

		MaterialPbrMetallicRoughness materialPbrMetallicRoughness = new MaterialPbrMetallicRoughness();
		float[] baseColorFactor = new float[] { 0.8f, 0.8f, 0.8f, 1 };

		materialPbrMetallicRoughness.setBaseColorFactor(baseColorFactor);
		materialPbrMetallicRoughness.setMetallicFactor(0.5f);
		materialPbrMetallicRoughness.setRoughnessFactor(0.30000000000000004f);

		List<MaterialPbrMetallicRoughness> listMaterialRough = new ArrayList<MaterialPbrMetallicRoughness>();
		listMaterialRough.add(materialPbrMetallicRoughness);

		material.setPbrMetallicRoughness(materialPbrMetallicRoughness);
//		float[] emissiveFactor = new float[] { 0, 0, 0 };
//		material.setEmissiveFactor(emissiveFactor);

//		material.setAlphaMode("OPAQUE");
		material.setDoubleSided(true);

		materialList.add(material);
		
		return materialList;

	}

}
