import java.io.*;
import java.util.*;

/**
 *
 * @author Alexandru
 */
public class AnalizatorLexical {
    
    static Map <TokenType, Map <String,Integer>> tokenTable = new HashMap <TokenType, Map <String,Integer>> ();
    static Map <String,Integer> tokenId = new HashMap();
    static Map <String,Integer> tokenKey = new HashMap();
    static Map <String,Integer> tokenOp = new HashMap();
    static Map <String,Integer> tokenSep = new HashMap();
    static Map <String,Integer> tokenNum = new HashMap();
    static Map <String,Integer> tokenStr = new HashMap();
    static Map <String,Integer> tokenComm = new HashMap();
    static ArrayList <String> keyWords = new ArrayList <String> 
    (Arrays.asList("if","else","do","while","for","break","void","int","float","double","include","iostream","fstream",
    "main","using","namespace","struct","std","iostream","fstream","class","return","cin","cout"));
    static ArrayList <Character> Operators = new ArrayList <Character> 
    (Arrays.asList('+','*','-','/','{','}','[',']','=','<','>','.','%','!'));
    static ArrayList <Character> Separators = new ArrayList <Character> 
    (Arrays.asList('(',')',';',','));
    static ArrayList <Token> tokens = new ArrayList <Token>();
    static Boolean isOk, isValid;
    
    //End of File
    public static Boolean Eof(RandomAccessFile file) throws Exception {
        
        return file.getFilePointer() >= file.length();
    }
    
    //keyWord from keyWords array 
    public static Token isKeyWord(Token tkn, RandomAccessFile file) throws Exception
    {
       isOk = false;
       char c1 = ' ';
       char c = (char) file.read();
       while (Character.isAlphabetic(c) && !Eof(file))
       {
           tkn.length += 1;
           tkn.name += c;
           c = (char) file.read();
       }

       if(keyWords.contains(tkn.name) && !Character.isDigit(c)){
                isOk = true;
                tkn.type = TokenType.KeyWord;
       }
            else isOk = false;
       
       file.seek(file.getFilePointer()-1);
       return tkn; 
    }
    
    
        //isIdentifier (letter  (letter | digit | {_})*)
        public static Token isIdentifier(Token tkn, RandomAccessFile file) throws Exception
    {
       isOk = false;
       char c = (char) file.read();
       while (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' && !Eof(file)) {
           tkn.length += 1;
           tkn.name += c;
           c = (char) file.read();
       }
          isOk = true;
          tkn.type = TokenType.Identifier;
          file.seek(file.getFilePointer()-1);
      return tkn; 
    }
        
    public static Token isNumber(Token tkn, RandomAccessFile file) throws Exception {
        
        isValid = false;
        tkn.type = TokenType.Number;
        char c ;
        file.seek(file.getFilePointer()-1);
        c = (char) file.read();
        if(c == '0')
        {
            
            if (!Eof(file)) {
                c = (char) file.read();
            
            if (c == 'x' || c == 'X')
            {
                tkn.length++;
                tkn.name += c;
                
                if (!Eof(file)) {
                    
                c = (char) file.read();
                
                while (Character.isDigit(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f' && Eof(file)) {
                    
                    tkn.name += c;
                    tkn.length += 1;
                    c = (char) file.read();
                }
                
                if (Character.isAlphabetic(c)){
                    
                    isValid = false;
                    
                    while (c != ' ' && c != '\n' && c != '\t') {
                        c = (char) file.read();
                        tkn.name += c;
                        tkn.length += 1;
                    }
                    
                    file.seek(file.getFilePointer()-1);
                    tkn.error = "Error : Line : " + tkn.line + " Not a valid number : " + tkn.name;
                    return tkn;
                }
                    
                else{
                    isValid = true;
                    file.seek(file.getFilePointer()-1);
                    tkn.type = TokenType.Number;
                    return tkn;
                }
                
            }
          }
        }
     }  
        else{
         tkn.length = 0;
         tkn.name = "";
        }
        
        while (Character.isDigit(c) && ! Eof(file)) {
                tkn.length += 1;
                tkn.name += c;
                c = (char) file.read();
        }
          
                if (c == '.') {
                    
                tkn.name += c;
                tkn.length += 1;
                
                c = (char) file.read();
                
                while (Character.isDigit(c) && !Eof(file)) {
                        tkn.length += 1;
                        tkn.name += c;
                        c = (char) file.read();
                    }
                    
                    if (Character.isAlphabetic(c) || c == '.'){
                        
                        tkn.name += c;
                        tkn.length += 1;
                        
                        isValid = false;
                        while (c != ' ' && c != '\t' && c != '\n' && !Eof(file)) {
                            tkn.length += 1;
                            tkn.name += c;
                            c = (char) file.read();
                        }
                        
                        file.seek(file.getFilePointer()-1);
                        tkn.error = "Error : Line : " + tkn.line + " Invalid format : " +tkn.name;
                        return tkn;
                    }
                
                    
                    else {
                        isValid = true;
                        file.seek(file.getFilePointer()-1);
                        return tkn;
                    }
                }
                
                else if(!Character.isAlphabetic(c))
                {
                    isValid = true;
                    file.seek(file.getFilePointer()-1);
                    return tkn;
                }
                
                else {
                    isValid = false;
                    while (c  != '\t' && c != ' ' && c != '\n' && !Eof(file)) {
                        tkn.length ++;
                        tkn.name += c;
                        c = (char) file.read();
                }
                   
            tkn.error = "Error : Line : " + tkn.line + " Invalid : " + tkn.name;
            file.seek(file.getFilePointer()-1);
            return tkn;
        }
               
    }
    
     public static Token isComment(Token tkn, RandomAccessFile file) throws Exception {
        
         isValid = false;
         char c,c1=' ';
         c = (char) file.read();
         if (c == '/') {
         while (c != '\n' && c != '\r' && !Eof(file))
             {
                 tkn.length++;
                 tkn.name += c;
                 c = (char) file.read();
             }
         isValid = true;
         tkn.type = TokenType.Comment;
         file.seek(file.getFilePointer()-1);
         }
         
          else if (c == '*') {
             tkn.length++;
             tkn.name += c;
             c = (char) file.read();
             while ((c != '/' || c1 != '*') && !Eof(file)) {
                 tkn.length ++;
                 tkn.name = tkn.name + c ;
                 if (c == '\n') tkn.line ++;
                 c1 = c;
                 c = (char) file.read();             
             }
             
            if (Eof(file)) {
                isValid = false;
                tkn.error = "Error : Comments are not closed";
            }
            
            else {
                tkn.length++;
                tkn.name += '/';
                isValid = true;
                tkn.type = TokenType.Comment;
            }
         }
         
         else{
             file.seek(file.getFilePointer()-1);
             isOperator(tkn,file);
         }        
         return tkn;
     }
     
     public static Token isOperator(Token tkn, RandomAccessFile file) throws Exception {
        
         char c;
         tkn.type = TokenType.Operator;
         file.seek(file.getFilePointer()-1);
         c = (char) file.read();
         
         if (c == '+' && !Eof(file)) {
             c = (char) file.read();
             if (c == '+'){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
          if (c == '-' && !Eof(file)) {
             c = (char) file.read();
             if (c == '-'){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
         if (c == '+' || c == '-' || c == '*' || c == '/' && !Eof(file)) {
             c = (char) file.read();
             if (c == '='){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
         else if (c == '=' && !Eof(file))
         {
             c = (char) file.read();
             if (c == '='){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
         else if (c == '<' && !Eof(file))
         {
                 c = (char) file.read();
             if (c == '<' || c == '='){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
         else if (c == '>' && !Eof(file))
         {
                  c = (char) file.read();
             if (c == '>' || c == '='){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
         else if (c == '!' && !Eof(file))
         {
             c = (char) file.read();
             if (c == '='){
                 tkn.name += c;
                 tkn.length += 1;
                 return tkn;
             }
             else file.seek(file.getFilePointer()-1);
         }
         
         return tkn;
    }
     
  
     public static  Token isString (Token tkn, RandomAccessFile file) throws Exception{
         
         char c;
         tkn.type = TokenType.String;
         if (!Eof(file)) {
         c = (char) file.read();
         
         while (c != '"' && c != '\n' && !Eof(file))
         {
             tkn.name += c;
             tkn.length += 1;
             c = (char) file.read();
         }
         
         if (c == '"')
         {
             isValid = true;
             tkn.name += c;
             tkn.length += 1;
             tkn.type = TokenType.String;
             return tkn;
         }
         
         else
         {
             isValid = false;
             tkn.error = "Error : Line : " + tkn.line + " Expected \"";
             file.seek(file.getFilePointer()-1);
             return tkn;
         }
     }
         else {
             tkn.error = "Error : Line : " + tkn.line + " Expected \"";
             return tkn;
         }
  }
     
     public static void addHash(Token tkn, Map <String,Integer> map) {
         
         if(map.containsKey(tkn.name))
                    map.put(tkn.name, map.get(tkn.name) + 1);
                    else map.put(tkn.name,1);
                tokenTable.put(tkn.type, map);
     }
        
     public static void addToken(Token tkn) {
         
         switch (tkn.type) {
             case String : addHash(tkn,tokenStr); break;
             case Identifier : addHash(tkn,tokenId); break;
             case KeyWord : addHash(tkn,tokenKey); break;
             case Operator : addHash(tkn,tokenOp); break;
             case Separator : addHash(tkn,tokenSep); break;
             case Comment : addHash(tkn,tokenComm); break;
             case Number : addHash(tkn,tokenNum); break;
         }
         
         tokens.add(tkn);
     }
    
     public static int skipLines(char c, RandomAccessFile file) throws Exception{
         
         int lines = 0;
           //sar peste spatii, linii noi
        while (c == ' ' || c == '\n' || c == '\t' || c == '\r' && file.getFilePointer() < file.length())
        {
            if(c == '\n' || c == '\r') lines++;
            c = (char) file.read();
        }
        return lines;
     }
    
    public static Token findTokens(int currPointer, int prvTknLength,int lin, RandomAccessFile file) throws Exception
    {
       
        char c;
        Token currToken = new Token("",null,0,lin,currPointer + prvTknLength,"");
        c = (char) file.read();
        
               //sar peste spatii, linii noi
        while (c == ' ' || c == '\n' || c == '\t' || c == '\r' && file.getFilePointer() < file.length())
        {
            if(c == '\n') currToken.line += 1;
            c = (char) file.read();
        } 
        
        currToken.pointer = (int)file.getFilePointer();
        
        
        currToken.length = 1;
        currToken.name += c;
        
        if(file.getFilePointer() >= file.length()) 
            return currToken;
            
        
        
        
        //verific daca poate fi identificator sau cuvant cheie
        if(Character.isAlphabetic(c))
        {
            currToken = isKeyWord(currToken,file);
           
            if(isOk) {                
                addToken(currToken);
            }
            
            else {
                
                currToken = isIdentifier(currToken,file);
                if (isOk) {
                    addToken(currToken);
                }
                else{
                    
                    System.out.println(currToken.error);
                }   
            }   
        }
        
        else if (Operators.contains(c))
        {
            if(c == '/') {
                currToken = isComment(currToken,file);
                if (isValid) addToken(currToken);
                else System.out.println(currToken.error);
            
            }
            else { 
                currToken = isOperator(currToken,file);
                addToken(currToken);   
            }
        }
        
        else if (Separators.contains(c))
        {
            currToken.type = TokenType.Separator;
            addToken(currToken);
        }
        
        else if (c == '"')
        {
            currToken = isString(currToken,file);
            if (isValid){
                addToken(currToken);
            }
            else{
                System.out.println(currToken.error);
            }
        }
        
        else if (Character.isDigit(c)) 
        {
            currToken = isNumber(currToken,file);
            if (isValid) {
                addToken(currToken);
            }
            
            else System.out.println(currToken.error);
        }
  
        return currToken;
    }
    
    public static void main(String[] args) throws Exception {
        
        RandomAccessFile fisier = new RandomAccessFile("fisier.txt","r");
        PrintWriter writer = new PrintWriter("fisout.txt");
        Token tkn = new Token();
        int currentPointer = 0 , TokenLength = 0, lin = 0;
        
        tkn = findTokens (0,0,1,fisier);
        
        while (fisier.getFilePointer() < fisier.length())
        {
           tkn = findTokens(tkn.pointer,tkn.length,tkn.line,fisier);
        }
        
        Iterator<Map.Entry<TokenType, Map<String,Integer>>> iterator = tokenTable.entrySet().iterator() ;
        while(iterator.hasNext()){
            Map.Entry<TokenType, Map<String,Integer>> tokenTab = iterator.next();
            System.out.println(tokenTab.getKey() +" :: "+ tokenTab.getValue());
            writer.println(tokenTab.getKey() +" :: "+ tokenTab.getValue());
            //iterator.remove();
        }
        
        for (int i=0; i<tokens.size(); i++) {
            tokens.get(i).print();
            tokens.get(i).print_file(writer);
        }
        
        writer.close();
    }
    
}
