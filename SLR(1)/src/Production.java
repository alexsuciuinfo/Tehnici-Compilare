import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Alexandru
 */
public class Production {

    public String left_symbol;
    public String[] right_symbols;
    public int number;

    public Production(String left, String[] right, int number) {
        this.left_symbol = left;
        this.right_symbols = right;
        this.number = number;
    }
    

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Production)) {
            return false;
        }
        Production obj = (Production) other;
        if (!left_symbol.equals(obj.left_symbol)) {
            return false;
        }
        if (right_symbols.length != obj.right_symbols.length) {
            return false;
        }
        for (int i = 0; i < right_symbols.length; i++) {
            if (!right_symbols[i].equals(obj.right_symbols[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.left_symbol);
        hash = 79 * hash + Arrays.deepHashCode(this.right_symbols);
        return hash;
    }
}
