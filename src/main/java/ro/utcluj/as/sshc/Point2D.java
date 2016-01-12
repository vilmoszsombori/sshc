package ro.utcluj.as.sshc;

public class Point2D extends java.awt.Point {

    private static final long serialVersionUID = 6903835441595153917L;
    
    private double w = 1;

    public Point2D() {
        super();
    }

    public Point2D(java.awt.Point p) {
        super(p);
    }

    public Point2D(int x, int y) {
        super(x, y);
    }

    public Point2D(double x, double y, double w) {
        setLocation(x, y);
        this.w = w;
    }

    public double getW() {
        return w;
    }

    public boolean setW(double w) {
        if (w >= 0) {
            this.w = w;
            return true;
        } else
            return false;
    }

    public boolean modify(java.awt.Point p, double w) {
        if (w >= 0) {
            setLocation(p.getX(), p.getY());
            this.w = w;
            return true;
        } else
            return false;
    }

    public void modify(java.awt.Point p) {
        setLocation(p.getX(), p.getY());
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


