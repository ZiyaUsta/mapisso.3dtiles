package com.mapisso.model.gltf.v2;

public class BufferState {
	
	private byte[] positionBuffers;
	
	private byte[] normalBuffers;
	
	private byte[] uvBuffers  ;
	
	private byte[] indexBuffers  ;
	
	private int[] positionAccessors  ;
	
	private int[] normalAccessors  ;
			
	private int[] uvAccessors  ;	
	
	private int[] indexAccessors  ;

	public byte[] getPositionBuffers() {
		return positionBuffers;
	}

	public void setPositionBuffers(byte[] positionBuffers) {
		this.positionBuffers = positionBuffers;
	}

	public byte[] getNormalBuffers() {
		return normalBuffers;
	}

	public void setNormalBuffers(byte[] normalBuffers) {
		this.normalBuffers = normalBuffers;
	}

	public byte[] getUvBuffers() {
		return uvBuffers;
	}

	public void setUvBuffers(byte[] uvBuffers) {
		this.uvBuffers = uvBuffers;
	}

	public byte[] getIndexBuffers() {
		return indexBuffers;
	}

	public void setIndexBuffers(byte[] indexBuffers) {
		this.indexBuffers = indexBuffers;
	}

	public int[] getPositionAccessors() {
		return positionAccessors;
	}

	public void setPositionAccessors(int[] positionAccessors) {
		this.positionAccessors = positionAccessors;
	}

	public int[] getNormalAccessors() {
		return normalAccessors;
	}

	public void setNormalAccessors(int[] normalAccessors) {
		this.normalAccessors = normalAccessors;
	}

	public int[] getUvAccessors() {
		return uvAccessors;
	}

	public void setUvAccessors(int[] uvAccessors) {
		this.uvAccessors = uvAccessors;
	}

	public int[] getIndexAccessors() {
		return indexAccessors;
	}

	public void setIndexAccessors(int[] indexAccessors) {
		this.indexAccessors = indexAccessors;
	}

	
			
}
