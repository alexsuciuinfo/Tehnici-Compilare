import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * @author Alexandru
 */
public class UtilNewtonAscendent {

    MathContext mc = new MathContext(40);

    public BigDecimal f_newton(BigDecimal x) {
        BigDecimal value = BigDecimal.ONE;
        for (int i = 0; i < 10; i++) {
            value = value.multiply(x, mc);
        }
        return value;
    }

    public BigDecimal comb(BigDecimal q, int i) {
        if (i == 0) {
            return BigDecimal.ONE;
        }
        BigDecimal produs = BigDecimal.ONE;
        for (int j = 1; j <= i; j++) {
            produs = produs.multiply(q.subtract(BigDecimal.valueOf(j), mc).add(BigDecimal.ONE, mc).divide(BigDecimal.valueOf(j), mc), mc);
        }
        return produs;
    }

    public BigDecimal deltah(int n, BigDecimal x, BigDecimal h) {
        if (n == 0) {
            return f_newton(x);
        } else {
            return deltah(n - 1, x.add(h, mc), h).subtract(deltah(n - 1, x, h), mc);
        }
    }
}
