package ro.utcluj.as.sshc;

import java.awt.*;

public class Drawable extends javax.swing.JComponent {
	private int x1[];
	private int y1[];
	private int x2[];
	private int y2[];
	private int k,l,n,xm,ym, m, ycoord;
	private double maxX, maxY, minX, minY;
	private Color dom, ed, in;
	private boolean edge, vertex, gridn, grid;
	private boolean boundary[];
		
	public Drawable(int x1[], int y1[], int x2[], int y2[], boolean[] boundary,
					 int k, int l, double maxX, double maxY, double minX, double minY,
					 int n, int m, int xm, int ym, Color c1, Color c2, Color c3,
					 boolean edge, boolean vertex, boolean gridn, boolean grid, int ycoord) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1 ;
		this.y2 = y2;
		this.n = n;
		this.k = k;
		this.l = l;
		this.maxX = maxX;
		this.minX = minX;
		this.maxY = maxY;
		this.minY = minY;
		this.dom = c1;
		this.ed = c2;
		this.in = c3;
		this.xm = xm;
		this.ym = ym;
		this.edge = edge;
		this.vertex = vertex;
		this.gridn = gridn;
		this.grid =grid;
		this.m = m;
		this.boundary = boundary;
		this.ycoord = ycoord;
	}
	
	
	public void paint(java.awt.Graphics g) {
		// drawing the grid
		double w = (maxX-minX)/k;
		double h = (maxY-minY)/l;
		if (n==m)
		for (int i=0; i<=k; i++) {
			g.setColor(ed);
			if (grid) g.drawLine((int)(minX+i*w+xm), (int)(ym-minY)*ycoord, (int)(minX+i*w+xm), (int)(ym-maxY)*ycoord);
			g.setColor(in);
			if ((i!=0)&&(gridn)) g.drawString(i-1+"", (int)(minX+i*w+xm-w/2), (int)(ym-minY+15*((ycoord==-1)?0:1))*ycoord);
		}
		if (n==m)
		for (int i=0; i<=l; i++) {
			g.setColor(ed);
			if (grid) g.drawLine((int)(minX+xm), (int)(ym-(minY+i*h))*ycoord, (int)(maxX+xm), (int)(ym-(minY+i*h))*ycoord);
			g.setColor(in);
			if ((i!=0)&&(gridn)) g.drawString(i-1+"", (int)(minX+xm-10), (int)(ym-(minY+i*h-h/2*((ycoord==-1)?0:1)))*ycoord);
		}
		
		//drawing the domain
		for (int i=0; i<n; i++) {
			g.setColor(dom);
			g.drawLine(x1[i] + xm, (ym - y1[i])*ycoord, x2[i] + xm, (ym - y2[i])*ycoord);
			if ((n!=m)&&(boundary[i])) g.drawLine(x1[i] + xm+1, (ym - y1[i])*ycoord, x2[i] + xm+1, (ym - y2[i])*ycoord);
			if ((n!=m)&&(boundary[i])) g.drawLine(x1[i] + xm, (ym - y1[i]+1)*ycoord, x2[i] + xm, (ym - y2[i]+1)*ycoord);
			g.setColor(in);
			if (edge) {
				g.setFont(new Font(null, Font.ITALIC, 10));
				g.drawString(i+"", (x1[i] + x2[i])/2 + xm, (ym - (y1[i] + y2[i])/2)*ycoord);
			}
			if ((vertex)&&(i<m)) {
				g.setFont(new Font(null, Font.ITALIC, 10));
				g.drawString(i+"", x1[i] + xm, (ym - y1[i])*ycoord);
//				g.drawString(i+1+"", x2[i] + xm, ym - y2[i]);
			}
		}
	}
	
	public void chEdge() {
		edge = !edge;
	}

	public void chVertex() {
		vertex = !vertex;
	}

	public void chGrid() {
		grid = !grid;
	}
	public void chGridn() {
		gridn = !gridn;
	}
}