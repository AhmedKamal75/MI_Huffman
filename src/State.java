import java.util.ArrayList;
import java.util.Map;

public class State implements Comparable<State>{
    private State left;
    private State right;
    private final ArrayList<Byte> symbol;
    private final double probability; // from the frequency

    public State(ArrayList<Byte> symbol, double probability) {
        this.symbol = symbol;
        this.probability = probability;
    }

    public State(State left, State right, ArrayList<Byte> symbol, double probability) {
        this.left = left;
        this.right = right;
        this.symbol = symbol;
        this.probability = probability;
    }

    public ArrayList<Byte> getSymbol() {
        return symbol;
    }

    public double getProbability() {
        return probability;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public void buildCode(String codeSequence, Map<ArrayList<Byte>, String> codeMap) {
        if (this.isLeaf()){
            codeMap.put(this.symbol,codeSequence);
        } else {
            this.left.buildCode(codeSequence + "0", codeMap);
            this.right.buildCode(codeSequence + "1", codeMap);
        }
    }


    public void printTree(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        for (Byte b : this.symbol) {
            sb.append((char) b.byteValue());
        }
        System.out.println(prefix + (isTail ? "└── " : "├── ") + '[' + sb.toString() + "]:" +  String.format("%.4f",probability));
        if (right != null) {
            right.printTree(prefix + (isTail ? "    " : "│   "), left == null);
        }
        if (left != null) {
            left.printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    @Override
    public int compareTo(State o) {
        double result =  this.probability - o.probability;
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "[" + left + " (" + symbol + "," + probability + ")" + right + "]";
    }
}
