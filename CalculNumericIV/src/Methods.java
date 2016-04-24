import javafx.util.Pair;

/**
 *
 * @author Alexandru
 */
public class Methods {

    public double f1(double x, double y) {
        return (7 * x * x * x - y + 1) * 0.1;
    }

    public double g1(double x, double y) {
        return (double) (8 * y * y * y + x - 1) / 11;
    }

    public double f(double x, double y) {
        return (double) 7 * x * x * x - 10 * x - y + 1;
    }

    public double g(double x, double y) {
        return (double) 8 * y * y * y - 11 * y + x - 1;
    }

    public double fx(double x, double y) {
        double epsilon = 1.e-10;
        return (f(x + epsilon, y) - f(x, y)) / epsilon;
    }

    public double gx(double x, double y) {
        double epsilon = 1.e-10;
        return (g(x + epsilon, y) - g(x, y)) / epsilon;
    }

    public double fy(double x, double y) {
        double epsilon = 1.e-10;
        return (f(x, epsilon + y) - f(x, y)) / epsilon;
    }

    public double gy(double x, double y) {
        double epsilon = 1.e-10;
        return (g(x, y + epsilon) - g(x, y)) / epsilon;
    }

    public double f_polinom(double x) {
        return (double) 1 / (1 + 100 * x * x);
    }

    public double f_newton(double x) {
        return Math.pow(x, 10);
        //return 1 + (x-4) + (double) 3/8 * (x-4) * (x-6) + (double) 4/48 * (x-4)*(x-6)*(x-8);
    }

    public double comb(double q, int i) {
        if (i == 0) {
            return 1;
        }
        double produs = 1;
        for (int j = 1; j <= i; j++) {
            produs *= (double) (q - j + 1) / j;
        }
        return produs;
    }
    
    public double deltah(int n, double x, double h) {
        if (n == 0) return f_newton(x);
        else return deltah(n - 1, x + h, h) - deltah(n - 1, x, h);
    }
    
    public double f_test(double x) {
        if (x == 0) return 1;
        if (x == 1) return 7;
        if (x == 2) return 23;
        if (x == 3) return 55;
        if (x == 4) return 109;
        return 1;
    }

    public double get_norma(double x0, double y0, double x1, double y1) {

        if (Math.abs(x1 - x0) > Math.abs(y1 - y0)) {
            return Math.abs(x1 - x0);
        }
        return Math.abs(y1 - y0);
    }

    public Pair<Double, Double> metoda_contractiei(double x0, double y0, double epsilon) {

        double x1, y1, norma;
        do {
            x1 = f1(x0, y0);
            y1 = g1(x0, y0);
            norma = get_norma(x0, y0, x1, y1);
            x0 = x1;
            y0 = y1;
        } while (norma > epsilon);

        Pair result = new Pair(x0, y0);
        return result;
    }

    public Pair<Double, Double> metoda_Gauss(double x0, double y0, double epsilon) {

        double x1, y1, norma;
        do {
            x1 = f1(x0, y0);
            y1 = g1(x1, y0);
            norma = get_norma(x0, y0, x1, y1);
            x0 = x1;
            y0 = y1;
        } while (norma > epsilon);

        Pair result = new Pair(x0, y0);
        return result;
    }

    public void metoda_newton(double a, double h, double o, double epsilon) {

        double x0, y0, x1, y1;
        int i1 = 0, j1 = 0, p = 0, iter = 0;
        double norma;
        double n = 0;
        double[] sx = new double[10];
        double[] sy = new double[10];
        int k = (int) ((int) (2 * a) / h);
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= k; j++) {
                x0 = -a + i * h;
                y0 = -a + j * h;
                n = 0;
                do {
                    double d = fx(x0, y0) * gy(x0, y0) - fy(x0, y0) * gx(x0, y0);
                    x1 = x0 - (double) (gy(x0, y0) * f(x0, y0) - fy(x0, y0) * g(x0, y0)) / d;
                    y1 = y0 - (double) (-gx(x0, y0) * f(x0, y0) + fx(x0, y0) * g(x0, y0)) / d;
                    norma = get_norma(x0, y0, x1, y1);
                    x0 = x1;
                    y0 = y1;
                    n++;
                } while (norma > epsilon);

                if (iter == 0) {
                    sx[p] = x0;
                    sy[p] = y0;
                    iter = 1;
                } else {

                    boolean isgood = true;
                    for (int t = 0; t <= p; t++) {
                        if (!(Math.abs(sx[t] - x0) > epsilon && Math.abs(sy[t] - y0) > epsilon)) {
                            isgood = false;
                        }
                    }

                    if (isgood) {
                        p++;
                        sx[p] = x0;
                        sy[p] = y0;
                    }
                }
            }
        }

        for (int i = 0; i <= p; i++) {
            System.out.println("x = " + sx[i] + " y = " + sy[i]);
        }
        System.out.println();
    }
    
    public void metoda_newton_simplificata(double a, double h, double o, double epsilon) {

        double x1, y1, x0, y0;
        int i1 = 0, j1 = 0, p = 0, iter = 0;
        double norma;
        double n = 0;
        double[] sx = new double[30];
        double[] sy = new double[30];
        int k = (int) ((int) (2 * a) / h);
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= k; j++) {
                x0 = -a + i * h;
                y0 = -a + j * h;
                n = 0;
                double gx = gx(x0, y0);
                double gy = gy(x0,y0);
                double fx = fx(x0,y0);
                double fy = fy(x0,y0);
                double d = fx * gy - fy * gx;

                do {
                   // double d = fx(x0, y0) * gy(x0, y0) - fy(x0, y0) * gx(x0, y0);
                    x1 = x0 - (double) (gy * f(x0, y0) - fy * g(x0, y0)) / d;
                    y1 = y0 - (double) (-gx * f(x0, y0) + fx * g(x0, y0)) / d;
                    norma = get_norma(x0, y0, x1, y1);
                    x0 = x1;
                    y0 = y1;
                    n++;
                } while (norma > epsilon && n < o);
                
                if (iter == 0 && n < o) {
                    sx[p] = x0;
                    sy[p] = y0;
                    iter = 1;
                } else {

                    boolean isgood = true;
                    for (int t = 0; t <= p; t++) {
                        if (!(Math.abs(sx[t] - x0) > 0.00001 && Math.abs(sy[t] - y0) > 0.00001)) {
                            isgood = false;
                        }
                    }

                    if (isgood && n < o) {
                        p++;
                        sx[p] = x0;
                        sy[p] = y0;
                    }
                }
            }
        }

        for (int i = 0; i <= p; i++) {
            System.out.println("x = " + sx[i] + " y = " + sy[i]);
        }
        System.out.println();
    }

    public void Lagrangia(double[] x, int n, double a, double b, int m) {

        double p = 0, produs;
        double y;
        double h = (double) (b - a) / (m - 1);
        for (int k = 0; k < m; k++) {
            y = a + k * h;
            p = 0;
            produs = 1;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        produs *= ((y - x[j]) / (x[i] - x[j]));
                    }
                }
                p += f_polinom(x[i]) * produs;
                produs = 1;
            }
            System.out.println("y = " + y + " p = " + p);
        }
        System.out.println();
    }

    public void Newton_Divizat(double x[], int n, double a, double b, int m) {

        double[][] ax = new double[n][n];
        double c[] = new double[n];
        double p = 0, produs;
        double y = 0;
        double h = (double) (b - a) / (m - 1);

        for (int i = 0; i < n; i++) {
            ax[0][i] = f_polinom(x[i]);
        }

        c[0] = ax[0][0];

        for (int i = 1; i < n; i++) {
            for (int j = i; j < n; j++) {
                ax[i][j] = (double) (ax[i - 1][j] - ax[i - 1][j - 1]) / (x[j] - x[j - i]);
            }
            c[i] = ax[i][i];
        }

        for (int k = 0; k < m; k++) {
            y = a + k * h;
            p = c[0];
            produs = 1;
            for (int i = 1; i < n; i++) {
                produs = c[i];
                for (int j = 0; j < i; j++) {
                    produs *= (y - x[j]);
                }
                p += produs;
            }
            System.out.println("y = " + y + " p = " + p);
        }
        System.out.println();
    }

    public void Newton_ascendent(double x0, int n, double a, double b, double h, int m) {

        double h1 = (b - a) / (m - 1);
        double y, q;
        double p = 0;
        for (int k = 0; k < m; k++) {
            y = x0 + k * h1;
            q = (y - x0) / h;
            p = 0;
            for (int i = 0; i <= n; i++) {
                p += (comb(q, i) * deltah(i, x0, h));
            }
            
            System.out.println("y = " + y + " p = " + p);
        }
        System.out.println();
    }
    
    public void Newton_ascendent1(double x0, int n, double a, double b, double h, int m) {

        double h1 = (b - a) / (m - 1);
        double y, q;
        double p = 0;
            y = 0.5;
            q = (y - x0) / h;
            for (int i = 0; i <= 3; i++) {
                p += (comb(q, i) * deltah(i, x0, h));
            }
            
            System.out.println("y = " + y + " p = " + p);
        System.out.println();
    }

}
