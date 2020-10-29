package kr.ac.sejong.data_analysis.world;

public class Sphere extends Circle {
	
	private double z;

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Sphere(double x, double y, double z, double r) {
		super(x, y, r);
		this.z = z;
	}
	
	Object obj;

}
