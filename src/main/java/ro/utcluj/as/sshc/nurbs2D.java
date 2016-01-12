package ro.utcluj.as.sshc;

//package g.twoD;

public class nurbs2D extends javax.swing.JComponent implements java.io.Serializable {
	
	static final int MAXNRPOINT = 90;
	static final int MAXORDER = 16;
	
	public boolean showPoints;
	public boolean showNumbers;

	private int render;
	private int order;
	private java.util.Vector controlPoint;
	private java.util.Vector poligon;
	private double[] knot;
	private boolean curveClosed;
	private java.awt.Color color;

	public nurbs2D(java.awt.Color color) { 
		init();
		this.color = color;
	}
	
	public point2D getControlPoint(int i) {	
		if ((i >= 0) && (i < controlPoint.size())) 
			return ((point2D)controlPoint.elementAt(i)); 
		else return null; 
	}
	
	public double getKnot(int i) { 
		if ((i >= 0) && (i < knotSize())) 
			return knot[i]; 
		else return Double.NaN; 
	}
	
	public int controlSize() { 
		return controlPoint.size(); 
	}
	
	public int knotSize() { 
		return controlPoint.size() + order; 
	}
	
	public boolean setOrder(int i) { 
		if ((i > 1) && (i <= MAXORDER)) { 
			order = i; 
			return true; 
		} 
		else return false; 
	}
	
	public boolean setRender(int i) { 
		if ((i > 1) && (i < 17)) { 
			render = i; 
			return true; 
		} 
		else return false; 
	}
	
	public int getRender() { 
		return render; 
	}
	
	public int getOrder() { 
		return order; 
	}
	
	public void setColor(java.awt.Color c) {
		color = c;
	}
	
	public boolean addPoint(java.awt.Point p) {
		if ((controlPoint.size() < MAXNRPOINT) && (!(curveClosed))) {
			point2D temp = new point2D(p); 
			controlPoint.add((point2D)temp);
			return true;
		}
		else return false;
	}

	public boolean modifyPoint(int i, java.awt.Point mp, double w) {
		if ((controlPoint.size() > 0) && (i < controlPoint.size())) {
			return ((point2D)controlPoint.elementAt(i)).modify(mp,w);
		}
		else return false;
	}

	public boolean modifyPoint(int i, double w) {
		if ((controlPoint.size() > 0) && (i < controlPoint.size())) {
			return ((point2D)controlPoint.elementAt(i)).setW(w);
		}
		else return false;
	}

	public void modifyPoint(int i, java.awt.Point mp) {
		if ((controlPoint.size() > 0) && (i < controlPoint.size())) {
			((point2D)controlPoint.elementAt(i)).modify(mp);
		}
	}
	
	public void setClosedCurve(boolean what) {
		curveClosed = what;
	}
	
	public boolean isClosed() {
		return curveClosed;
	}
	
	public boolean setKnot(int i, double j) {
		if ((i > 0) && (i < knotSize()-1) && ( knot[i-1] <= j) && (knot[i+1] >= j)) { 
			knot[i] = j; 
			return true; 
		}
		else return false;
	}
	
	public java.util.Vector toPolyline()  {
		if (controlPoint.size()>0) {
			if (!curveClosed) { 
				curveClosed = true; 
				updatePolygon(); 
				for(int i=0; i<render; i++) poligon.remove(poligon.size()-1);
			}
			return poligon;	
		}
		else return null;
	}


	public void paint(java.awt.Graphics g) {
		g.setColor(color);
		if (showPoints) {
			int x,y;
			for(int i = 0; i < controlPoint.size(); i++) {
				x = ((point2D)controlPoint.elementAt(i)).x;
				y = ((point2D)controlPoint.elementAt(i)).y;
				g.fill3DRect(x,y,3,3,true); 
				if (showNumbers) g.drawString(String.valueOf(i),x,y-3);
			}
		}	
		if (controlPoint.size() > order) {
			updatePolygon();
			int x,y;
			java.awt.Point p = new java.awt.Point();
			p = (java.awt.Point)poligon.elementAt(0);
			for(int i=0; i<poligon.size()-1; i++) {
				x = p.x; y = p.y;
				p = (java.awt.Point)poligon.elementAt(i+1);
				g.drawLine(x,y,p.x,p.y);
			}
			if (curveClosed) {
				x = p.x; y = p.y;
				p = (java.awt.Point)poligon.elementAt(0);
				g.drawLine(x,y,p.x,p.y);
			}
		}
		else g.drawString("Order must be lower than the number of control points!",10,10);		
	}
	
	
	private void updatePolygon() {
		poligon.removeAllElements();
		double t,epsilon,temp,Sx,Sy,S;
		int i,j,k,l,N=controlPoint.size();	    
		int vege = curveClosed ? knotSize()-1 : N;
		int masikvege = curveClosed ? N+order : N;
		for (i = order-1; i < vege; i++) {
			t = knot[i]; epsilon =  (knot[i+1]-knot[i])/render;
			for (j = 0; j<render; j++) {
				temp=0; Sx=0; Sy=0; S=0;
				for(k = 0; k<masikvege; k++) {
					temp = N(k,order,t) * ((point2D)controlPoint.elementAt(k%N)).getW();
					Sx += temp * ((point2D)controlPoint.elementAt(k%N)).getX();
					Sy += temp * ((point2D)controlPoint.elementAt(k%N)).getY();
					S  += temp;
				}
				poligon.add(new java.awt.Point((int)(Sx/S),(int)(Sy/S)));
				t+=epsilon;
			}
		}
	}


	private double N(int i, int k, double t) {
		if (k==1) {
			if ((knot[i]<=t)&&(knot[i+1]>t)) return 1;
			else return 0;
		}
		else {
			return ((t-knot[i])*N(i,k-1,t)/(knot[i+k-1]-knot[i]))+((knot[i+k]-t)*N(i+1,k-1,t)/(knot[i+k]-knot[i+1]));
		}
	}
	
	private void init() {
		render = 2; order = 4; 
		showPoints = true; showNumbers = false; curveClosed = false; 
		controlPoint = new java.util.Vector(); 
		poligon = new java.util.Vector();
		knot = new double[MAXNRPOINT + MAXORDER];
		for(int i=0; i<MAXNRPOINT + MAXORDER;i++) knot[i]=i;
	}
	
}