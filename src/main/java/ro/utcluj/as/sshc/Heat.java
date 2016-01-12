package ro.utcluj.as.sshc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

class closeWindowListener extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}

public class Heat extends JFrame {
    
    private static final long serialVersionUID = -6253011607478671976L;
    
    JMenu file = new JMenu("File");
    JMenuItem neww = new JMenuItem("New");
    JMenuItem open = new JMenuItem("Open...");
    JMenuItem save = new JMenuItem("Save...");
    JMenuItem exit = new JMenuItem("Exit");
    JMenuItem about = new JMenuItem("About...");

    JMenu view = new JMenu("View");
    JMenuItem veditor = new JMenuItem("Editor");
    JMenuItem vgeom = new JMenuItem("Geom Predicates");
    JMenuItem vmesh = new JMenuItem("Mesh");
    JMenuItem vboundary = new JMenuItem("Function");
    JMenuItem vresults = new JMenuItem("Results");

    JMenu generate = new JMenu("Generate");
    JMenuItem gcmesh = new JMenuItem("Custom Mesh");
    JMenuItem gomesh = new JMenuItem("Ordinary Mesh...");
    JMenuItem gboundary = new JMenuItem("Function...");
    JMenuItem gsource = new JMenuItem("Source...");
    JMenuItem gresults = new JMenuItem("Results...");
    JMenuItem gresultswithboundary = new JMenuItem("Results + boundary...");

    JMenu options = new JMenu("Options");
    JMenuItem omesh = new JMenuItem("Mesh generator...");
    JMenuItem osolver = new JMenuItem("Solver...");

    JPanel center = new JPanel();
    JPanel space = new JPanel();
    JPanel south = new JPanel();

    JLabel edgeVisibilityLabel = new JLabel("EdgeVisibility: ");
    JTextField edgeVisibility = new JTextField("", 3);
    JLabel vertexVisibilityLabel = new JLabel("VertexVisibility: ");
    JTextField vertexVisibility = new JTextField("", 3);

    JCheckBox showVertex = new JCheckBox("", false);
    JCheckBox showEdge = new JCheckBox("", false);
    JCheckBox showGridNumbers = new JCheckBox("grid numbers", false);
    JCheckBox showGrid = new JCheckBox("grid", false);
    JCheckBox setStrict = new JCheckBox("Strict", false);

    JCheckBox statistics = new JCheckBox("Triangle statistics", false);
    JButton removeVertex = new JButton("Remove Last Vertex");
    JLabel angleLabel = new JLabel("Minimum angle: ");
    JTextField angle = new JTextField("30", 3);
    JButton refine = new JButton("Refine");

    Color dom = Color.black;
    Color grid = new Color(220, 220, 220);
    Color num = Color.pink;
    Color boxedge = Color.green;
    Color boxvertex = Color.green;
    Color boxback = new Color(220, 220, 220);
    Color from = Color.blue;
    Color vertexvisil = Color.red;
    Color triangle = Color.blue;
    Color circle = Color.red;
    Color low = Color.yellow;
    Color high = Color.black;

    JMultiNurbsInterface editNurbs;
    DcelGrid mesher, geomer, solver;
    double sourceX, sourceY, radius, smin, smax, rest;
    Drawable domain;
    JComponent box;
    JComponent query;
    JComponent boundaryGraphic;
    JComponent sourceGraphic;
    JPanel res;


    BoxMotionAdapter boxMotion = new BoxMotionAdapter();
    TriangleMotionAdapter triangleMotion = new TriangleMotionAdapter();
    InsertListener insertListener = new InsertListener();
    SourceListener sl;

    boolean existsMesh = false;
    boolean existsResults = false;
    boolean existsBoundary = false;
    boolean viewMesh = false;
    boolean source = true;
    String func = "";
    int k, l, I, J, includingTriangle;
    int xm = 0;
    int ym = 0;
    int ycoord = -1;
    File dataFile;


    class BoxMotionAdapter extends MouseMotionAdapter {
        public void mouseMoved(MouseEvent m) {
            Point mouse = (Point) m.getPoint();
            int x = (int) (mouse.getX() - xm);
            int y = (int) (ym - mouse.getY()) * ycoord;
            int i = geomer.getI(x);
            int j = geomer.getJ(y);
            if ((i != -1) && (j != -1)) {
                if ((i != I) || (j != J)) {
                    I = i;
                    J = j;
                    updateBox();
                }
            } else {
                try {
                    center.remove(box);
                    center.repaint();
                } catch (Exception e) {
                }
            }
        }
    }

    class TriangleMotionAdapter extends MouseMotionAdapter {
        public void mouseMoved(MouseEvent m) {
            Point mouse = (Point) m.getPoint();
            mouse.setLocation(mouse.getX() - xm, (ym - mouse.getY()) * ycoord);
            int ic = mesher.getIncludingTriangle(mouse);
            if (ic != includingTriangle) {
                includingTriangle = ic;
                updateTriangle();
            }
        }
    }

    class InsertListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            Point mouse = (Point) e.getPoint();
            mouse.setLocation(mouse.getX() - xm, (ym - mouse.getY()) * ycoord);
            mesher.insertVertex(mouse);
            center.setVisible(false);
            center.removeAll();
            if (statistics.isSelected()) {
                JComponent stati = mesher.getCompStatTriangle(xm, ym, low, high, ycoord);
                stati.addMouseListener(insertListener);
                center.add(stati);
            } else {
                domain = mesher.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                        showVertex.isSelected(), showGridNumbers.isSelected(),
                        showGrid.isSelected(), ycoord);
                domain.addMouseMotionListener(triangleMotion);
                domain.addMouseListener(insertListener);
                center.add(domain);
            }
            center.setVisible(true);
        }
    }


    public Heat() {
        super("SSHC");
        addWindowListener(new closeWindowListener());
        setSize(new Dimension(800, 500));
        init();
        setVisible(true);
    }

    private void init() {
        Container cp = getContentPane();
        cp.setVisible(false);
        JMenuBar mb = new JMenuBar();
        k = l = 20;

        setJMenuBar(mb);
        mb.add(file);
        file.add(neww);
        file.add(open);
        file.add(save);
        file.addSeparator();
        // file.add(about);
        // file.addSeparator();
        file.add(exit);

        mb.add(view);
        view.add(veditor);
        view.add(vgeom);
        vmesh.setEnabled(existsMesh);
        view.add(vmesh);
        // vboundary.setEnabled(existsBoundary);
        // view.add(vboundary);
        // vresults.setEnabled(existsResults);
        // view.add(vresults);

        mb.add(generate);
        generate.add(gcmesh);
        generate.add(gomesh);
        generate.addSeparator();
        generate.add(gboundary);
        generate.add(gsource);
        generate.addSeparator();
        gresults.setEnabled(existsMesh && existsBoundary);
        generate.add(gresults);
        gresultswithboundary.setEnabled(existsMesh && existsBoundary);
        generate.add(gresultswithboundary);

        // mb.add(options);
        // options.add(omesh);
        // options.add(osolver);

        addListeners();
        cp.setLayout(new BorderLayout());
        cp.add(space, BorderLayout.CENTER);
        space.setBackground(Color.white);
        space.setLayout(new OverlayLayout(space));
        center.setLayout(new OverlayLayout(center));
        center.setBackground(Color.white);
        cp.setVisible(true);
    }

    private void addListeners() {

        addCheckListeners();

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newNurbs();
            }
        };
        neww.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDialog();
            }
        };
        open.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveDialog();
            }
        };
        save.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        exit.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editor();
            }
        };
        veditor.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                geom();
            }
        };
        vgeom.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vvmesh();
            }
        };
        vmesh.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vvboundary();
            }
        };
        vboundary.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ggboundary();
            }
        };
        gboundary.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cmesh();
            }
        };
        gcmesh.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goomesh();
            }
        };
        gomesh.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gsolve();
            }
        };
        gresults.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gsource();
            }
        };
        gsource.addActionListener(al);


        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gsolvewithboundary();
            }
        };
        gresultswithboundary.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // t.setText(((JMenuItem)e.getSource()).getText());
            }
        };
        omesh.addActionListener(al);
    }

    private void addCheckListeners() {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                domain.chVertex();
                domain.repaint();
            }
        };
        showVertex.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                domain.chEdge();
                domain.repaint();
            }
        };
        showEdge.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                domain.chGrid();
                domain.repaint();
            }
        };
        showGrid.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                domain.chGridn();
                domain.repaint();
            }
        };
        showGridNumbers.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chStatistics();
            }
        };
        statistics.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mesher.removeLastVertex();
                center.setVisible(false);
                center.removeAll();
                if (statistics.isSelected()) {
                    JComponent stati = mesher.getCompStatTriangle(xm, ym, low, high, ycoord);
                    stati.addMouseListener(insertListener);
                    center.add(stati);
                } else {
                    domain = mesher.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                            showVertex.isSelected(), showGridNumbers.isSelected(),
                            showGrid.isSelected(), ycoord);
                    domain.addMouseMotionListener(triangleMotion);
                    domain.addMouseListener(insertListener);
                    center.add(domain);
                }
                center.setVisible(true);
            }
        };
        removeVertex.addActionListener(al);

        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refineMesh();
            }
        };
        refine.addActionListener(al);

        angle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER)
                    refineMesh();
            }
        });

        vertexVisibility.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER)
                    chVisibleVertex();
            }
        });

        edgeVisibility.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == evt.VK_ENTER)
                    chVisibleEdge();
            }
        });
    }

    private void newNurbs() {
        editNurbs = new JMultiNurbsInterface();
        space.setVisible(false);
        space.removeAll();
        space.setLayout(new BorderLayout());
        space.add(editNurbs.getCenter(), BorderLayout.CENTER);
        space.add(editNurbs.getSouth(), BorderLayout.SOUTH);
        space.setVisible(true);

    }

    private void openDialog() {
        JFileChooser fc = new JFileChooser();
        if (dataFile != null)
            fc.setCurrentDirectory(dataFile);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dataFile = fc.getSelectedFile();
            JMultiNurbs temp = null;
            try {
                java.io.ObjectInputStream objInStream =
                        new java.io.ObjectInputStream(new java.io.FileInputStream(dataFile));
                temp = (JMultiNurbs) objInStream.readObject();
            } catch (Exception e) {
                System.out.println("Hiba a loadNurbs()-ba: " + e.getMessage());
            }
            if (temp != null) {
                // System.out.println("done loading");
                // temp.addMouseClickListener();
                editNurbs = new JMultiNurbsInterface(temp);
                space.setVisible(false);
                space.removeAll();
                space.setLayout(new BorderLayout());
                space.add(editNurbs.getCenter(), BorderLayout.CENTER);
                space.add(editNurbs.getSouth(), BorderLayout.SOUTH);
                space.setVisible(true);
            }
            // System.out.println("Opening: " + dataFile.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void saveDialog() {
        JFileChooser fc = new JFileChooser();
        if (dataFile != null)
            fc.setCurrentDirectory(dataFile);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dataFile = fc.getSelectedFile();
            editNurbs.saveNurbs(dataFile);
            System.out.println("Saving: " + dataFile.getName() + ".");
        } else {
            System.out.println("Open command cancelled by user.");
        }
    }

    private void editor() {
        if (editNurbs != null) {
            existsMesh = false;
            vmesh.setEnabled(existsMesh);
            vresults.setEnabled(existsMesh);
            gresults.setEnabled(existsMesh && existsBoundary);
            gresultswithboundary.setEnabled(existsMesh && existsBoundary);
            space.setVisible(false);
            space.removeAll();
            space.setLayout(new BorderLayout());
            space.add(editNurbs.getCenter(), BorderLayout.CENTER);
            space.add(editNurbs.getSouth(), BorderLayout.SOUTH);
            space.setVisible(true);
        }
    }

    private void geom() {
        try {
            java.util.Vector v = ((JMultiNurbs) editNurbs.getCenter()).toPolyline();
            if (v.size() > 0) {
                geomer = new DcelGrid(v, k, l);
                // JComponent a = mesher.getCompDomain();
                space.setVisible(false);
                space.removeAll();
                space.setLayout(new BorderLayout());
                southSetGeom();
                centerSetGeom();
                space.add(center, BorderLayout.CENTER);
                space.add(south, BorderLayout.SOUTH);
                space.setVisible(true);
            }
        } catch (Exception e) {
        }
    }

    private void createOrdinaryMesh(double a) {
        try {
            java.util.Vector v = ((JMultiNurbs) editNurbs.getCenter()).toPolyline();
            if (v.size() > 0) {
                existsMesh = true;
                viewMesh = true;
                vmesh.setEnabled(true);
                mesher = new DcelGrid(v, k, l);
                mesher.delaunayRefine(a);
                gresults.setEnabled(existsMesh && existsBoundary);
                gresultswithboundary.setEnabled(existsMesh && existsBoundary);
                centerSetCmesh();
                southSetCmesh();
                south.remove(removeVertex);
                space.setVisible(false);
                space.removeAll();
                space.setLayout(new BorderLayout());
                space.add(center, BorderLayout.CENTER);
                space.add(south, BorderLayout.SOUTH);
                space.setVisible(true);

            }
        } catch (Exception e) {
            System.out.println("exp in ordinary mesh");
        }
    }

    private void cmesh() {
        try {
            java.util.Vector v = ((JMultiNurbs) editNurbs.getCenter()).toPolyline();
            if (v.size() > 0) {
                existsMesh = true;
                viewMesh = false;
                gresults.setEnabled(existsMesh && existsBoundary);
                gresultswithboundary.setEnabled(existsMesh && existsBoundary);
                vmesh.setEnabled(true);
                mesher = new DcelGrid(v, k, l);
                mesher.computeConstrainedDelaunay();
                // JComponent a = mesher.getCompDomain();
                centerSetCmesh();
                southSetCmesh();
                space.setVisible(false);
                space.removeAll();
                space.setLayout(new BorderLayout());
                space.add(center, BorderLayout.CENTER);
                space.add(south, BorderLayout.SOUTH);
                space.setVisible(true);

            }
        } catch (Exception e) {
            System.out.println("exp in custom mesh");
        }
    }

    private void centerSetCmesh() {
        if (statistics.isSelected()) {

            center.setVisible(false);
            center.removeAll();
            JComponent stat = mesher.getCompStatTriangle(xm, ym, low, high, ycoord);
            if (!viewMesh)
                stat.addMouseListener(insertListener);
            center.add(stat);
            center.repaint();
            center.setVisible(true);
        } else {
            center.setVisible(false);
            center.removeAll();
            domain = mesher.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                    showVertex.isSelected(), showGridNumbers.isSelected(), showGrid.isSelected(),
                    ycoord);
            domain.addMouseMotionListener(triangleMotion);
            if (!viewMesh)
                domain.addMouseListener(insertListener);
            center.add(domain);
            center.repaint();
            center.setVisible(true);
        }
    }

    private void southSetCmesh() {
        south.setVisible(false);
        south.removeAll();
        south.add(removeVertex);
        south.add(statistics);
        south.add(angleLabel);
        south.add(angle);
        south.add(refine);
        south.setVisible(true);
    }

    private void centerSetGeom() {
        center.setVisible(false);
        center.removeAll();
        domain = geomer.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                showVertex.isSelected(), showGridNumbers.isSelected(), showGrid.isSelected(),
                ycoord);
        domain.addMouseMotionListener(boxMotion);
        center.add(domain);
        center.setVisible(true);
    }

    private void southSetGeom() {
        south.setVisible(false);
        south.removeAll();
        south.add(showGrid);
        south.add(showGridNumbers);
        south.add(vertexVisibilityLabel);
        south.add(vertexVisibility);
        south.add(showVertex);
        south.add(edgeVisibilityLabel);
        south.add(edgeVisibility);
        south.add(showEdge);
        south.setVisible(true);
    }

    private void updateBox() {
        JComponent t = null;
        try {
            t = box;
            box = geomer.getCompVoxel(I, J, xm, ym, boxedge, boxvertex, boxback, ycoord);
        } catch (Exception e) {
        }
        if (center != null) {
            center.setVisible(false);
            if (t != null)
                center.remove(t);
            center.add(box, -1);
            center.repaint();
            center.setVisible(true);
        }
    }

    private void chVisibleVertex() {
        JComponent t = null;
        try {
            int i = (new Integer(vertexVisibility.getText())).intValue();
            t = query;
            query = geomer.getCompVertexVisil(i, xm, ym, from, vertexvisil, false, ycoord);
        } catch (Exception e) {
        }
        center.setVisible(false);
        if (t != null)
            center.remove(t);
        center.add(query, -1);
        center.repaint();
        center.setVisible(true);
    }

    private void chVisibleEdge() {
        JComponent t = null;
        try {
            int i = (new Integer(edgeVisibility.getText())).intValue();
            t = query;
            query = geomer.getCompEdgeVisil(i, xm, ym, from, vertexvisil, false, ycoord);
        } catch (Exception e) {
        }
        center.setVisible(false);
        if (t != null)
            center.remove(t);
        center.add(query, -1);
        center.repaint();
        center.setVisible(true);
    }

    private void updateTriangle() {
        JComponent t = null;
        try {
            t = query;
            if (includingTriangle > 0)
                query = mesher.getCompTriangle(xm, ym, triangle, circle, ycoord);
            else if (includingTriangle < 0)
                query = mesher.getCompHole(includingTriangle, xm, ym, triangle, ycoord);
            else
                query = null;
        } catch (Exception e) {
        }
        center.setVisible(false);
        if (t != null)
            center.remove(t);
        if (query != null)
            center.add(query, -1);
        center.repaint();
        center.setVisible(true);
    }

    private void chStatistics() {
        if (statistics.isSelected()) {
            center.setVisible(false);
            center.removeAll();
            JComponent stat = mesher.getCompStatTriangle(xm, ym, low, high, ycoord);
            if (!viewMesh)
                stat.addMouseListener(insertListener);
            center.add(stat);
            center.repaint();
            center.setVisible(true);
        } else {
            center.setVisible(false);
            center.removeAll();
            domain = mesher.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                    showVertex.isSelected(), showGridNumbers.isSelected(), showGrid.isSelected(),
                    ycoord);
            domain.addMouseMotionListener(triangleMotion);
            if (!viewMesh)
                domain.addMouseListener(insertListener);
            center.add(domain);
            center.repaint();
            center.setVisible(true);
        }
    }

    private void refineMesh() {
        try {
            double i = (new Double(angle.getText())).doubleValue();
            if ((i < 35.0) && (i > 0)) {
                try {
                    south.remove(removeVertex);
                } catch (Exception exp) {
                }
                mesher.refine(i);
                center.setVisible(false);
                center.removeAll();
                if (statistics.isSelected()) {
                    JComponent stati = mesher.getCompStatTriangle(xm, ym, low, high, ycoord);
                    stati.addMouseListener(insertListener);
                    center.add(stati);
                } else {
                    domain = mesher.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                            showVertex.isSelected(), showGridNumbers.isSelected(),
                            showGrid.isSelected(), ycoord);
                    domain.addMouseMotionListener(triangleMotion);
                    domain.addMouseListener(insertListener);
                    center.add(domain);
                }
                center.setVisible(true);
            }
        } catch (Exception exp) {
        }
    }

    private void vvmesh() {
        space.setVisible(false);
        space.removeAll();
        space.setLayout(new BorderLayout());
        south.setVisible(false);
        south.removeAll();
        south.add(statistics);
        south.setVisible(true);
        viewMesh = true;
        if (statistics.isSelected()) {
            center.setVisible(false);
            center.removeAll();
            JComponent stat = mesher.getCompStatTriangle(xm, ym, low, high, ycoord);
            center.add(stat);
            center.repaint();
            center.setVisible(true);
        } else {
            center.setVisible(false);
            center.removeAll();
            domain = mesher.getCompDomainGrid(xm, ym, dom, grid, num, showEdge.isSelected(),
                    showVertex.isSelected(), showGridNumbers.isSelected(), showGrid.isSelected(),
                    ycoord);
            domain.addMouseMotionListener(triangleMotion);
            center.add(domain);
            center.repaint();
            center.setVisible(true);
        }
        space.add(center, BorderLayout.CENTER);
        space.add(south, BorderLayout.SOUTH);
        space.setVisible(true);
    }

    private void vvboundary() {
        space.setVisible(false);
        space.removeAll();
        System.out.println("boundary graphic");
        System.out.println(space.getSize());
        space.add(boundaryGraphic);
        space.repaint();
        space.setVisible(true);
    }

    private void ggboundary() {
        final JLabel kxString = new JLabel("kx = ");
        final JTextField kx = new JTextField(ecuation.kx + "", 5);
        JPanel x = new JPanel();
        x.add(kxString);
        x.add(kx);
        final JLabel kyString = new JLabel("ky = ");
        final JTextField ky = new JTextField(ecuation.ky + "", 5);
        JPanel y = new JPanel();
        y.add(kyString);
        y.add(ky);
        final String funcString = "Boundary function: ";
        final JTextField boundaryFunction = new JTextField(func + "", 5);
        Object[] array = {x, y, funcString, boundaryFunction};

        final String btnString1 = "Show";
        final String btnString2 = "OK";
        final String btnString3 = "Cancel";
        Object[] options = {btnString2, btnString3, btnString1};

        JOptionPane pane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options, array[1]);
        JDialog dialog = pane.createDialog(this, "ecuation");
        dialog.show();
        Object selectedValue = pane.getValue();

        if (selectedValue == btnString1) {
            try {
                ecuation.kx = (new Double(kx.getText())).doubleValue();
                ecuation.ky = (new Double(ky.getText())).doubleValue();
                func = boundaryFunction.getText();
                TwoVariableFunction t = new TwoVariableFunction(func, "x", "y");
                boundaryGraphic = t.graphicReprezentation(space.getSize());
                ecuation.BoundaryFunction = t;
                existsBoundary = true;
                ecuation.sourceFlag = false;
                gresults.setEnabled(existsMesh && existsBoundary);
                gresultswithboundary.setEnabled(existsMesh && existsBoundary);
                space.setVisible(false);
                space.removeAll();
                existsBoundary = true;
                vboundary.setEnabled(true);
                space.add(boundaryGraphic);
                space.repaint();
                space.setVisible(true);
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
                ggboundary();
            }
        } else if (selectedValue == btnString2) {
            try {
                ecuation.kx = (new Double(kx.getText())).doubleValue();
                ecuation.ky = (new Double(ky.getText())).doubleValue();
                TwoVariableFunction t =
                        new TwoVariableFunction(boundaryFunction.getText(), "x", "y");
                if (t.syntacticallyCorrect()) {
                    func = boundaryFunction.getText();
                    existsBoundary = true;
                    ecuation.BoundaryFunction = t;
                    gresults.setEnabled(existsMesh && existsBoundary);
                    ecuation.sourceFlag = false;
                    gresultswithboundary.setEnabled(existsMesh && existsBoundary);
                } else
                    ggboundary();

            } catch (Exception exp) {
                ggboundary();
            }
        }
    }

    private void goomesh() {
        final String minAngleString = "Minimum angle for triangles: ";
        final JTextField minAngle = new JTextField("30", 5);
        Object[] array = {minAngleString, minAngle};

        final String btnString1 = "Generate mesh";
        final String btnString2 = "Cancel";
        Object[] options = {btnString1, btnString2};

        JOptionPane pane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options, array[1]);
        JDialog dialog = pane.createDialog(this, "mesh generator");
        dialog.show();
        Object selectedValue = pane.getValue();

        if (selectedValue == btnString1) {
            try {
                double a = (new Double(minAngle.getText())).doubleValue();
                if ((a > 0) && (a <= 35)) {
                    createOrdinaryMesh(a);
                } else
                    goomesh();
            } catch (Exception exp) {
                goomesh();
            }
        }
    }

    private void gsolve() {
        final JLabel text1Solve = new JLabel("Choose the system solution method");
        final JLabel text2Solve = new JLabel("  - iterative: fast, but approximative");
        final JLabel text3Solve = new JLabel("  - direct: slow");
        Object[] array = {text1Solve, text2Solve, text3Solve};

        final String btnString1 = "Iterative";
        final String btnString2 = "Direct";
        final String btnString3 = "Cancel";
        Object[] options = {btnString1, btnString2};

        JOptionPane pane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options, array[1]);
        JDialog dialog = pane.createDialog(this, "solver");
        dialog.show();
        Object selectedValue = pane.getValue();

        if (selectedValue == btnString1) {
            try {
                solver = mesher;
                res = new JPanel();
                res.setLayout(new OverlayLayout(res));
                res.setBackground(Color.white);
                solver.solve(res, LinearSystem.GAUSS_SEIDEL);
                existsResults = true;
                vresults.setEnabled(true);
                space.setVisible(false);
                space.removeAll();
                space.add(res);
                space.setVisible(true);
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        } else if (selectedValue == btnString2) {
            try {
                solver = mesher;
                res = new JPanel();
                res.setLayout(new OverlayLayout(res));
                res.setBackground(Color.white);
                solver.solve(res, LinearSystem.ELIM_GAUSS);
                vresults.setEnabled(true);
                existsResults = true;
                space.setVisible(false);
                space.removeAll();
                space.add(res);
                space.setVisible(true);
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        }
    }


    private void gsolvewithboundary() {
        final JLabel text1Solve = new JLabel("Choose the system solution method");
        final JLabel text2Solve = new JLabel("  - iterative: fast, but approximative");
        final JLabel text3Solve = new JLabel("  - direct: slow");
        Object[] array = {text1Solve, text2Solve, text3Solve};

        final String btnString1 = "Iterative";
        final String btnString2 = "Direct";
        final String btnString3 = "Cancel";
        Object[] options = {btnString1, btnString2};

        JOptionPane pane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options, array[1]);
        JDialog dialog = pane.createDialog(this, "solver");
        dialog.show();
        Object selectedValue = pane.getValue();

        if (selectedValue == btnString1) {
            try {
                solver = mesher;
                res = new JPanel();
                res.setLayout(new OverlayLayout(res));
                res.setBackground(Color.white);
                solver.solve(res, LinearSystem.GAUSS_SEIDEL);
                double oldmin = Triangle.min;
                double oldmax = Triangle.max;
                javax.swing.JComponent boundaryGraphic;
                if (ecuation.sourceFlag) {
                    boundaryGraphic = ecuation.source.getGraphics();
                    Triangle.min = ecuation.source.min;
                    Triangle.max = ecuation.source.max;
                    res.setBackground(ecuation.source.restColor());
                } else {
                    TwoVariableFunction t = ecuation.BoundaryFunction;
                    boundaryGraphic = t.graphicReprezentation(space.getSize());
                    Triangle.min = t.min;
                    Triangle.max = t.max;
                }
                res.add(boundaryGraphic);
                existsResults = true;
                vresults.setEnabled(true);
                space.setVisible(false);
                space.removeAll();
                space.add(res);
                space.setVisible(true);
                // Triangle.min = oldmin;
                // Triangle.max = oldmax;
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        } else if (selectedValue == btnString2) {
            try {
                TwoVariableFunction t = ecuation.BoundaryFunction;
                boundaryGraphic = t.graphicReprezentation(space.getSize());
                solver = mesher;
                res = new JPanel();
                res.setLayout(new OverlayLayout(res));
                res.setBackground(Color.white);
                solver.solve(res, LinearSystem.ELIM_GAUSS);
                res.add(boundaryGraphic);
                vresults.setEnabled(true);
                existsResults = true;
                space.setVisible(false);
                space.removeAll();
                space.add(res);
                space.setVisible(true);
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
            }
        }

    }

    class SourceListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            Point mouse = (Point) e.getPoint();
            mouse.setLocation(mouse.getX() - xm, (ym - mouse.getY()) * ycoord);
            sourceX = mouse.getX() - xm;
            sourceY = (ym - mouse.getY()) * ycoord;
            dialogSource();
        }
    }

    private void gsource() {
        space.setVisible(false);
        space.removeAll();
        DcelGrid temporary;
        space.setLayout(new OverlayLayout(space));
        try {
            java.util.Vector v = ((JMultiNurbs) editNurbs.getCenter()).toPolyline();
            if (v.size() > 0) {
                temporary = new DcelGrid(v, k, l);
                Color tip = Color.black;
                if (sourceGraphic != null) {
                    if (ecuation.source.restCloseToMin())
                        tip = Color.white;
                    else
                        tip = Color.black;
                }
                space.add(temporary.getCompDomainGrid(xm, ym, Color.green, grid, num,
                        showEdge.isSelected(), showVertex.isSelected(),
                        showGridNumbers.isSelected(), showGrid.isSelected(), ycoord));
            }
        } catch (Exception exp) {
        };
        if (sourceGraphic != null) {
            sourceGraphic = ecuation.source.getGraphics();
            space.setBackground(ecuation.source.restColor());
            space.add(sourceGraphic);
        } else
            space.setBackground(Color.white);
        sl = new SourceListener();
        space.addMouseListener(sl);
        space.setVisible(true);
    }

    private void dialogSource() {
        space.removeMouseListener(sl);

        final JLabel kxString = new JLabel("kx = ");
        final JTextField kx = new JTextField(ecuation.kx + "", 5);
        final JLabel kyString = new JLabel("ky = ");
        final JTextField ky = new JTextField(ecuation.ky + "", 5);
        JPanel kpanel = new JPanel();
        kpanel.add(kyString);
        kpanel.add(ky);
        kpanel.add(kxString);
        kpanel.add(kx);


        final String centerp = "x: " + sourceX + "  y: " + sourceY;
        final JLabel raString = new JLabel("radius = ");
        final JTextField ra = new JTextField(radius + "", 5);
        final JLabel reString = new JLabel("Outside of circle = ");
        final JTextField re = new JTextField(rest + "", 5);
        JPanel x = new JPanel();
        x.add(raString);
        x.add(ra);
        x.add(reString);
        x.add(re);
        final JLabel minString = new JLabel("end = ");
        final JTextField min = new JTextField(smin + "", 5);
        final JLabel maxString = new JLabel("middle = ");
        final JTextField max = new JTextField(smax + "", 5);
        JPanel y = new JPanel();
        y.add(minString);
        y.add(min);
        y.add(maxString);
        y.add(max);
        Object[] array = {centerp, x, y, kpanel};

        final String btnString1 = "Show";
        final String btnString2 = "OK";
        final String btnString3 = "Cancel";
        Object[] options = {btnString2, btnString3, btnString1};

        JOptionPane pane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options, array[1]);
        JDialog dialog = pane.createDialog(this, "source");
        dialog.show();
        Object selectedValue = pane.getValue();
        if (selectedValue == btnString1) {
            try {
                double kxvalue = (new Double(kx.getText())).doubleValue();
                double kyvalue = (new Double(ky.getText())).doubleValue();
                smin = (new Double(min.getText())).doubleValue();
                smax = (new Double(max.getText())).doubleValue();
                radius = (new Double(ra.getText())).doubleValue();
                rest = (new Double(re.getText())).doubleValue();
                ecuation.kx = kxvalue;
                ecuation.ky = kyvalue;
                source = true;
                existsBoundary = true;
                ecuation.sourceFlag = true;
                ecuation.source = new Source(sourceX, sourceY, smax, smin, radius, rest);
                space.setVisible(false);
                space.removeAll();
                sourceGraphic = ecuation.source.getGraphics();
                space.setBackground(ecuation.source.restColor());
                try {
                    java.util.Vector v = ((JMultiNurbs) editNurbs.getCenter()).toPolyline();
                    if (v.size() > 0) {
                        DcelGrid temporary = new DcelGrid(v, k, l);
                        Color tip = Color.black;
                        if (sourceGraphic != null) {
                            if (ecuation.source.restCloseToMin())
                                tip = Color.white;
                            else
                                tip = Color.black;
                        }
                        space.add(temporary.getCompDomainGrid(xm, ym, Color.green, grid, num,
                                showEdge.isSelected(), showVertex.isSelected(),
                                showGridNumbers.isSelected(), showGrid.isSelected(), ycoord));
                    }
                } catch (Exception exp) {
                };
                space.add(sourceGraphic);
                space.setVisible(true);
                gresults.setEnabled(existsMesh && existsBoundary);
                gresultswithboundary.setEnabled(existsMesh && existsBoundary);
            } catch (Exception exp) {
                System.out.println("show error");
                dialogSource();
            }
        } else if (selectedValue == btnString2) {
            try {
                double kxvalue = (new Double(kx.getText())).doubleValue();
                double kyvalue = (new Double(ky.getText())).doubleValue();
                smin = (new Double(min.getText())).doubleValue();
                smax = (new Double(max.getText())).doubleValue();
                radius = (new Double(ra.getText())).doubleValue();
                rest = (new Double(re.getText())).doubleValue();
                ecuation.kx = kxvalue;
                ecuation.ky = kyvalue;
                source = true;
                existsBoundary = true;
                ecuation.sourceFlag = true;
                ecuation.source = new Source(sourceX, sourceY, smax, smin, radius, rest);
                gresults.setEnabled(existsMesh && existsBoundary);
                gresultswithboundary.setEnabled(existsMesh && existsBoundary);
            } catch (Exception exp) {
                System.out.println("ok error");
                dialogSource();
            }
        }
    }

}

