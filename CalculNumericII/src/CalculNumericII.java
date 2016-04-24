
import java.math.BigDecimal;
import static java.math.BigDecimal.ROUND_HALF_UP;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author Alexandru
 */
public class CalculNumericII {
    

    static int Decimal = 200;
    static int n = 5;
    static BigInteger One = BigInteger.ONE;
    static BigInteger Zero = BigInteger.ZERO;
    static MathContext mc = new MathContext(200);
    final static BigDecimal TWO = BigDecimal.valueOf(2);

    public static BigDecimal sqrt(BigDecimal x) {
        double z = x.doubleValue();
        double y = StrictMath.sqrt(z);
        return BigDecimal.valueOf(y);
    }

    public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, SCALE, ROUND_HALF_UP);

        }
        return x1;
    }

    public static Pair<Matrix, Matrix> get_LU(Matrix<Number> A) {

        Number inter;
        Number mult, mult1;
        Matrix<Number> L = new Matrix(A.row_length());
        Matrix<Number> U = new Matrix(A.row_length());
        L.initialize(new Number(Zero, One));
        U.initialize(new Number(Zero, One));

        for (int i = 0; i < A.row_length(); i++) {
            L.set(i, i, new Number(One, One));
        }

        for (int k = 0; k < A.row_length(); k++) {
            for (int j = k; j < A.row_length(); j++) {
                U.set(k, j, A.get(k, j));
                mult1 = new Number(One, One);
                for (int p = 0; p <= k - 1; p++) {
                    mult = Number.multiply(L.get(k, p), U.get(p, j));
                    inter = Number.substract(U.get(k, j), mult);
                    U.set(k, j, inter);
                }
            }

            for (int i = k + 1; i < A.row_length(); i++) {
                L.set(i, k, A.get(i, k));
                for (int p = 0; p <= k - 1; p++) {
                    mult = Number.multiply(L.get(i, p), U.get(p, k));
                    inter = Number.substract(L.get(i, k), mult);
                    L.set(i, k, inter);
                }
                L.set(i, k, Number.divide(L.get(i, k), U.get(k, k)));
            }
        }
        return new Pair(L, U);
    }

    public static Matrix get_Y(Matrix<Number> L, Matrix<Number> b) {
        Matrix<Number> Y = new Matrix(L.row_length(), b.column_length());
        Y.initialize(new Number(Zero, One));
        Number inter, mult;
        for (int i = 0; i < L.row_length(); i++) {
            Y.set(i, 0, b.get(i, 0));
            for (int k = 0; k <= i - 1; k++) {
                mult = Number.multiply(L.get(i, k), Y.get(k, 0));
                inter = Number.substract(Y.get(i, 0), mult);
                Y.set(i, 0, inter);
            }
        }
        return Y;
    }

    public static Matrix get_X(Matrix<Number> U, Matrix<Number> Y) {
        Matrix<Number> X = new Matrix(U.row_length(), Y.column_length());
        X.initialize(new Number(Zero, One));
        Number inter, mult, divide;
        for (int i = U.row_length() - 1; i >= 0; i--) {
            X.set(i, 0, Y.get(i, 0));
            for (int k = i + 1; k < U.row_length(); k++) {
                mult = Number.multiply(U.get(i, k), X.get(k, 0));
                inter = Number.substract(X.get(i, 0), mult);
                X.set(i, 0, inter);
            }
            X.set(i, 0, Number.divide(X.get(i, 0), U.get(i, i)));
        }
        return X;
    }

    public static void LU(Matrix A, Matrix b) {

        Pair<Matrix, Matrix> LU_pair = get_LU(A);
        //System.out.println("L = ");
        //LU_pair.getKey().show();
        //System.out.println("U = ");
        //LU_pair.getValue().show();
        Matrix<Number> Y = get_Y(LU_pair.getKey(), b);
        //System.out.println("Y = ");
        //Y.show();
        Matrix<Number> X = get_X(LU_pair.getValue(), Y);
        System.out.println("X = ");
        X.show();
        System.out.println("AX = ");
        LU_pair.getKey().multiply(A,X).show();
    }

    public static Matrix get_L(Matrix<BigDecimal> B) {
        BigDecimal sum = BigDecimal.ZERO;
        Matrix<BigDecimal> L = new Matrix(B.row_length(), B.column_length());
        L.initialize(sum);
        for (int j = 0; j < B.row_length(); j++) {
            sum = BigDecimal.ZERO;
            for (int k = 0; k <= j - 1; k++) {
                sum = sum.add(L.get(j, k).multiply(L.get(j, k), mc), mc);
            }
            L.set(j, j, B.get(j, j).subtract(sum, mc));
            L.set(j, j, sqrt(L.get(j, j), Decimal));

            for (int i = j + 1; i < B.row_length(); i++) {
                sum = BigDecimal.ZERO;
                for (int k = 0; k <= j - 1; k++) {
                    sum = sum.add(L.get(i, k).multiply(L.get(j, k), mc));
                }
                L.set(i, j, B.get(i, j).subtract(sum, mc));
                L.set(i, j, L.get(i, j).divide(L.get(j, j), mc));
            }
        }
        return L;
    }

    public static Matrix get_Cholesky_Y(Matrix<BigDecimal> L, Matrix<BigDecimal> c) {
        Matrix<BigDecimal> Y = new Matrix(L.row_length(), c.column_length());
        Y.initialize(BigDecimal.ZERO);
        BigDecimal sum;

        for (int i = 0; i < L.row_length(); i++) {
            sum = BigDecimal.ZERO;
            for (int k = 0; k <= i - 1; k++) {
                sum = sum.add(L.get(i, k).multiply(Y.get(k, 0), mc), mc);
            }
            Y.set(i, 0, c.get(i, 0).subtract(sum, mc));
            Y.set(i, 0, Y.get(i, 0).divide(L.get(i, i), mc));
        }
        return Y;
    }

    public static Pair<Matrix, Matrix> get_QR(Matrix<BigDecimal> A) {
        Matrix<BigDecimal> R = new Matrix(A.row_length(), A.column_length());
        Matrix<BigDecimal> Q = new Matrix(A.row_length(), A.column_length());
        R.initialize(BigDecimal.ZERO);
        Q.initialize(BigDecimal.ZERO);
        BigDecimal sum1;
        BigDecimal sum2;

        for (int i = 0; i < A.row_length(); i++) {
            R.set(0, 0, R.get(0, 0).add(A.get(i, 0).pow(2)));
        }
        R.set(0, 0, sqrt(R.get(0, 0), Decimal));
        for (int i = 0; i < A.row_length(); i++) {
            Q.set(i, 0, A.get(i, 0).divide(R.get(0, 0), mc));
        }

        for (int k = 1; k < A.row_length(); k++) {
            for (int j = 0; j <= k - 1; j++) {
                for (int i = 0; i < A.row_length(); i++) {
                    R.set(j, k, R.get(j, k).add(A.get(i, k).multiply(Q.get(i, j), mc), mc));
                }
            }

            sum1 = sum2 = BigDecimal.ZERO;
            for (int i = 0; i < A.row_length(); i++) {
                sum1 = sum1.add(A.get(i, k).pow(2, mc), mc);
                if (i <= k - 1) {
                    sum2 = sum2.add(R.get(i, k).pow(2, mc), mc);
                }
            }
            // System.out.println("sum1 = " + sum1.subtract(sum2) + " C = " + sum1.compareTo(sum2));
            // if(sum1.compareTo(sum2) != -1) R.set(k,k,sqrt(sum1.subtract(sum2)));
            // else R.set(k,k,BigDecimal.ZERO);
            R.set(k, k, sqrt(sum1.subtract(sum2, mc).abs(mc),Decimal));

            sum1 = BigDecimal.ZERO;
            for (int i = 0; i < A.row_length(); i++) {
                for (int j = 0; j <= k - 1; j++) {
                    sum1 = sum1.add(R.get(j, k).multiply(Q.get(i, j), mc), mc);
                }
                Q.set(i, k, A.get(i, k).subtract(sum1, mc));
                Q.set(i, k, Q.get(i, k).divide(R.get(k, k), mc));
                sum1 = BigDecimal.ZERO;
            }
        }
        return new Pair(Q, R);
    }

    public static Matrix get_QR_Y(Matrix<BigDecimal> Q, Matrix<BigDecimal> b) {
        Matrix<BigDecimal> Y = new Matrix(Q.row_length(), b.column_length());
        Y.initialize(BigDecimal.ZERO);
        for (int i = 0; i < Q.row_length(); i++) {
            for (int j = 0; j < Q.row_length(); j++) {
                Y.set(i, 0, Y.get(i, 0).add(Q.get(j, i).multiply(b.get(j, 0), mc), mc));
            }
        }
        return Y;
    }

    public static Matrix get_QR_X(Matrix<BigDecimal> R, Matrix<BigDecimal> Y) {
        Matrix<BigDecimal> X = new Matrix(R.row_length(), Y.column_length());
        X.initialize(BigDecimal.ZERO);
        BigDecimal sum;
        for (int i = R.row_length() - 1; i >= 0; i--) {
            sum = BigDecimal.ZERO;
            for (int j = i + 1; j < R.row_length(); j++) {
                sum = sum.add(R.get(i, j).multiply(X.get(j, 0), mc), mc);
            }
            sum = Y.get(i, 0).subtract(sum, mc);
            sum = sum.divide(R.get(i, i), mc);
            X.set(i, 0, sum);
        }
        return X;
    }

    public static Matrix get_Cholesky_X(Matrix<BigDecimal> Y, Matrix<BigDecimal> L) {
        Matrix<BigDecimal> X = new Matrix(L.row_length(), Y.column_length());
        X.initialize(BigDecimal.ZERO);
        BigDecimal sum;

        for (int i = L.row_length() - 1; i >= 0; i--) {
            sum = BigDecimal.ZERO;
            for (int k = i + 1; k < L.row_length(); k++) {
                sum = sum.add(L.get(k, i).multiply(X.get(k, 0), mc), mc);
            }
            X.set(i, 0, Y.get(i, 0).subtract(sum, mc));
            X.set(i, 0, X.get(i, 0).divide(L.get(i, i), mc));
        }
        return X;
    }

    public static void Cholesky(Matrix A, Matrix b) {
        Matrix B = new Matrix(A.row_length(), A.column_length());
        Matrix c = new Matrix(A.column_length(), B.column_length());
        Matrix<BigDecimal> L = get_L(A);
        System.out.println("L = ");
        L.show();
        Matrix<BigDecimal> Y = get_Cholesky_Y(L, b);
        System.out.println("Y = ");
        Y.show();
        //L.multiplyBig(L,Y).show();
        Matrix<BigDecimal> X = get_Cholesky_X(Y, L);
        System.out.println("X = ");
        X.show();
        //System.out.println("A = ");
        //A.show();
        System.out.println("AX = ");
        A.multiplyBig(A,X).show();
    }

    public static void QR(Matrix<BigDecimal> A, Matrix<BigDecimal> b) {
        Matrix B = new Matrix(A.row_length(), A.column_length());
        Pair<Matrix, Matrix> QR_pair = get_QR(A);
       // System.out.println("Q = ");
       // QR_pair.getKey().show();
       // System.out.println("R = ");
       // QR_pair.getValue().show();
       // System.out.println();
        Matrix Y = get_QR_Y(QR_pair.getKey(), b);
       // System.out.println("Y = ");
       // Y.show();
        Matrix X = get_QR_X(QR_pair.getValue(), Y);
        System.out.println("X = ");
        X.show();
       // System.out.println("A = ");
       // A.show();
        System.out.println("AX = ");
        A.multiplyBig(A,X).show();
    }

    public static void main(String[] args) {
        
       int n,p;
       // BigDecimal pi = sqrt(new BigDecimal(3),2000000);
        //System.out.println(pi);
        Scanner input = new Scanner( System.in );
        System.out.println("n = ");
        n = input.nextInt();
        System.out.println ("p = ");
        p = input.nextInt();
        
        Matrix A = new Matrix(n);
        Matrix bi = new Matrix(n,1);
        Matrix At = new Matrix(n);
        bi.initialize(BigDecimal.ONE);
        A  = A.Combine(n,p);
        At = A.Transpose(A);
        A = A.multiplyBig(At,A);
        bi = At.multiplyBig(At, bi);
        A.show();
        bi.show();
        Cholesky(A,bi);
        QR(A,bi);
        bi.initialize(new Number(BigInteger.ONE,BigInteger.ONE));
        A = A.CombineF(n, p);
        At = A.Transpose(A);
        A = A.multiply(At, A);
        bi = At.multiply(At, bi);
        A.show();
        bi.show();
        LU(A,bi);
       
        /*
        //System.out.println(sqrt(s,Decimal).add(BigDecimal.ZERO));
        for (int i = 21; i <= 20; i += 5) {
            System.out.println("n = " + i);
            System.out.println("LU : ");
            Matrix H = new Matrix(i, i);
            Matrix b = new Matrix(i, 1);
            H = H.Hilbert(i);
            b.initialize(new Number(One, One));
            H.show();
            LU(H, b);

            System.out.println("Cholesky : ");
            H = H.Hilbert(i, i);
            b.initialize(BigDecimal.ONE);
            Cholesky(H, b);

            System.out.println("QR : ");
            QR(H, b);
        }
     */       
    }
}
