import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author Alexandru
 */
public class CalculNumericIV {

    public static double[] polinom = {-1, (double) -1 / 3, (double) -1 / 5, (double) -1 / 10, 0,
        (double) 1 / 10, (double) 1 / 5, (double) 1 / 3, 1};
    public static Methods Met = new Methods();
    public static double eps = 1.e-10;

    public static void get_met_contractie(double x, double y, double eps) {
        Pair<Double, Double> solution;
        solution = Met.metoda_contractiei(x, y, eps);
        System.out.println("x = " + solution.getKey() + " y = " + solution.getValue());
        System.out.println();
    }

    public static void get_Gauss(double x, double y, double eps) {
        Pair<Double, Double> solution;
        solution = Met.metoda_contractiei(x, y, eps);
        System.out.println("x = " + solution.getKey() + " y = " + solution.getValue());
        System.out.println();
    }

    public static double func1(double x) {
        return -(x * x) / 8 + (3 * x) / 2 + 6;
    }

    public static double func3(double x) {
        return 6 * Math.sqrt(2 * x + 1) - 2 * x;
    }

    public static double func2(double x) {
        return 6 + 4 * x - 3 * x * x + (double) 9 / 16 * x * x * x - (double) 7 / 64 * x * x * x * (x - 4);
    }

    public static double f_newton(double x) {
        return Math.pow(x, 10);
        // return 1 + (x-4) + (double) 3/8 * (x-4) * (x-6) + (double) 4/48 * (x-4)*(x-6)*(x-8);
    }

    public static void main(String arg[]) throws Exception {

        int m = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("Metoda Contractiei : ");
        get_met_contractie(0, 0, eps);

        System.out.println("Metoda Gauss Seidel neliniara :");
        get_Gauss(0, 0, eps);

        System.out.println("Metoda lui Newton : ");
        Met.metoda_newton(2, 0.5, 10000, 1.e-9);

        System.out.println("Metoda lui Newton simplificata : ");
        Met.metoda_newton_simplificata(2, 1, 10000, 1.e-9);

        System.out.print("m = ");
        m = sc.nextInt();

        System.out.println("Formula Lagrange : ");
        Met.Lagrangia(polinom, 9, (double) -3 / 2, (double) 3 / 2, m);

        System.out.println("Formula Newton divizata : ");
        Met.Newton_Divizat(polinom, 9, (double) -3 / 2, (double) 3 / 2, m);

        System.out.println("Formula Newton ascendenta : ");
        Met.Newton_ascendent(1, 10, 1, 10, 1, m);

    }
}
