package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mapisso.model.gltf.v2.Accessor;
import com.mapisso.model.gltf.v2.Asset;
import com.mapisso.model.gltf.v2.Buffer;
import com.mapisso.model.gltf.v2.BufferView;
import com.mapisso.model.gltf.v2.Center;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.model.gltf.v2.Material;
import com.mapisso.model.gltf.v2.Mesh;
import com.mapisso.model.gltf.v2.Node;
import com.mapisso.model.gltf.v2.Scene;
import com.mapisso.model.obj.Obj;
import com.mapisso.utils.GLTFUtils;
import com.mapisso.utils.ObjUtils;

public class GLTFEngine {
	
	private static Logger logger = LoggerFactory.getLogger(GLTFEngine.class);
	
	private boolean isRTCused;
	private boolean isGlb;
	
	private CreateAsset createAsset = new CreateAsset();
	private CreateAccessors createAccessors = new CreateAccessors();
	private CreateBuffer createBuffer = new CreateBuffer();
	private CreateBufferViews createBufferViews = new CreateBufferViews();
	private CreateMaterials createMaterials = new CreateMaterials();
	private CreateMeshs createMeshs = new CreateMeshs();
	private CreateNodes createNodes = new CreateNodes();
	private CreateScenes createScenes = new CreateScenes();
	private GLTFUtils gltfUtils = new GLTFUtils();
	private ObjUtils objUtils = new ObjUtils();
	
	public GLTFEngine() {
	}
	
	public GLTFEngine(boolean isRTCused, boolean isGlb) {
		super();
		
		this.isRTCused = isRTCused;
		this.isGlb = isGlb;
		
		logger.info("GLTF ENGINE: isRTCused value:" + isRTCused + ", isGlb paramater value: " + isGlb);
		
	}

	public GLTF createGLTF(List<Obj> objList) {
		
		logger.info("GLTF ENGINE: transformation process started.");
		long methodStartTime = System.currentTimeMillis();
		
		GLTF gltf = null;
		
		if (objList != null) {
			
			Asset asset = createAsset.create();
			
			if(isRTCused && isGlb) {
				
				Map<String, Object> extensions = gltfUtils.createRTCCenter(objList);
				Center center = (Center) extensions.get("CESIUM_RTC");
				
				objList = objUtils.updateObjWithRtcCenter(objList, center.getCenter());
				
				List<Accessor> accessors = createAccessors.create(objList);
				List<Buffer> buffers = createBuffer.create(objList);
				List<BufferView> bufferViews = createBufferViews.create(objList);
				List<Material> materials = createMaterials.create();
				List<Mesh> meshes = createMeshs.createMeshList(objList);
				List<Node> nodes = createNodes.createNodeList(objList);
				List<Scene> scenes = createScenes.createSceneList(objList);
				int scene = 0;
				
				List<String> extensionsRequired = new ArrayList<String>();
				extensionsRequired.add("CESIUM_RTC");

				List<String> extensionsUsed = new ArrayList<String>();
				extensionsUsed.add("CESIUM_RTC");
				
				gltf = new GLTF(accessors, asset, buffers,  bufferViews, materials, meshes, nodes, scene, scenes, extensionsRequired, extensionsUsed);
				gltf.setExtensions(extensions);
				
			}else {
				
				List<Accessor> accessors = createAccessors.create(objList);
				List<Buffer> buffers = createBuffer.create(objList);
				List<BufferView> bufferViews = createBufferViews.create(objList);
				List<Material> materials = createMaterials.create();
				List<Mesh> meshes = createMeshs.createMeshList(objList);
				List<Node> nodes = createNodes.createNodeList(objList);
				List<Scene> scenes = createScenes.createSceneList(objList);
				int scene = 0;
				
				gltf = new GLTF(accessors, asset, buffers,  bufferViews, materials, meshes, nodes, scene, scenes);
			}
			
		}
		
		long methodFinishedTime = System.currentTimeMillis();
		logger.info("GLTF ENGINE: transformation process finished. elapsed time: " + (methodFinishedTime - methodStartTime) + " ms.");

		return gltf;

	}

	public boolean isRTCused() {
		return isRTCused;
	}

	public void setRTCused(boolean isRTCused) {
		this.isRTCused = isRTCused;
	}

	public boolean isGlb() {
		return isGlb;
	}

	public void setGlb(boolean isGlb) {
		this.isGlb = isGlb;
	}
	
}
