package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;

import com.mapisso.constants.GLTFConstants;
import com.mapisso.model.gltf.v2.BufferView;
import com.mapisso.model.obj.Obj;

public class CreateBufferViews {
	
	public List<BufferView> create(List<Obj> objList) {
		
		List<BufferView> bufferViewList = new ArrayList<BufferView>();

		Integer triangleSize = 0;
		Integer byteOffset = 0;

		for (int i = 0; i < objList.size(); i++) {

			Obj tempObj = objList.get(i);

			triangleSize = tempObj.getTriangles().size();
			
			BufferView bufferView0 = new BufferView();
			
			bufferView0.setBuffer(0);
			bufferView0.setByteLength(triangleSize * 4 * 3 * 3);
			bufferView0.setByteOffset(byteOffset);
//			bufferView0.setByteStride(12);
			bufferView0.setTarget(GLTFConstants.TARGET_OF_BUFFER[0]);
			bufferView0.setName("bufferView_0_coords");
			
			byteOffset = byteOffset + triangleSize * 4 * 3 * 3;
			
			BufferView bufferViewNormal = new BufferView();
			bufferViewNormal.setBuffer(0);
			bufferViewNormal.setByteLength(triangleSize * 4 * 3 * 3);
			bufferViewNormal.setByteOffset(byteOffset);
			bufferViewNormal.setTarget(GLTFConstants.TARGET_OF_BUFFER[0]);
			bufferViewNormal.setName("bufferView_2_normal");
			
			byteOffset = byteOffset + triangleSize * 4 * 3 * 3;
			
			BufferView bufferView1 = new BufferView();
			bufferView1.setBuffer(0);
			bufferView1.setByteLength(triangleSize * 3 * 2);
			bufferView1.setByteOffset(byteOffset);
			bufferView1.setTarget(GLTFConstants.TARGET_OF_BUFFER[0]);
			bufferView1.setName("bufferView_1_batchids");
			
			
			byteOffset = byteOffset +  triangleSize * 3 * 2;
			
			BufferView bufferView2 = new BufferView();
			bufferView2.setBuffer(0);
			bufferView2.setByteLength(triangleSize * 3 * 2);
			bufferView2.setByteOffset(byteOffset);
			bufferView2.setTarget(GLTFConstants.TARGET_OF_BUFFER[1]);
			bufferView2.setName("bufferView_2_indices");
			
			byteOffset = byteOffset + triangleSize * 3 * 2;
			
			bufferViewList.add(bufferView0);
			bufferViewList.add(bufferViewNormal);
			bufferViewList.add(bufferView1);
			bufferViewList.add(bufferView2);

		}

		return bufferViewList;

	}

}
