import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;


/**
 *
 * @author Alexandru
 */
public class Matrix <T> {
    
    private final int row,column;
    private ArrayList<ArrayList<T>> a;
    private final MathContext mc = new MathContext(100);
    
    
    Matrix(int r, int c) {
        row = r;
        column = c;
        a = new ArrayList<ArrayList<T>>();
        for (int i=0; i<r; i++) {
            a.add(i,new ArrayList<>(c));
            for (int j=0; j<c; j++)
                a.get(i).add(null);
        }
    }
    
        static BigDecimal Comb (int n, int k)
   {  if(k>n-k)
       k=n-k;
       BigDecimal B[][]=new BigDecimal[n+1][k+1];

    for (int i = 0; i <= n; i++)
   { int min;
     if(i>=k)
      min=k;
    else
     min=i;
   for (int j = 0; j <= min; j++)
    { if (j == 0 || j == i)
           B[i][j] =new BigDecimal(1);
       else{ 
           if(j>i-j)
            B[i][j]=B[i][i-j];
            else
            B[i][j] = B[i - 1][j - 1].add(B[i - 1] [j]);
          }
    }
 }
BigDecimal div=new BigDecimal(142857);
return B[n][k];
}
    
    Matrix(int n) {
        row = column = n;
        a = new ArrayList<ArrayList<T>>();
        for (int i=0; i<n; i++) {
            a.add(i,new ArrayList<>(n));
            for (int j=0; j<n; j++)
                a.get(i).add(null);
        }
    }
    
    public void initialize(T elem) {
        for (int i=0; i<this.row; i++)
            for(int j=0; j<this.column; j++) {
                a.get(i).set(j,elem);
            }
    }
    
    
    public void set(int i, int j, T elem) {
        a.get(i).set(j,elem);
    }
    
    public T get(int i, int j) {
        return this.a.get(i).get(j);
    }
    
    public int row_length() {
        return row;
    }
    
    public int column_length() {
        return column;
    }
    
    public Matrix<Number> multiply (Matrix<Number> A, Matrix<Number> B) {
        Number prod,sum;
        Matrix<Number> C = new Matrix(A.row_length(),B.column_length());
        C.initialize(new Number(BigInteger.ZERO,BigInteger.ONE));
        for (int i=0; i<A.row_length(); i++)
            for (int j=0; j<B.column_length(); j++) 
                for (int k=0; k<A.column_length(); k++) {
                    prod = Number.multiply(A.get(i,k),B.get(k, j));
                    sum = Number.add(C.get(i,j),prod);
                    C.set(i,j,sum);
                }       
        return C;   
    }
    
    public Matrix<BigDecimal> multiplyBig (Matrix<BigDecimal> A, Matrix<BigDecimal> B) {
        BigDecimal prod = BigDecimal.ZERO, sum = BigDecimal.ZERO;
        Matrix<BigDecimal> C = new Matrix(A.row_length(),B.column_length());
        C.initialize(BigDecimal.ZERO);
        for (int i=0; i<A.row_length(); i++)
            for (int j=0; j<B.column_length(); j++) 
                for (int k=0; k<A.column_length(); k++) {
                    prod = A.get(i,k).multiply(B.get(k, j),mc);
                    C.set(i,j,C.get(i,j).add(prod,mc));
                }       
        return C;   
    }
    
    public Matrix Transpose(Matrix A) {
        Matrix B = new Matrix(A.row,A.column);
        for(int i=0; i<A.row; i++)
            for(int j=0; j<A.column; j++)
                B.set(j,i,A.get(i,j));
        return B;
    }
    
    public Matrix<Number> Hilbert(int n) {
        Matrix <Number> H = new Matrix(n);
        H.initialize(new Number(BigInteger.ZERO,BigInteger.ZERO));
        if (row != column)
            throw new RuntimeException("Hilbert is a square matrix ");
        for (int i=0; i<row; i++)
            for(int j=0; j<row; j++) {
                H.a.get(i).set(j,new Number(BigInteger.ONE,BigInteger.valueOf(i).add(BigInteger.valueOf(j).add(BigInteger.ONE))));
            }
        return H;
    }
    
    public Matrix<Number> CombineF(int n,int p) {
        Matrix <Number> C = new Matrix(n);
        C.initialize(new Number(BigInteger.ZERO,BigInteger.ZERO));
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++){
                C.a.get(i).set(j,new Number(BigInteger.valueOf(Comb(p+j,i).longValue()),BigInteger.ONE));
        }
        return C;
    }
    
    public Matrix<BigDecimal> Combine(int n,int p) {
        Matrix<BigDecimal> C = new Matrix(n);
        C.initialize(BigDecimal.ZERO);
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++){
                C.a.get(i).set(j,Comb(p+j,i));
        }
        return C;
    }
    
    public Matrix<BigDecimal> Hilbert(int n, int m) {
        Matrix<BigDecimal> H = new Matrix(m,m);
        H.initialize(BigDecimal.ZERO);
        int ii,jj;
        BigDecimal sum_i,sum_j,sum;
        if (row != column)
            throw new RuntimeException("Hilbert is a square matrix ");
        for (int i=0; i<row; i++)
            for(int j=0; j<row; j++) {
                sum_i = BigDecimal.ZERO;
                sum_j = BigDecimal.ZERO;
                sum = BigDecimal.ZERO;
                for(ii=0; ii<i; ii++)
                    sum_i = sum_i.add(BigDecimal.ONE,mc);
                for(jj=0; jj<j; jj++)
                    sum_j = sum_j.add(BigDecimal.ONE,mc);
                sum = sum.add(sum_i,mc);
                sum = sum.add(sum_j,mc);
                sum = sum.add(BigDecimal.ONE,mc);
                H.a.get(i).set(j,BigDecimal.ONE.divide(sum,mc));
            }
        return H;
    }
    
    public void show() {
        for (int i=0; i<row; i++) {
            if (i>0) System.out.println();
            for(int j=0; j<column; j++) {
                System.out.print(a.get(i).get(j) + " ");
            }
        }
        System.out.println();
    }
    
}
