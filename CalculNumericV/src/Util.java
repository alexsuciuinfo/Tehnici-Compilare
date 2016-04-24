
/**
 *
 * @author Alexandru
 */
public class Util {

    public double f_spline_derivat(double x) {
        double epsilon = 1.e-10;
        return (f_spline(x + epsilon) - f_spline(x)) / epsilon;
    }

    public double f_spline(double x) {
        return x * Math.sin(x) + (x * x + 4) * Math.pow(Math.E, x) - Math.cos(x);
    }

    public double f_integrat(double x) {
        return (Math.pow(x, 7) * Math.sqrt(1 - x * x)) / Math.pow(2 - x, (double) 13 / 2);
    }

    public double f_integrat(double x, double y) {
        return 3 * Math.pow(x, -3) * (Math.pow(y, 3) / (Math.pow(Math.E, y) - 1));
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

    public double[] Jacobi(double A[][], double a[], int m, double eps) {
        double max = 0, sum = 0, na, sigma;
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
        return x;
    }

    public void interpolare(double a, double b, int nr_pcte, int m, double int_x, double int_y) {
        double w = f_spline(a);
        double z = f_spline_derivat(b);
        double[] v;
        double[] vb = new double[nr_pcte];
        double[] x = new double[nr_pcte];
        double[] y = new double[nr_pcte];
        double[] h = new double[nr_pcte - 1];
        double[] ax = new double[nr_pcte - 1];
        double[] bx = new double[nr_pcte - 1];
        double[] cx = new double[nr_pcte - 1];
        double[][] matrix = new double[nr_pcte][nr_pcte];
        double pas = (b - a) / (nr_pcte - 1);

        //det x(0)..x(n-1)
        for (int i = 0; i < nr_pcte; i++) {
            x[i] = a + i * pas;
        }
        //det y(i) = f(xi)
        for (int i = 0; i < nr_pcte; i++) {
            y[i] = f_spline(x[i]);
        }

        for (int i = 0; i < nr_pcte - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }

        initialize(ax, nr_pcte - 1, 0);
        initialize(bx, nr_pcte - 1, 0);
        initialize(cx, nr_pcte - 1, 0);

        for (int i = 0; i < nr_pcte - 2; i++) {
            ax[i + 1] = h[i + 1] / (h[i] + h[i + 1]);
            bx[i + 1] = h[i] / (h[i] + h[i + 1]);
            cx[i + 1] = (3 * h[i + 1] * (y[i + 1] - y[i])) / (h[i] * (h[i] + h[i + 1]))
                    + (3 * h[i] * (y[i + 2] - y[i + 1])) / (h[i + 1] * (h[i] + h[i + 1]));
        }

        //rez sistemul si aflu v
        //intializez vb
        vb[0] = w;
        vb[nr_pcte - 1] = z;
        System.arraycopy(cx, 1, vb, 1, nr_pcte - 1 - 1);

        //initializez matrix
        initialize(matrix, nr_pcte, 0);
        matrix[0][0] = matrix[nr_pcte - 1][nr_pcte - 1] = 1;
        for (int i = 1; i < nr_pcte - 1; i++) {
            matrix[i][i] = 2;
            matrix[i][i + 1] = bx[i];
            matrix[i][i - 1] = ax[i];
        }
        matrix[nr_pcte - 2][nr_pcte - 2] = 2;

        for (int i = 0; i < nr_pcte; i++) {
            for (int j = 0; j < nr_pcte; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        //aflu v
        v = Jacobi(matrix, vb, nr_pcte, 1.e-10);

        double[] alpha = new double[nr_pcte - 1];
        double[] beta = new double[nr_pcte - 1];

        for (int i = 0; i < nr_pcte - 1; i++) {
            alpha[i] = (double) 3 / Math.pow(h[i], 2) * (y[i + 1] - y[i]);
            alpha[i] -= (double) 1 / h[i] * (v[i + 1] + 2 * v[i]);
            beta[i] = (double) 2 / Math.pow(h[i], 3) * (y[i + 1] - y[i]);
            beta[i] -= (double) 1 / Math.pow(h[i], 2) * (v[i + 1] + v[i]);
        }

        pas = (int_y - int_x) / (m - 1);

        for (int j = 0; j < m; j++) {
            double t = a + j * pas;
            for (int i = 0; i < nr_pcte - 1; i++) {
                if (x[i] <= t && t <= x[i + 1]) {
                    double p = y[i] + v[i] * (t - x[i]) + alpha[i] * Math.pow(t - x[i], 2)
                            + beta[i] * Math.pow(t - x[i], 3);
                    System.out.println("t = " + t + " p = " + p);
                }
            }
        }
    }

    public double Trapez(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += f_integrat(y[i - 1]) + f_integrat(y[i]);
        }
        return res * (b - a) / (2 * n);
    }

    public double Simpson(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += f_integrat(y[i - 1]) + 4 * f_integrat((y[i - 1] + y[i]) / 2) + f_integrat(y[i]);
        }
        return res * (b - a) / (6 * n);
    }

    public double Newton(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += f_integrat(y[i - 1]) + 3 * f_integrat((2 * y[i - 1] + y[i]) / 3)
                    + 3 * f_integrat((y[i - 1] + 2 * y[i]) / 3) + f_integrat(y[i]);
        }
        return res * (b - a) / (8 * n);
    }

    public double Boole(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += 7 * f_integrat(y[i - 1]) + 32 * f_integrat((3 * y[i - 1] + y[i]) / 4)
                    + 12 * f_integrat((y[i - 1] + y[i]) / 2)
                    + 32 * f_integrat((y[i - 1] + 3 * y[i]) / 4)
                    + 7 * f_integrat(y[i]);
        }
        return res * (b - a) / (90 * n);
    }

    public double dreptunghi(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += f_integrat(b, (y[i - 1] + y[i]) / 2);
        }
        return res * (b - a) / (n);
    }

    public double Newton_Cotes_2(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += f_integrat(b, (2 * y[i - 1] + y[i]) / 3)
                    + f_integrat(b, (y[i] + 2 * y[i - 1]) / 3);
        }
        return res * (b - a) / (2 * n);
    }

    public double Newton_Cotes_3(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += 2 * f_integrat(b, (3 * y[i - 1] + y[i]) / 4)
                    + 2 * f_integrat(b, (y[i - 1] + 3 * y[i]) / 4)
                    - f_integrat(b, (y[i - 1] + y[i]) / 2);
        }
        return res * (b - a) / (3 * n);
    }

    public double Newton_Cotes_4(double a, double b, int n) {
        double h = (b - a) / n;
        double res = 0;
        double[] y = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            y[i] = a + i * h;
        }
        for (int i = 1; i <= n; i++) {
            res += 11 * f_integrat(b, (4 * y[i - 1] + y[i]) / 5)
                    + 3 * f_integrat(b, (3 * y[i - 1] + 2 * y[i]) / 5)
                    + f_integrat(b, (2 * y[i - 1] + 3 * y[i]) / 5)
                    + 11 * f_integrat(b, (y[i - 1] + 4 * y[i]) / 5);
        }
        return res * (b - a) / (24 * n);
    }

}
