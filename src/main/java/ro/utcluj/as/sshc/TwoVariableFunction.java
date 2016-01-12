package ro.utcluj.as.sshc;

import java.awt.Color;

import ro.utcluj.as.sshc.exception.DivisionByZeroException;
import ro.utcluj.as.sshc.exception.InvalidNumericValueException;
import ro.utcluj.as.sshc.exception.NullStringException;
import ro.utcluj.as.sshc.exception.SyntaxErrorException;

public class TwoVariableFunction extends Expresion {

    public static double min, max;
    private java.awt.Dimension frameSize;
    private double[][] valueMatrix;

    public TwoVariableFunction(String function, String firstVariable, String secondVariable) {
        super(function);
        variableSymbolList = new String[2];
        variableValueList = new double[2];
        numberOfVariables = 2;
        variableSymbolList[0] = firstVariable;
        variableSymbolList[1] = secondVariable;
    }

    public TwoVariableFunction(String function, String firstVariable, String secondVariable,
            java.awt.Dimension d) throws InvalidNumericValueException, SyntaxErrorException,
                    NullStringException, DivisionByZeroException {
        super(function);
        frameSize = d;
        variableSymbolList = new String[2];
        variableValueList = new double[2];
        numberOfVariables = 2;
        variableSymbolList[0] = firstVariable;
        variableSymbolList[1] = secondVariable;
        calculateValues(d);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double valueIn(double x, double y) {
        return valueMatrix[(int) x][(int) y];
    }

    public double evaluateIn(double x, double y) throws InvalidNumericValueException,
            SyntaxErrorException, NullStringException, DivisionByZeroException {
        variableValueList[0] = x;
        variableValueList[1] = y;
        return rezultat();
    }

    public void setDimension(java.awt.Dimension d) throws InvalidNumericValueException,
            SyntaxErrorException, NullStringException, DivisionByZeroException {
        frameSize = d;
        calculateValues(d);
    }

    public javax.swing.JComponent graphicReprezentation() {
        return new GraphicReprezentation(frameSize);
    }

    public javax.swing.JComponent graphicReprezentation(java.awt.Dimension d)
            throws InvalidNumericValueException, SyntaxErrorException, NullStringException,
            DivisionByZeroException {
        calculateValues(d);
        return new GraphicReprezentation(d);
    }

    private void calculateValues(java.awt.Dimension d) throws InvalidNumericValueException,
            SyntaxErrorException, NullStringException, DivisionByZeroException {
        variableValueList[0] = 0;
        variableValueList[1] = 0;
        min = rezultat();
        max = min;
        valueMatrix = new double[d.width][d.height];
        for (int i = 0; i < d.width; i++)
            for (int j = 0; j < d.height; j++) {
                variableValueList[0] = i;
                variableValueList[1] = j;
                valueMatrix[i][j] = rezultat();
                if (valueMatrix[i][j] < min)
                    min = valueMatrix[i][j];
                if (valueMatrix[i][j] > max)
                    max = valueMatrix[i][j];
            }
    }

    private class GraphicReprezentation extends javax.swing.JComponent {
        
        private static final long serialVersionUID = -5283937770866212927L;
        
        java.awt.Dimension d;

        public GraphicReprezentation(java.awt.Dimension d) {
            this.d = d;
        }

        public void paint(java.awt.Graphics g) {
            double piros;
            // System.out.println(d);
            for (int i = 0; i < d.width; i++)
                for (int j = 0; j < d.height; j++) {
                    piros = max != min ? Math.abs(valueMatrix[i][j] - min) / Math.abs(max - min)
                            : 0;
                    if ((piros >= 0) && (piros <= 1)) {
                        if (piros < 1 / 3.0) {
                            // piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
                            piros = 3 * piros;
                            Color high = Color.blue;
                            Color low = Color.black;
                            int red = (int) (low.getRed() + (high.getRed() - low.getRed()) * piros);
                            int green = (int) (low.getGreen()
                                    + (high.getGreen() - low.getGreen()) * piros);
                            int blue = (int) (low.getBlue()
                                    + (high.getBlue() - low.getBlue()) * piros);
                            g.setColor(new Color(red, green, blue));
                        } else {
                            if (piros < 2 / 3.0) {
                                piros = piros - 1 / 3.0;
                                // piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
                                piros = 3 * piros;
                                Color high = Color.red;
                                Color low = Color.blue;
                                int red = (int) (low.getRed()
                                        + (high.getRed() - low.getRed()) * piros);
                                int green = (int) (low.getGreen()
                                        + (high.getGreen() - low.getGreen()) * piros);
                                int blue = (int) (low.getBlue()
                                        + (high.getBlue() - low.getBlue()) * piros);
                                g.setColor(new Color(red, green, blue));
                            } else {
                                piros = piros - 2 / 3.0;
                                // piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
                                piros = 3 * piros;
                                Color high = Color.yellow;
                                Color low = Color.red;
                                int red = (int) (low.getRed()
                                        + (high.getRed() - low.getRed()) * piros);
                                int green = (int) (low.getGreen()
                                        + (high.getGreen() - low.getGreen()) * piros);
                                int blue = (int) (low.getBlue()
                                        + (high.getBlue() - low.getBlue()) * piros);
                                g.setColor(new Color(red, green, blue));
                            }
                        }
                        g.drawLine(i, j, i, j);
                    }
                }
        }
    }

}
