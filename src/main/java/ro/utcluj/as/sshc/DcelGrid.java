package ro.utcluj.as.sshc;

import java.awt.*;

import ro.utcluj.as.sshc.exception.DivisionByZeroException;
import ro.utcluj.as.sshc.exception.InvalidNumericValueException;
import ro.utcluj.as.sshc.exception.NullStringException;
import ro.utcluj.as.sshc.exception.SyntaxErrorException;

class DoubleData {
	
	public double value;
	
	public DoubleData() {
		value = 0;
	}

	public DoubleData(double param) {
		value = param;
	}
	
}

class DrawHole extends javax.swing.JComponent {
	private int xc, yc, n, xm, ym, ycoord;
	private int[] x,y;
	private Color tr;
	
	public DrawHole(int x[], int y[], int n, 
					int xm, int ym, Color tr, int ycoord) {
		this.x = x;
		this.y = y;
		this.n = n;
		this.xm = xm;
		this.ym = ym;
		this.tr = tr;
		this.ycoord = ycoord;
	}
	
	public void paint(Graphics g) {
//		System.out.println("xc: "+xc+" yc: "+yc+" radius: "+radius);
		g.setColor(tr);	
		for (int u=0; u<n-1; u++) {
//			System.out.println("x: "+x[u]+" y: "+y[u]);
			g.fillOval(x[u] + xm - 3, (ym - y[u] - 3*((ycoord==-1)?-1:1))*ycoord, 7, 7);
			g.drawLine(x[u] + xm+1, (ym - y[u])*ycoord, x[u+1] + xm+1, (ym - y[u+1])*ycoord);
			g.drawLine(x[u] + xm, (ym - y[u]-1)*ycoord, x[u+1] + xm, (ym - y[u+1]-1)*ycoord);
		}
		g.drawLine(x[n-1] + xm+1, (ym - y[n-1])*ycoord, x[0] + xm+1, (ym - y[0])*ycoord);
		g.drawLine(x[n-1] + xm, (ym - y[n-1]-1)*ycoord, x[0] + xm, (ym - y[0]-1)*ycoord);
		g.fillOval(x[n-1] + xm - 3, (ym - y[n-1] - 3*((ycoord==-1)?-1:1))*ycoord, 7, 7);
		
	}	
}

class DrawStatTriangle extends javax.swing.JComponent {
	private int[][] x;
	private int[][] y;
	private double[] colorCoef;
	private int n;
	Color low, high;
	
	public DrawStatTriangle(int xm, int ym, int[][] x, int[][] y, double[] colorCoef, int n, 
							Color low, Color high, int ycoord) {
		this.x = x;
		this.y = y;
		this.colorCoef = colorCoef;
		this.n = n;
		this.low = low;
		this.high = high;
		for (int i=0; i<n; i++)
			for (int j=0; j<3; j++) {
				x[i][j] = x[i][j]+xm;
				y[i][j] = (ym-y[i][j])*ycoord;
			}
	}
	
	public void paint(Graphics g) {
//		System.out.println("xm: "+xm+" ym: "+ym);
		for (int i=0; i<n; i++) {
			int[] x1 = x[i];
			int[] y1 = y[i];
//			for (int j=0; j<3; j++) {
//				x1[j] = x1[j]+xm;
//				y1[j] = ym-y1[j];
//			}
//			if (i==0) System.out.println("x1[0]: "+x1[0]+" y1[0]: "+y1[0]);
			int red = high.getRed() - low.getRed();
			int green = high.getGreen() - low.getGreen();
			int blue = high.getBlue() - low.getBlue();
			g.setColor(new Color((int)(low.getRed()+red*colorCoef[i]), (int)(low.getGreen()+green*colorCoef[i]), (int)(low.getBlue()+blue*colorCoef[i])));
			g.fillPolygon(x1, y1, 3);
		}
	}
}

class DrawTriangle extends javax.swing.JComponent {
	private int xc, yc, radius, xm, ym, ycoord;
	private int[] x,y;
	private Color tr, ci;
	
	public DrawTriangle(int x[], int y[], int xc, int yc, int radius, 
					int xm, int ym, Color tr, Color ci, int ycoord) {
		this.x = x;
		this.y = y;
		this.xc = xc;
		this.yc = yc;
		this.radius = radius;
		this.xm = xm;
		this.ym = ym;
		this.tr = tr;
		this.ci = ci;
		this.ycoord=ycoord;
	}
	
	public void paint(Graphics g) {
		
//		System.out.println("xc: "+xc+" yc: "+yc+" radius: "+radius);
		g.setColor(tr);	
		for (int u=0; u<3; u++) {
//			System.out.println("x: "+x[u]+" y: "+y[u]);
			g.fillOval(x[u] + xm - 3, ym - y[u] - 3, 7, 7);
		}
		g.drawLine(x[0] + xm+1, (ym - y[0])*ycoord, x[1] + xm+1, (ym - y[1])*ycoord);
		g.drawLine(x[0] + xm, (ym - y[0]-1)*ycoord, x[1] + xm, (ym - y[1]-1)*ycoord);
		g.drawLine(x[1] + xm+1, (ym - y[1])*ycoord, x[2] + xm+1, (ym - y[2])*ycoord);
		g.drawLine(x[1] + xm, (ym - y[1]-1)*ycoord, x[2] + xm, (ym - y[2]-1)*ycoord);
		g.drawLine(x[0] + xm+1, (ym - y[0])*ycoord, x[2] + xm+1, (ym - y[2])*ycoord);
		g.drawLine(x[0] + xm, (ym - y[0]-1)*ycoord, x[2] + xm, (ym - y[2]-1)*ycoord);

		g.setColor(ci);
		g.drawOval(xc - radius + xm, (ym - (yc + radius*((ycoord==-1)?-1:1)))*ycoord, 2*radius, 2*radius);
		g.fillOval(xc + xm - 3,(ym - yc - 3 * ((ycoord==-1)?-1:1))*ycoord, 7, 7);
	}	
}

class DrawEdge extends javax.swing.JComponent {
	private int xc1, yc1, radius1, xc2, yc2, radius2, xm, ym, ycoord;
	private int[] x,y;
	private Color tr, ci;
	
	public DrawEdge(int x[], int y[], int xc1, int yc1, int radius1, 
					int xc2, int yc2, int radius2,
					int xm, int ym, Color tr, Color ci, int ycoord) {
		this.x = x;
		this.y = y;
		this.xc1 = xc1;
		this.yc1 = yc1;
		this.radius1 = radius1;
		this.xc2 = xc2;
		this.yc2 = yc2;
		this.radius2 = radius2;
		this.xm = xm;
		this.ym = ym;
		this.tr = tr;
		this.ci = ci;
		this.ycoord = ycoord;
	}
	
	public void paint(Graphics g) {
		
//		System.out.println("xc: "+xc+" yc: "+yc+" radius: "+radius);
		g.setColor(tr);	
		for (int u=0; u<2; u++) {
//			System.out.println("x: "+x[u]+" y: "+y[u]);
			g.fillOval(x[u] + xm - 3, (ym - y[u] - 3 * ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		}
		g.drawLine(x[0] + xm+1, (ym - y[0])*ycoord, x[1] + xm+1, (ym - y[1])*ycoord);
		g.drawLine(x[0] + xm, (ym - y[0]-1)*ycoord, x[1] + xm, (ym - y[1]-1)*ycoord);
		

		g.setColor(ci);
		if (radius1!=0) {
			g.drawOval(xc1 - radius1 + xm, (ym - (yc1 + radius1 * ((ycoord==-1)?-1:1)))*ycoord, 2*radius1, 2*radius1);
			g.fillOval(xc1 + xm - 3,(ym - yc1 - 3 * ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		}
		if (radius2!=0) {
			g.drawOval(xc2 - radius2 + xm, (ym - (yc2 + radius2 * ((ycoord==-1)?-1:1)))*ycoord, 2*radius2, 2*radius2);
			g.fillOval(xc2 + xm - 3,(ym - yc2 - 3 * ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		}
	}	
}

class DrawVertexVisil extends javax.swing.JComponent {
	private int x1[];
	private int y1[];
	private int xo;
	private int yo, ycoord;
	private int n, xm, ym;
	private Color ed, ve;

	public DrawVertexVisil(int xo, int yo, int n,
					 int x1[], int y1[], int xm, int ym,
					 Color ed, Color ve, int ycoord) {
		this.ve = ve;
		this.ed = ed;
		this.x1 = x1;
		this.y1 = y1;
		this.xo = xo; 
		this.yo = yo;
		this.xm = xm;
		this.ym = ym;
		this.n = n;
		this.ycoord = ycoord;
	}

	public void paint(java.awt.Graphics g) {

		g.setColor(ve);
		for (int u=0; u<n; u++) {
			g.drawLine(x1[u] + xm, (ym - y1[u])*ycoord, xo+xm,(ym-yo)*ycoord );
			g.fillOval(x1[u] + xm-3, (ym - y1[u]-3* ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		}

		g.setColor(ed);
		g.fillOval(xo + xm-3, (ym - yo-3*((ycoord==-1)?-1:1))*ycoord, 7, 7);
	}
}


class DrawEdgeDelaunayVertex extends javax.swing.JComponent {
	private int x;
	private int y;
	private int xo;
	private int yo;
	private int xd;
	private int yd, ycoord;
	private int xm, ym;
	private Color ed, ve;

	public DrawEdgeDelaunayVertex(int xo, int yo, int xd, int yd, int x, int y,
			 int xm, int ym, Color ed, Color ve, int ycoord) {
		this.ve = ve;
		this.ed = ed;
		this.x = x;
		this.y = y;
		this.xo = xo;
		this.yo = yo;
		this.xd = xd;
		this.yd = yd;
		this.xm = xm;
		this.ym = ym;
		this.ycoord = ycoord;
	}

	public void paint(java.awt.Graphics g) {

		g.setColor(ve);
		g.fillOval(x + xm - 3, (ym - y - 3 * ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		g.drawLine(xo + xm, (ym - yo)*ycoord, x + xm+1, (ym - y)*ycoord);
		g.drawLine(xd + xm, (ym - yd)*ycoord, x + xm, (ym - y-1)*ycoord);

		g.setColor(ed);
		g.drawLine(xo + xm+1, (ym - yo)*ycoord, xd + xm+1, (ym - yd)*ycoord);
		g.drawLine(xo + xm, (ym - yo-1)*ycoord, xd + xm, (ym - yd-1)*ycoord);
//		g.fillOval(xo + xm -4, ym - yo - 4, 9, 9);
//		g.fillOval(xd + xm -4, ym - yd - 4, 9, 9);
	}
}


class DrawEdgeVisil extends javax.swing.JComponent {
	private int x1[];
	private int y1[];
	private int xo, ycoord;
	private int yo;
	private int xd;
	private int yd;
	private int n, xm, ym;
	private Color ed, ve;

	public DrawEdgeVisil(int xo, int yo, int xd, int yd, int n,
					 int x1[], int y1[], int xm, int ym,
					 Color ed, Color ve, int ycoord) {
		this.ve = ve;
		this.ed = ed;
		this.x1 = x1;
		this.y1 = y1;
		this.xo = xo;
		this.yo = yo;
		this.xd = xd;
		this.yd = yd;
		this.xm = xm;
		this.ym = ym;
		this.n = n;
		this.ycoord = ycoord;
	}

	public void paint(java.awt.Graphics g) {

		g.setColor(ve);
		for (int u=0; u<n; u++) {
			g.fillOval(x1[u] + xm - 3, (ym - y1[u] - 3* ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		}

		g.setColor(ed);
		g.drawLine(xo + xm+1, (ym - yo)*ycoord, xd + xm+1, (ym - yd)*ycoord);
		g.drawLine(xo + xm, (ym - yo-1)*ycoord, xd + xm, (ym - yd-1)*ycoord);
//		g.fillOval(xo + xm -4, ym - yo - 4, 9, 9);
//		g.fillOval(xd + xm -4, ym - yd - 4, 9, 9);
	}
}


class DrawVoxel extends javax.swing.JComponent {
	private int x1[];
	private int y1[];
	private int x2[];
	private int y2[];
	private int xv[];
	private int yv[];
	private int i,j,k,l,xm,ym,nv,ne, ycoord;
	private double maxX, maxY, minX, minY;
	private Color ve, ed, gr;

	public DrawVoxel(int xv[], int yv[], int x1[], int y1[], int x2[], int y2[],
					int i, int j, int k, int l, int xm, int ym, int nv, int ne, 
					double maxX, double maxY, double minX, double minY, 
					Color ve, Color ed, Color gr, int ycoord) {
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
		this.maxX = maxX;
		this.minX = minX;
		this.maxY = maxY;
		this.minY = minY;
		this.ve = ve;
		this.ed = ed;
		this.gr = gr;
		this.xm = xm;
		this.ym = ym;
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1 ;
		this.y2 = y2;
		this.xv = xv;
		this.yv = yv;
		this.nv = nv;
		this.ne = ne;
		this.ycoord = ycoord;
	}

	public void paint(java.awt.Graphics g) {
		// drawing the grid
		double w = (maxX-minX)/k;
		double h = (maxY-minY)/l;

		g.setColor(gr);
		g.fillRect((int)(minX+(i)*w+xm), (int)(ym-(minY+(j+ ((ycoord==-1)?0:1))*h))*ycoord, (int)(w), (int)(h));

		g.setColor(ve);
		for (int u=0; u<nv; u++) {
			g.fillOval(xv[u] + xm -3, (ym - yv[u] - 3 * ((ycoord==-1)?-1:1))*ycoord, 7, 7);
		}

		g.setColor(ed);
		for (int u=0; u<ne; u++) {
			g.drawLine(x1[u] + xm+1, (ym - y1[u])*ycoord, x2[u] + xm+1, (ym - y2[u])*ycoord);
			g.drawLine(x1[u] + xm, (ym - y1[u]-1)*ycoord, x2[u] + xm, (ym - y2[u]-1)*ycoord);
		}
		
	}

}

public class DcelGrid {
	public static final double EPS = 0.0000001;
	public static final double D = 0.01;
	private double angle = 0.0;
	private static HalfEdge triangleEdge;
	private java.util.Vector vertices;
	private java.util.Vector triangles;
	private java.util.Vector halfedges;
	private java.util.Vector outers;
	private Grid g;
	private int k, l, initialVertexNumber;
	private FIFO encroachedEdges;
	private java.util.Vector smallTriangles;
	
	class CompareTriangles implements java.util.Comparator {
		public int compare(Object o1, Object o2) {
			Triangle t1 = (Triangle)o1;
			Triangle t2 = (Triangle)o2;
			if (t1.getSmallestAngle()<t2.getSmallestAngle())
				return -1;
			return 1;
		}
		
		public boolean equals(Object o1, Object o2) {
			Triangle t1 = (Triangle)o1;
			Triangle t2 = (Triangle)o2;
			if (t1.getSmallestAngle()==t2.getSmallestAngle()) return true;
			return false;
		}
	}

	class FIFO {
		private java.util.Vector data;
		
		public FIFO() {
			data = new java.util.Vector();
		}
		
		public void push(HalfEdge e) {
			data.add(e);
		}

		public HalfEdge pull() {
			return (HalfEdge)data.remove(0);
		}
//			getClass		
		public boolean isEmpty() {
			if (data.size()>0) return false;
			return true;
		}
	}
	
	public int getI(int x) {
		return g.getI(x);
	}

	public int getJ(int y) {
		return g.getJ(y);
	}
	
	public int getInitialVertexNumber() {
		return initialVertexNumber;
	}
	
	public double getValuePoint(double x, double y) {
		Triangle t = triangleEdge.getTriangle();
		return t.S(x,y);
	}
	
	public DcelGrid(java.util.Vector contours, int i, int j) {
		int n = contours.size();
		this.k = i;
		this.l = j;
		java.util.Vector oc, ic;
		oc = new java.util.Vector();
		ic = new java.util.Vector();

		// initiating vectors and the grid
		vertices = new java.util.Vector();
		triangles = new java.util.Vector();
		halfedges = new java.util.Vector();
		outers = new java.util.Vector();
		g = new Grid(i, j);

		// processing contours to dcel setting maxXY, minXY for grid
		if (n==0)  {
			System.out.println("Rossz Subdivision konstruktor!!");
			System.exit(0);
		}
		else {
			oc = (java.util.Vector)contours.elementAt(0);
			extractContour(oc, false);
			for (int u=1 ; u<n; u++) {
				ic = (java.util.Vector)contours.elementAt(u);
				extractContour(ic, true);
			}
		}
//		System.out.println(g.getMaxX());
		g.setMaxX(g.getMaxX()+10);
		g.setMinX(g.getMinX()-10);
		g.setMaxY(g.getMaxY()+10);
		g.setMinY(g.getMinY()-10);
			
		// creating grid structure dimension [i,j] using maxXY, minXY
//		System.out.println("["+g.getMaxX()+","+g.getMaxY()+"] - ["+g.getMinX()+","+g.getMinY()+"]");
//		Face domain = (Face)(faces.elementAt(0));
//		System.out.println(domain);
//		System.out.println(domain.getOuterComponent());
//		System.out.println(domain.getInnerComponents().size());

		//inserting vertices in the grid
		for (i=0; i< vertices.size(); i++) {
//			System.out.println(i);
			g.addVertex((Vertex)(vertices.elementAt(i)));
		}

		//inserting halfedges in the grid
		int hen = halfedges.size();
		for (int u=0; u<hen; u++) {
			HalfEdge e = (HalfEdge)(halfedges.elementAt(u));
			g.addEdge(e);
		}
		initialVertexNumber = vertices.size();
		// testing FindEdge
//		HalfEdge e = domain.getOuterComponent();
//		System.out.println("Talalt kivulre: "+ FindEdge(e)+" functional: "+functional(e));
//		java.util.Vector ico = domain.getInnerComponents();
//		for (int u=0; u<ico.size(); u++) {
//			e=(HalfEdge)(ico.elementAt(u));
//			System.out.println("Talaltam bentre"+u+": "+FindEdge(e)+" functional: "+functional(e));
//		}
//		System.out.println(g.getDelaunayVertex(domain.getOuterComponent()));
//		computeConstrainedDelaunay();
//		for (int u=0; u<vertices.size(); u++) {
//			Vertex vt = (Vertex)(vertices.elementAt(u));
//			System.out.println(vt+" from "+vt.getEdge());
//		}
//		HalfEdge e = (HalfEdge)(halfedges.elementAt(halfedges.size()-2));
//		HalfEdge t = e.getPrev();
//		while (t!=e) {
//			System.out.println(t+" and the findedge for it: "+ FindEdge(t));
//			t = t.getPrev();
//		}
//		System.out.println(t);
//		System.out.println(e.getPrev());
//		System.out.println(e.getNext());
		
	}
	
	public void computeConstrainedDelaunay() {
		HalfEdge e = (HalfEdge)(halfedges.elementAt(halfedges.size()-1));
		smallTriangles = new java.util.Vector();
		delaunayize(e);
		encroachedEdges = new FIFO();
//		for (int u=0; u<triangles.size(); u++) {
//			Triangle t = (Triangle)triangles.elementAt(u);
//			if (t.calcSmallestAngle()<angle) smallTriangles.add(t);
//			System.out.println(t+"  "+t.getSmallestAngle());
//		}
//		java.util.Collections.sort(triangles, new CompareTriangles());
//		System.out.println(triangles.size());
//		for (int u=0; u<triangles.size(); u++) {
//			Triangle t = (Triangle)triangles.elementAt(u);
//			System.out.println(t+"  "+t.getSmallestAngle());
//		}
//		System.out.println();
//		java.util.Collections.sort(smallTriangles, new CompareTriangles());
//		listSmallTriangles();
//		delaunayRefine(25.0);
//		System.out.println("Number of triangles: "+triangles.size());
//		System.out.println("Number of edges: "+halfedges.size()/2);
	}
	
	
	public void solve(javax.swing.JPanel panel, int method) throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		for (int u=0; u<halfedges.size(); u++) {
			HalfEdge e = (HalfEdge)halfedges.elementAt(u);
			e.createDoubleData();
		}
		for (int u=0; u<vertices.size(); u++) {
			Vertex v = (Vertex)vertices.elementAt(u);
			v.createDoubleData();
		}
		for (int u=0; u<triangles.size(); u++) {
			Triangle t = (Triangle)triangles.elementAt(u);
			t.computeStiffnessMatrix();
			panel.add(t);
		}
		LinearSystem l = new LinearSystem(halfedges, vertices.size(), method);
	}

	private void listSmallTriangles() {
		System.out.println(smallTriangles.size());
		for (int u=0; u<smallTriangles.size(); u++) {
			Triangle t = (Triangle)smallTriangles.elementAt(u);
			System.out.println(t+"  "+t.getSmallestAngle());
		}
		System.out.println();
	}
	
	public void removeLastVertex() {
		if (initialVertexNumber<vertices.size()) {
			Vertex a = (Vertex)(vertices.elementAt(vertices.size()-1));
			vertices.remove(vertices.size()-1);
			if (a.onBoundary()) {
			}
			else {
				HalfEdge e = a.getEdge();
				HalfEdge cont = null;
				if (e==null) System.out.println("Isolated vertex: "+a);
				cont = e.getNext();
				HalfEdge t = e;
				do {
					t.getNext().getOrig().setHalfEdge(t.getNext());
					splice(t.getNext(), t.getTwin());
					if (!halfedges.remove(t)) halfedges.remove(t.getTwin());
						g.removeEdge(t);
					triangles.remove(t.getTriangle());
					t.getNext().setTriangle(null);
					t = t.originNext();
				} while(t!=e);
				g.removeVertex(a);
				t = cont;
				delaunayize(cont);
			}
//			System.out.println("cont: "+cont);
//			System.out.println("e: "+e);
//			do {
//				System.out.println(t);
//				t = t.getNext();
//			} while(t!=cont);
//			System.out.println();
//			splice(t.getNext(), t.getTwin());
//			halfedges.remove(t);
		}
		
	}
	
	public int getTriangleNumber() {
		return triangles.size();
	}

	public int getEdgeNumber() {
		return halfedges.size();
	}
	public int getVertexNumber() {
		return vertices.size();
	}
	
	public void delaunayRefine(double angle) {
		this.angle = angle;
		HalfEdge e = (HalfEdge)(halfedges.elementAt(0));
		smallTriangles = new java.util.Vector();
		delaunayize(e);
		
		encroachedEdges = new FIFO();
		for (int u = 0; u<initialVertexNumber; u++) {
			e = (HalfEdge)halfedges.elementAt(u);
			if (e.oppositeAngle()>90) encroachedEdges.push(e);
		}
		
		do {
			while (!encroachedEdges.isEmpty()) {
				e = encroachedEdges.pull();
				splitEdge(e);
			}
			if (smallTriangles.size()>0) {
				java.util.Collections.sort(smallTriangles, new CompareTriangles());
//				listSmallTriangles();
				Triangle t = (Triangle)smallTriangles.elementAt(0);
//				System.out.println(t);
				Vertex a = insertCircumCenter(t);
				if (updateEncroach(a)>0) {
					removeLastVertex();
				} else updateTriangles(a);
			}
		} while ((!encroachedEdges.isEmpty())||(smallTriangles.size()>0));
		for (int u=0; u<triangles.size(); u++) {
			Triangle t = (Triangle)triangles.elementAt(u);
			t.calcRealSmallestAngle();
		}
		int[] num = new int[11];
		java.util.Collections.sort(triangles, new CompareTriangles());

//		for (int u=0; u<triangles.size(); u++) {
//			Triangle t = (Triangle)triangles.elementAt(u);
//			double a = t.getSmallestAngle();
//			int i = (int)((a-angle) / (60.0 - angle) * 10.0);
//			System.out.println("a: "+a+" i: "+i);
//			if (i<0) num[10]++; else num[i]++;
//		}
//		for (int u=0; u<10; u++) {
//			double p = num[u]/(double)triangles.size()*100;
//			System.out.println(angle+u*(60-angle)/10.0+" - "+(angle+(u+1)*(60-angle)/10.0)+":  "+num[u]+" this is procent: "+ p);
//			
//		}
//		double p = num[10]/(double)triangles.size()*100;
//		System.out.println("input imposed angles small: "+num[10]+" this is procent: "+p);
		
//		listSmallTriangles();
	}

	public void refine(double angle) {
		this.angle = angle;
		HalfEdge e = (HalfEdge)(halfedges.elementAt(0));
		smallTriangles = new java.util.Vector();
		for (int u=0; u<triangles.size(); u++) {
			Triangle t = (Triangle)triangles.elementAt(u);
			if (t.calcSmallestAngle()<angle) smallTriangles.add(t);
		}
		
		encroachedEdges = new FIFO();
		for (int u = 0; u<halfedges.size(); u++) {
			e = (HalfEdge)halfedges.elementAt(u);
			if (e.getTwin().getTriangle().getLabel()<0)
				if (e.oppositeAngle()>90) encroachedEdges.push(e);
		}
		
		do {
			while (!encroachedEdges.isEmpty()) {
				e = encroachedEdges.pull();
				splitEdge(e);
			}
			if (smallTriangles.size()>0) {
				java.util.Collections.sort(smallTriangles, new CompareTriangles());
//				listSmallTriangles();
				Triangle t = (Triangle)smallTriangles.elementAt(0);
//				System.out.println(t);
				Vertex a = insertCircumCenter(t);
				if (updateEncroach(a)>0) {
					removeLastVertex();
				} else updateTriangles(a);
			}
		} while ((!encroachedEdges.isEmpty())||(smallTriangles.size()>0));
		for (int u=0; u<triangles.size(); u++) {
			Triangle t = (Triangle)triangles.elementAt(u);
			t.calcRealSmallestAngle();
		}
		int[] num = new int[11];
		java.util.Collections.sort(triangles, new CompareTriangles());
	}
	
	private void delaunayize(HalfEdge e) {
		if (e.getNext().getNext().getNext()==e) {
//			System.out.println("already triangle");
			if (e.getTriangle()==null) {
				Vertex a = e.getPrev().getOrig();
				double x1 = a.getX();
				double y1 = a.getY();
				Vertex b = e.getOrig();
				double x2 = b.getX();
				double y2 = b.getY();
				Vertex c = e.getTwin().getOrig();
				double x3 = c.getX();
				double y3 = c.getY();
			
				double b1 = x1*x1 + y1*y1;
				double b2 = x2*x2 + y2*y2;
				double b3 = x3*x3 + y3*y3;	
		
				double det = (x2-x1)*(y3-y1) - (y2-y1)*(x3-x1);
				double A = ((b2-b1)*(y3-y1) - (y2-y1)*(b3-b1))/det;
				double B = ((x2-x1)*(b3-b1) - (b2-b1)*(x3-x1))/det;
				double C = (x1*y2*b3 + x2*y3*b1 + x3*y1*b2 - x3*y2*b1 - x2*y1*b3 - x1*y3*b2)/det;
				
				double xc = A/2;
				double yc = B/2;
				double radius = Math.sqrt(C+xc*xc+yc*yc);
				
				Triangle tr = new Triangle(xc,yc,radius, e, triangles.size()+1);
				e.setTriangle(tr);
				e.getNext().setTriangle(tr);
				e.getPrev().setTriangle(tr);	

				if (tr.calcSmallestAngle()<angle) smallTriangles.add(tr);
				triangles.add(tr);
			}
		}
		else {
			e = FindEdge(e);
//			System.out.println();
//			System.out.println("edge to be delaunayized: "+e);
			Vertex v = g.getDelaunayVertex(e);
//			System.out.println("vertex for this edge: "+v);
			Vertex a = e.getOrig();
			Vertex b = e.getTwin().getOrig();

			if (e.getPrev().getOrig()==v) {
//				System.out.println("bounded from left");
				HalfEdge t = e.getPrev().getPrev();
				HalfEdge d = makeEdge(e.getTwin().getOrig(),v);
				g.addEdge(d);
				
				splice(d, e.getNext());
				splice(d.getTwin(), e.getPrev());
				
				Triangle tr = new Triangle(g.xc,g.yc,g.radius, e, triangles.size()+1);
				e.setTriangle(tr);
				e.getNext().setTriangle(tr);
				e.getPrev().setTriangle(tr);

				triangles.add(tr);
				if (tr.calcSmallestAngle()<angle) smallTriangles.add(tr);
				delaunayize(d.getTwin());
			}
			else
			if (e.getNext().getTwin().getOrig()==v) {
//				System.out.println("bounded from right");
				HalfEdge t = e.getNext().getNext();
				HalfEdge d = makeEdge(e.getOrig(),v);
				g.addEdge(d);

				splice(d, e);
				splice(d.getTwin(), e.getNext().getNext());
				
				Triangle tr = new Triangle(g.xc,g.yc,g.radius, e, triangles.size()+1);
				e.setTriangle(tr);
				e.getNext().setTriangle(tr);
				e.getPrev().setTriangle(tr);

				triangles.add(tr);
				if (tr.calcSmallestAngle()<angle) smallTriangles.add(tr);
				delaunayize(d);
			
			}
			else {
//				System.out.println("general case");
				HalfEdge d1 = makeEdge(a,v);
				HalfEdge d2 = makeEdge(b,v);
				HalfEdge t = v.getEdge();
				g.addEdge(d1);
				g.addEdge(d2);
//				System.out.println("edge for the vertex: "+t);
				
				while (!(
				         ((a.leftOf(t))&&( (a.rightOf(t.getPrev().getTwin()))||(t.getPrev().getOrig().rightOf(t)) ))||
				         ((a.leftOf(t.getPrev()))&&( (a.rightOf(t.getTwin()))||(t.getOrig().rightOf(t.getPrev())) ))
				        )
				      ) {
							t = t.originNext();
						}
						
				splice(d1.getTwin(), t);
				splice(d1, e);
				
				splice (d2.getTwin(), d1.getTwin());
				splice(d2, e.getNext());		
	
				Triangle tr = new Triangle(g.xc,g.yc,g.radius, e, triangles.size()+1);
				e.setTriangle(tr);
				e.getNext().setTriangle(tr);
				e.getPrev().setTriangle(tr);

				triangles.add(tr);
				if (tr.calcSmallestAngle()<angle) smallTriangles.add(tr);
				delaunayize(e.getNext().getTwin());
				boolean same = false;
				HalfEdge or = e.getNext().getTwin();
				HalfEdge te = or;
				do {
					if (te == e.getPrev().getTwin()) same = true;
					te = te.getNext();
				} while ((!same)&&(or!=te));
				if (!same) delaunayize(e.getPrev().getTwin());
			}
			
		}

	}
	
	private HalfEdge localizeVertex(Vertex a) {
		HalfEdge e = g.getCloseEdge(a);
//		System.out.println(a);
//		System.out.println("Close edge: "+e+" for vertex: "+a);
		if (e!=null)
		while (true) {
			if ((a.equals(e.getOrig()))||(a.equals(e.getTwin().getOrig())))
				return e;
			else {
				if (!(a.leftOf(e)))  {
					e = e.getTwin();
//					System.out.println("Next: "+e);
				}
				else {
					if (e.getTriangle().getLabel()<0) {
						if (a.rightOf(e)) {
						 	e = e.getTwin();
//							System.out.println("Right on the edge of hole");
						}
						else
						if (a.insidePolygon(e))  {
//							System.out.println("vertex: "+a+" inside the polygon: "+e);
							return e;
						}
						else  {
							HalfEdge t = e;
							do {
								t = t.getNext();
//								System.out.println("Avoiding cavities "+t);
							} while (!(t.getTwin().getOrig().leftOf(e)));
							e = t;
							
//							System.out.println("Next: "+e);
						}
					}
					else {
						if (a.leftOf(e.originNext()))  {
							e = e.originNext();
//							System.out.println("Next: "+e);
						}
						else {
							if (!(a.rightOf(e.getNext()))) return e;
							else { 
								e = e.getNext().getTwin();
//								System.out.println("Next: "+e);
							}
						}
					}
				}
			}	
		}
		return null;
	}
	
	private HalfEdge makeEdge(Vertex a, Vertex b) {
		HalfEdge e = new HalfEdge();
		e.setOrigin(a);
		HalfEdge p = new HalfEdge();
		p.setOrigin(b);
		e.setTwin(p);
		p.setTwin(e);
		e.setPrev(p);
		p.setPrev(e);
		halfedges.add(e);
//		halfedges.add(p);
		return e;
	}
	
	public javax.swing.JComponent getCompEdge(int i, int xm, int ym, java.awt.Color tr, java.awt.Color ci, int ycoord) {
		HalfEdge e = (HalfEdge)(halfedges.elementAt(i));
		System.out.println(e.oppositeAngle());
		Triangle t1 = e.getTriangle();
		Triangle t2 = e.getTwin().getTriangle();
		int xc1, yc1, radius1, xc2, yc2, radius2;
		xc1=yc1=radius1=xc2=yc2=radius2=0;
		if (t1!=null) {
			xc1 = (int)(t1.getXc());
			yc1 = (int)(t1.getYc());
			radius1 = (int)(t1.getRadius());
		}
		if (t2!=null) {
			xc2 = (int)(t2.getXc());
			yc2 = (int)(t2.getYc());
			radius2 = (int)(t2.getRadius());
		}
		int[] x = new int[2];
		int[] y = new int[2];
		x[0] = (int)(e.getOrig().getX());
		y[0] = (int)(e.getOrig().getY());
		x[1] = (int)(e.getTwin().getOrig().getX());
		y[1] = (int)(e.getTwin().getOrig().getY());
		return new DrawEdge(x, y, xc1, yc1, radius1, xc2, yc2, radius2, xm, ym, tr, ci, ycoord);
	}

//	public javax.swing.JComponent getCompTriangle(int i, int xm, int ym, java.awt.Color tr, java.awt.Color ci) {
//		Triangle t = (Triangle)(triangles.elementAt(i-1));
//		int[] x, y;
//		x = new int[3];
//		y = new int[3];
//		double[] tx, ty;
//		tx = t.getX();
//		ty = t.getY();
//		for (int u=0; u<3; u++) {
//			x[u] = (int)tx[u];
//			y[u] = (int)ty[u];
//		}
//		return new DrawTriangle(x, y, (int)(t.getXc()), (int)(t.getYc()), (int)(t.getRadius()), xm, ym, tr, ci);
//	}
	
	public javax.swing.JComponent getCompHole(int i, int xm, int ym, Color tr, int ycoord) {
		java.util.Vector xv = new java.util.Vector();
		java.util.Vector yv = new java.util.Vector();
//		System.out.println("Hole reffered in vector: "+(-1*i-1));
		Triangle t = (Triangle)(outers.elementAt(-1*i-1));
		HalfEdge e = t.getEdge();
		HalfEdge temp = e;
		do {
//			System.out.println(temp);
			xv.add(new Integer((int)(temp.getOrig().getX())));
			yv.add(new Integer((int)(temp.getOrig().getY())));
			temp = temp.getNext();
		}while(temp!=e);

		int n = xv.size();
		int[] x = new int[n];
		int[] y = new int[n];
		Integer p;
		for(int u=0; u<n; u++) {
			p =(Integer)(xv.elementAt(u));
			x[u] = p.intValue();
			p =(Integer)(yv.elementAt(u));
			y[u] = p.intValue();
		}		
		return new DrawHole(x, y, n, xm, ym, tr, ycoord);

	}
	
		
	public javax.swing.JComponent getCompEdgeVisil(int i, int xm, int ym, java.awt.Color ed, java.awt.Color ve, boolean strict, int ycoord) {
		int xo, yo, xd, yd;
		int n;
		HalfEdge e = (HalfEdge)(halfedges.elementAt(i));
//		System.out.println(e);
//		System.out.println(e.getTwin().getTriangle().getLabel());
//		System.out.println(e);
		Vertex v2;
		java.util.Vector v = new java.util.Vector();
		for (int u=0; u<vertices.size(); u++) {
//			if (u==i) u=u+2;
			v2 = (Vertex)(vertices.elementAt(u));
			boolean bo = g.isVisible(v2,e, strict);				
			if (bo)	{
//				System.out.println("vertex "+v2+" isVisible ");
				v.add(v2);
			}
		}
		n = v.size();
		int x1[] = new int[n];
		int y1[] = new int[n];
		xo = (int)(e.getOrig().getX());
		yo = (int)(e.getOrig().getY());
		xd = (int)(e.getTwin().getOrig().getX());
		yd = (int)(e.getTwin().getOrig().getY());
		for (int u=0; u<n; u++) {
			Vertex t = (Vertex)(v.elementAt(u));
			x1[u] = (int)(t.getX());
			y1[u] = (int)(t.getY());
		}
		return new DrawEdgeVisil(xo, yo, xd, yd, n, x1, y1, xm, ym, ed, ve, ycoord);
	}
	
	public javax.swing.JComponent getCompVertexVisil(int i, int xm, int ym, java.awt.Color ed, java.awt.Color ve, boolean strict, int ycoord) {
		int xo, yo;
		int n;
		Vertex p = (Vertex)(vertices.elementAt(i));
//		System.out.println(e);
		Vertex v2;
		java.util.Vector v = new java.util.Vector();
		for (int u=0; u<vertices.size(); u++) {
			v2 = (Vertex)(vertices.elementAt(u));
			boolean bo = g.isVisible(p,v2, strict);
			if (bo)	{
//				System.out.println("vertex "+v2+" isVisible ");
				v.add(v2);
			}
		}
		n = v.size();
		int x1[] = new int[n];
		int y1[] = new int[n];
		xo = (int)(p.getX());
		yo = (int)(p.getY());
		for (int u=0; u<n; u++) {
			Vertex t = (Vertex)(v.elementAt(u));
			x1[u] = (int)(t.getX());
			y1[u] = (int)(t.getY());
		}
		return new DrawVertexVisil(xo, yo, n, x1, y1, xm, ym, ed, ve, ycoord);
	}
	
	public int getIncludingTriangle(java.awt.Point p) {
		Vertex a  =  new Vertex(p, 0, false);
		HalfEdge e = localizeVertex(a);
		if (e!=null) {
			triangleEdge = e;
			return e.getTriangle().getLabel();
		}
		return 0;
//		System.out.println("Triangle: "+localizeVertex(a));
	}
	
	public void getIncludingTriangle(int i) {
		triangleEdge = ((Triangle)(triangles.elementAt(i))).getEdge();
	}
	
	public javax.swing.JComponent getCompTriangle(int xm, int ym, java.awt.Color tr, java.awt.Color ci, int ycoord) {
		Triangle t = triangleEdge.getTriangle();
//		t.calcSmallestAngle();
		int[] x, y;
		x = new int[3];
		y = new int[3];
		double[] tx, ty;
		tx = t.getXs();
		ty = t.getYs();
		for (int u=0; u<3; u++) {
			x[u] = (int)tx[u];
			y[u] = (int)ty[u];
		}
		return new DrawTriangle(x, y, (int)(t.getXc()), (int)(t.getYc()), (int)(t.getRadius()), xm, ym, tr, ci, ycoord);
	}
	
	public boolean insertVertex(java.awt.Point p) {
		Vertex a  =  new Vertex(p, vertices.size(), false);
		return insertVertex(a);
	}
	
	public void insertCircumCenter() {
		Triangle t = triangleEdge.getTriangle();
		insertCircumCenter(t);
	}

	public Vertex insertCircumCenter(Triangle t) {
		Vertex a = new Vertex(t.getXc(), t.getYc(), vertices.size(), false);
//		System.out.println(a);
		insertVertex(a);
		return a;
	}

	
	
	public void splitEdge(HalfEdge e) {
//		System.out.println("edge to be splittted : "+e);
		Vertex a = e.getOrig();
		double x1 = a.getX();
		double y1 = a.getY();
		int alabel = a.getLabel();
		Vertex b = e.getTwin().getOrig();
		double x2 = b.getX();
		double y2 = b.getY();
		int blabel = b.getLabel();
		double xs = (x1+x2)/2;
		double ys = (y1+y2)/2;
		if (alabel<initialVertexNumber) {
			if (blabel>=initialVertexNumber) {
				double d = e.getLength();
				int k = (int)(Math.log(d/2/D)/Math.log(2));
				xs = x1 + (x2-x1)*D*Math.exp(k*Math.log(2))/d;
				ys = y1 + (y2-y1)*D*Math.exp(k*Math.log(2))/d;
//				System.out.println(e);
//				System.out.println("origin initial");
//				System.out.println("k: "+k);
//				System.out.println("xs: "+xs+" ys: "+ys);
			}
		}
		else if (blabel<initialVertexNumber) {
				double d = e.getLength();
				int k = (int)(Math.log(d/2/D)/Math.log(2));
				xs = x2 - (x2-x1)*D*Math.exp(k*Math.log(2))/d;
				ys = y2 - (y2-y1)*D*Math.exp(k*Math.log(2))/d;
//				System.out.println(e);
//				System.out.println("destination initial");
//				System.out.println("k: "+k);
//				System.out.println("xs: "+xs+" ys: "+ys);
		}

		Vertex v = new Vertex(xs, ys, vertices.size(), true);
//		System.out.println("New vertex: "+v);
		vertices.add(v);
				HalfEdge d = makeEdge(v, e.getPrev().getOrig());
				HalfEdge eh = makeEdge(v, e.getTwin().getOrig());
				v.setHalfEdge(eh);
				g.addVertex(v);
				g.addEdge(d);
				g.addEdge(eh);

				HalfEdge en = e.getNext();
				HalfEdge ep = e.getPrev();
				e.getTwin().setOrigin(v);
				g.removeEdge(e);
				g.addEdge(e);
				a.setHalfEdge(eh);
				splice(e.getTwin(), en);
				
				splice(d.getTwin(), ep);
				splice(e.getTwin(), eh);
				splice(d, eh);
				splice(eh.getTwin(), en);

				Triangle t = e.getTriangle();
//				System.out.println("triagnle to be modified: "+t+" "+t.getSmallestAngle());
				t.calcTriangle(e);
				ep.setTriangle(t);
				d.setTriangle(t);
				if (t.getSmallestAngle()<angle) smallTriangles.remove(t);
//				listSmallTriangles();

				t = new Triangle(eh, triangles.size()+1);
				eh.setTriangle(t);
//				System.out.println("edge: "+eh+" new triangle: "+eh.getTriangle());
				d.getTwin().setTriangle(t);
				en.setTriangle(t);
				triangles.add(t);
				eh.getTwin().setTriangle(e.getTwin().getTriangle());
//				System.out.println("edge: "+eh.getTwin()+" new triangle: "+eh.getTwin().getTriangle());
//				System.out.println("e: "+e+" prev: "+e.getPrev()+" next: "+e.getNext());
//				System.out.println("en: "+en+" prev: "+en.getPrev()+" next: "+en.getNext());
//				System.out.println("eh: "+eh+" prev: "+eh.getPrev()+" next: "+eh.getNext());
//				System.out.println("d: "+d+" prev: "+d.getPrev()+" next: "+d.getNext());
//				System.out.println("dt: "+d.getTwin()+" prev: "+d.getTwin().getPrev()+" next: "+d.getTwin().getNext());
//				System.out.println("et: "+e.getTwin()+" prev: "+e.getTwin().getPrev()+" next: "+e.getTwin().getNext());
//				System.out.println("eht: "+eh.getTwin()+" prev: "+eh.getTwin().getPrev()+" next: "+eh.getTwin().getNext());
//				System.out.println("e: "+e.getTriangle());
//				System.out.println("eh: "+eh.getTriangle());
//				
//				System.out.println("ehtTriangle: "+eh.getTwin()+" "+eh.getTwin().getTriangle().getLabel());
//				
//				System.out.println();

				legalize(v, e.getPrev().getTwin());
				legalize(v, eh.getNext().getTwin());
//		System.out.println("be van rakva");
//		listSmallTriangles();
		updateTriangles(v);
		updateEncroach(v);
//		System.out.println();
//		java.util.Collections.sort(smallTriangles, new CompareTriangles());
//		listSmallTriangles();
	}
	
	private int updateEncroach(Vertex v) {
		HalfEdge ed = v.getEdge();
//		System.out.println("vertex: "+v+" edge: "+ed);
		int i=0;
		do {
			Triangle t = ed.getTriangle();
			if (t.getLabel()>0) {
				HalfEdge e = ed;
				do {
					if (e.getTwin().getTriangle().getLabel()<0)
						if (e.oppositeAngle()>90) {
							encroachedEdges.push(e);
							i++;
						}
					e = e.getNext();
				} while(e!=ed.getPrev());
			}
			else {
				if (ed.getTwin().oppositeAngle()>90) {
					encroachedEdges.push(ed.getTwin());
					i++;
				}
			}
			ed = ed.originNext();
		} while (ed!=v.getEdge());
		return i;
	}
	
	private void updateTriangles(Vertex v) {
		HalfEdge ed = v.getEdge();
		do {
			Triangle t = ed.getTriangle();
			if (t.getLabel()>0) {
				if (t.calcSmallestAngle()<angle)  {
					smallTriangles.add(t);
//					System.out.println("Berakva: "+t+"  "+t.getSmallestAngle());
				}
			}
			ed = ed.originNext();
		} while (ed!=v.getEdge());
	
	}

	public void splitEdge(int i) {
		HalfEdge e = (HalfEdge)(halfedges.elementAt(i));
		splitEdge(e);
	}
	
	private boolean insertVertex(Vertex a) {
		HalfEdge e = localizeVertex(a);
//		System.out.println("vertex to be inserted: "+a);
//		System.out.println("Vertex: "+a+" localized edge: "+e+" its triangle: "+ e.getTriangle());
		if (e.getTriangle().getLabel()>0) {
//			if (a.onEdge(e)&&(e.getTwin().getTriangle().getLabel()<0)) {
//			}
//			else {
				vertices.add(a);
				Triangle t = e.getTriangle();
				if (t.getSmallestAngle()<angle) smallTriangles.remove(t);
				triangles.remove(t);
				HalfEdge en = e.getNext();
				HalfEdge ep = e.getPrev();
//				System.out.println("e: "+e+" prev: "+e.getPrev()+" next: "+e.getNext());
//				System.out.println("en: "+en+" prev: "+en.getPrev()+" next: "+en.getNext());
//				System.out.println("e: "+ep+" prev: "+ep.getPrev()+" next: "+ep.getNext());
//				System.out.println();
				HalfEdge d = makeEdge(a, e.getOrig());
				HalfEdge dn = makeEdge(a, en.getOrig());
				HalfEdge dp = makeEdge(a, ep.getOrig());
				a.setHalfEdge(d);
//				System.out.println("Vertex: "+a+" and setted edge: "+a.getEdge());
				g.addVertex(a);
				g.addEdge(d);
				g.addEdge(dn);
				g.addEdge(dp);	

				splice(e, d.getPrev());
				splice(en, dn.getPrev());
				splice(ep, dp.getPrev());
				
				splice(d, dn);
				splice(dn, dp);
				
				t = new Triangle(d, triangles.size()+1);
				triangles.add(t);
				HalfEdge temp = d;
				do {
					temp.setTriangle(t);
					temp = temp.getNext();
				} while (temp!=d);
				t = new Triangle(dn, triangles.size()+1);
				triangles.add(t);
				temp = dn;
				do {
					temp.setTriangle(t);
					temp = temp.getNext();
				} while (temp!=dn);
				t = new Triangle(dp, triangles.size()+1);
				triangles.add(t);
				temp = dp;
					do {
					temp.setTriangle(t);
					temp = temp.getNext();
				} while (temp!=dp);
				legalize(a, e.getTwin());
				legalize(a, en.getTwin());
				legalize(a, ep.getTwin());
//		updateTriangles(a);
//		updateEncroach(a);
			
//				System.out.println("e: "+e+" prev: "+e.getPrev()+" next: "+e.getNext());
//				System.out.println("en: "+en+" prev: "+en.getPrev()+" next: "+en.getNext());
//				System.out.println("ep: "+ep+" prev: "+ep.getPrev()+" next: "+ep.getNext());
//				System.out.println();
//				System.out.println("d: "+d+" prev: "+d.getPrev()+" next: "+d.getNext());
//				System.out.println("dn: "+dn+" prev: "+dn.getPrev()+" next: "+dn.getNext());
//				System.out.println("dp: "+dp+" prev: "+dp.getPrev()+" next: "+dp.getNext());
//				System.out.println("Triangles: "+(triangles.size()+1));
//				System.out.println("Edges: "+halfedges.size());
//			}
//			return true;
		} 
		return false;
	}
	
	private void legalize(Vertex a, HalfEdge e) {
		Triangle t = e.getTriangle();
		double xc = t.getXc();
		double yc = t.getYc();
		double radius = t.getRadius();
		double x = a.getX();
		double y = a.getY();
		if ((radius-Math.sqrt((x-xc)*(x-xc)+(y-yc)*(y-yc)))>DcelGrid.EPS) {
			if (e.getTriangle().getLabel()>0) swap(e);
			legalize(a, e.getNext().getTwin());
			legalize(a, e.getTwin().getPrev().getTwin());
		}
	}
	
	private void swap(HalfEdge e) {
		g.removeEdge(e);
		HalfEdge a = e.getNext();
		HalfEdge b = e.getTwin().getNext();
		splice(e.getTwin(), a);
		splice(e, b);
		e.getTwin().setOrigin(a.getTwin().getOrig());
		a.getTwin().getOrig().setHalfEdge(e.getTwin());
		e.setOrigin(b.getTwin().getOrig());
		b.getTwin().getOrig().setHalfEdge(e);
		splice(a.getNext(), e.getTwin());
		splice(b.getNext(), e);
		Triangle t = a.getTriangle();
		if (t.getSmallestAngle()<angle) smallTriangles.remove(t);
		t.calcTriangle(a);
		e.getTwin().setTriangle(t);
		a.getPrev().setTriangle(t);
		t = b.getTriangle();
		if (t.getSmallestAngle()<angle) smallTriangles.remove(t);
		t.calcTriangle(b);
		e.setTriangle(t);
		b.getPrev().setTriangle(t);
		g.addEdge(e);
	}
	
	private void splice(HalfEdge a, HalfEdge b) {
		HalfEdge ap = a.getPrev();
		HalfEdge bp = b.getPrev();
		
		a.setPrev(bp);
		bp.setNext(a);

		b.setPrev(ap);
		ap.setNext(b);
	}
	
	public javax.swing.JComponent getCompDelaunayVertex(int i, int xm, int ym, Color ed, Color ve, int ycoord) {
		HalfEdge e = (HalfEdge)(halfedges.elementAt(i));
		Vertex a = g.getDelaunayVertex(e);
		int xo = (int)(e.getOrig().getX());
		int yo = (int)(e.getOrig().getY());
		int xd = (int)(e.getTwin().getOrig().getX());
		int yd = (int)(e.getTwin().getOrig().getY());
		int x = (int)(a.getX());
		int y = (int)(a.getY());
		return new DrawEdgeDelaunayVertex(xo,yo, xd, yd, x, y, xm, ym, ed, ve, ycoord);
	}

	public javax.swing.JComponent getCompStatTriangle(int xm, int ym, java.awt.Color low, java.awt.Color high, int ycoord) {
		int n = triangles.size();
		int[][] x = new int[n][3];
		int[][] y = new int[n][3];
		double[] coefColor = new double[n];
		
		for (int i=0; i<n; i++) {
			HalfEdge e = ((Triangle)triangles.elementAt(i)).getEdge();
			Vertex a = e.getOrig();
			x[i][0] = (int)a.getX();
			y[i][0] = (int)a.getY();
			e = e.getNext();
			a = e.getOrig();
			x[i][1] = (int)a.getX(); 
			y[i][1] = (int)a.getY();
			e = e.getNext();
			a = e.getOrig();
			x[i][2] = (int)a.getX();
			y[i][2] = (int)a.getY();
			coefColor[i] = ((Triangle)triangles.elementAt(i)).calcRealSmallestAngle()/60.0;
		}
		return new DrawStatTriangle(xm, ym, x, y, coefColor, n, low, high, ycoord);
	}
	
	private	void extractContour(java.util.Vector c, boolean inner) {
		int edgesn = c.size();
		double temp;
		
		// first two vertices
		Vertex v1 = new Vertex((java.awt.Point)(c.elementAt(0)), vertices.size(), true);
		vertices.add(v1);
		if (!inner) {
			g.setMaxX(v1.getX());
			g.setMinX(v1.getX());
			g.setMaxY(v1.getY());
			g.setMinY(v1.getY());
		}

		Vertex v2 = new Vertex((java.awt.Point)(c.elementAt(1)), vertices.size(), true);
		vertices.add(v2);
		if (!inner) {
			temp=v2.getX();
			if (temp>g.getMaxX()) g.setMaxX(temp);
			else if (temp<g.getMinX()) g.setMinX(temp);
			temp=v2.getY();
			if (temp>g.getMaxY()) g.setMaxY(temp);
			else if (temp<g.getMinY()) g.setMinY(temp);
		}	
//		System.out.println(v1.getX()+", "+v1.getY()+"  -  "+v2.getX()+", "+v2.getY());
		
		// first edge
		HalfEdge prev, first, now;
		now = first = prev = MakeEdge(v1, v2);
		halfedges.add(first);
//		halfedges.add(first.getTwin());

		// first edge's face settings
		Triangle out;
		if (inner) {
			out = new Triangle(0.0, 0.0, 0.0, first.getTwin(), -1*outers.size()-1);
			outers.add(out);
		}
		else {
			out = new Triangle(0.0, 0.0, 0.0, first.getTwin(), -1);
			outers.add(out);
		}
		first.setTriangle(null);
		first.getTwin().setTriangle(out);

		// rest of the edges
		for (int i=2; i<edgesn; i++) {
			v1 = v2;
			v2 = new Vertex((java.awt.Point)(c.elementAt(i)), vertices.size(), true);
			if (!inner) {
				temp=v2.getX();
				if (temp>g.getMaxX()) g.setMaxX(temp);
				else if (temp<g.getMinX()) g.setMinX(temp);
				temp=v2.getY();
				if (temp>g.getMaxY()) g.setMaxY(temp);
				else if (temp<g.getMinY()) g.setMinY(temp);
			}
			vertices.add(v2);
			now = MakeEdge(v1, v2);
			halfedges.add(now);
//			halfedges.add(now.getTwin());
			now.getTwin().setTriangle(out);
			Connect(prev, now);
			prev = now;
//		System.out.println(v1.getX()+", "+v1.getY()+"  -  "+v2.getX()+", "+v2.getY());
		}
		
		// last edge
		v1 = v2;
		prev = now;
		v2 = first.getOrig();
		now = MakeEdge(v1, v2);
		halfedges.add(now);
//		halfedges.add(now.getTwin());

		first.setPrev(now);
		now.setNext(first);
		now.setPrev(prev);
		prev.setNext(now);

		first.getTwin().setNext(now.getTwin());		
		now.getTwin().setPrev(first.getTwin());
		
		prev.getTwin().setPrev(now.getTwin());
		now.getTwin().setNext(prev.getTwin());
		
//		Connect(prev, now);
//		Connect(now, first);
		v2.setHalfEdge(first);
		now.getTwin().setTriangle(out);
//		System.out.println(v1.getX()+", "+v1.getY()+"  -  "+v2.getX()+", "+v2.getY());
//		System.out.println();
	}

	private HalfEdge FindEdge(HalfEdge h) {
		HalfEdge t = h.getNext();
		HalfEdge max = h;
		double fmax = functional(max);
//		System.out.println(t+"   func: "+functional(t));
		while (t!=h)  {
			double tfun = functional(t);
			if (tfun>fmax)  {
				max = t;
				fmax = tfun;
			}
//			System.out.println(t+"   func: "+functional(t));
			t = t.getNext();
		}
		double tfun = functional(t);
		if (tfun>fmax)  {
			max = t;
			fmax = tfun;
		}
//			System.out.println(t+"   func: "+functional(t));
//		System.out.println("max: "+max+"   func: "+functional(max));
		return max;
	}
	
	private double functional(HalfEdge h) {
		Vertex a = h.getOrig();
		Vertex b = h.getTwin().getOrig();
		Vertex c = h.getNext().getTwin().getOrig();
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(c);

//		System.out.println(h);
		
		double s1 = a.triArea(a,b,c)/h.getLength()/h.getNext().getLength();
		double c1 = h.getTwin().scalarProduct(h.getNext())/h.getLength()/h.getNext().getLength();
//		System.out.println("sin v1: "+ s1+"   cos v1: "+ c1);

		double v1 = 0;
		if ((c1>0)&&(s1>0)) { v1 = (Math.asin(s1))*180/Math.PI; }
		else if (c1<0) { v1 = (Math.PI-Math.asin(s1))*180/Math.PI; }
			 else { v1 = (2*Math.PI - Math.acos(c1))*180/Math.PI; }
//		System.out.println("v1: "+v1);
		
		c = h.getPrev().getOrig();
		double s2 = c.triArea(c,a,b)/h.getLength()/h.getPrev().getLength();
		double c2 = h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength();
//		System.out.println("sin s2: "+ s2+"   cos v2: "+ c2);
		
		double v2 = 0;
		if ((c2>0)&&(s2>0)) { v2 = Math.asin(s2)*180/Math.PI; }
		else if (c2<0) { v2 = (Math.PI-Math.asin(s2))*180/Math.PI; }
			 else {	v2 = (2*Math.PI - Math.acos(c2))*180/Math.PI; }
//		System.out.println("v2: "+v2);

//		System.out.println("Func: "+(v1+v2));
//		System.out.println();
		return v1+v2;
	}

	public Drawable getCompDomainGrid(int xm, int ym, java.awt.Color c1, java.awt.Color c2, java.awt.Color c3,
	             boolean edge, boolean vertex, boolean gridn, boolean grid, int ycoord) {
		int n = halfedges.size();
		int x1[] = new int[n];
		int y1[] = new int[n];
		int x2[] = new int[n];
		int y2[] = new int[n];
		boolean boundary[] = new boolean[n];
//		System.out.println("get new");
//		System.out.println("number of real edges: "+n);
		for (int i=0; i<n; i++) {
			HalfEdge e = (HalfEdge)(halfedges.elementAt(i));
			x1[i] = (int)(e.getOrig().getX());
			y1[i] = (int)(e.getOrig().getY());
			e = e.getTwin();
			x2[i] = (int)(e.getOrig().getX());
			y2[i] = (int)(e.getOrig().getY());
			if (e.getTriangle().getLabel()<0) boundary[i] = true;
			else boundary[i] = false;
//			System.out.println(x1[i]+","+y1[i]+"  -  "+x1[i]+","+y1[i]);
		}
//		System.out.println("vertices number: "+ vertices.size());
//		System.out.println("halfedges number: "+ halfedges.size());
//		System.out.println("faces number: "+ faces.size());
		return new Drawable(x1, y1, x2, y2, boundary, k, l, g.getMaxX(), g.getMaxY(), g.getMinX(), g.getMinY(),
		                    n, initialVertexNumber, xm, ym, c1, c2, c3, edge, vertex, gridn, grid, ycoord);
	}
	
	public javax.swing.JComponent getCompVoxel(int i, int j, int xm, int ym,
			Color ve, Color ed, Color gr, int ycoord) {
		int nv, ne;
		java.util.Vector vert;
		java.util.Vector edge;
		vert = g.getVertices(i,j);
		edge = g.getEdges(i,j);
		nv = vert.size();
		ne = edge.size();

		int x1[] = new int[ne];
		int y1[] = new int[ne];
		int x2[] = new int[ne];
		int y2[] = new int[ne];
		int xv[] = new int[nv];
		int yv[] = new int[nv];
		
		for (int u=0; u<ne; u++) {
			HalfEdge e = (HalfEdge)(edge.elementAt(u));
			x1[u] = (int)(e.getOrig().getX());
			y1[u] = (int)(e.getOrig().getY());
			e = e.getTwin();
			x2[u] = (int)(e.getOrig().getX());
			y2[u] = (int)(e.getOrig().getY());
		}
		for (int u=0; u<nv; u++) {
			Vertex v = (Vertex)(vert.elementAt(u));
			xv[u] = (int)(v.getX());
			yv[u] = (int)(v.getY());
		}
		
		return new DrawVoxel(xv, yv, x1, y1, x2, y2, i, j, k, l, xm, ym, nv, ne, 
			 g.getMaxX(), g.getMaxY(), g.getMinX(), g.getMinY(), ve, ed, gr, ycoord);
	}

	
	private HalfEdge MakeEdge(Vertex orig, Vertex dest) {
		HalfEdge d = new HalfEdge(orig);
		HalfEdge b = new HalfEdge(dest, d);
		return d;
	}
	
	private void Connect(HalfEdge one, HalfEdge two) {
		HalfEdge onetwin = one.getTwin();
		HalfEdge twotwin = two.getTwin();
		
		one.setNext(two);
		two.setPrev(one);
	
		twotwin.setNext(onetwin);
		onetwin.setPrev(twotwin);
	}
}

class Vertex {
	private double x;
	private double y;
	private int label;
	private HalfEdge e;
	private int i, j;
	private boolean onBoundary;
	public DoubleData xi,aii,bi;
	
	public void createDoubleData()  throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		if (onBoundary)  {
			if (ecuation.sourceFlag) bi = new DoubleData(ecuation.source.valueIn(x,y));
			else bi = new DoubleData(ecuation.BoundaryFunction.evaluateIn(x,y));
		}
		else bi = new DoubleData();
		aii = new DoubleData();
		xi = new DoubleData();
	}
	
	public Vertex(java.awt.Point p, int label, boolean ob) {
		this.x = p.getX();
		this.y = p.getY();
		this.label = label;
		e = null;
		onBoundary = ob;
	}

	public Vertex(double x, double y, int label, boolean ob) {
		this.x = x;
		this.y = y;
		this.label = label;
		e = null;
		onBoundary = ob;
	}
	
	public boolean onBoundary() {
		return onBoundary;
	}
	
	public int getLabel() {
		return label;
	}
	public boolean equals(Vertex a) {
		if ((Math.abs(a.getX()-x)<DcelGrid.EPS)&&(Math.abs(a.getY()-y)<DcelGrid.EPS))
			return true;
		else return false;	
	}
	
	public boolean onEdge(HalfEdge e) {
		Vertex a;
		double x1, y1, x2, y2;
		a = e.getOrig();
		x1 = a.getX();
		y1 = a.getY();
		a = e.getTwin().getOrig();
		x2 = a.getX();
		y2 = a.getY();
		if (Math.abs((x-x1)*(y2-y1) - (y-y1)*(x2-x1))<DcelGrid.EPS)
			return true;
		return false;	
		
	}
	
	public double triArea(Vertex a, Vertex b, Vertex c) {
		double ax = a.getX();
		double ay = a.getY();
		double bx = b.getX();
		double by = b.getY();
		double cx = c.getX();
		double cy = c.getY();
		
		return (bx-ax)*(cy-ay) - (by-ay)*(cx-ax);
	}
	
	public boolean ccw(Vertex a, Vertex b, Vertex c) {
		return triArea(a,b,c) > 0;
	}
	
	public boolean rightOf(HalfEdge e) {
		return triArea(this, e.getOrig(), e.getTwin().getOrig())<=0;
	}

	public boolean leftOf(HalfEdge e) {
		return triArea(this, e.getOrig(), e.getTwin().getOrig())>=0;
	}
	
	public void setVoxel(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public HalfEdge getEdge() {
		return e;
	}
	
	public boolean insidePolygon(HalfEdge e) {
		int intersects = 0;
		double xmin, xmax, ymin, ymax;
		double x1, y1, x2, y2;
		Vertex a;
		HalfEdge t = e;
//		System.out.println("Starting edge for the hole: "+t);
//		System.out.println("x: "+x+" y: "+y);
		do {
			a = t.getOrig();
			x1 = a.getX();
			y1 = a.getY();
			a = t.getTwin().getOrig();
			x2 = a.getX();
			y2 = a.getY();
			xmin = Math.min(x1, x2);
			xmax = Math.max(x1, x2);
			ymin = Math.min(y1, y2);
			ymax = Math.max(y1, y2);
//			System.out.println("edge to be tested: "+t);
			if ((ymin<=y)&&(y<=ymax)) {
				if (x<=xmin)  {
								if (Math.abs(y-y1)<DcelGrid.EPS) {
//									System.out.println("Origin intersection "+t);
									HalfEdge temp = t;
									do {
										temp = temp.getPrev();
									} while(temp.horizontal());
									e = temp;
//									System.out.println(e);
									if (((e.getOrig().getY()-y)*(t.getTwin().getOrig().getY()-y))<0) {
										intersects++;
//										System.out.println("origin intersection counted");
									}
								}
								else
								if (Math.abs(y-y2)<DcelGrid.EPS) {
//									System.out.println("Destination intersection "+t);
									HalfEdge temp = t;
									do {
										temp = temp.getNext();
									} while((temp.horizontal())&&(temp!=e));
//									System.out.println(temp);
									if (((temp.getTwin().getOrig().getY()-y)*(t.getOrig().getY()-y))<0) {
										intersects++;
//										System.out.println("Vertex intersection");
									}
									t = temp;
									if (temp==e) t = t.getPrev();
								}
								else {
//									System.out.println("Edge clearly right "+t+"  y: ["+ymin+","+ymax+"] x:["+xmin+","+xmax+"]");
									intersects++;
								}
				}
				else {
					if (x<=xmax) {
						if ((Math.abs(y2-y1)<DcelGrid.EPS)&&(Math.abs(y-y1)<DcelGrid.EPS)) {
//							System.out.println("Edge horizontal and goes trough our y: "+y+"  y: ["+ymin+","+ymax+"] x:["+xmin+","+xmax+"]");
							intersects++;
						}
						else	
							if (x-((y-y1)/(y2-y1)*(x2-x1)+x1)<(DcelGrid.EPS)) {
								if (Math.abs(y-y1)<DcelGrid.EPS) {
									HalfEdge temp = t;
									do {
										temp = temp.getPrev();
									} while(temp.horizontal());
									e = temp;
									if (((e.getTwin().getOrig().getY()-y)*(t.getOrig().getY()-y))<0) {
										intersects++;
//										System.out.println("Origin intersection");
									}
								}
								else
								if (Math.abs(y-y2)<DcelGrid.EPS) {
									HalfEdge temp = t;
									do {
										temp = temp.getNext();
									} while(temp.horizontal());
									t = temp;
									if (((e.getOrig().getY()-y)*(t.getTwin().getOrig().getY()-y))<0) {
										intersects++;
//										System.out.println("Destination intersection");
									}
								}
								else {
									intersects++;
								}
								
							}
					}
				}
			}
			t = t.getNext();
		} while(t!=e);
//		System.out.println("intersects for "+this+": "+intersects);
		if (e.getTriangle().getLabel()==-1) return (intersects%2==0);
		else   return (intersects%2==1) ;
	}
	
	public int getVoxelI() {
		return i;
	}

	public int getVoxelJ() {
		return j;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String toString() {
		return label+"->["+x+","+y+"]";
	}
	
	public void setHalfEdge(HalfEdge e) {
		this.e = e;
	}
	
}

class Triangle extends javax.swing.JComponent {

	public static double min, max;

	private int label;
	private HalfEdge edge;
	private double xc,yc,radius;
	private double smallestAngle;
	private Vertex[] node;
	
	public int norm() {
		double[] S = new double[6];
		S[0] = Math.abs(node[1].getX() - node[0].getX());
		S[1] = Math.abs(node[2].getX() - node[1].getX());
		S[2] = Math.abs(node[0].getX() - node[2].getX());
		S[3] = Math.abs(node[1].getY() - node[0].getY());
		S[4] = Math.abs(node[2].getY() - node[1].getY());
		S[5] = Math.abs(node[0].getY() - node[2].getY());
		double m = S[0];
		for(int i=1; i<6; i++) if (m < S[i]) m = S[i];
		return (int)m+10;
	}
	
	public void paint(java.awt.Graphics g)  {
		if (node!=null)  {
			double i,j; double c1,c2,c3; int x,y;
			int n = norm();
			for(i=0;i<=n;i++) {
				c1=i/n;
				for(j=0;j<=n;j++) {
					c2=j/n;
					c3=1-c1-c2;
					if ((c3<=1)&&(c3>=0)) {
						double Sl = c1 * node[0].xi.value + c2 * node[1].xi.value + c3 * node[2].xi.value;
						double piros = Math.abs(Sl-min)/Math.abs(max-min);
						if ((piros>=0)&&(piros<=1)) {
							if (piros<1/3.0) {
//								piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
								piros=3*piros;
								Color high = Color. blue;
								Color low = Color.black;
								int red = (int)(low.getRed()+(high.getRed() - low.getRed())*piros);
								int green = (int)(low.getGreen()+(high.getGreen() - low.getGreen())*piros);
								int blue = (int)(low.getBlue()+(high.getBlue() - low.getBlue())*piros);
								g.setColor(new Color(red, green, blue));
							}else {
								if (piros<2/3.0) {
									piros = piros - 1/3.0;
//									piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
									piros=3*piros;
									Color high = Color.red;
									Color low = Color.blue;
									int red = (int)(low.getRed()+(high.getRed() - low.getRed())*piros);
									int green = (int)(low.getGreen()+(high.getGreen() - low.getGreen())*piros);
									int blue = (int)(low.getBlue()+(high.getBlue() - low.getBlue())*piros);
									g.setColor(new Color(red, green, blue));
								}else {
									piros = piros - 2/3.0;
//									piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
									piros=3*piros;
									Color high = Color.yellow;
									Color low = Color.red;
									int red = (int)(low.getRed()+(high.getRed() - low.getRed())*piros);
									int green = (int)(low.getGreen()+(high.getGreen() - low.getGreen())*piros);
									int blue = (int)(low.getBlue()+(high.getBlue() - low.getBlue())*piros);
									g.setColor(new Color(red, green, blue));
								}
							}
							x = (int)(c1 * node[0].getX() + c2 * node[1].getX() + c3 * node[2].getX());
							y = (int)(c1 * node[0].getY() + c2 * node[1].getY() + c3 * node[2].getY());
							g.drawLine(x,y,x,y); 		
						}
					}
				}
			}
		}
		else  {
			System.out.println("node null");
		}
	}
	

	public double S(double x, double y) {
		double delta = (node[1].getX()-node[0].getX())*(node[2].getY()-node[0].getY()) - 
		  			   (node[2].getX()-node[0].getX())*(node[1].getY()-node[0].getY());
			
		double c1 = ((node[1].getY()-node[2].getY())*x + 
					 (node[2].getX()-node[1].getX())*y +
					 node[1].getX()*node[2].getY()-node[2].getX()*node[1].getY())/delta;
		double c2 = ((node[2].getY()-node[0].getY())*x + 
					 (node[0].getX()-node[2].getX())*y +
					 node[2].getX()*node[0].getY()-node[0].getX()*node[2].getY())/delta;
		double c3 = 1 - c1 - c2;
	
		return c1 * node[0].xi.value + c2 * node[1].xi.value + c3 * node[2].xi.value;
	}
	

	public void computeStiffnessMatrix() {
		HalfEdge temp = edge;
		node = new Vertex[3];
		int u=0;
		do {
			node[u]=temp.getOrig();
			u++;
			temp = temp.getNext();
		}
		while (temp!=edge);
		

//		double area = Math.abs(node[1].getX()*node[2].getY()+
//							   node[0].getX()*node[1].getY()+
//							   node[2].getX()*node[0].getY()-
//							   node[1].getX()*node[0].getY()-
//							   node[2].getX()*node[1].getY()-
//							   node[0].getX()*node[2].getY())/2;

		double delta = (node[1].getX()-node[0].getX())*(node[2].getY()-node[0].getY()) - 
		  			   (node[2].getX()-node[0].getX())*(node[1].getY()-node[0].getY());
	
		double area = Math.abs(delta)/2;
		
		double[][] stiff = new double[3][3];

		double kx = ecuation.kx;
		double ky = ecuation.ky;
		
		stiff[0][0] = area*(kx*Math.pow(node[1].getY()-node[2].getY(),2.)+ky*Math.pow(node[2].getX()-node[1].getX(),2.))/Math.pow(delta,2.);
		stiff[1][1] = area*(kx*Math.pow(node[2].getY()-node[0].getY(),2.)+ky*Math.pow(node[0].getX()-node[2].getX(),2.))/Math.pow(delta,2.);
		stiff[2][2] = area*(kx*Math.pow(node[0].getY()-node[1].getY(),2.)+ky*Math.pow(node[1].getX()-node[0].getX(),2.))/Math.pow(delta,2.);
		stiff[0][1] = area*(kx*(node[1].getY()-node[2].getY())*(node[2].getY()-node[0].getY()) + ky*(node[2].getX()-node[1].getX())*(node[0].getX()-node[2].getX()))/Math.pow(delta,2.); 
		stiff[0][2] = area*(kx*(node[1].getY()-node[2].getY())*(node[0].getY()-node[1].getY()) + ky*(node[2].getX()-node[1].getX())*(node[1].getX()-node[0].getX()))/Math.pow(delta,2.); 
		stiff[1][2] = area*(kx*(node[2].getY()-node[0].getY())*(node[0].getY()-node[1].getY()) + ky*(node[0].getX()-node[2].getX())*(node[1].getX()-node[0].getX()))/Math.pow(delta,2.); 
		stiff[1][0] = stiff[0][1];
		stiff[2][0] = stiff[0][2];
		stiff[2][1] = stiff[1][2];

//		System.out.println(this);
//
//		System.out.println("["+stiff[0][0]+","+stiff[0][1]+","+stiff[0][2]+"]");
//		System.out.println("["+stiff[1][1]+","+stiff[1][2]+"]");
//		System.out.println("["+stiff[2][2]+"]");

		int i,j,k;
		for(i=0; i<3; i++) node[i].aii.value += stiff[i][i];

		do {
			Vertex a = temp.getOrig();
			Vertex b = temp.getTwin().getOrig();
			for(i=0; i<3; i++) if (a.equals(node[i])) break;
			for(j=0; j<3; j++) if (b.equals(node[j])) break;
			temp.aij.value += stiff[i][j];
			temp = temp.getNext();
		}
		while (temp!=edge);
	}
	
	public Triangle(double xc, double yc, double radius, HalfEdge edge, int label) {
//		System.out.println("xc: "+xc+" yc: "+yc+" radius: "+radius+" edge: "+edge);
		this.xc = xc;
		this.yc = yc;
		this.radius = radius;
		this.edge = edge;
		this.label = label;
	}
	
	public Triangle(HalfEdge e, int label) {
		this.label = label;
		calcTriangle(e);
	}
	
	public void calcTriangle(HalfEdge e) {
		Vertex a = e.getOrig();
		double x1 = a.getX();
		double y1 = a.getY();
		Vertex b = e.getPrev().getOrig();
//		System.out.println(b);
		double x2 = b.getX();
		double y2 = b.getY();
		Vertex c = e.getNext().getOrig();
//		System.out.println(c);
		double x3 = c.getX();
		double y3 = c.getY();
		
		double b1 = x1*x1 + y1*y1;
		double b2 = x2*x2 + y2*y2;
		double b3 = x3*x3 + y3*y3;

		double det = (x2-x1)*(y3-y1) - (y2-y1)*(x3-x1);
		double A = ((b2-b1)*(y3-y1) - (y2-y1)*(b3-b1))/det;
		double B = ((x2-x1)*(b3-b1) - (b2-b1)*(x3-x1))/det;
		double C = (x1*y2*b3 + x2*y3*b1 + x3*y1*b2 - x3*y2*b1 - x2*y1*b3 - x1*y3*b2)/det;
		
		xc = A/2;
		yc = B/2;
		radius = Math.sqrt(C+xc*xc+yc*yc);
		
		edge = e;
	}
	
	public double getSmallestAngle() {
		return smallestAngle;
	}
	
	public double calcSmallestAngle() {
		double c1,c2,c3;
		HalfEdge h = edge;
		c1 = 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
		try  {
		if ((h.getPrev().getTwin().getTriangle().getLabel()<0)&&(h.getTwin().getTriangle().getLabel()<0)) {
//			System.out.println("1: "+h+" 2: "+h.getPrev());
			c1 = 180;
		}
		} catch (Exception e)  { }
		h = h.getNext();
		c2 = 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
		try  {
		if ((h.getPrev().getTwin().getTriangle().getLabel()<0)&&(h.getTwin().getTriangle().getLabel()<0)) {
//			System.out.println("1: "+h+" 2: "+h.getPrev());
			c2 = 180;
		}
		} catch (Exception e)  { }
		h = h.getNext();
		c3 = 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
		try  {
		if ((h.getPrev().getTwin().getTriangle().getLabel()<0)&&(h.getTwin().getTriangle().getLabel()<0)) {
//			System.out.println("1: "+h+" 2: "+h.getPrev());
			c3 = 180;
		}
		} catch (Exception e)  { }
		smallestAngle = Math.min(c1, Math.min(c2, c3));
		return smallestAngle;
//		System.out.println("c1: "+c1+" c2: "+c2+" c3: "+c3);
//		System.out.println("smallestAngle: "+smallestAngle);
	}

	public double calcRealSmallestAngle() {
		double c1,c2,c3;
		HalfEdge h = edge;
		c1 = 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
		h = h.getNext();
		c2 = 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
		h = h.getNext();
		c3 = 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
		smallestAngle = Math.min(c1, Math.min(c2, c3));
		return Math.min(c1, Math.min(c2, c3));
	}
	
	public String toString() {
		return "T"+label+" "+edge.getOrig()+" "+edge.getTwin().getOrig()+" "+edge.getPrev().getOrig();
	}
	
	public int getLabel() {
		return label;
	}
	
	public double getXc() {
		return xc;
	}

	public double getYc() {
		return yc;
	}

	public double getRadius() {
		return radius;
	}
	
	public double[] getXs() {
		double[] temp = new double[3];
		temp[0] = edge.getOrig().getX();
		temp[1] = edge.getTwin().getOrig().getX();
		temp[2] = edge.getPrev().getOrig().getX();
		return temp;
	}
	
	public HalfEdge getEdge() {
		return edge;
	}

	public double[] getYs() {
		double[] temp = new double[3];
		temp[0] = edge.getOrig().getY();
		temp[1] = edge.getTwin().getOrig().getY();
		temp[2] = edge.getPrev().getOrig().getY();
		return temp;
	}
	
}

class HalfEdge {
	private Vertex origin;
	private HalfEdge twin;
	private Triangle triangle;
	private HalfEdge next;
	private HalfEdge prev;
	public DoubleData aij;
	
	public HalfEdge(Vertex origin) {
		this.origin = origin;
		origin.setHalfEdge(this);
	}
	
	public void createDoubleData() {
		aij = new DoubleData();
		this.getTwin().aij = this.aij;
	}
	
	public HalfEdge() {
	}
	
	public void setOrigin(Vertex a) {
		origin = a;
	}
	
	public HalfEdge originNext() {
		return this.prev.getTwin();
	}
	
	public boolean horizontal() {
		if (Math.abs(origin.getY()-twin.getOrig().getY())<DcelGrid.EPS) return true;
		return false;
	} 
	
	public double oppositeAngle() {
		HalfEdge h = this.getPrev();
		return 180/Math.PI*Math.acos(h.scalarProduct(h.getPrev().getTwin())/h.getLength()/h.getPrev().getLength());
	}

	public double getLength() {
		double x2 = (origin.getX() - twin.getOrig().getX()); x2 = x2*x2;
		double y2 = (origin.getY() - twin.getOrig().getY()); y2 = y2*y2;
		return Math.sqrt(x2+y2);
	}
	
	public double scalarProduct(HalfEdge e) {
		double x = twin.getOrig().getX()- origin.getX();
		double y = twin.getOrig().getY()- origin.getY();
		double xe = e.getTwin().getOrig().getX() - e.getOrig().getX();
		double ye = e.getTwin().getOrig().getY() - e.getOrig().getY();
		return x*xe+y*ye;
	}
	
	public HalfEdge(Vertex origin, HalfEdge twin) {
		this.origin = origin;
		origin.setHalfEdge(this);
		this.twin = twin;
		twin.setTwin(this);
	}
	
	public void setNext(HalfEdge e) {
		next = e;
	}

	public void setPrev(HalfEdge e) {
		prev = e;
	}
	
	public void setTwin(HalfEdge e) {
		twin = e;
	}
	
	public void setTriangle(Triangle t) {
		triangle = t;
	}

	public Triangle getTriangle() {
		return triangle;
	}
	
	public Vertex getOrig() {
			return origin;
	}

	public HalfEdge getTwin() {
		return twin;
	}
	
	public HalfEdge getNext() {
		return next;
	}

	public HalfEdge getPrev() {
		return prev;
	}
	
	public String toString() {
		return origin+" - "+twin.getOrig();
	}
}

class Grid {
	private Voxel m[][];
	private double maxX, maxY, minX, minY;
	private int k, l;
	public static double xc;
	public static double yc;
	public static double radius;
	

class Voxel {
	private java.util.Vector e;
	private java.util.Vector v;
	
	public Voxel() {
			e = new java.util.Vector();
			v = new java.util.Vector();
		}
		
		public void addVertex(Vertex p) {
			try {
			v.add(p);
//			System.out.println("added: "+p);
			}
			catch (Exception f) {
				v = new java.util.Vector();
				v.add(p);
//				System.out.println("added: "+p);
			}
		}
		public void removeVertex(Vertex p) {
				v.remove(p);
		}
			
		public void addEdge(HalfEdge he) {
			try {
			e.add(he);
			}
			catch (Exception f) {
				e = new java.util.Vector();
				e.add(he);
			}
		}

		public void removeEdge(HalfEdge he) {
			if (!e.remove(he)) e.remove(he.getTwin());
		}
			
		public java.util.Vector getVertices() {
			return v;
		}
	
		public java.util.Vector getEdges() {
			return e;
		}
	}

	public Grid(int i, int j) {
		k = i;
		l = j;
//		this.maxX = maxX;
//		this.minX = minX;
//		this.maxY = maxY;
//		this.minY = minY;
		m = new Voxel[i][j];
		for (int k=0; k<i; k++) 
			for(int l=0; l<j; l++)
				m[k][l]=new Voxel();
		
	}
	
	public int getI(int x) {
		double w = (maxX-minX)/k;
		double i = (x-minX)/w;
		if ((i<0)||(i>=k)) return -1;
		else return (int)i;
	}
	
	public int getJ(int y) {
		double h = (maxY-minY)/l;
		double j = (y-minY)/h;
		if ((j<0)||(j>=l)) return -1;
		else return (int)j;
	}
	
	public HalfEdge getCloseEdge(Vertex a) {
		double x = a.getX();
		double y = a.getY();
		double w = (maxX-minX)/k;
		double h = (maxY-minY)/l;
		int i = (int)((x-minX)/w);
		if ((i<0)||(i>=k)) return null;
		int j = (int)((y-minY)/h);
		if ((j<0)||(j>=l)) return null;
		
		if (m[i][j].getEdges().size()>0) return (HalfEdge)(m[i][j].getEdges().elementAt(m[i][j].getEdges().size()-1));
		
		int imin, imax, jmin, jmax;
		imin = imax = i;
		jmin = jmax = j;		
		
		while (true) {
			if (imin>0) {
				imin--;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=jmin; u<=jmax; u++) {
					if  (m[imin][u].getEdges().size()>0)
						return (HalfEdge)(m[imin][u].getEdges().elementAt(0));
				}
			}
			if (jmin>0) {
				jmin--;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=imin; u<=imax; u++) {
					if  (m[u][jmin].getEdges().size()>0)
						return (HalfEdge)(m[u][jmin].getEdges().elementAt(0));
				}
			}
			if (imax<k-1) {
				imax++;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=jmin; u<=jmax; u++) {
					if  (m[imax][u].getEdges().size()>0)
						return (HalfEdge)(m[imax][u].getEdges().elementAt(0));
				}
			}
			if (jmax<l-1) {
				jmax++;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=imin; u<=imax; u++) {
					if  (m[u][jmax].getEdges().size()>0)
						return (HalfEdge)(m[u][jmax].getEdges().elementAt(0));
				}
			}
		
		}
	}
	
	public Vertex getDelaunayVertex(HalfEdge e) {
		Vertex a = e.getOrig();
		double xo = a.getX();
		double yo = a.getY();
		Vertex b = e.getTwin().getOrig();
		double xd = b.getX();
		double yd = b.getY();
//		System.out.println(e);
		double wi = (maxX-minX)/k;
		double h = (maxY-minY)/l;
//		System.out.println("w: "+wi+" h: "+h);
		
		int imin = (int)Math.min((xo-minX)/wi, (xd-minX)/wi);
		int imax = (int)Math.max((xo-minX)/wi, (xd-minX)/wi);
		int jmin = (int)Math.min((yo-minY)/h, (yd-minY)/h);
		int jmax = (int)Math.max((yo-minY)/h, (yd-minY)/h);
//		System.out.println("yo: "+yo+" yd: "+yd+" xo: "+xo+" wd: "+xd);
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
//		System.out.println("yo/h: "+yo/h+" yd/h: "+yd/h+" xo/wi: "+xo/wi+" wd/wi: "+xd/wi);

		java.util.Vector vert;
		for (int u=imin; u<=imax; u++) 
			for (int w=jmin; w<=jmax; w++) {
				vert = m[u][w].getVertices();
//				System.out.println("u: "+u+" w: "+w+" vertices: "+vert);
				for (int q=0; q<vert.size(); q++) {
					Vertex t = (Vertex)(vert.elementAt(q));
					if ((t!=a)&&(t!=b)) {
//						System.out.println("testing vertex "+t+" for edge "+e);
					if (isVisible(t,e,false)) {
//						System.out.println("VISIBLE VERTEX: "+t+" for edge "+e);
						if (emptyCircumcircle(t,e)) return t;
					}
					}	
				}
//				System.out.println();
			}
		boolean tt = false;
		while (!tt) {
			tt = true;
			if (imin>0) {
				tt = false;
				imin--;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=jmin; u<=jmax; u++) {
					vert = m[imin][u].getVertices();
//					System.out.println("u: "+imin+" w: "+u+" vertices: "+vert);
					for (int q=0; q<vert.size(); q++) {
						Vertex t = (Vertex)(vert.elementAt(q));
						if ((t!=a)&&(t!=b)) {
//							System.out.println("testing vertex "+t+" for edge "+e);
						if (isVisible(t,e,false))
							if (emptyCircumcircle(t,e)) return t;
						}	
					}
//					System.out.println();
				}
			}
			if (jmin>0) {
				tt = false;
				jmin--;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=imin; u<=imax; u++) {
					vert = m[u][jmin].getVertices();
//					System.out.println("u: "+u+" w: "+jmin+" vertices: "+vert);
					for (int q=0; q<vert.size(); q++) {
						Vertex t = (Vertex)(vert.elementAt(q));
						if ((t!=a)&&(t!=b)) {
//							System.out.println("testing vertex "+t+" for edge "+e);
						if (isVisible(t,e,false))
							if (emptyCircumcircle(t,e)) return t;
						}	
					}
//					System.out.println();
				}
			}
			if (imax<k-1) {
				tt = false;
				imax++;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=jmin; u<=jmax; u++) {
					vert = m[imax][u].getVertices();
//					System.out.println("u: "+imin+" w: "+u+" vertices: "+vert);
					for (int q=0; q<vert.size(); q++) {
						Vertex t = (Vertex)(vert.elementAt(q));
						if ((t!=a)&&(t!=b)) {
//							System.out.println("testing vertex "+t+" for edge "+e);
						if (isVisible(t,e,false))
							if (emptyCircumcircle(t,e)) return t;
						}	
					}
//					System.out.println();
				}
			}
			if (jmax<l-1) {
				tt = false;
				jmax++;
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
				for (int u=imin; u<=imax; u++) {
					vert = m[u][jmax].getVertices();
//					System.out.println("u: "+u+" w: "+jmax+" vertices: "+vert);
					for (int q=0; q<vert.size(); q++) {
						Vertex t = (Vertex)(vert.elementAt(q));
						if ((t!=a)&&(t!=b)) {
//							System.out.println("testing vertex "+t+" for edge "+e);
						if (isVisible(t,e,false))
							if (emptyCircumcircle(t,e)) return t;
						}	
					}
//					System.out.println();
				}
			}
		}
		if (tt)  {
			System.out.println("bad ordine");
			System.exit(0);
		}
		return null;
	}
	
	private boolean emptyCircumcircle(Vertex a, HalfEdge e) {

//		System.out.println();
//		System.out.println(a);
		double x1 = a.getX();
		double y1 = a.getY();
		Vertex b = e.getOrig();
//		System.out.println(b);
		double x2 = b.getX();
		double y2 = b.getY();
		Vertex c = e.getTwin().getOrig();
//		System.out.println(c);
		double x3 = c.getX();
		double y3 = c.getY();
		
		double b1 = x1*x1 + y1*y1;
		double b2 = x2*x2 + y2*y2;
		double b3 = x3*x3 + y3*y3;

		double det = (x2-x1)*(y3-y1) - (y2-y1)*(x3-x1);
		double A = ((b2-b1)*(y3-y1) - (y2-y1)*(b3-b1))/det;
		double B = ((x2-x1)*(b3-b1) - (b2-b1)*(x3-x1))/det;
		double C = (x1*y2*b3 + x2*y3*b1 + x3*y1*b2 - x3*y2*b1 - x2*y1*b3 - x1*y3*b2)/det;
		
		double xc = A/2;
		double yc = B/2;
		double radius = Math.sqrt(C+xc*xc+yc*yc);
		if (det==0) {
			xc=yc=0.0;
			radius = 10000000000.0;
		}
//		System.out.println("xc: "+xc+" yc: "+yc+" radius: "+radius);

//		System.out.println("xmin: "+xmin+" xmax: "+xmax+" ymin: "+ymin+" ymax: "+ymax);
//		System.out.println("k: "+k+" l: "+l);

		double wi = (maxX-minX)/k;
		double h = (maxY-minY)/l;
//		System.out.println("w: "+w+" h: "+h);
		
		int imin = (int)((xc-radius-minX)/wi);
		int imax = (int)((xc+radius-minX)/wi);
		int jmin = (int)((yc-radius-minY)/h);
		int jmax = (int)((yc+radius-minY)/h);
		imin = (imin<0) ? 0 : (imin>k-1) ? k-1 : imin;
		imax = (imax<0) ? 0 : (imax>k-1) ? k-1 : imax;
		jmin = (jmin<0) ? 0 : (jmin>l-1) ? l-1 : jmin;
		jmax = (jmax<0) ? 0 : (jmax>l-1) ? l-1 : jmax;
//		System.out.println("xc-radius: "+(int)(xc-radius)+" xc+radius: "+(int)(xc+radius)+" yc-radius: "+(int)(yc-radius)+" yc+radius: "+(int)(yc+radius));
//		System.out.println("imin: "+imin+" imax: "+imax+" jmin: "+jmin+" jmax: "+jmax);
		
		for(int u=imin; u<=imax; u++)
			for (int w=jmin; w<=jmax; w++) {
				java.util.Vector vert = m[u][w].getVertices();
				for (int q=0; q<vert.size(); q++) {
					Vertex t = (Vertex)(vert.elementAt(q));
					if ((t!=b)&&(t!=c)&&(t!=a))  {
						double xt = t.getX();
						double yt = t.getY();
// 						System.out.println("Vertex to be tested for circumcircle: "+t);
						double dist = Math.sqrt((xt-xc)*(xt-xc) + (yt-yc)*(yt-yc));
//						System.out.println("The distance from circumcircle: "+dist);
						if ((radius-dist)>DcelGrid.EPS) {
//							System.out.println("Circumcircle includes:"+t);
							if (isVisible(t,e,false)) {
								return false;
							}
						}
					}
				}	
			}
		this.xc = xc;
		this.yc = yc;
		this.radius = radius;
		return true;
	}

	public boolean isVisible(Vertex a, HalfEdge e, boolean strict) {
//		return (
//		        (isVisible(a, e.getOrig(), strict))&&
//		        (isVisible(a, e.getTwin().getOrig(), strict))
//				);
		if (
		    (
		     (e.getTwin().getOrig().rightOf(e.getPrev()))||
	 		 (a.leftOf(e.getPrev()))
		    )&&
		    (a.leftOf(e))&&
		    (
		     (e.getOrig().rightOf(e.getNext()))||
			 (a.leftOf(e.getNext()))
		    )
		   )
		return ((notIntersects(a, e.getOrig(), strict))&&(notIntersects(a, e.getTwin().getOrig(), strict)));
		return false;	
	}

	public boolean isVisible(Vertex a, Vertex b, boolean strict) {
		HalfEdge e = a.getEdge();
//		System.out.println("e: "+e+" e.getPrev: "+e.getPrev());
//		System.out.println("a: "+a+" b: "+b);
		if (b.leftOf(e))
			if ((e.getPrev().getOrig().rightOf(e))||(b.leftOf(e.getPrev()))) {
//				System.out.println("etol van balra: "+a+"  "+b);
				return notIntersects(a,b, strict);
			}
		e = e.getPrev().getTwin();
		if (b.rightOf(e))
			if ((e.getPrev().getOrig().leftOf(e))||(b.rightOf(e.getPrev()))) {
//				System.out.println("e.getPrev()-tol van balra: "+a+"  "+b);
				return notIntersects(a,b, strict);
			}
		return false;
	}	
	
	public boolean notIntersects(Vertex a, Vertex b, boolean strict) {
		double ox, oy, dx, dy, xf, yf;
		double tMaxX, tMaxY, tDeltaX, tDeltaY, w, h;
		int oi, oj, di, dj, stepX, stepY;
		int X, Y;
		java.util.Vector edges;
		HalfEdge e;
		java.util.Vector lv, rv;
		
		w = (maxX-minX)/k;
		h = (maxY-minY)/l;
		ox = a.getX();
		oy = a.getY();
		dx = b.getX();
		dy = b.getY();
		oi = a.getVoxelI();
		oj = a.getVoxelJ();
		di = b.getVoxelI();
		dj = b.getVoxelJ();
//		System.out.println(a+" i: "+oi+" j: "+oj);
//		System.out.println(b+" i: "+di+" j: "+dj);
		stepX = ((dx-ox)>0) ? 1 : ((dx-ox)==0) ? 0 : -1;
		stepY = ((dy-oy)>0) ? 1 : ((dy-oy)==0) ? 0 : -1;
		xf = (stepX>0) ? minX + (oi+1)*w : minX + oi*w;
		yf = (stepY>0) ? minY + (oj+1)*h : minY + oj*h;

		tMaxX = (xf-ox)/(dx-ox);
		tMaxY = (yf-oy)/(dy-oy);
		tDeltaX = (dx-ox!=0) ? Math.abs(w/(dx-ox)) : 2.0;
		tDeltaY = (dy-oy!=0) ? Math.abs(h/(dy-oy)) : 2.0;
		for (X=oi, Y=oj; (X!=di)||(Y!=dj); ) {
//				System.out.println("Voxel     X: "+X+" Y: "+Y);
				edges = getEdges(X,Y);
				for (int u=0; u<edges.size(); u++) {
					e = (HalfEdge)(edges.elementAt(u));
//					System.out.println("edge: "+e);
					if (intersects(a,b,e,X,Y, strict)) return false;
				}

				if (stepX==0) Y = Y + stepY;
				else
					if (stepY==0) X = X + stepX;
					else	
						if (tMaxX<tMaxY)  {
							tMaxX = tMaxX + tDeltaX;
							X = X + stepX;
						}
						else {
							tMaxY = tMaxY + tDeltaY;
							Y = Y + stepY;
						}
		}
		edges = getEdges(X,Y);
//		System.out.println("Voxel     X: "+X+" Y: "+Y);
		for (int u=0; u<edges.size(); u++) {
			e = (HalfEdge)(edges.elementAt(u));
//			System.out.println("edge: "+e);
			if (intersects(a,b,e,X,Y, strict)) return false;
		}
		return true;
	}
	
	private boolean intersects(Vertex a, Vertex b, HalfEdge e, int i, int j, boolean strict) {
		double ox, oy, dx, dy, xf, yf, xmax, xmin, ymax, ymin, x1, y1, x2, y2;
		double tMaxX, tMaxY, tDeltaX, tDeltaY, w, h;
		int oi, oj, di, dj, stepX, stepY;
		double X, Y;
		w = (maxX-minX)/k;
		h = (maxY-minY)/l;
		ox = a.getX();
		oy = a.getY();
		dx = b.getX();
		dy = b.getY();
		Vertex v = e.getOrig();
		x1 = v.getX();
		y1 = v.getY();
		v = e.getTwin().getOrig();
		x2 = v.getX();
		y2 = v.getY();
//		System.out.println("ox: "+ox+" oy: "+oy+" dx: "+dx+" dy: "+dy);
//		System.out.println("x1: "+x1+" y1: "+y1+" x2: "+x2+" y2: "+y2);
//		
//		System.out.println("Edge tested in "+i+","+j+" is "+e);
//		System.out.println("w: "+w+" h: "+h);
		
		double det = (dy-oy)*(x1-x2) - (y2-y1)*(ox-dx);
//		System.out.println("det: "+det);
		double b1 = dy*ox - oy*ox - dx*oy + ox*oy;
		double b2 = y2*x1 - y1*x1 - x2*y1 + x1*y1;
		if (det!=0) {
//			System.out.println("b1: "+b1+" b2: "+b2);
			X = (b1*(x1-x2) - b2*(ox-dx))/det;
			Y = ((dy-oy)*b2 - (y2-y1)*b1)/det;
//			System.out.println("Edge: "+e+"  X: "+X+" Y: "+Y);
			
			if ((((x1<=X)&&(x2>=X))||((x1>=X)&&(x2<=X)))&&
			    (((y1<=Y)&&(y2>=Y))||((y1>=Y)&&(y2<=Y)))&&
			    (((ox<=X)&&(dx>=X))||((ox>=X)&&(dx<=X)))&&
			    (((oy<=Y)&&(dy>=Y))||((oy>=Y)&&(dy<=Y))))
			if (((minX+i*w)<=X)&&(X<=(minX+(i+1)*w))&&
				((minY+j*h)<=Y)&&(Y<=(minY+(j+1)*h))) {
					if (((Math.abs(X-x1))<DcelGrid.EPS)&&((Math.abs(Y-y1))<DcelGrid.EPS)) {
						if (strict) {
							 	if ((e.getOrig()==a)||(e.getOrig()==b))
									return false;
								else {
//									System.out.println("Exact intersection not true: "+e+" X: "+X+" Y: "+Y);
									return true;
								}
						}
						else
						if ((a.triArea(e.getTwin().getOrig(), a, b)*
						     a.triArea(e.getPrev().getOrig(), a, b))<0) 
							 	if ((e.getOrig()==a)||(e.getOrig()==b))
									return false;
								else  {
//									System.out.println("Intersection should'n be beginning: "+e.getOrig());
									return true;
								}
					} else
					if (((Math.abs(X-x2))<DcelGrid.EPS)&&((Math.abs(Y-y2))<DcelGrid.EPS))  {
						if (strict) {
							 	if ((e.getTwin().getOrig()==a)||(e.getTwin().getOrig()==b))
									return false;
								else {
//									System.out.println("Exact intersection not true: "+e+" X: "+X+" Y: "+Y);
									return true;
								}
						}
						else
						if ((a.triArea(e.getOrig(), a, b)*
						     a.triArea(e.getNext().getTwin().getOrig(), a, b))<=0) 
							 	if ((e.getTwin().getOrig()==a)||(e.getTwin().getOrig()==b))
									return false;
								else  {
//									System.out.println("Intersection shouldn't be end: "+e.getOrig());
									return true;
								}
					} else return true;
			}
//					else  {	System.out.println("Edge: "+e+" Over"); }
//			else  {	System.out.println("Edge: "+e+" in voxel: "+i+","+j+"intersests outside: "+X+","+Y);}
		} 
		else  { 
			if (strict) {
//				System.out.println("yv: "+b1/(ox-dx)+" ye: "+b2/(x1-x2));			
//				System.out.println("xv: "+b1/(dy-oy)+" xe: "+b2/(y2-y1));			
				if (
				    (
					 (Math.abs(b1/(ox-dx)-b2/(x1-x2)))<DcelGrid.EPS
					)||
				    (
					 (Math.abs(b1/(dy-oy)-b2/(y2-y1)))<DcelGrid.EPS
					) 
				   )  {
					double maxXv = Math.max(ox, dx);
					double minXv = Math.min(ox, dx);
					double maxYv = Math.max(oy, dy);
					double minYv = Math.min(oy, dy);
					double maxXe = Math.max(x1, x2);
					double minXe = Math.min(x1, x2);
					double maxYe = Math.max(y1, y2);
					double minYe = Math.min(y1, y2);
//					System.out.println("maxXv: "+maxXv+" minXv: "+minXv+" maxYv: "+maxYv+" minYv: "+minYv);
//					System.out.println("maxXe: "+maxXe+" minXe: "+minXe+" maxYe: "+maxYe+" minYe: "+minYe);
					if ((
					     ((minXe-maxXv)>-DcelGrid.EPS)||
					     ((minXv-maxXe)>-DcelGrid.EPS)
					    )&&
					    (
					     ((minYe-maxYv)>-DcelGrid.EPS)||
					     ((minYv-maxYe)>-DcelGrid.EPS)
					    )) {
//							System.out.println("Edge: "+e+" is the continuity of "+a+" and "+b); 
					   	return false;
					}
					else  {
//							System.out.println("Edge: "+e+" is over "+a+" and "+b); 
						return true;	
					}
				}
				else {
//					System.out.println("Edge: "+e+" is paralell with "+a+" and "+b); 
					 return false;
				}
			}
		}
		return false;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMinX() {
		return minX;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMinY() {
		return minY;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}
	
	public void addVertex(Vertex p)  {
		double w = (maxX-minX)/k; //width of the Voxel
		double h = (maxY-minY)/l; //height of the Voxel
		int r = (int)((p.getX()-minX)/w);
		int t = (int)((p.getY()-minY)/h);
//		System.out.println("x: "+p.getX()+"   y:"+p.getY());
//		System.out.println("r: "+r+"  t: "+t);
//		System.out.println("vertexek: "+m[r][t].getVertices().size());
		p.setVoxel(r, t);
//		System.out.println("r: "+r+" t: "+t+" vertex: "+p);
		m[r][t].addVertex(p);
	}

	public void addEdge(HalfEdge e)  {
		double ox, oy, dx, dy, xf, yf, xmax, xmin, ymax, ymin;
		double tMaxX, tMaxY, tDeltaX, tDeltaY, w, h;
		int oi, oj, di, dj, stepX, stepY;
		int X, Y;
		w = (maxX-minX)/k;
		h = (maxY-minY)/l;

			ox = e.getOrig().getX();
			oy = e.getOrig().getY();
			oi = e.getOrig().getVoxelI();
			oj = e.getOrig().getVoxelJ();
			e = e.getTwin();
			dx = e.getOrig().getX();
			dy = e.getOrig().getY();
			di = e.getOrig().getVoxelI();
			dj = e.getOrig().getVoxelJ();
			stepX = ((dx-ox)>0) ? 1 : ((dx-ox)==0) ? 0 : -1;
			stepY = ((dy-oy)>0) ? 1 : ((dy-oy)==0) ? 0 : -1;
			xf = (stepX>0) ? minX + (oi+1)*w : minX + oi*w;
			yf = (stepY>0) ? minY + (oj+1)*h : minY + oj*h;
			tMaxX = (xf-ox)/(dx-ox);
			tMaxY = (yf-oy)/(dy-oy);
			tDeltaX = (dx-ox!=0) ? Math.abs(w/(dx-ox)) : 2.0;
			tDeltaY = (dy-oy!=0) ? Math.abs(h/(dy-oy)) : 2.0;
			e = e.getTwin();
//			System.out.println(e);
//			System.out.println("stepX: "+stepX+"   stepY: "+stepY);
			for (X=oi, Y=oj; (X!=di)||(Y!=dj); ) {
//				System.out.println("X: "+X+ "Y: "+ Y);
				m[X][Y].addEdge(e);

				if (stepX==0) Y = Y + stepY;
				else
					if (stepY==0) X = X + stepX;
					else	
						if (tMaxX<tMaxY)  {
							tMaxX = tMaxX + tDeltaX;
							X = X + stepX;
						}
						else {
							tMaxY = tMaxY + tDeltaY;
							Y = Y + stepY;
						}
			}
		m[di][dj].addEdge(e);
	}
	
	public void removeVertex(Vertex p) {
		double w = (maxX-minX)/k; //width of the Voxel
		double h = (maxY-minY)/l; //height of the Voxel
		int r = (int)((p.getX()-minX)/w);
		int t = (int)((p.getY()-minY)/h);
//		System.out.println("x: "+p.getX()+"   y:"+p.getY());
//		System.out.println("r: "+r+"  t: "+t);
//		System.out.println("vertexek: "+m[r][t].getVertices().size());
		m[r][t].removeVertex(p);
	}
	
	public void removeEdge(HalfEdge e)  {
		double ox, oy, dx, dy, xf, yf, xmax, xmin, ymax, ymin;
		double tMaxX, tMaxY, tDeltaX, tDeltaY, w, h;
		int oi, oj, di, dj, stepX, stepY;
		int X, Y;
		w = (maxX-minX)/k;
		h = (maxY-minY)/l;

			ox = e.getOrig().getX();
			oy = e.getOrig().getY();
			oi = e.getOrig().getVoxelI();
			oj = e.getOrig().getVoxelJ();
			e = e.getTwin();
			dx = e.getOrig().getX();
			dy = e.getOrig().getY();
			di = e.getOrig().getVoxelI();
			dj = e.getOrig().getVoxelJ();
			stepX = ((dx-ox)>0) ? 1 : ((dx-ox)==0) ? 0 : -1;
			stepY = ((dy-oy)>0) ? 1 : ((dy-oy)==0) ? 0 : -1;
			xf = (stepX>0) ? minX + (oi+1)*w : minX + oi*w;
			yf = (stepY>0) ? minY + (oj+1)*h : minY + oj*h;
			tMaxX = (xf-ox)/(dx-ox);
			tMaxY = (yf-oy)/(dy-oy);
			tDeltaX = (dx-ox!=0) ? Math.abs(w/(dx-ox)) : 2.0;
			tDeltaY = (dy-oy!=0) ? Math.abs(h/(dy-oy)) : 2.0;
			e = e.getTwin();
//			System.out.println(e);
//			System.out.println("stepX: "+stepX+"   stepY: "+stepY);
			for (X=oi, Y=oj; (X!=di)||(Y!=dj); ) {
//				System.out.println("X: "+X+ "Y: "+ Y);
				m[X][Y].removeEdge(e);

				if (stepX==0) Y = Y + stepY;
				else
					if (stepY==0) X = X + stepX;
					else	
						if (tMaxX<tMaxY)  {
							tMaxX = tMaxX + tDeltaX;
							X = X + stepX;
						}
						else {
							tMaxY = tMaxY + tDeltaY;
							Y = Y + stepY;
						}
			}
		m[di][dj].removeEdge(e);
	}
	
	public java.util.Vector getVertices(int i, int j) {
		return m[i][j].getVertices();
	}

	public java.util.Vector getEdges(int i, int j) {
		return m[i][j].getEdges();
	}
	
}