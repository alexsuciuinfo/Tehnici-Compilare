
/**
 *
 * @author Alexandru
 */
public class CalculNumericV {

    public static void main(String[] args) {

        Util U = new Util();
        System.out.println(U.f_spline_derivat(2 * Math.PI));
        System.out.println("Valorile functiei spline : ");
        U.interpolare(0, 2 * Math.PI, 10, 20, 0, 6.5);
        int[] N = {1, 10, 50, 100, 1000, 10000};
        int index = 1;
        
        while (index < 6) {
            int n = N[index];
            System.out.println("N = " + n);
            System.out.print("Trapez = " + U.Trapez(-1, 1, n));
            System.out.print(" Simpson = " + U.Simpson(-1, 1, n));
            System.out.print(" Newton = " + U.Newton(-1, 1, n));
            System.out.println(" Boole = " + U.Boole(-1, 1, n));
            index++;
        }
        
        for (int i = 0; i < N.length - 2; i++) {
            for (int j = 1; j < N.length; j++) {
                System.out.println("X = " + N[i] + " N = " + N[j]);
                System.out.print("Dreptunghi = " + U.dreptunghi(0, N[i], N[j]));
                System.out.print(" NCII = " + U.Newton_Cotes_2(0, N[i], N[j]));
                System.out.print(" NCIII = " + U.Newton_Cotes_3(0, N[i], N[j]));
                System.out.println(" NCIV = " + U.Newton_Cotes_4(0, N[i], N[j]));
            }
        }
    }

}
