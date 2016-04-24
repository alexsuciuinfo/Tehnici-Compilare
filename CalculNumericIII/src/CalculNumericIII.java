import java.util.Scanner;
/**
 *
 * @author Alexandru
 */
public class CalculNumericIII {

    static double eps = 1.e-10;
    static int m;
    static double A[][];
    static double a[], x[];
    
    public static void main(String[] args) {
        
        
        Methods Met = new Methods();
        System.out.print("m = ");
        Scanner sc = new Scanner(System.in);
        m = sc.nextInt();
        A = new double[m][m];
        A = Met.mymatrix(A, m);
        Met.showMat(A, m);
        a = new double[m];
        a = Met.myvector(a, m);
        System.out.println("Metoda Jacobi : ");
        x = Met.Jacobi(A, a, m, eps);
        Met.showVec(x, m);
        System.out.println("Metoda Gauss-Seindel : ");
        x = Met.Gauss_Seidel(A, a, m, eps);
        Met.showVec(x, m);
        System.out.println("Metoda Gradient : ");
        x = Met.Gradient(A, a, m, eps);
        Met.showVec(x, m);
        System.out.println("Metoda Rotatiilor : ");
        x = Met.Rotatie(A, m, eps);
        Met.showVec(x, m);
    }
    
}
