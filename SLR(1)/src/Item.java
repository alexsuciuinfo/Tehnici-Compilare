
import java.util.Objects;


/**
 *
 * @author Alexandru
 */
public class Item  {

    public Production production;
    public int dot_pos;

    public Item(Production prod, int pos) {
        this.production = prod;
        this.dot_pos = pos;
        if (this.production.right_symbols[0].equals("$"))
            this.dot_pos = 1;
    }

    //dot is final
    public boolean isfinal() {
        if (dot_pos >= production.right_symbols.length) {
            return true;
        }
        return false;
    }

    //next symbol after dot
    public String next_symbol() {
        return production.right_symbols[dot_pos];
    }

    //move the dot
    public Item next_item() {
        return new Item(production, dot_pos + 1);
    }
    
    //last symbol
    public String last_symbol() {
        return production.right_symbols[production.right_symbols.length - 1];
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Item)) {
            return false;
        }
        Item obj = (Item) other;
        if (!production.equals(obj.production)) {
            return false;
        }
        if (dot_pos != obj.dot_pos) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.production);
        hash = 89 * hash + this.dot_pos;
        return hash;
    }
}
