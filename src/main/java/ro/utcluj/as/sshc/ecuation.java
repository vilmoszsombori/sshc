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
		this.xc=x;
		this.yc=y;
		this.middle=middle;
		this.end=end;
		this.radius=radius;
		this.rest = rest;
		max = Math.max(Math.max(middle, end),rest);
		min = Math.min(Math.min(middle, end),rest);
	}
	
	public double valueIn(double x, double y) {
		double dist = Math.sqrt((x-xc)*(x-xc)+(y-yc)*(y-yc));
		return (dist<=radius) ? middle+((dist/radius)*(end-middle)) : rest;
	}
	
	class GSource extends javax.swing.JComponent{
		double xc, yc, middle, end, radius, rest;

		public GSource(double x, double y, double middle, double end, double radius, double rest) {
			this.rest = rest;
			this.xc=x;
			this.yc=y;
			this.middle=middle;
			this.end=end;
			this.radius=radius;
		}
		
		public void paint(java.awt.Graphics g) {
			double max = Math.max(Math.max(middle, end),rest);
			double min = Math.min(Math.min(middle, end),rest);
			System.out.println("max: "+max+" min: "+min);
			System.out.println("end: "+end+" middle: "+middle);
			for (int i=(int)(xc-radius); i<(int)(xc+radius); i++)
				for (int j=(int)(yc-radius); j<(int)(yc+radius); j++ ) {
					double val = valueIn(i,j);
					double piros;
					if (max == min) piros=0;
					else piros = (val-min)/(max-min);
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
						g.drawLine(i,j,i,j); 		
					}else {
						System.out.println("bad color");
					}
				}
		}
	}
	
	public java.awt.Color restColor() {
			double max = Math.max(Math.max(middle, end),rest);
			double min = Math.min(Math.min(middle, end),rest);
					double piros;
					if (max == min) piros=0;
					else piros = (rest-min)/(max-min);
					if ((piros>=0)&&(piros<=1)) {
							if (piros<1/3.0) {
//								piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
								piros=3*piros;
								Color high = Color. blue;
								Color low = Color.black;
								int red = (int)(low.getRed()+(high.getRed() - low.getRed())*piros);
								int green = (int)(low.getGreen()+(high.getGreen() - low.getGreen())*piros);
								int blue = (int)(low.getBlue()+(high.getBlue() - low.getBlue())*piros);
								 return new Color(red, green, blue);
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
								 return new Color(red, green, blue);
								}else {
									piros = piros - 2/3.0;
//									piros = 1-(Math.cos(Math.PI*(piros*3-1/2))+1)/2;
									piros=3*piros;
									Color high = Color.yellow;
									Color low = Color.red;
									int red = (int)(low.getRed()+(high.getRed() - low.getRed())*piros);
									int green = (int)(low.getGreen()+(high.getGreen() - low.getGreen())*piros);
									int blue = (int)(low.getBlue()+(high.getBlue() - low.getBlue())*piros);
								 return new Color(red, green, blue);
								}
							}
					} else return null;
	}
	
	public boolean restCloseToMin() {
			double max = Math.max(Math.max(middle, end),rest);
			double min = Math.min(Math.min(middle, end),rest);
			double piros;
			if (max == min) piros=0;
			else piros = (rest-min)/(max-min);
			if (piros<0.5)  {
				System.out.println("restclose");
				return true;
			}
			else  {
				System.out.println("restnot close");
				return true;
			}
	}
	
	public javax.swing.JComponent getGraphics() {
		return new GSource(xc,yc,middle,end,radius,rest);
	}
}


class TwoVariableFunction extends expresion {

	public static double min, max;
	private java.awt.Dimension frameSize;
	private double[][] valueMatrix; 

	public TwoVariableFunction(String function, String firstVariable, String secondVariable) {
		super(function);
		VariableSymbolList = new String[2];
		VariableValueList  = new double[2]; 
		numberOfVariables = 2;
		VariableSymbolList[0] = firstVariable;			
		VariableSymbolList[1] = secondVariable;
	}
	
	public TwoVariableFunction(String function, String firstVariable, String secondVariable, java.awt.Dimension d)  throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		super(function);
		frameSize = d;
		VariableSymbolList = new String[2];
		VariableValueList  = new double[2]; 
		numberOfVariables = 2;
		VariableSymbolList[0] = firstVariable;			
		VariableSymbolList[1] = secondVariable;
		calculateValues(d);
	}
	
	public double getMin() {
		return min;
	}
	
	public double getMax() {
		return max;
	}
	
	public double valueIn(double x, double y) {
		return valueMatrix[(int)x][(int)y];
	}
	
	public double evaluateIn(double x, double y)  throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		VariableValueList[0] = x;
		VariableValueList[1] = y;
		return rezultat();
	}

	public void setDimension(java.awt.Dimension d)  throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		frameSize = d;
		calculateValues(d);
	}
	 
	public javax.swing.JComponent graphicReprezentation() {
		return new GraphicReprezentation(frameSize) ;
	}
	
	public javax.swing.JComponent graphicReprezentation(java.awt.Dimension d)  throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		calculateValues(d);
		return new GraphicReprezentation(d) ;
	}

	private void calculateValues(java.awt.Dimension d)  throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		VariableValueList[0] = 0;
		VariableValueList[1] = 0;
		min = rezultat();
		max = min;
		valueMatrix = new double[d.width][d.height];
		for(int i=0; i<d.width; i++)
			for(int j=0; j<d.height; j++) {
				VariableValueList[0] = i;
				VariableValueList[1] = j;
				valueMatrix[i][j] = rezultat();
				if (valueMatrix[i][j]<min) min = valueMatrix[i][j];
				if (valueMatrix[i][j]>max) max = valueMatrix[i][j];
			}
	}

	class GraphicReprezentation extends javax.swing.JComponent {
		java.awt.Dimension d;
	
		public GraphicReprezentation(java.awt.Dimension d) {
			this.d=d;
		}
		
		public void paint(java.awt.Graphics g) {
			double piros;
//			System.out.println(d);
			for(int i=0; i<d.width; i++)
				for(int j=0; j<d.height; j++) {
					piros = max!=min ? Math.abs(valueMatrix[i][j]-min)/Math.abs(max-min) : 0;
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
						g.drawLine(i,j,i,j); 		
					}
				}	
		}
	}

}



class expresion {
   
	public static final char[] OPERATOR = {'+','-','*','/','^'};
	public static final String[] FUNCTION = {"sin","cos","tan","asin","acos","atan","log","exp"}; 
	protected String Expresion;
	protected double DoubleValue;
	protected String[] VariableSymbolList;
	protected double[] VariableValueList; 
	protected int numberOfVariables;	
	private Tree HeadOfTree;
	
   /**
   * constructor vid
   */
   public expresion() {
   		Expresion = "";
   		DoubleValue = 0;
   }
   
   /**
   * constructor cu un parametru String (expr)
   * Expresion primeste expr
   */
   public expresion(String expr) {
   		Expresion = expr.toLowerCase();
   		DoubleValue = 0;
   }

   /**
   * constructor cu expresia(String), lista variabilelor(String[])
   * lista valorilor variabilelor(double[]) si numarul variabilelor
   */
   public expresion(String expr, String[] variables, int length) {
   		Expresion = expr.toLowerCase();
		if (length>0) {
			VariableSymbolList = new String[length];
			VariableValueList  = new double[length];
			numberOfVariables = length;
			for(int i=0; i<length; i++) VariableSymbolList[i] = variables[i];
		}
   		DoubleValue = 0;
   }

   /**
   * metod� public, care returneaz� expresia obiectului instan�iat clasei expresion
   */
   public String whoAmI() {
   		return Expresion;
   }

	/**
   * metod� public, care returneaz� expresia obiectului instan�iat clasei expresion
   */
   public void set(String expr) {
   		Expresion = expr.toLowerCase();
   }

   /**
   * metoda public, care returneaza radacina arborelui binar de evaluare a expresiei
   */
   public Tree toTree() {
		HeadOfTree = createTree(Expresion);
		return HeadOfTree;
   }
 
   /**
   * metoda public, care returneaza rezultatul expresiei (prima, fara argument, al doilea utlizeaza 
   * arborele creat, al carui radacina trebuie dat ca parametru);
   * arunca exceptiile:
   *		- InvalidNumericValueException: in caz ca expresia nu este algebrica
   * 	- SysntaxErrorException: in caz ca nr. parantezelor deschise nu coincid cu nr. parantezelor inchise
   *		- NullStringException: in caz ca expresia este vida
   *		- DivisionByZeroException
   */
   public double rezultat() throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		syntaxOk(); 			
		return DoubleValue = evaluate(Expresion);
   }
      
   public double rezultat(Tree nod) throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		syntaxOk(); 			
		return DoubleValue = evaluate(nod);	   
   }
   
   public double rezultat(double[] variableList) throws InvalidNumericValueException, SyntaxErrorException, NullStringException, DivisionByZeroException {
		VariableValueList = variableList;
		syntaxOk(); 			
		return DoubleValue = evaluate(Expresion);
   }
   
	public boolean syntacticallyCorrect() {
		try { syntaxOk(); return syntacticallyCorrect(Expresion); }
		catch(Exception e) { return false; }		
    }

   
   /**
   * metoda public, care verifica sintaxa expresiei
   * arunca exceptiile:
   * 	- SysntaxErrorException: in caz ca nr. parantezelor deschise nu coincid cu nr. parantezelor inchise
   *		- NullStringException: in caz ca expresia este vida
   */
   public void syntaxOk() throws SyntaxErrorException, NullStringException {
		String expr = Expresion;
		if (expr.length() == 0) throw new NullStringException("You have to set an expresion (use set(String))") ;
		int iNrOfBrakets = 0;
			for(int i = 0; i < expr.length(); i++) {
   				if (expr.charAt(i) == '(') iNrOfBrakets++;
   				if (expr.charAt(i) == ')') iNrOfBrakets--;
   			}
   			if (iNrOfBrakets != 0) throw new SyntaxErrorException("Syntax Error - No. of '(' != no. of ')' ");		
   }

   /**
   * metoda createTree este private; creaza arborele binar de evaluare dintr-un String utlitzand recursivitatea
   * se apeleaza cu un obiect (Tree) care va fi radacina arborelui, si cu un String -> expresia
   * din care vrem sa construiasca arborele
   */
   private Tree createTree(String expr) {
   		expr = expr.trim();
   		if ((expr.startsWith("(")) && (expr.endsWith(")"))) return createTree(expr.substring(1,expr.length()-1));
   		for(int i = 0; i < OPERATOR.length; i++) {
   			for(int j = 0; j < expr.length() ; j++) {
   				if (expr.charAt(j) == '(') j = endOfBraket(expr, j);
   				if ((expr.charAt(j) == OPERATOR[i]) && (j > 0)) {
   					Tree nod = new Tree(OPERATOR[i]); 
   					nod.Left  = createTree(expr.substring(0,j));
   					nod.Right = createTree(expr.substring(j+1));
   					return nod;
   				}
   			}
   		}	
   		for(int i = 0; i < FUNCTION.length; i++) {
				if (expr.startsWith(FUNCTION[i])) {
					Tree nod = new Tree(FUNCTION[i]);
					nod.Left = createTree(expr.substring(FUNCTION[i].length()));
					return nod;
				}
			}
   		Tree nod = new Tree(expr);
   		return nod;
   }
       
   /**
   * metoda evaluate este private; primeste ca parametru un String (expr) si evaluaza expr
   * utilizand recursivitatea: cauta primul operator inafara parantezelor in ordinea inversa
   * a precedentei daca gaseste -> apeleaza pe ea insusi cu tot ce inainte si cu tot ce e dupa 
   * operator ca operand
   */
   private double evaluate(String expr) throws InvalidNumericValueException, DivisionByZeroException {

		expr = expr.trim();

   		if ((expr.startsWith("(")) && (expr.endsWith(")"))) return evaluate(expr.substring(1,expr.length()-1));
   		for(int i = 0; i < OPERATOR.length; i++) {
   			for(int j = 0; j < expr.length() ; j++) {
   					if (expr.charAt(j) == '(') 
   						j = endOfBraket(expr, j);
   					if ((expr.charAt(j) == OPERATOR[i]) && (j > 0)) 
   						return operate(evaluate(expr.substring(0,j)), OPERATOR[i], evaluate(expr.substring(j+1)));
   			}
   		}	

   		for(int i=0; i<FUNCTION.length; i++)
   			if (expr.startsWith(FUNCTION[i])) return operate(FUNCTION[i], evaluate(expr.substring(FUNCTION[i].length())));

		for(int i=0; i<numberOfVariables; i++)
			if (expr.startsWith(VariableSymbolList[i])) return VariableValueList[i];
			
   		if (expr.equalsIgnoreCase("pi")) return 2*Math.asin(1);
		if (expr.equalsIgnoreCase("e"))  return Math.exp(1);
   		try { 
   			Double r = new Double(expr);	 
   			return r.doubleValue();
			}
   		catch(NumberFormatException e) { throw new InvalidNumericValueException("Invalid numeric value: " + expr); }
   }

   private boolean syntacticallyCorrect(String expr) {

		expr = expr.trim();

   		if ((expr.startsWith("(")) && (expr.endsWith(")"))) return syntacticallyCorrect(expr.substring(1,expr.length()-1));
   		for(int i = 0; i < OPERATOR.length; i++) {
   			for(int j = 0; j < expr.length() ; j++) {
   					if (expr.charAt(j) == '(') 
   						j = endOfBraket(expr, j);
   					if ((expr.charAt(j) == OPERATOR[i]) && (j > 0)) 
   						return syntacticallyCorrect(expr.substring(0,j)) && syntacticallyCorrect(expr.substring(j+1));
   			}
   		}	

   		for(int i=0; i<FUNCTION.length; i++)
   			if (expr.startsWith(FUNCTION[i])) return syntacticallyCorrect(expr.substring(FUNCTION[i].length()));

		for(int i=0; i<numberOfVariables; i++)
			if (expr.startsWith(VariableSymbolList[i])) return true;
			
   		if (expr.equalsIgnoreCase("pi")) return true;
		if (expr.equalsIgnoreCase("e"))  return true;
   		try { 
   			Double r = new Double(expr);	 
   			return true;
		}
   		catch(NumberFormatException e) { return false; }
   }

   
	/**
	* Evaluare statica in doi variabile "x" si "y"
	*/
	public static double evaluateTwoVariableXY(String expr, double x, double y) throws InvalidNumericValueException, DivisionByZeroException {

	   expr = expr.trim();

	   if ((expr.startsWith("(")) && (expr.endsWith(")"))) return evaluateTwoVariableXY(expr.substring(1,expr.length()-1),x,y);
	   for(int i = 0; i < OPERATOR.length; i++) {
	   	for(int j = 0; j < expr.length() ; j++) {
			if (expr.charAt(j) == '(') 
				j = endOfBraket(expr, j);
			if ((expr.charAt(j) == OPERATOR[i]) && (j > 0)) 
				return operate(evaluateTwoVariableXY(expr.substring(0,j),x,y), OPERATOR[i], evaluateTwoVariableXY(expr.substring(j+1),x,y));
	   	}
	   }	

	   for(int i=0; i<FUNCTION.length; i++)
   		if (expr.startsWith(FUNCTION[i])) return operate(FUNCTION[i], evaluateTwoVariableXY(expr.substring(FUNCTION[i].length()),x,y));
	
	   if (expr.equalsIgnoreCase("pi")) return 2*Math.asin(1);
	   if (expr.equalsIgnoreCase("e"))  return Math.exp(1);
	   if (expr.equalsIgnoreCase("x")) return x;
	   if (expr.equalsIgnoreCase("y")) return y;
	   
	   try { 
		   	Double r = new Double(expr);	 
		   	return r.doubleValue();
	   }
	   catch(NumberFormatException e) { throw new InvalidNumericValueException("Invalid numeric value: " + expr); }
	}


	/**
	* Evaluare statica intr-o variabila "x"
	*/
	public static double evaluateSingleVariableX(String expr, double x) throws InvalidNumericValueException, DivisionByZeroException {

	   expr = expr.trim();

	   if ((expr.startsWith("(")) && (expr.endsWith(")"))) return evaluateSingleVariableX(expr.substring(1,expr.length()-1),x);
	   for(int i = 0; i < OPERATOR.length; i++) {
	   	for(int j = 0; j < expr.length() ; j++) {
			if (expr.charAt(j) == '(') 
				j = endOfBraket(expr, j);
			if ((expr.charAt(j) == OPERATOR[i]) && (j > 0)) 
				return operate(evaluateSingleVariableX(expr.substring(0,j),x), OPERATOR[i], evaluateSingleVariableX(expr.substring(j+1),x));
	   	}
	   }	

	   for(int i=0; i<FUNCTION.length; i++)
		if (expr.startsWith(FUNCTION[i])) return operate(FUNCTION[i], evaluateSingleVariableX(expr.substring(FUNCTION[i].length()),x));
	
	   if (expr.equalsIgnoreCase("pi")) return 2*Math.asin(1);
	   if (expr.equalsIgnoreCase("e"))  return Math.exp(1);
	   if (expr.equalsIgnoreCase("x")) return x;
	   
	   try { 
		   	Double r = new Double(expr);	 
		   	return r.doubleValue();
	   }
	   catch(NumberFormatException e) { throw new InvalidNumericValueException("Invalid numeric value: " + expr); }
	}
       
   /**
   * metoda evaluate cu un parametru de tip Tree evalueaza arborele al carui radacina primeste ca 
   * parametru
   */
   private double evaluate(Tree nod) throws InvalidNumericValueException, DivisionByZeroException {
   		if (nod.is("pi")) return 2*Math.asin(1);
			if (nod.is("e"))  return Math.exp(1);	
   		if ((nod.Left == null) && (nod.Right == null)) return nod.doubleValue();  		
			if (nod.Right == null) return operate(nod.stringValue(),evaluate(nod.Left));
			return operate(evaluate(nod.Left),nod.charValue(),evaluate(nod.Right));
   }
	  
   /**
   * metoda endOfBraket este private; primeste ca parametru un String (expr) si un int (firstBraket)
   * se apeleaza cu un String, care contine paranteze rotunde, si cu indicele primei paranteze deschise
   * metoda returneza indicele sfarsitului parantezei, daca exista
   * daca nu gaseste, returneza lungimea Stringului primit ca parametru
   */
   private static int endOfBraket(String expr, int firstBraket) {
  			int iNrOfBraket = 1, i;
			for(i = firstBraket + 1; i<expr.length(); i++) { 
					if (expr.charAt(i) == '(' ) iNrOfBraket++;
					if (expr.charAt(i) == ')' ) iNrOfBraket--;
					if (iNrOfBraket == 0) break;
			}	
			if (iNrOfBraket == 0) return i;
			return expr.length()-1;
   }
   
   /**
   * metoda operate este private; primeste ca parametru 2 valori double (dValue1, dValue2), 
   * un char (operator) si returneaza un double in functie de parametrul operator
   * care poate sa aiba valoare din sirul final OPERATOR (+,-,*,/,^);
   * daca operatorul este '/' si dValue2 == 0 (ar trebui sa returneze ceva pe 0) este
   * aruncat o exceptie: DivisionByZeroException
   */
   private static double operate(double dValue1, char operator, double dValue2) throws DivisionByZeroException {
  			switch (operator) {
  				case '+' : return (dValue1 + dValue2);
  				case '-' : return (dValue1 - dValue2);
  				case '*' : return (dValue1 * dValue2);
  				case '/' : if (dValue2 == 0) throw new DivisionByZeroException("Division by zero");
  									return (dValue1 / dValue2);
  				case '^' : return Math.pow(dValue1, dValue2);	
  			}
  			return 0;
   }

   /**
   * metoda operate este private; primeste ca parametru un String (function) si 
   * un double (dValue) si returneaza un double in functie de parametrul function
   * care poate sa aiba valoare din sirul final FUNCTION (sin, cos, tan, exp, log, atan, asin, acos);
   */
   private static double operate(String function, double dValue) {
				if (function.equalsIgnoreCase("sin"))  return Math.sin (dValue);
				if (function.equalsIgnoreCase("cos"))  return Math.cos (dValue);
				if (function.equalsIgnoreCase("tan"))  return Math.tan (dValue);
				if (function.equalsIgnoreCase("exp"))  return Math.exp (dValue);
				if (function.equalsIgnoreCase("log"))  return Math.log (dValue);
				if (function.equalsIgnoreCase("asin")) return Math.asin(dValue);
				if (function.equalsIgnoreCase("acos")) return Math.acos(dValue);
				if (function.equalsIgnoreCase("atan")) return Math.atan(dValue);
  				return 0;
   }
 
   
}





class Tree {

   public String Info;
   public Tree Left;
   public Tree Right;
   
   /**
	*	constructor vid -> Info primeste String-ul ""
   */
   public Tree() {
   	Info = ""; Left = Right = null;
   }
   
   /**
	*	constructor cu un param. String -> Info primeste valoarea parametrului   
	*/
   public Tree(String expr) {
   	Info = expr; Left = Right = null;
   }
	
   /**
	*	constructor cu un param. char -> Info primeste valoarea parametrului convertit in String
	*/  
   public Tree(char expr) {
   	Character c = new Character(expr);
   	Info = c.toString();
   }

	/**
	*	metoda public, care returneaza valoarea in double a field-ului Info
	*	arunca exceptia InvalidNumericValueException in caz ca Info nu poate fi convertit in double
	*	daca Info este String-ul vid (""), returneza 0
	*/  
   public double doubleValue() throws InvalidNumericValueException {
   			try { 
   			Double r = new Double(Info);	 
   			return r.doubleValue();
				}
  		 		catch(NumberFormatException e) { throw new InvalidNumericValueException("Invalid numeric value: " + Info); }	
   }
   
   /**
	*	metoda public, care returneaza valoarea lui Info in String
	*/  
   public String stringValue() {
   		return Info;
   }

   /**
	*	metoda public, care returneaza primul caracter din Info 
	*/  
   public char charValue() {
			return Info.charAt(0);
   }

   /**
	*	metoda public, care returneaza true daca parametrul primit este egal cu atributul Info si
	* false in caz contrar
	*/  
   public boolean is(String expr) {
			if (expr.equalsIgnoreCase(Info)) return true;	
   		return false;
   }
   
   
}



class DivisionByZeroException extends Exception
{
	public DivisionByZeroException() {}
	public DivisionByZeroException(String exception)
	{
		super(exception);
	}
}

class InvalidNumericValueException extends Exception
{
	public InvalidNumericValueException() {}
	public InvalidNumericValueException(String exception)
	{
		super(exception);
	}
}

class NullStringException extends Exception
{
	public NullStringException() {}
	public NullStringException(String exception)
	{
		super(exception);
	}
}

class SyntaxErrorException extends Exception
{
	public SyntaxErrorException() {}
	public SyntaxErrorException(String exception)
	{
		super(exception);
	}
}