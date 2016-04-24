import javafx.util.Pair;

/**
 *
 * @author Alexandru
 */
public class Methods {

    public void showMat(double a[][], int m) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void showVec(double x[], int m) {
        for (int i = 0; i < m; i++) {
            System.out.println(x[i] + " ");
        }
    }

    public double[][] mymatrix(double a[][], int m) {
        a = initialize(a, m, 0);
        for (int i = 0; i < m; i++) {
            a[i][i] = 2 + (double) (i + 1) / (m * m);
            if (i < m - 1) {
                a[i][i + 1] = a[i + 1][i] = -1;
            }
        }
        return a;
    }

    public double[] myvector(double v[], int m) {
        v = initialize(v, m, 0);
        for (int i = 0; i < m; i++) {
            v[i] = 2;
        }
        return v;
    }

    public double[] initialize(double x[], int n, double val) {
        for (int i = 0; i < n; i++) {
            x[i] = val;
        }
        return x;
    }

    public double[][] initialize(double a[][], int n, double val) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = val;
            }
        }
        return a;
    }

    Pair<Integer, Integer> get_p_q(double a[][], int n) {
        Pair<Integer, Integer> pair = new Pair(0, 0);
        double max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (max < Math.abs(a[i][j])) {
                    max = Math.abs(a[i][j]);
                    pair = new Pair(i, j);
                }
            }
        }
        return pair;
    }

    public double seminorma(double x[], int n) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += x[i] * x[i];
        }
        return sum;
    }

    public double[] Jacobi(double A[][], double a[], int m, double eps) {
        double max = 0, sum = 0, na, sigma = 0;
        double[] x = new double[m];
        double[] b = new double[m];
        double[][] B = new double[m][m];
        double[] y = new double[m];
        int p = 10, no = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                sum += Math.abs(A[i][j]);
            }
            if (max < sum) {
                max = sum;
            }
            sum = 0;
        }
        double ni = max;
        double l = (double) 2 / ni;
        for (int k = 1; k < p; k++) {
            sigma = (double) l / p * k;
            x = initialize(x, m, 0);
            B = initialize(B, m, 0);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    if (i == j) {
                        B[i][j] = (double) (1 - sigma * A[i][i]);
                    } else {
                        B[i][j] = (double) (-sigma * A[i][j]);
                    }
                }
            }
            for (int i = 0; i < m; i++) {
                b[i] = sigma * a[i];
            }
            int n = 0;
            do {
                na = 0;
                n++;
                y = initialize(y, m, 0);
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < m; j++) {
                        y[i] += B[i][j] * x[j];
                    }
                    y[i] += b[i];
                }
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < m; j++) {
                        na += A[i][j] * (y[i] - x[i]) * (y[j] - x[j]);
                    }
                }
                na = Math.sqrt(na);
                System.arraycopy(y, 0, x, 0, m);
            } while (na > eps);

            if (k == 1) {
                no = n;
            } else if (n < no) {
                no = n;
            }
        }
        System.out.println("Nr de iteratii optime = " + no);
        System.out.println("Parametrul optim de relaxare = " + sigma);
        return x;
    }

    public double[] Gauss_Seidel(double[][] A, double[] a, int m, double eps) {
        double na, sigma = 0;
        double[] x = new double[m];
        double[] y = new double[m];
        int p = 20, no = 0;

        for (int k = 1; k < p; k++) {
            sigma = (double) 2 / p * k;
            x = initialize(x, m, 0);
            int n = 0;
            do {
                na = 0;
                n++;
                y = initialize(y, m, 0);
                for (int i = 0; i < m; i++) {
                    double sum1 = 0;
                    double sum2 = 0;
                    for (int j = 0; j < i; j++) {
                        sum1 += A[i][j] * y[j];
                    }
                    for (int j = i + 1; j < m; j++) {
                        sum2 += A[i][j] * x[j];
                    }

                    y[i] = (double) ((1 - sigma) * x[i] + sigma / A[i][i] * (a[i] - sum1 - sum2));
                }
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < m; j++) {
                        na += A[i][j] * (y[i] - x[i]) * (y[j] - x[j]);
                    }
                }
                na = Math.sqrt(na);
                System.arraycopy(y, 0, x, 0, m);

            } while (na > eps);

            if (k == 1) {
                no = n;
            } else if (n < no) {
                no = n;
            }
        }
        System.out.println("Nr de iteratii optime = " + no);
        System.out.println("Parametrul optim de relaxare = " + sigma);
        return x;
    }

    public double[] Gradient(double A[][], double b[], int m, double eps) {
        int k = 0;
        double alpha, c, norma;
        double[] dif = new double[m];
        double[] r = new double[m];
        double[] v = new double[m];
        double[] y = new double[m];
        double[] w = new double[m];
        double[] p = new double[m];
        double[] x = new double[m];
        x = initialize(x, m, 0);
        r = initialize(r, m, 0);
        v = initialize(v, m, 0);
        System.arraycopy(b, 0, r, 0, m);
        System.arraycopy(r, 0, v, 0, m);

        do {
            initialize(y, m, 0);
            initialize(p, m, 0);
            initialize(w, m, 0);

            // calculez alpha(i)
            double scalar = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    scalar += A[i][j] * v[i] * v[j];
                }
            }
            alpha = seminorma(r, m) / scalar;

            // calculez x(i+1)
            for (int i = 0; i < m; i++) {
                y[i] = x[i] + alpha * v[i];
            }

            // calculez r(i+1)
            for (int i = 0; i < m; i++) {
                p[i] = b[i];
                for (int j = 0; j < m; j++) {
                    p[i] -= A[i][j] * y[j];
                }
            }

            //calculez c(i)
            c = seminorma(p, m) / seminorma(r, m);

            //calculez v(i+1)
            for (int i = 0; i < m; i++) {
                w[i] = p[i] + c * v[i];
            }

            // norma(x(i+1) - x(i))
            for (int i = 0; i < m; i++) {
                dif[i] = y[i] - x[i];
            }
            norma = Math.sqrt(seminorma(dif, m));

            System.arraycopy(y, 0, x, 0, m);
            System.arraycopy(p, 0, r, 0, m);
            System.arraycopy(w, 0, v, 0, m);

            k++;
        } while (norma > eps);

        System.out.println("Nr de iteratii optim = " + k);
        return x;
    }

    public double[] Rotatie(double A[][], int m, double eps) {
        Pair<Integer, Integer> pair;
        int p, q, nr_iter = 0;
        double teta, PI = Math.PI, c, s, modul;
        double[][] B = new double[m][m];
        double z[] = new double[m];
        initialize(z, m, 0);
        initialize(B, m, 0);

        do {
            pair = get_p_q(A, m);
            p = pair.getKey();
            q = pair.getValue();
            if (A[p][p] == A[q][q]) {
                teta = PI / 4;
            } else {
                teta = (double) 1 / 2 * Math.atan(2 * A[p][q] / (A[p][p] - A[q][q]));
            }
            c = Math.cos(teta);
            s = Math.sin(teta);

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    if (i != p && i != q && j != p && j != q) {
                        B[i][j] = A[i][j];
                    }

                    if (j != p && j != q) {
                        B[p][j] = B[j][p] = c * A[p][j] + s * A[q][j];
                        B[q][j] = B[j][q] = -s * A[p][j] + c * A[q][j];
                    }
                }
            }
            B[p][q] = B[q][p] = 0;
            B[p][p] = c * c * A[p][p] + 2 * c * s * A[p][q] + s * s * A[q][q];
            B[q][q] = s * s * A[p][p] - 2 * c * s * A[p][q] + c * c * A[q][q];

            for (int i = 0; i < m; i++) {
                System.arraycopy(B[i], 0, A[i], 0, m);
            }
            modul = 0;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    if (i != j) {
                        modul += A[i][j] * A[i][j];
                    }
                }
            }
            modul = Math.sqrt(modul);
            // System.out.println(modul);
            //showMat(A, m);

        } while (modul > eps);

        for (int i = 0; i < m; i++) {
            z[i] = A[i][i];
        }

        return z;
    }
}
