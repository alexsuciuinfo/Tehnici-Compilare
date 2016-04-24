
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Alexandru
 */
public class Operation {
    
    public Set<Production> get_Production_of(String symbol, Grammar gr) {
        Set<Production> productions = new HashSet<Production>();
        for (Production prod : gr.productions) 
            if (prod.left_symbol.equals(symbol)) {
                productions.add(prod);
            }
        return productions;
    }
}
