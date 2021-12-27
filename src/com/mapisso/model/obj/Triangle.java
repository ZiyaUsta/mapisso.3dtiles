package com.mapisso.model.obj;

public class Triangle {
	
	private Integer t1;
	
	private Integer t2;
	
	private Integer t3;
	
	private Float[] normal;
	
	public Triangle(Integer t1, Integer t2, Integer t3) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}

	public Integer getT1() {
		return t1;
	}

	public void setT1(Integer t1) {
		this.t1 = t1;
	}

	public Integer getT2() {
		return t2;
	}

	public void setT2(Integer t2) {
		this.t2 = t2;
	}

	public Integer getT3() {
		return t3;
	}

	public void setT3(Integer t3) {
		this.t3 = t3;
	}
	
	public Float[] getNormal() {
		return normal;
	}

	public void setNormal(Float[] normal) {
		this.normal = normal;
	}

	@Override
	public String toString() {
		return "IndicesTriangle [t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + "]";
	}
	
	

}
