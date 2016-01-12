package ro.utcluj.as.sshc;

import java.awt.Color;

public class ecuation {

    public static boolean sourceFlag;
    public static double kx = 1;
    public static double ky = 1;

    public static TwoVariableFunction BoundaryFunction;
    public static Source source;


}

class Source {

    public static double min, max;
    double xc, yc, middle, end, radius, rest;

    public Source(double x, double y, double middle, double end, double radius, double rest) {
        this.xc = x;
        this.yc = y;
        this.middle = middle;
        this.end = end;
        this.radius = radius;
        this.rest = rest;
        max = Math.max(Math.max(middle, end), rest);
        min = Math.min(Math.min(middle, end), rest);
    }

    public double valueIn(double x, double y) {
        double dist = Math.sqrt((x - xc) * (x - xc) + (y - yc) * (y - yc));
        return (dist <= radius) ? middle + ((dist / radius) * (end - middle)) : rest;
    }

    class GSource extends javax.swing.JComponent {
        double xc, yc, middle, end, radius, rest;

        public GSource(double x, double y, double middle, double end, double radius, double rest) {
            this.rest = rest;
            this.xc = x;
            this.yc = y;
            this.middle = middle;
            this.end = end;
            this.radius = radius;
        }

        public void paint(java.awt.Graphics g) {
            double max = Math.max(Math.max(middle, end), rest);
            double min = Math.min(Math.min(middle, end), rest);
            System.out.println("max: " + max + " min: " + min);
            System.out.println("end: " + end + " middle: " + middle);
            for (int i = (int) (xc - radius); i < (int) (xc + radius); i++)
                for (int j = (int) (yc - radius); j < (int) (yc + radius); j++) {
                    double val = valueIn(i, j);
                    double piros;
                    if (max == min)
                        piros = 0;
                    else
                        piros = (val - min) / (max - min);
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
                    } else {
                        System.out.println("bad color");
                    }
                }
        }
    }

    public java.awt.Color restColor() {
        double max = Math.max(Math.max(middle, end), rest);
        double min = Math.min(Math.min(middle, end), rest);
        double piros;
        if (max == min)
            piros = 0;
        else
            piros = (rest - min) / (max - min);
        if ((piros >= 0) && (piros <= 1)) {
            if (piros < 1 / 3.0) {
                // piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
                piros = 3 * piros;
                Color high = Color.blue;
                Color low = Color.black;
                int red = (int) (low.getRed() + (high.getRed() - low.getRed()) * piros);
                int green = (int) (low.getGreen() + (high.getGreen() - low.getGreen()) * piros);
                int blue = (int) (low.getBlue() + (high.getBlue() - low.getBlue()) * piros);
                return new Color(red, green, blue);
            } else {
                if (piros < 2 / 3.0) {
                    piros = piros - 1 / 3.0;
                    // piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
                    piros = 3 * piros;
                    Color high = Color.red;
                    Color low = Color.blue;
                    int red = (int) (low.getRed() + (high.getRed() - low.getRed()) * piros);
                    int green = (int) (low.getGreen() + (high.getGreen() - low.getGreen()) * piros);
                    int blue = (int) (low.getBlue() + (high.getBlue() - low.getBlue()) * piros);
                    return new Color(red, green, blue);
                } else {
                    piros = piros - 2 / 3.0;
                    // piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
                    piros = 3 * piros;
                    Color high = Color.yellow;
                    Color low = Color.red;
                    int red = (int) (low.getRed() + (high.getRed() - low.getRed()) * piros);
                    int green = (int) (low.getGreen() + (high.getGreen() - low.getGreen()) * piros);
                    int blue = (int) (low.getBlue() + (high.getBlue() - low.getBlue()) * piros);
                    return new Color(red, green, blue);
                }
            }
        } else
            return null;
    }

    public boolean restCloseToMin() {
        double max = Math.max(Math.max(middle, end), rest);
        double min = Math.min(Math.min(middle, end), rest);
        double piros;
        if (max == min)
            piros = 0;
        else
            piros = (rest - min) / (max - min);
        if (piros < 0.5) {
            System.out.println("restclose");
            return true;
        } else {
            System.out.println("restnot close");
            return true;
        }
    }

    public javax.swing.JComponent getGraphics() {
        return new GSource(xc, yc, middle, end, radius, rest);
    }
}
