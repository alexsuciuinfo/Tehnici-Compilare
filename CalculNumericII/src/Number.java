
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author Alexandru
 */
public class Number  {
    
    BigInteger  Denominator,Numerator;
    static MathContext mc = new MathContext(50);
    
    Number(BigInteger N, BigInteger D) {
        this.Denominator = D;
        this.Numerator = N;
    }
    
    Number (Number num) {
        Denominator = num.Denominator;
        Numerator = num.Numerator;
    }
    
    Number() {
        
    }
    
    
    public BigInteger getN(Number num) {
        return num.Numerator;
    }
    
    public BigInteger getD(Number num) {
        return num.Denominator;
    }
    
    public void set(Number num) {
        this.Denominator = num.Denominator;
        this.Numerator = num.Numerator;
    }
    
    public void set(BigInteger n,BigInteger d) {
        this.Denominator = d;
        this.Numerator = n;
    }
    
    public static Number add(Number n1, Number n2) {
        Number num = new Number();
        if (n1.Numerator == BigInteger.ZERO) return n2;
        if (n2.Numerator == BigInteger.ZERO) return n1;
        BigInteger Den = n1.Denominator.multiply(n2.Denominator);
        BigInteger Num = (n1.Numerator.multiply(n2.Denominator)).add(n1.Denominator.multiply(n2.Numerator));
        BigInteger cmmdc = Num.gcd(Den);
        num.Denominator = Den.divide(cmmdc);
        num.Numerator = Num.divide(cmmdc);
        return num;

    }
    
    public static Number substract(Number n1, Number n2) {
        Number num = new Number();
        if(n1.Numerator == BigInteger.ZERO) {
            num.Numerator = n2.Numerator.negate();
            num.Denominator = n2.Denominator;
            return num;
        }
        if (n2.Numerator == BigInteger.ZERO) return n1;
        BigInteger Den = n1.Denominator.multiply(n2.Denominator);
        BigInteger Num = (n1.Numerator.multiply(n2.Denominator)).subtract(n1.Denominator.multiply(n2.Numerator));
        BigInteger cmmdc = Num.gcd(Den);
        num.Denominator = Den.divide(cmmdc);
        num.Numerator = Num.divide(cmmdc);        
        return num;
    }
    
    public static Number multiply(Number n1, Number n2) {
        
        Number num = new Number();
        if (n1.Numerator == BigInteger.ZERO) return new Number(BigInteger.ZERO,BigInteger.ONE);
        if (n2.Numerator == BigInteger.ZERO) return new Number(BigInteger.ZERO,BigInteger.ONE);
        BigInteger Den = n1.Denominator.multiply(n2.Denominator);
        BigInteger Num = n1.Numerator.multiply(n2.Numerator);
        BigInteger cmmdc = Num.gcd(Den);
        num.Denominator = Den.divide(cmmdc);
        num.Numerator = Num.divide(cmmdc);        
        return num;
    }
    
    public static Number divide(Number n1,Number n2) {
        
        Number num = new Number();
        if(n1.Numerator == BigInteger.ZERO) return new Number(BigInteger.ZERO,BigInteger.ONE);
        BigInteger Den = n1.Denominator.multiply(n2.Numerator);
        BigInteger Num = n1.Numerator.multiply(n2.Denominator);
        BigInteger cmmdc = Num.gcd(Den);
        num.Denominator = Den.divide(cmmdc);
        num.Numerator = Num.divide(cmmdc);        
        return num;
    }
    
    @Override
    public String toString() {
        
      //  return get_type() + " ";
       return this.Numerator + "/" + this.Denominator;
    }
}
