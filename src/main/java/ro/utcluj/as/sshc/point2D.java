package ro.utcluj.as.sshc;

//package g.twoD;

public class point2D extends java.awt.Point {
	
	private double w = 1;

	public point2D() {
		super();
	}

	public point2D(java.awt.Point p) {
		super(p);
	}

	public point2D(int x,int y) {
		super(x,y);
	}

	public point2D(double x,double y,double w) {
		setLocation(x,y);
		this.w = w;
	}
	
	public double getW() {
		return w;
	}
	
	public boolean setW(double w) {
		if (w>=0) {
			this.w = w;
			return true;
		}
		else return false;
	}

	public boolean modify(java.awt.Point p, double w) {
		if (w>=0) {
			setLocation(p.getX(),p.getY());
			this.w = w;
			return true;
		}
		else return false;
	}

	public void modify(java.awt.Point p) {
		setLocation(p.getX(),p.getY());
	}
	
	public java.lang.Double getx() {
		return new java.lang.Double(getX());
	}
	
	public java.lang.Double gety() {
		return new java.lang.Double(getY());
	}

	public java.lang.Double getw() {
		return new java.lang.Double(w);
	}

}





