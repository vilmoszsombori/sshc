package ro.utcluj.as.sshc;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class JMultiNurbsInterface {

    private JMultiNurbs MultiNurb = new JMultiNurbs(Color.blue, Color.green);
    private JPanel ControlToolBar = new JPanel();
    private JPanel CurveToolBar = new JPanel();

    private JButton addNurbs = new JButton("+");
    private JComboBox idNurbs = new JComboBox();
    private JButton removeNurbs = new JButton("-");

    private JPanel ShapeControl = new JPanel();
    private JToggleButton closecurve = new JToggleButton("Close Curve", false);
    private JCheckBox showpoint = new JCheckBox("Points", true);
    private JCheckBox shownumber = new JCheckBox("Numbered", false);
    private JButton modifpoints = new JButton("Control Points");
    private JButton modifknots = new JButton("Knots");
    private JLabel rend = new JLabel("Render:");
    private JLabel ord = new JLabel(" Order:");
    private JTextField render = new JTextField("", 3);
    private JTextField order = new JTextField("", 3);

    private JPanel PointControl = new JPanel();
    private JTextField weight = new JTextField("", 9);
    private JLabel controlPointLabel = new JLabel("ControlPoint:");
    private JComboBox controlPoint = new JComboBox();
    private JLabel weightLabel = new JLabel(" Weight:");
    private JButton back1 = new JButton("Back");
    private JScrollBar scrollWeight = new JScrollBar(JScrollBar.HORIZONTAL);

    private JPanel KnotControl = new JPanel();
    private JTextField knotValue = new JTextField("", 9);
    private JLabel knotLabel = new JLabel("Knot:");
    private JLabel prevKnot = new JLabel("");
    private JLabel nextKnot = new JLabel("");
    private JComboBox knotId = new JComboBox();
    private JButton back2 = new JButton("Back");
    private JScrollBar scrollKnot = new JScrollBar(JScrollBar.HORIZONTAL);


    public JMultiNurbsInterface() {

        ShapeControl.add(addNurbs);
        idNurbs.setPreferredSize(new Dimension(50, 25));
        ShapeControl.add(idNurbs);
        ShapeControl.add(removeNurbs);
        JComponent s = new JSeparator(SwingConstants.VERTICAL);
        s.setPreferredSize(new Dimension(50, 0));
        ShapeControl.add(s);

        ShapeControl.add(modifpoints);
        ShapeControl.add(modifknots);
        ShapeControl.add(closecurve);
        ShapeControl.add(rend);
        ShapeControl.add(render);
        ShapeControl.add(ord);
        ShapeControl.add(order);
        PointControl.add(showpoint);
        PointControl.add(shownumber);
        PointControl.add(controlPointLabel);
        PointControl.add(controlPoint);
        PointControl.add(weightLabel);
        PointControl.add(scrollWeight);
        PointControl.add(weight);
        PointControl.add(back1);
        KnotControl.add(knotLabel);
        KnotControl.add(knotId);
        KnotControl.add(scrollKnot);
        KnotControl.add(prevKnot);
        KnotControl.add(knotValue);
        KnotControl.add(nextKnot);
        KnotControl.add(back2);

        addListeners();
        MultiNurb.addMouseClickListener();
        initComponents();
    }

    public JMultiNurbsInterface(JMultiNurbs t) {

        ShapeControl.add(addNurbs);
        idNurbs.setPreferredSize(new Dimension(50, 25));
        ShapeControl.add(idNurbs);
        ShapeControl.add(removeNurbs);
        JComponent s = new JSeparator(SwingConstants.VERTICAL);
        s.setPreferredSize(new Dimension(50, 0));
        ShapeControl.add(s);

        ShapeControl.add(modifpoints);
        ShapeControl.add(modifknots);
        ShapeControl.add(closecurve);
        ShapeControl.add(rend);
        ShapeControl.add(render);
        ShapeControl.add(ord);
        ShapeControl.add(order);
        PointControl.add(showpoint);
        PointControl.add(shownumber);
        PointControl.add(controlPointLabel);
        PointControl.add(controlPoint);
        PointControl.add(weightLabel);
        PointControl.add(scrollWeight);
        PointControl.add(weight);
        PointControl.add(back1);
        KnotControl.add(knotLabel);
        KnotControl.add(knotId);
        KnotControl.add(scrollKnot);
        KnotControl.add(prevKnot);
        KnotControl.add(knotValue);
        KnotControl.add(nextKnot);
        KnotControl.add(back2);

        addListeners();
        MultiNurb = t;
        MultiNurb.addMouseClickListener();
        for (int u = 1; u <= t.nurbsCount(); u++) {
            idNurbs.addItem(u + "");
        }
        initComponents();
    }

    public javax.swing.JComponent getCenter() {
        return MultiNurb;
    }

    public javax.swing.JComponent getNorth() {
        return CurveToolBar;
    }

    public javax.swing.JComponent getSouth() {
        return ControlToolBar;
    }


    public void addListeners() {

        addNurbs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MultiNurb.setEditing(false);
                MultiNurb.setDrawing(true);
                idNurbs.addItem(MultiNurb.addNurbs() + "");
                idNurbs.setSelectedIndex(MultiNurb.nurbsCount() - 1);
                initComponents();
            }
        });

        idNurbs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MultiNurb.switchTo(idNurbs.getSelectedIndex());
                initComponents();
            }
        });

        removeNurbs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int aux = MultiNurb.removeCurrentNurbs();
                idNurbs.removeAllItems();
                for (int i = 1; i <= aux; i++)
                    idNurbs.addItem(String.valueOf(i));
                if (aux > 0) {
                    MultiNurb.switchTo(0);
                    MultiNurb.setDrawing(true);
                } else
                    MultiNurb.setDrawing(false);
                MultiNurb.setEditing(false);
                initComponents();
            }
        });

        modifpoints.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MultiNurb.thereIsActive()) {
                    MultiNurb.setEditing(true);
                    controlPoint.removeAllItems();
                    for (int i = 0; i < MultiNurb.getCurrentNurbs().controlSize(); i++)
                        controlPoint.addItem(String.valueOf(i));
                    ControlToolBar.setVisible(false);
                    ControlToolBar.removeAll();
                    ControlToolBar.add(PointControl);
                    ControlToolBar.setVisible(true);
                }
            }
        });

        modifknots.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MultiNurb.thereIsActive()) {
                    MultiNurb.setEditing(true);
                    knotId.removeAllItems();
                    for (int i = 1; i < MultiNurb.getCurrentNurbs().knotSize() - 1; i++)
                        knotId.addItem(String.valueOf(i));
                    ControlToolBar.setVisible(false);
                    ControlToolBar.removeAll();
                    ControlToolBar.add(KnotControl);
                    ControlToolBar.setVisible(true);
                }
            }
        });

        back1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MultiNurb.setEditing(false);
                MultiNurb.setDrawing(true);
                ControlToolBar.setVisible(false);
                ControlToolBar.removeAll();
                ControlToolBar.add(ShapeControl);
                ControlToolBar.setVisible(true);
            }
        });

        back2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MultiNurb.setEditing(false);
                MultiNurb.setDrawing(true);
                ControlToolBar.setVisible(false);
                ControlToolBar.removeAll();
                ControlToolBar.add(ShapeControl);
                ControlToolBar.setVisible(true);
            }
        });

        closecurve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeCurve();
            }
        });

        showpoint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MultiNurb.thereIsActive()) {
                    MultiNurb.getCurrentNurbs().showPoints =
                            !MultiNurb.getCurrentNurbs().showPoints;
                    MultiNurb.repaint();
                }
            }
        });

        shownumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MultiNurb.thereIsActive()) {
                    MultiNurb.getCurrentNurbs().showNumbers =
                            !MultiNurb.getCurrentNurbs().showNumbers;
                    MultiNurb.repaint();
                }
            }
        });

        scrollWeight.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollWeightChanged();
            }
        });
        scrollKnot.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                scrollKnotChanged();
            }
        });

        render.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterHitInRender(evt);
            }
        });
        order.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterHitInOrder(evt);
            }
        });
        controlPoint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controlPointComboChanged();
            }
        });
        knotId.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                knotComboChanged();
            }
        });
        weight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterHitInWeight(evt);
            }
        });
        knotValue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterHitInKnotValue(evt);
            }
        });

        // loadNurbs.addActionListener(new ActionListener() { public void
        // actionPerformed(ActionEvent e) { loadNurbs(); } });
        // saveNurbs.addActionListener(new ActionListener() { public void
        // actionPerformed(ActionEvent e) { saveNurbs(); } });
    }


    private void closeCurve() {
        if (MultiNurb.thereIsActive()) {
            if (MultiNurb.getCurrentNurbs().isClosed()) {
                closecurve.setText("Close Curve");
                MultiNurb.setCloseCurrentCurve(false);
            } else {
                closecurve.setText("Open  Curve");
                MultiNurb.setCloseCurrentCurve(true);
            }
            ControlToolBar.repaint();
            MultiNurb.repaint();
        }
    }


    private void initComponents() {
        ControlToolBar.setVisible(false);
        MultiNurb.setEditing(false);
        MultiNurb.setDrawing(true);
        Dimension d = scrollWeight.getPreferredSize();
        d.width = 100;
        scrollWeight.setMaximum(160);
        scrollWeight.setMinimum(-150);
        scrollWeight.setValue(0);
        scrollWeight.setPreferredSize(d);
        scrollKnot.setMaximum(137);
        scrollKnot.setMinimum(1);
        scrollKnot.setPreferredSize(d);
        render.setText("");
        order.setText("");
        closecurve.setSelected(false);
        closecurve.setLabel("Close Curve");
        try {
            if (MultiNurb.thereIsActive()) {
                Nurbs2D currentNurbs = MultiNurb.getCurrentNurbs();
                order.setText(String.valueOf(currentNurbs.getOrder()));
                render.setText(String.valueOf(currentNurbs.getRender()));
                showpoint.setSelected(currentNurbs.showPoints);
                shownumber.setSelected(currentNurbs.showNumbers);
                if (currentNurbs.isClosed()) {
                    closecurve.setSelected(true);
                    closecurve.setLabel("Open  Curve");
                }
            }
        } catch (Exception e) {
        }
        ControlToolBar.removeAll();
        ControlToolBar.add(ShapeControl);
        ControlToolBar.setVisible(true);
    }


    private void controlPointComboChanged() {
        try {
            int i = controlPoint.getSelectedIndex();
            Point2D p = MultiNurb.getCurrentNurbs().getControlPoint(i);
            MultiNurb.setCurrentControlPointIndex(i);
            weight.setText(String.valueOf(p.getW()));
            ControlToolBar.repaint();
        } catch (Exception e) {
            System.out.println("hiba a controlPointComboChanged()-be");
            System.out.println(e.getMessage());
        }
    }


    private void knotComboChanged() {
        try {
            int i = knotId.getSelectedIndex() + 1;
            double min = MultiNurb.getCurrentNurbs().getKnot(i - 1),
                    max = MultiNurb.getCurrentNurbs().getKnot(i + 1),
                    x = MultiNurb.getCurrentNurbs().getKnot(i);
            int a = (int) (128 * (x - min) / (max - min));
            scrollKnot.setValue(a);
            prevKnot.setText(String.valueOf(min));
            nextKnot.setText(String.valueOf(max));
            knotValue.setText(String.valueOf(x));
            ControlToolBar.repaint();
        } catch (Exception e) {
            System.out.println("hiba a knotComboChanged()-be");
            System.out.println(e.getMessage());
        }
    }

    private void enterHitInWeight(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            try {
                int i = controlPoint.getSelectedIndex();
                Nurbs2D currentNurbs = MultiNurb.getCurrentNurbs();
                Point2D p = currentNurbs.getControlPoint(i);
                double temp = p.getW();
                double w = (new Double(weight.getText())).doubleValue();
                if (currentNurbs.modifyPoint(i, currentNurbs.getControlPoint(i), w))
                    currentNurbs.repaint();
                else {
                    weight.setText(p.getw().toString());
                }
            } catch (Exception e) {
            }
        }
    }

    private void enterHitInKnotValue(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            try {
                int i = knotId.getSelectedIndex() + 1;
                Nurbs2D currentNurbs = MultiNurb.getCurrentNurbs();
                double p = currentNurbs.getKnot(i);
                double w = (new Double(knotValue.getText())).doubleValue();
                if (currentNurbs.setKnot(i, w))
                    currentNurbs.repaint();
                else
                    knotValue.setText(String.valueOf(p));
            } catch (Exception e) {
            }
        }
    }


    private void scrollWeightChanged() {
        try {
            int i = controlPoint.getSelectedIndex();
            int a = scrollWeight.getValue();
            double w = a < 0 ? Math.tan(a / 250.0) + 1 : Math.tan(a / (250.0 - a)) + 1;
            weight.setText((new Double(w)).toString().substring(0, 9));
            MultiNurb.redrawCurrentNurbsWith("ControlPoint", i, w);
            MultiNurb.repaint();
        } catch (Exception e) {
        }
    }

    private void scrollKnotChanged() {
        try {
            int i = knotId.getSelectedIndex() + 1;
            int a = scrollKnot.getValue();
            double min = MultiNurb.getCurrentNurbs().getKnot(i - 1),
                    max = MultiNurb.getCurrentNurbs().getKnot(i + 1);
            double w = min + a * (max - min) / 128;
            MultiNurb.redrawCurrentNurbsWith("Knot", i, w);
            knotValue.setText(String.valueOf(w));
            MultiNurb.repaint();
        } catch (Exception e) {
            System.out.println("hiba a scrollKnotChanged()-be");
            System.out.println(e.getMessage());
        }
    }


    private void enterHitInOrder(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            if (MultiNurb.thereIsActive()) {
                Nurbs2D currentNurbs = MultiNurb.getCurrentNurbs();
                String oldvalue = String.valueOf(currentNurbs.getOrder());
                if (!currentNurbs.setOrder((Integer.valueOf(order.getText()).intValue())))
                    order.setText(oldvalue);
                currentNurbs.repaint();
            }
        }
    }

    private void enterHitInRender(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == evt.VK_ENTER) {
            if (MultiNurb.thereIsActive()) {
                Nurbs2D currentNurbs = MultiNurb.getCurrentNurbs();
                String oldvalue = String.valueOf(currentNurbs.getRender());
                if (!currentNurbs.setRender((Integer.valueOf(render.getText()).intValue())))
                    render.setText(oldvalue);
                currentNurbs.repaint();
            }
        }
    }

    private void loadNurbs() {
        try {
            java.io.ObjectInputStream objInStream =
                    new java.io.ObjectInputStream(new java.io.FileInputStream("nurbs.tmp"));
            JMultiNurbs temp = (JMultiNurbs) objInStream.readObject();
            objInStream.close();
            MultiNurb = temp;
            idNurbs.removeAllItems();
            for (int i = 1; i <= MultiNurb.nurbsCount(); i++)
                idNurbs.addItem(String.valueOf(i));
            MultiNurb.addMouseClickListener();
            initComponents();
        } catch (Exception e) {
            System.out.println("Hiba a loadNurbs()-ba: " + e.getMessage());
        }
    }

    public void saveNurbs(java.io.File file) {
        try {
            java.io.ObjectOutputStream objOutStream =
                    new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
            objOutStream.writeObject(MultiNurb);
            objOutStream.close();
        } catch (Exception e) {
            System.out.println("Hiba a saveNurbs()-ba: " + e.getMessage());
        }
    }


}



class JMultiNurbs extends javax.swing.JPanel implements java.io.Serializable {

    private java.util.Vector Nurbs;
    private java.awt.Color activeNurbsColor, inactiveNurbsColor;

    private Nurbs2D currentNurbs;
    private int CurrentControlPointIndex;

    private boolean IsEditing;
    private boolean IsDrawing;

    public JMultiNurbs(java.awt.Color active, java.awt.Color inactive) {
        setLayout(new OverlayLayout(this));
        setBackground(Color.white);
        Nurbs = new java.util.Vector();
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
            Nurbs.add(currentNurbs);
            setVisible(false);
            add(currentNurbs);
            setVisible(true);
        } catch (Exception e) {
        }
        return Nurbs.size();
    }

    public int removeCurrentNurbs() {
        if (currentNurbs != null) {
            Nurbs.remove(currentNurbs);
            setVisible(false);
            remove(currentNurbs);
            currentNurbs = null;
            setVisible(true);
        }
        return Nurbs.size();
    }

    public void switchTo(int index) {
        try {
            if ((index >= 0) && (index < Nurbs.size())) {
                if (currentNurbs != null) {
                    currentNurbs.setColor(inactiveNurbsColor);
                    currentNurbs.showPoints = false;
                    currentNurbs.setClosedCurve(true);
                }
                currentNurbs = (Nurbs2D) Nurbs.elementAt(index);
                currentNurbs.setClosedCurve(false);
                currentNurbs.setColor(activeNurbsColor);
                currentNurbs.showPoints = true;
                repaint();
            }
        } catch (Exception e) {
        }
    }

    public int nurbsCount() {
        return Nurbs.size();
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

    public java.util.Vector toPolyline() {
        java.util.Vector polyline = new java.util.Vector();
        for (int i = 0; i < Nurbs.size(); i++) {
            java.util.Vector o = ((Nurbs2D) Nurbs.elementAt(i)).toPolyline();
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
