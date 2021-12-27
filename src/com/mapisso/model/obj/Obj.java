package com.mapisso.model.obj;

import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

public class Obj {
	
	private String o;
	
	private List<Coordinate> v;
	
	private List<List<Integer>> f;
	
	private Number[] maxCoord;
	
	private Number[] minCoord;
	
	private Integer minIndice;
	
	private Integer maxIndice;
	
	private Number[] center;
	
	private List<Triangle> triangles;
	
	private Map<Triangle, Float[]> normals;
	
	private Number[] minNormal;
	
	private Number[] maxNormal;
	
	public Obj() {
		
	}
	
	public Obj(String o, List<Coordinate> v, List<List<Integer>> f) {
		super();
		this.o = o;
		this.v = v;
		this.f = f;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public List<Coordinate> getV() {
		return v;
	}

	public void setV(List<Coordinate> v) {
		this.v = v;
	}

	public List<List<Integer>> getF() {
		return f;
	}

	public void setF(List<List<Integer>> f) {
		this.f = f;
	}

	public Number[] getMaxCoord() {
		return maxCoord;
	}

	public void setMaxCoord(Number[] maxCoord) {
		this.maxCoord = maxCoord;
	}

	public Number[] getMinCoord() {
		return minCoord;
	}

	public void setMinCoord(Number[] minCoord) {
		this.minCoord = minCoord;
	}

	public Integer getMinIndice() {
		return minIndice;
	}

	public void setMinIndice(Integer minIndice) {
		this.minIndice = minIndice;
	}

	public Integer getMaxIndice() {
		return maxIndice;
	}

	public void setMaxIndice(Integer maxIndice) {
		this.maxIndice = maxIndice;
	}
	
	public Number[] getCenter() {
		return center;
	}

	public void setCenter(Number[] center) {
		this.center = center;
	}
	
	public List<Triangle> getTriangles() {
		return triangles;
	}

	public void setTriangles(List<Triangle> triangles) {
		this.triangles = triangles;
	}
	
	public Map<Triangle, Float[]> getNormals() {
		return normals;
	}

	public void setNormals(Map<Triangle, Float[]> normals) {
		this.normals = normals;
	}
	
	public Number[] getMinNormal() {
		return minNormal;
	}

	public void setMinNormal(Number[] minNormal) {
		this.minNormal = minNormal;
	}

	public Number[] getMaxNormal() {
		return maxNormal;
	}

	public void setMaxNormal(Number[] maxNormal) {
		this.maxNormal = maxNormal;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String response = "o " + this.o + "\n" +"v " + this.v.toString() + "\n" + "f " + this.f;
		return response;
	}
	
	
	
}
