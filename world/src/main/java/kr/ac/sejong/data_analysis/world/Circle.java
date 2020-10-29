package kr.ac.sejong.data_analysis.world;

public class Circle {
	private double x, y, r;
	
	public static String description = "x, y, r width";
	// 생성자
	public Circle(double x, double y, double r) {
		super();
		this.x = x;
		this.y = y;
		this.r = r;
	}
	public Circle() {
		
	}


	public double getX() {
		return x;
	}



	public void setX(double x) {
		this.x = x;
	}



	public double getY() {
		return y;
	}



	public void setY(double y) {
		this.y = y;
	}



	public double getR() {
		return r;
	}



	public void setR(double r) {
		this.r = r;
	}



	double getArea() {
		return 3.14 * r * r; //double -> float 는 자동 형변환 안
	}							//3.14 디폴트 = double !
	
}
