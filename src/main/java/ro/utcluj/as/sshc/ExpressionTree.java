package ro.utcluj.as.sshc;

import ro.utcluj.as.sshc.exception.InvalidNumericValueException;

class ExpressionTree {

    public String info;
    public ExpressionTree left;
    public ExpressionTree right;

    /**
     * constructor vid -> Info primeste String-ul ""
     */
    public ExpressionTree() {
        info = "";
        left = right = null;
    }

    /**
     * constructor cu un param. String -> Info primeste valoarea parametrului
     */
    public ExpressionTree(String expr) {
        info = expr;
        left = right = null;
    }

    /**
     * constructor cu un param. char -> Info primeste valoarea parametrului convertit in String
     */
    public ExpressionTree(char expr) {
        Character c = new Character(expr);
        info = c.toString();
    }

    /**
     * metoda public, care returneaza valoarea in double a field-ului Info arunca exceptia
     * InvalidNumericValueException in caz ca Info nu poate fi convertit in double daca Info este
     * String-ul vid (""), returneza 0
     */
    public double doubleValue() throws InvalidNumericValueException {
        try {
            Double r = new Double(info);
            return r.doubleValue();
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Invalid numeric value: " + info);
        }
    }

    /**
     * metoda public, care returneaza valoarea lui Info in String
     */
    public String stringValue() {
        return info;
    }

    /**
     * metoda public, care returneaza primul caracter din Info
     */
    public char charValue() {
        return info.charAt(0);
    }

    /**
     * metoda public, care returneaza true daca parametrul primit este egal cu atributul Info si
     * false in caz contrar
     */
    public boolean is(String expr) {
        if (expr.equalsIgnoreCase(info))
            return true;
        return false;
    }

}
