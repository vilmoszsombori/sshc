package ro.utcluj.as.sshc;

import ro.utcluj.as.sshc.exception.DivisionByZeroException;
import ro.utcluj.as.sshc.exception.InvalidNumericValueException;
import ro.utcluj.as.sshc.exception.NullStringException;
import ro.utcluj.as.sshc.exception.SyntaxErrorException;

public class Expresion {

    public static final char[] OPERATOR = {'+', '-', '*', '/', '^'};
    public static final String[] FUNCTION = {"sin", "cos", "tan", "asin", "acos", "atan", "log", "exp"};
    protected String expressionStr;
    protected double doubleValue;
    protected String[] variableSymbolList;
    protected double[] variableValueList;
    protected int numberOfVariables;
    private ExpressionTree head;

    /**
     * constructor vid
     */
    public Expresion() {
        expressionStr = "";
        doubleValue = 0;
    }

    /**
     * constructor cu un parametru String (expr) Expresion primeste expr
     */
    public Expresion(String expr) {
        expressionStr = expr.toLowerCase();
        doubleValue = 0;
    }

    /**
     * constructor cu expresia(String), lista variabilelor(String[]) lista valorilor
     * variabilelor(double[]) si numarul variabilelor
     */
    public Expresion(String expr, String[] variables, int length) {
        expressionStr = expr.toLowerCase();
        if (length > 0) {
            variableSymbolList = new String[length];
            variableValueList = new double[length];
            numberOfVariables = length;
            for (int i = 0; i < length; i++)
                variableSymbolList[i] = variables[i];
        }
        doubleValue = 0;
    }

    /**
     * metod� public, care returneaz� expresia obiectului instan�iat clasei expresion
     */
    public String whoAmI() {
        return expressionStr;
    }

    /**
     * metod� public, care returneaz� expresia obiectului instan�iat clasei expresion
     */
    public void set(String expr) {
        expressionStr = expr.toLowerCase();
    }

    /**
     * metoda public, care returneaza radacina arborelui binar de evaluare a expresiei
     */
    public ExpressionTree toTree() {
        head = createTree(expressionStr);
        return head;
    }

    /**
     * metoda public, care returneaza rezultatul expresiei (prima, fara argument, al doilea
     * utlizeaza arborele creat, al carui radacina trebuie dat ca parametru); arunca exceptiile: -
     * InvalidNumericValueException: in caz ca expresia nu este algebrica - SysntaxErrorException:
     * in caz ca nr. parantezelor deschise nu coincid cu nr. parantezelor inchise -
     * NullStringException: in caz ca expresia este vida - DivisionByZeroException
     */
    public double rezultat() throws InvalidNumericValueException, SyntaxErrorException,
            NullStringException, DivisionByZeroException {
        syntaxOk();
        return doubleValue = evaluate(expressionStr);
    }

    public double rezultat(ExpressionTree nod) throws InvalidNumericValueException, SyntaxErrorException,
            NullStringException, DivisionByZeroException {
        syntaxOk();
        return doubleValue = evaluate(nod);
    }

    public double rezultat(double[] variableList) throws InvalidNumericValueException,
            SyntaxErrorException, NullStringException, DivisionByZeroException {
        variableValueList = variableList;
        syntaxOk();
        return doubleValue = evaluate(expressionStr);
    }

    public boolean syntacticallyCorrect() {
        try {
            syntaxOk();
            return syntacticallyCorrect(expressionStr);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * metoda public, care verifica sintaxa expresiei arunca exceptiile: - SysntaxErrorException: in
     * caz ca nr. parantezelor deschise nu coincid cu nr. parantezelor inchise -
     * NullStringException: in caz ca expresia este vida
     */
    public void syntaxOk() throws SyntaxErrorException, NullStringException {
        String expr = expressionStr;
        if (expr.length() == 0)
            throw new NullStringException("You have to set an expresion (use set(String))");
        int iNrOfBrakets = 0;
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(')
                iNrOfBrakets++;
            if (expr.charAt(i) == ')')
                iNrOfBrakets--;
        }
        if (iNrOfBrakets != 0)
            throw new SyntaxErrorException("Syntax Error - No. of '(' != no. of ')' ");
    }

    /**
     * metoda createTree este private; creaza arborele binar de evaluare dintr-un String utlitzand
     * recursivitatea se apeleaza cu un obiect (Tree) care va fi radacina arborelui, si cu un String
     * -> expresia din care vrem sa construiasca arborele
     */
    private ExpressionTree createTree(String expr) {
        expr = expr.trim();
        if ((expr.startsWith("(")) && (expr.endsWith(")")))
            return createTree(expr.substring(1, expr.length() - 1));
        for (int i = 0; i < OPERATOR.length; i++) {
            for (int j = 0; j < expr.length(); j++) {
                if (expr.charAt(j) == '(')
                    j = endOfBraket(expr, j);
                if ((expr.charAt(j) == OPERATOR[i]) && (j > 0)) {
                    ExpressionTree nod = new ExpressionTree(OPERATOR[i]);
                    nod.left = createTree(expr.substring(0, j));
                    nod.right = createTree(expr.substring(j + 1));
                    return nod;
                }
            }
        }
        for (int i = 0; i < FUNCTION.length; i++) {
            if (expr.startsWith(FUNCTION[i])) {
                ExpressionTree nod = new ExpressionTree(FUNCTION[i]);
                nod.left = createTree(expr.substring(FUNCTION[i].length()));
                return nod;
            }
        }
        ExpressionTree nod = new ExpressionTree(expr);
        return nod;
    }

    /**
     * metoda evaluate este private; primeste ca parametru un String (expr) si evaluaza expr
     * utilizand recursivitatea: cauta primul operator inafara parantezelor in ordinea inversa a
     * precedentei daca gaseste -> apeleaza pe ea insusi cu tot ce inainte si cu tot ce e dupa
     * operator ca operand
     */
    private double evaluate(String expr)
            throws InvalidNumericValueException, DivisionByZeroException {

        expr = expr.trim();

        if ((expr.startsWith("(")) && (expr.endsWith(")")))
            return evaluate(expr.substring(1, expr.length() - 1));
        for (int i = 0; i < OPERATOR.length; i++) {
            for (int j = 0; j < expr.length(); j++) {
                if (expr.charAt(j) == '(')
                    j = endOfBraket(expr, j);
                if ((expr.charAt(j) == OPERATOR[i]) && (j > 0))
                    return operate(evaluate(expr.substring(0, j)), OPERATOR[i],
                            evaluate(expr.substring(j + 1)));
            }
        }

        for (int i = 0; i < FUNCTION.length; i++)
            if (expr.startsWith(FUNCTION[i]))
                return operate(FUNCTION[i], evaluate(expr.substring(FUNCTION[i].length())));

        for (int i = 0; i < numberOfVariables; i++)
            if (expr.startsWith(variableSymbolList[i]))
                return variableValueList[i];

        if (expr.equalsIgnoreCase("pi"))
            return 2 * Math.asin(1);
        if (expr.equalsIgnoreCase("e"))
            return Math.exp(1);
        try {
            Double r = new Double(expr);
            return r.doubleValue();
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Invalid numeric value: " + expr);
        }
    }

    private boolean syntacticallyCorrect(String expr) {

        expr = expr.trim();

        if ((expr.startsWith("(")) && (expr.endsWith(")")))
            return syntacticallyCorrect(expr.substring(1, expr.length() - 1));
        for (int i = 0; i < OPERATOR.length; i++) {
            for (int j = 0; j < expr.length(); j++) {
                if (expr.charAt(j) == '(')
                    j = endOfBraket(expr, j);
                if ((expr.charAt(j) == OPERATOR[i]) && (j > 0))
                    return syntacticallyCorrect(expr.substring(0, j))
                            && syntacticallyCorrect(expr.substring(j + 1));
            }
        }

        for (int i = 0; i < FUNCTION.length; i++)
            if (expr.startsWith(FUNCTION[i]))
                return syntacticallyCorrect(expr.substring(FUNCTION[i].length()));

        for (int i = 0; i < numberOfVariables; i++)
            if (expr.startsWith(variableSymbolList[i]))
                return true;

        if (expr.equalsIgnoreCase("pi"))
            return true;
        if (expr.equalsIgnoreCase("e"))
            return true;
        try {
            Double r = new Double(expr);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Evaluare statica in doi variabile "x" si "y"
     */
    public static double evaluateTwoVariableXY(String expr, double x, double y)
            throws InvalidNumericValueException, DivisionByZeroException {

        expr = expr.trim();

        if ((expr.startsWith("(")) && (expr.endsWith(")")))
            return evaluateTwoVariableXY(expr.substring(1, expr.length() - 1), x, y);
        for (int i = 0; i < OPERATOR.length; i++) {
            for (int j = 0; j < expr.length(); j++) {
                if (expr.charAt(j) == '(')
                    j = endOfBraket(expr, j);
                if ((expr.charAt(j) == OPERATOR[i]) && (j > 0))
                    return operate(evaluateTwoVariableXY(expr.substring(0, j), x, y), OPERATOR[i],
                            evaluateTwoVariableXY(expr.substring(j + 1), x, y));
            }
        }

        for (int i = 0; i < FUNCTION.length; i++)
            if (expr.startsWith(FUNCTION[i]))
                return operate(FUNCTION[i],
                        evaluateTwoVariableXY(expr.substring(FUNCTION[i].length()), x, y));

        if (expr.equalsIgnoreCase("pi"))
            return 2 * Math.asin(1);
        if (expr.equalsIgnoreCase("e"))
            return Math.exp(1);
        if (expr.equalsIgnoreCase("x"))
            return x;
        if (expr.equalsIgnoreCase("y"))
            return y;

        try {
            Double r = new Double(expr);
            return r.doubleValue();
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Invalid numeric value: " + expr);
        }
    }


    /**
     * Evaluare statica intr-o variabila "x"
     */
    public static double evaluateSingleVariableX(String expr, double x)
            throws InvalidNumericValueException, DivisionByZeroException {

        expr = expr.trim();

        if ((expr.startsWith("(")) && (expr.endsWith(")")))
            return evaluateSingleVariableX(expr.substring(1, expr.length() - 1), x);
        for (int i = 0; i < OPERATOR.length; i++) {
            for (int j = 0; j < expr.length(); j++) {
                if (expr.charAt(j) == '(')
                    j = endOfBraket(expr, j);
                if ((expr.charAt(j) == OPERATOR[i]) && (j > 0))
                    return operate(evaluateSingleVariableX(expr.substring(0, j), x), OPERATOR[i],
                            evaluateSingleVariableX(expr.substring(j + 1), x));
            }
        }

        for (int i = 0; i < FUNCTION.length; i++)
            if (expr.startsWith(FUNCTION[i]))
                return operate(FUNCTION[i],
                        evaluateSingleVariableX(expr.substring(FUNCTION[i].length()), x));

        if (expr.equalsIgnoreCase("pi"))
            return 2 * Math.asin(1);
        if (expr.equalsIgnoreCase("e"))
            return Math.exp(1);
        if (expr.equalsIgnoreCase("x"))
            return x;

        try {
            Double r = new Double(expr);
            return r.doubleValue();
        } catch (NumberFormatException e) {
            throw new InvalidNumericValueException("Invalid numeric value: " + expr);
        }
    }

    /**
     * metoda evaluate cu un parametru de tip Tree evalueaza arborele al carui radacina primeste ca
     * parametru
     */
    private double evaluate(ExpressionTree nod) throws InvalidNumericValueException, DivisionByZeroException {
        if (nod.is("pi"))
            return 2 * Math.asin(1);
        if (nod.is("e"))
            return Math.exp(1);
        if ((nod.left == null) && (nod.right == null))
            return nod.doubleValue();
        if (nod.right == null)
            return operate(nod.stringValue(), evaluate(nod.left));
        return operate(evaluate(nod.left), nod.charValue(), evaluate(nod.right));
    }

    /**
     * metoda endOfBraket este private; primeste ca parametru un String (expr) si un int
     * (firstBraket) se apeleaza cu un String, care contine paranteze rotunde, si cu indicele primei
     * paranteze deschise metoda returneza indicele sfarsitului parantezei, daca exista daca nu
     * gaseste, returneza lungimea Stringului primit ca parametru
     */
    private static int endOfBraket(String expr, int firstBraket) {
        int iNrOfBraket = 1, i;
        for (i = firstBraket + 1; i < expr.length(); i++) {
            if (expr.charAt(i) == '(')
                iNrOfBraket++;
            if (expr.charAt(i) == ')')
                iNrOfBraket--;
            if (iNrOfBraket == 0)
                break;
        }
        if (iNrOfBraket == 0)
            return i;
        return expr.length() - 1;
    }

    /**
     * metoda operate este private; primeste ca parametru 2 valori double (dValue1, dValue2), un
     * char (operator) si returneaza un double in functie de parametrul operator care poate sa aiba
     * valoare din sirul final OPERATOR (+,-,*,/,^); daca operatorul este '/' si dValue2 == 0 (ar
     * trebui sa returneze ceva pe 0) este aruncat o exceptie: DivisionByZeroException
     */
    private static double operate(double dValue1, char operator, double dValue2)
            throws DivisionByZeroException {
        switch (operator) {
            case '+':
                return (dValue1 + dValue2);
            case '-':
                return (dValue1 - dValue2);
            case '*':
                return (dValue1 * dValue2);
            case '/':
                if (dValue2 == 0)
                    throw new DivisionByZeroException("Division by zero");
                return (dValue1 / dValue2);
            case '^':
                return Math.pow(dValue1, dValue2);
        }
        return 0;
    }

    /**
     * metoda operate este private; primeste ca parametru un String (function) si un double (dValue)
     * si returneaza un double in functie de parametrul function care poate sa aiba valoare din
     * sirul final FUNCTION (sin, cos, tan, exp, log, atan, asin, acos);
     */
    private static double operate(String function, double dValue) {
        if (function.equalsIgnoreCase("sin"))
            return Math.sin(dValue);
        if (function.equalsIgnoreCase("cos"))
            return Math.cos(dValue);
        if (function.equalsIgnoreCase("tan"))
            return Math.tan(dValue);
        if (function.equalsIgnoreCase("exp"))
            return Math.exp(dValue);
        if (function.equalsIgnoreCase("log"))
            return Math.log(dValue);
        if (function.equalsIgnoreCase("asin"))
            return Math.asin(dValue);
        if (function.equalsIgnoreCase("acos"))
            return Math.acos(dValue);
        if (function.equalsIgnoreCase("atan"))
            return Math.atan(dValue);
        return 0;
    }

}
