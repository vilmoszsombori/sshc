package ro.utcluj.as.sshc;

public class LinearSystem {
	public static final int GAUSS_SEIDEL = 0;
	public static final int ELIM_GAUSS = 1;
	public static final int MAXITERATIONNUMBER = 999;
	public static double tolerance = 0.00001;

	private double[][] a,L;
	private double[] b,y;
	private DoubleData[][] A;
	private DoubleData[] B;
	private boolean[] boundaryVertex;
	private DoubleData[] x;
	private DoubleData zero, one;
	
	private int n;
	
	public LinearSystem(java.util.Vector edge, int n, int method) {

		this.n = n;

		if (method==0) {
			A = new DoubleData[n][n];
			B = new DoubleData[n];
			zero = new DoubleData();
			one  = new DoubleData(1);
			boundaryVertex = new boolean[n];
		}
		else {
			a = new double[n][n];
			b = new double[n];
		}

		x = new DoubleData[n];

		int i,j,k;

		for(k=0; k<edge.size(); k++) {

			HalfEdge e = (HalfEdge)edge.elementAt(k);
			Vertex ni = e.getOrig();
			Vertex nj = e.getTwin().getOrig();

			i=ni.getLabel(); x[i]=ni.xi; 
			j=nj.getLabel(); x[j]=nj.xi; 

			if (method==0) {

				if (nj.onBoundary()) {
					A[j][j]=one; A[j][i]=zero;
				}
				else {
					A[j][j]=nj.aii; A[j][i]=e.aij;
				}

				if (ni.onBoundary()) {
					A[i][i]=one; A[i][j]=zero;		
				}
				else {
					A[i][i]=ni.aii; A[i][j]=e.aij;
				}

				B[i]=ni.bi; 
				B[j]=nj.bi;
			}

			else {

				if (nj.onBoundary()) {
					 a[j][j]=1; a[j][i]=0;
				}
				else {
					a[j][j]=nj.aii.value; a[j][i]=e.aij.value;
				}

				if (ni.onBoundary()) {
					a[i][i]=1; a[i][j]=0;		
				}
				else {
					a[i][i]=ni.aii.value; a[i][j]=e.aij.value;
				}

				b[i]=ni.bi.value; 
				b[j]=nj.bi.value; 
			}
		}

		if (method==0) gaussSeidel();
		if (method==1) {
			elimGauss();
			fsub();
		}
		
		miniMax();
	}

	private void writeA(double[][] matrix, String szoveg) {
		System.out.println(szoveg);
		int i,j; String s;
		for(i=0;i<n;i++) {
			s = "";
			for(j=0;j<n;j++) s += String.valueOf(matrix[i][j])+",";
			System.out.println(s);			
		}
	}
	
	private void printResult(String szoveg) {
//		System.out.println(szoveg);
		if (szoveg=="vertical")
			for(int i=0;i<n;i++) System.out.println("x["+i+"]="+String.valueOf(x[i].value));
		else {
			String s="";
			for(int i=0;i<n-1;i++) s += String.valueOf(x[i].value)+",";		
			s += String.valueOf(x[n-1].value);
			System.out.println("["+s+"]");
		}
	}

	private void cholesky() {
		int i,j,k; double S;
		System.out.println(L[0][0] = Math.sqrt(a[0][0]));
		for(i=1;i<n;i++) System.out.println(L[i][0] = a[i][0]/L[0][0]);
		for(k=1;k<n;k++) {
			S=0; for(j=0;j<k;j++) S += L[k][j]*L[k][j];
			System.out.println(L[k][k] = Math.sqrt(a[k][k]-S));
			for(i=k+1;i<n;i++) {
				S=0; for(j=0;j<k;j++) S += L[i][j]*L[j][k];
				L[i][k] = (a[i][k]-S)/L[k][k];		
			}
		}
	}

	private void gaussSeidel() {
		int i,j,k;
		double S, oldvalue, error=0, localerror;
		for(k=0; k<MAXITERATIONNUMBER; k++) {
		
			for(i=0; i<n; i++) {
				S=0;
				for(j=0;j<n;j++) if (j!=i) if (A[i][j]!=null) S += A[i][j].value * x[j].value;				
				oldvalue = x[i].value;
				x[i].value = (B[i].value - S) / A[i][i].value;
				localerror = Math.abs(x[i].value - oldvalue);
				if (i==0) error = localerror;
				else if (error < localerror) error = localerror;
			}
			
			if (error < tolerance) break;
//			printResult("horizontal");
		}	
		
		System.out.println("System dimension="+n+"   Iteration number="+k);
	}
	
	private void elimGauss() {
		int i,j,k; double m;
		for(j=0;j<n-1;j++) {
			for(i=j+1;i<n;i++) {
				m=a[i][j]/a[j][j];
				for(k=j;k<n;k++) a[i][k]=a[i][k] - m*a[j][k];
				b[i]=b[i]-m*b[j];
			}
		}		
	}
	
	private void fsub() {
		int i,k;
		x[n-1].value = b[n-1]/a[n-1][n-1];
		for(i=n-2;i>=0;i--) {
			for(k=i+1;k<n;k++) b[i]=b[i] - a[i][k]*x[k].value;
			x[i].value = b[i]/a[i][i];
		} 
	}
	
	private void bsub() {
		int i,k;
		y[0] = b[0]/a[0][0];
		for(i=1;i<n;i++) {
			for(k=i+1;k<n;k++) b[i] = b[i] - a[i][k] * y[k];
			y[i] = b[i]/a[i][i];
		} 
	}
	
	private void miniMax() {
		Triangle.max = x[0].value; Triangle.min = x[0].value;		
		for(int k=1;k<n;k++) {
			if (Triangle.max < x[k].value) Triangle.max = x[k].value;
			if (Triangle.min > x[k].value) Triangle.min = x[k].value;
		}
		System.out.println("min="+Triangle.min+" ;   max="+Triangle.max);
	}

}
