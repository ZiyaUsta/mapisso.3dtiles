package com.mapisso.gltf.v2;

import com.mapisso.constants.GLTFConstants;
import com.mapisso.model.gltf.v2.Asset;

public class CreateAsset {
	
	public Asset create() {
		
		Asset asset = new Asset();
		asset.setVersion(GLTFConstants.GLTF_VERSION);
		asset.setGenerator(GLTFConstants.GLTF_GENERATOR);
		
		return asset;
		
	}

}
