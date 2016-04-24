
import java.io.PrintWriter;

/**
 *
 * @author Alexandru
 */
public class Token {
    
    TokenType type;
    int length, line, pointer;
    String error,name;
    
    
    Token() {}
    
    Token(String n,TokenType ty, int len, int lin, int point, String err)
    {
        this.name = n;
        this.type = ty;
        this.length = len;
        this.line = lin;
        this.pointer = point;
        this.error = err;
    }
    
    public void print() {
        System.out.println("Type : " +  this.type + " Name : " + this.name + " Line : " + this.line);
    }
    
    public void print_file(PrintWriter out) {
        out.println("Type : " +  this.type + " Name : " + this.name + " Line : " + this.line);
    }
    
}
