package com.mapisso.gltf.v2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import com.mapisso.model.gltf.v2.BufferState;
import com.mapisso.model.gltf.v2.Mesh;


public class CreateBufferState {
	
	public BufferState create(List<Mesh> meshList) {
		
		byte[] positionBuffers = new byte[meshList.size() * 4];
		int[] positionAccesors = new int[meshList.size() * 4];
		
		byte[] indexBuffer = new byte[meshList.size() * 4];
		int[] indexAccessors = new int[meshList.size() * 4];
		
		
		int k = 0;
		
		System.out.println("mesh list size: " + meshList.size());
		for (int i = 0; i < meshList.size(); i++) {
			
			Mesh mesh = meshList.get(i);
			
			if(mesh.getPrimitives().get(0).getAttributes().get("POSITION") >= 0) {
				
				Integer positionValue = mesh.getPrimitives().get(0).getAttributes().get("POSITION");
				Integer indicesValue  = mesh.getPrimitives().get(0).getIndices();
				
				System.out.println("positionValue: " + positionValue);
				System.out.println("indicesValue: " + indicesValue);
				
				byte[] tempPositionBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float) positionValue).array();
				byte[] tempIndicesBuffer  = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat((float) indicesValue).array();
				
				for (int j = 0; j < tempPositionBuffer.length; j++) {
					positionBuffers[k + j] = tempPositionBuffer[j];
				}
				
				for (int j = 0; j < tempIndicesBuffer.length; j++) {
					indexBuffer[k + j] = tempIndicesBuffer[j];
				}
				
				positionAccesors[i] = positionValue;
				indexAccessors[i] = indicesValue;
				
			}
			
			k = k + 4;
			
		}
		
		BufferState bufferState = new BufferState();
		bufferState.setIndexAccessors(indexAccessors);
		bufferState.setIndexBuffers(indexBuffer);
		
		bufferState.setPositionAccessors(positionAccesors);
		bufferState.setPositionBuffers(positionBuffers);
		
		return bufferState;
		
	}

}
