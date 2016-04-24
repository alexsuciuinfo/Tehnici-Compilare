
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Alexandru
 */
public class State {

    public Set<Item> productions = new HashSet<Item>();
    public int state_number;

    public boolean add(Item it) {
        for (Item item : productions) {
            if (item.equals(it)) {
                return false;
            }
        }
        productions.add(it);
        return true;
    }

    public boolean isEmpty() {
        return productions.isEmpty();
    }

    public State() {

    }

    public State(State st) {
        productions.clear();
        for (Item it : st.productions) {
            this.productions.add(it);
        }
        this.state_number = st.state_number;
    }

    public void show() {
        for (Item item : productions) {
            Production production = item.production;
            int dot_pos = item.dot_pos;
            System.out.print(production.left_symbol + " -> ");
            for (int i = 0; i < production.right_symbols.length; i++) {
                if (i == dot_pos) {
                    System.out.print("." + production.right_symbols[i]);
                } else {
                    System.out.print(production.right_symbols[i]);
                }
            }
            System.out.println();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof State)) {
            return false;
        }
        State obj = (State) other;
        if (productions.size() != obj.productions.size()) {
            return false;
        }
        boolean check;
        for (Item it : productions) {
            check = false;
            for (Item it1 : obj.productions) {
                if (it1.equals(it)) {
                    check = true;
                }
            }
            if (check == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.productions);
        return hash;
    }

}
