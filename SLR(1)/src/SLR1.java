import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import javafx.util.Pair;

/**
 *
 * @author Alexandru
 */
public class SLR1 extends Operation {

    public Map<String, Set<String>> First = new HashMap<>();
    public Map<String, Set<String>> Follow = new HashMap<>();
    public Set<State> DFA_States = new HashSet<>();
    public Map<Pair<Integer, String>, Integer> DFA_Transitions = new HashMap<>();
    public Map<Pair<Integer, String>, String> Action = new HashMap<>();
    public Map<Pair<Integer, String>, String> Goto = new HashMap<>();

    public void generate_First(Grammar grammar) {

        //lambda
        String lambda = grammar.lambda;

        // if X is a terminal then First(X) = {X}
        for (String terminal : grammar.terminals) {
            First.put(terminal, new HashSet<String>());
            First.get(terminal).add(terminal);
        }

        //if X is a nonterminal then First(X) = {}
        for (String nonterminal : grammar.nonterminals) {
            First.put(nonterminal, new HashSet<String>());
        }

        // if X -> a_ First(X) = First(X) U {a}
        for (Production prod : grammar.productions) {
            if (prod.right_symbols.length == 1) {
                if (prod.right_symbols[0].contains(lambda)) {
                    First.get(prod.left_symbol).add(lambda);
                }
            }
        }

        // pt prod A -> B_ First(A) = First(B) U First(A)
        boolean check = true;
        while (check) {
            check = false;
            for (Production prod : grammar.productions) {
                String start = prod.left_symbol;

                String[] first_elems = prod.right_symbols;
                int index = 0;

                Set<String> first_n = First_n(first_elems, grammar, 0, first_elems.length);
                if (First.get(start).addAll(first_n)) {
                    check = true;
                }
            }
        }
    }

    //generate First(X1X2...Xn)
    public Set<String> First_n(String[] seq, Grammar grammar, int inc, int fin) {
        //First(alpha) = First(X1) - lambda
        Set<String> first = new HashSet<String>(First.get(seq[inc]));
        first.remove(grammar.lambda);
        int index = inc;

        while (index < fin - 1 && First.get(seq[index]).equals(grammar.lambda)) {
            for (String first_elem : First.get(seq[index])) {
                first.add(first_elem);
            }
            first.remove(grammar.lambda);
            index++;
        }

        if (index == fin - 1) {
            if (First.get(seq[index]).contains(grammar.lambda)) {
                first.add(grammar.lambda);
            }
        }
        return first;
    }

    public void genrate_Follow(Grammar grammar) {

        //lambda
        String lambda = grammar.lambda;

        // if X is a terminal then Follow(X) = {}
        for (String terminal : grammar.terminals) {
            Follow.put(terminal, new HashSet<String>());
        }

        //if X is a nonterminal then Follow(X) = {}
        for (String nonterminal : grammar.nonterminals) {
            Follow.put(nonterminal, new HashSet<String>());
        }

        Follow.get(grammar.start_symbol).add("$");

        for (Production prod : grammar.productions) {
            int index = 0;
            while (index < prod.right_symbols.length - 1 && grammar.isTerminal(prod.right_symbols[index])) {
                index++;
            }
            if (index < prod.right_symbols.length - 1) {
                Set<String> first_elems = First_n(prod.right_symbols, grammar, index + 1, prod.right_symbols.length);
                first_elems.remove(lambda);
                Follow.get(prod.right_symbols[index]).addAll(first_elems);
                index++;
            }

        }

        boolean check = true;
        while (check) {
            check = false;
            for (Production prod : grammar.productions) {
                int index = prod.right_symbols.length - 1;
                String start = prod.left_symbol;

                if (grammar.isNonTerminal(prod.right_symbols[index])) {
                    if (Follow.get(prod.right_symbols[index]).addAll(Follow.get(start))) {
                        check = true;
                    }
                }
                index = 0;
                while (index < prod.right_symbols.length - 1 && grammar.isTerminal(prod.right_symbols[index])) {
                    index++;
                }
                if (index < prod.right_symbols.length - 1) {
                    Set<String> first_elems = First_n(prod.right_symbols, grammar, index + 1, prod.right_symbols.length);
                    if (first_elems.contains(lambda)) {
                        if (Follow.get(prod.right_symbols[index]).addAll(Follow.get(start))) {
                            check = true;
                        }
                    }
                }
            }
        }
    }

    public State closure(State st, Grammar grammar) {
        State new_state = new State(st);
        State inter_state = new State(st);
        Set<Production> productions;
        boolean check = true;
        while (check) {
            check = false;
            new_state = new State(inter_state);
            for (Item item : new_state.productions) {
                if (!item.isfinal()) {
                    String symbol = item.next_symbol();
                    if (grammar.isNonTerminal(symbol)) {
                        productions = get_Production_of(symbol, grammar);
                        for (Production prod : productions) {
                            Item new_item = new Item(prod, 0);
                            if (inter_state.add(new_item)) {
                                check = true;
                            }
                        }
                    }
                }
            }
        }
        return new_state;
    }

    public void generate_DFA(Grammar grammar) {
        int state_number = 0, curr_number = 0;
        String[] str = {grammar.start_symbol};
        Production new_prod = new Production(grammar.start_symbol + "'", str, grammar.get_max() + 1); //add S' -> S
        grammar.nonterminals.add(grammar.start_symbol + "'");
        grammar.productions.add(new_prod);
        Item new_it = new Item(new_prod, 0); //add S' -> .S
        State initial_state = new State();
        initial_state.add(new_it);
        initial_state = closure(initial_state, grammar);
        initial_state.state_number = state_number;

        Map<State, Boolean> visit = new HashMap<State, Boolean>();
        visit.put(initial_state, Boolean.FALSE);
        DFA_States.add(initial_state);

        Iterator<State> iterator = DFA_States.iterator();

        while (iterator.hasNext()) {
            State curr_state = iterator.next();
            if (visit.get(curr_state) == Boolean.TRUE) {
                continue;
            }

            for (String sym : grammar.Sigma()) {
                State new_state = new State();
                for (Item item : curr_state.productions) {
                    if (!item.isfinal()) {
                        if (item.next_symbol().equals(sym)) {
                            new_state.add(item.next_item());
                        }
                    }
                }
                if (!new_state.isEmpty()) {
                    new_state = closure(new_state, grammar);
                    if (!DFA_States.contains(new_state)) {
                        state_number++;
                        new_state.state_number = state_number;
                        DFA_States.add(new_state);
                        visit.put(new_state, Boolean.FALSE);
                        DFA_Transitions.put(new Pair<Integer, String>(curr_state.state_number, sym), new_state.state_number);

                    } else {
                        for (State st : DFA_States) {
                            if (new_state.equals(st)) {
                                curr_number = st.state_number;
                            }
                        }
                        DFA_Transitions.put(new Pair<Integer, String>(curr_state.state_number, sym), curr_number);
                    }
                }
            }
            visit.put(curr_state, Boolean.TRUE);
            iterator = DFA_States.iterator();
        }
    }

    public boolean generate_parser_table(Grammar grammar) {
        String start_sym = grammar.start_symbol + "'";
        String[] str = {grammar.start_symbol};
        Production new_prod = new Production(grammar.start_symbol + "'", str, grammar.get_max() + 1); //add S' -> S
        grammar.nonterminals.remove(grammar.start_symbol + "'");
        grammar.productions.add(new_prod);
        grammar.terminals.remove(grammar.lambda);
        grammar.terminals.add("$");
        String special_sym = "$";

        //initialize with error all symbols
        for (State st : DFA_States) {
            for (String sym : grammar.Sigma()) {
                if (grammar.isTerminal(sym)) {
                    Action.put(new Pair<Integer, String>(st.state_number, sym), "error");
                } else if (grammar.isNonTerminal(sym)) {
                    Goto.put(new Pair<Integer, String>(st.state_number, sym), "error");
                }
            }
        }

        for (State st : DFA_States) {
            for (Item item : st.productions) {
                if (!item.isfinal()) {
                    if (grammar.isTerminal(item.next_symbol())) {
                        String action = "D";
                        Pair transition = new Pair<Integer, String>(st.state_number, item.next_symbol());
                        action += DFA_Transitions.get(transition).toString();
                        if (!Action.get(transition).equals("error") && !Action.get(transition).equals(action)) {
                            return false;
                        }
                        Action.put(transition, action);
                    } else {
                        Pair transition = new Pair<Integer, String>(st.state_number, item.next_symbol());
                        String got = DFA_Transitions.get(transition).toString();
                        if (!Goto.get(transition).equals("error") && !Goto.get(transition).equals(got)) {
                            return false;
                        }
                        Goto.put(transition, got);
                    }
                } else if (item.production.left_symbol.equals(start_sym)) {
                    Pair transition = new Pair<Integer, String>(st.state_number, special_sym);
                    if (!Action.get(transition).equals("error") && !Action.get(transition).equals("accept")) {
                        return false;
                    }
                    Action.put(transition, "accept");
                } else {
                    String reduce = "R";
                    reduce += item.production.number;
                    for (String sym : Follow.get(item.production.left_symbol)) {
                        Pair transition = new Pair<Integer, String>(st.state_number, sym);
                        if (!Action.get(transition).equals("error") && !Action.get(transition).equals(reduce)) {
                            return false;
                        }
                        Action.put(transition, reduce);
                    }
                }
            }
        }
        return true;
    }

    public void show_parser_table(Grammar grammar) {
        Iterator<Map.Entry<Pair<Integer, String>, String>> it_action = Action.entrySet().iterator();
        Iterator<Map.Entry<Pair<Integer, String>, String>> it_goto = Goto.entrySet().iterator();
        int nr_sym = grammar.nonterminals.size() + grammar.terminals.size() + 1;
        int index = 1;
        grammar.terminals.remove(grammar.lambda);
        grammar.terminals.add("$");
        System.out.print("     ");
        for (String str : grammar.terminals) {
            System.out.print(str + "     ");
        }
        for (String str : grammar.nonterminals) {
            if (!str.equals(grammar.start_symbol + "'")) {
                System.out.print(str + "     ");
            }
        }
        System.out.println();
        for (State st : DFA_States) {
            System.out.print(st.state_number + "    ");

            for (String str : grammar.terminals) {
                Pair<Integer, String> pair = new Pair<Integer, String>(st.state_number, str);
                if (Action.containsKey(pair)) {
                    String res = Action.get(pair);
                    System.out.print(res.substring(0, 2) + "    ");
                }
            }

            for (String str : grammar.nonterminals) {
                Pair<Integer, String> pair = new Pair<Integer, String>(st.state_number, str);
                if (Goto.containsKey(pair)) {
                    String res = Goto.get(pair);
                    if (res.equals("error")) {
                        System.out.print("er    ");
                    } else {
                        System.out.print(res + "    ");
                    }
                }
            }
            System.out.println();
        }
    }

    public boolean accept(String[] word, Grammar grammar) {
        int index = 0, curr_state;
        String curr_sym;
        String derivare = "";
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        while (true) {
            curr_state = stack.peek();
            curr_sym = word[index];
            Pair<Integer, String> trans = new Pair<Integer, String>(curr_state, curr_sym);
            if (Action.get(trans).equals("accept")) {
                System.out.print("Este acceptat : ");
                System.out.println(new StringBuilder(derivare).reverse().toString());
                return true;
            }
            if (Action.get(trans).startsWith("D")) {
                stack.push(Integer.parseInt(Action.get(trans).substring(1, Action.get(trans).length())));
                index++;
            } else if (Action.get(trans).startsWith("R")) {
                int number = Integer.parseInt(Action.get(trans).substring(1, Action.get(trans).length()));
                Production prod = grammar.get_Prod_By_number(number);
                derivare = derivare + " " + number;
                for (int i = 0; i < prod.right_symbols.length; i++) {
                    if (!prod.right_symbols[i].equals("$")) {
                        stack.pop();
                    }
                }
                Pair<Integer, String> goto_pair = new Pair<Integer, String>(stack.peek(), prod.left_symbol);
                stack.push(Integer.parseInt(Goto.get(goto_pair)));
            } else {
                return false;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        int nr_terminal, nr_nonterminal, nr_production;
        String input;
        Grammar gr = new Grammar();
        Set<Production> productions = new HashSet<>();
        Scanner sc = new Scanner(new FileInputStream("grammar.txt"));
        Scanner read = new Scanner(System.in);

        //read non-terminals
        nr_nonterminal = sc.nextInt();
        for (int i = 0; i < nr_nonterminal; i++) {
            input = sc.next();
            gr.nonterminals.add(input);
        }

        // read terminals
        nr_terminal = sc.nextInt();
        for (int i = 0; i < nr_terminal; i++) {
            input = sc.next();
            gr.terminals.add(input);
        }

        input = sc.next();
        gr.start_symbol = input;

        //read production
        nr_production = sc.nextInt();
        input = sc.nextLine();
        for (int i = 0; i < nr_production; i++) {
            input = sc.nextLine();
            String[] words = input.split("\\s+");
            String[] right = new String[words.length - 1];
            for (int j = 0; j < words.length; j++) {
                // words[j] = words[j].replaceAll("[^\\w]", "");
                if (j != 0) {
                    right[j - 1] = words[j];
                }
            }
            productions.add(new Production(words[0], right, i + 1));
        }

        //add productions
        gr.productions = productions;

        //contains lambda ?
        input = sc.next();
        if (input.equals("y")) {
            input = sc.next();
            gr.lambda = input;
            gr.terminals.remove(gr.lambda);
            gr.lambda = "$";
            gr.terminals.add(gr.lambda);
            for (Production p : gr.productions) {
                for (int i = 0; i < p.right_symbols.length; i++) {
                    if (p.right_symbols[i].equals(input)) {
                        p.right_symbols[i] = gr.lambda;
                    }
                }
            }
        } else {
            gr.lambda = "$";
        }

        SLR1 slr = new SLR1();
        slr.generate_First(gr);
        slr.genrate_Follow(gr);
        slr.generate_DFA(gr);
        if (!slr.generate_parser_table(gr)) {
            System.out.println("Nu este SLR(1)");
        } else {
            System.out.println("Este SLR(1)");
            System.out.println("Stari si tranzitii : ");
            Iterator<State> iterator3 = slr.DFA_States.iterator();

            while (iterator3.hasNext()) {
                State stat = iterator3.next();
                System.out.println(stat.state_number + ":");
                stat.show();
            }

            Iterator<Map.Entry<Pair<Integer, String>, Integer>> iterator2 = slr.DFA_Transitions.entrySet().iterator();
            while (iterator2.hasNext()) {
                Map.Entry<Pair<Integer, String>, Integer> tokenTab = iterator2.next();
                System.out.println("De la : " + tokenTab.getKey().getKey() + " cu "
                        + tokenTab.getKey().getValue() + " la " + tokenTab.getValue());

                //iterator.remove();
            }
            
            System.out.println("Tabel : ");
            slr.show_parser_table(gr);
            System.out.println("Introduceti cuvinte : ");
            String stop = "";
            while (!stop.equals("stop $")) {
                stop = "";
                while (! (input = read.next()).equals("$")) {
                    stop = stop + input + " ";
                }
                stop = stop + "$";
                if (!stop.equals("stop $")) {
                    String[] words = stop.split("\\s+");
                    if(!slr.accept(words, gr))
                    {
                        System.out.println("Nu este acceptat");
                    }
                }
            }

        }
    }

}
