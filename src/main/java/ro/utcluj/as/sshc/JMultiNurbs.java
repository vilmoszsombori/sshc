package ro.utcluj.as.sshc;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.OverlayLayout;

class JMultiNurbs extends javax.swing.JPanel implements java.io.Serializable {

	private static final long serialVersionUID = -2925805000754949479L;
	
	private Vector<Nurbs2D> nurbss = new Vector<>();
	
	private java.awt.Color activeNurbsColor, inactiveNurbsColor;

	private Nurbs2D currentNurbs;
	private int CurrentControlPointIndex;

	private boolean IsEditing;
	private boolean IsDrawing;

	public JMultiNurbs(java.awt.Color active, java.awt.Color inactive) {
		setLayout(new OverlayLayout(this));
		setBackground(Color.white);
		inactiveNurbsColor = inactive;
		activeNurbsColor = active;
		IsEditing = false;
		IsDrawing = true;
		CurrentControlPointIndex = 0;
	}

	public void setCurrentControlPointIndex(int i) {
		CurrentControlPointIndex = i;
	}

	public int getCurrentControlPointIndex() {
		return CurrentControlPointIndex;
	}

	public void setEditing(boolean what) {
		IsEditing = what;
	}

	public void setDrawing(boolean what) {
		IsDrawing = what;
	}

	public boolean editing() {
		return IsEditing;
	}

	public boolean drawing() {
		return IsDrawing;
	}

	public void redrawCurrentNurbsWith(String what, int index, double newvalue) {
		if (currentNurbs != null) {
			if (what == "ControlPoint")
				currentNurbs.modifyPoint(index, newvalue);
			if (what == "Knot")
				currentNurbs.setKnot(index, newvalue);
			currentNurbs.repaint();
		}
	}

	public int addNurbs() {
		try {
			if (currentNurbs != null) {
				currentNurbs.setColor(inactiveNurbsColor);
				currentNurbs.showPoints = false;
				currentNurbs.setClosedCurve(true);
			}
			currentNurbs = new Nurbs2D(activeNurbsColor);
			currentNurbs.setClosedCurve(false);
			nurbss.add(currentNurbs);
			setVisible(false);
			add(currentNurbs);
			setVisible(true);
		} catch (Exception e) {
		}
		return nurbss.size();
	}

	public int removeCurrentNurbs() {
		if (currentNurbs != null) {
			nurbss.remove(currentNurbs);
			setVisible(false);
			remove(currentNurbs);
			currentNurbs = null;
			setVisible(true);
		}
		return nurbss.size();
	}

	public void switchTo(int index) {
		try {
			if ((index >= 0) && (index < nurbss.size())) {
				if (currentNurbs != null) {
					currentNurbs.setColor(inactiveNurbsColor);
					currentNurbs.showPoints = false;
					currentNurbs.setClosedCurve(true);
				}
				currentNurbs = (Nurbs2D) nurbss.elementAt(index);
				currentNurbs.setClosedCurve(false);
				currentNurbs.setColor(activeNurbsColor);
				currentNurbs.showPoints = true;
				repaint();
			}
		} catch (Exception e) {
		}
	}

	public int nurbsCount() {
		return nurbss.size();
	}

	public void setCloseCurrentCurve(boolean what) {
		if (currentNurbs != null)
			currentNurbs.setClosedCurve(what);
	}

	public boolean getCloseCurrentCurve() {
		if (currentNurbs != null)
			return currentNurbs.isClosed();
		else
			return false;
	}

	public void addMouseClickListener() {
		addMouseListener(new MouseClickHandle());
	}

	class MouseClickHandle extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			Point mouse = (Point) e.getPoint();
			if (IsEditing) {
				currentNurbs.modifyPoint(CurrentControlPointIndex, mouse);
				currentNurbs.repaint();
			} else if (currentNurbs != null) {
				currentNurbs.addPoint(mouse);
				currentNurbs.repaint();
			}
		}
	}

	public Vector<Vector<Point>> toPolyline() {
		Vector<Vector<Point>> polyline = new Vector<>();
		for (int i = 0; i < nurbss.size(); i++) {
			Vector<Point> o = nurbss.elementAt(i).toPolyline();
			if (o != null)
				polyline.add(o);
		}
		return polyline;
	}

	public boolean thereIsActive() {
		if (currentNurbs != null)
			return true;
		else
			return false;
	}

	public Nurbs2D getCurrentNurbs() {
		return currentNurbs;
	}

}
