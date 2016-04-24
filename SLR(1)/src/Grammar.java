import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Alexandru
 */
public class Grammar {
    
    public Set<String> terminals = new HashSet<String>(); 
    public Set<String> nonterminals = new HashSet<String>();
    public Set<Production> productions = new HashSet<Production>();
    public String start_symbol;
    public String lambda;
    
     public Production get_Prod_By_number(int n) {
        for (Production prod : productions)
            if (prod.number == n) return prod;
        return null;
    }
    
    public int get_symbol_pos(String str) {
        int pos = 1;
        for (String sym : terminals) {
            if (sym.equals(str)) return pos;
            pos++;
        }
        pos = 1;
        for (String sym : nonterminals) {
            if (sym.equals(str)) return pos;
            pos++;
        }
        return 0;
    }
    
    public int get_max() {
        int max = 0;
        for (Production prod : productions) {
            if (prod.number > max)
                max = prod.number;
        }
        return max;
    }
    
    public boolean isTerminal(String s) {
        if (terminals.contains(s))
            return true;
        return false;
    }
    
    public boolean isNonTerminal(String s) {
        if (nonterminals.contains(s))
            return true;
        return false;
    }
    
    public Set<String> Sigma() {
        Set<String> sigma = new HashSet<String>();
        sigma.addAll(terminals);
        sigma.addAll(nonterminals);
        return sigma;
    }
       
}
