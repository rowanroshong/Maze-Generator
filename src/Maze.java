import java.util.ArrayList;
import java.util.List;

public class Maze {
    private int m; // Number of rows
    private int n; // Number of columns
    private List<Square> squares; // List of our squares
    private List<Edge> edges; // List of our edges

    public Maze(int m, int n){
        // Instantiate fields
        setM(m);
        setN(n);
        setSquares(createSquares(m, n));
        setEdges(createEdges(m, n));
    }

    // Method that creates our list of squares (for cleaner code in constructor)
    public List<Square> createSquares(int m, int n){
        List<Square> squares = new ArrayList<>();

        int numSquares = n * m;

        for(int i = 0; i < numSquares; i++){
            Square currentSquare = new Square(i);

            // If it is the first square, leave opening
            if(i == 0){
                currentSquare.setW(false);
            }

            // If it is the last square, leave opening
            if(i == (numSquares - 1)){
                currentSquare.setE(false);
            }

            currentSquare.setIndex(i);

            squares.add(currentSquare);
        }

        return squares;
    }

    public String mazeToTextFile(){
        String output = "";
        int numOfSquares = squares.size();

        output += m + " " + n + "\n";

        for(int i = 0; i < numOfSquares; i++){
            Square currentSquare = squares.get(i);
            String curSquareString = currentSquare.toString();
            output += curSquareString;

            if(i != (numOfSquares - 1)) {
                output += "\n";
            }
        }

        return output;
    }

    // Method that creates our list of edges (for cleaner code in constructor)
    public List<Edge> createEdges(int m, int n){
        List<Edge> edges = new ArrayList<>();

        int horizontalEdges = n * (m - 1);
        int verticalEdges = m * (n - 1);
        int numEdges = verticalEdges + horizontalEdges;

        // build vertical edges
        for(int i = 0; i < m; i++){
            int sq1Index = (i * getN());
            int sq2Index = sq1Index + 1;

            for(int j = 0; j < (n-1); j++){
                Square s1 = squares.get(sq1Index);
                Square s2 = squares.get(sq2Index);
                Edge curEdge = new Edge(s1, s2);
                edges.add(curEdge);

                sq1Index++;
                sq2Index++;
            }
        }

        // build horizontal edges
        for(int i = 0; i < n; i++){
            int sq1Index = i;
            int sq2Index = i + n;

            for(int j = 0; j < (m-1); j++){
                Square s1 = squares.get(sq1Index);
                Square s2 = squares.get(sq2Index);
                Edge curEdge = new Edge(s1, s2);
                edges.add(curEdge);

                sq1Index += n;
                sq2Index += n;
            }
        }

        return edges;
    }

    // To string that displays text representation of Maze
    public String toString(){
        String textMaze = "";

        // Add top wall
        textMaze += "+";
        for(int i = 0; i < getN(); i++){
            textMaze += "---+";
        }

        textMaze += "\n";

        // Add Rows
        for(int i = 0; i < getM(); i++){


            for(int j = 0; j < getN(); j++){
                int sqIndex = (i * getN()) + j;
                Square curSquare = getSquares().get(sqIndex);
                boolean w = curSquare.getW();
                boolean e = curSquare.getE();

                // Check left wall
                if(j ==0) {
                    if (w) {
                        textMaze += "|";
                    } else {
                        textMaze += " ";
                    }
                }

                // Add spaces
                textMaze += "   ";

                // Check right wall
                if(e){
                    textMaze += "|";
                } else {
                    textMaze += " ";
                }

            }

            textMaze += "\n";

            for(int j = 0; j < getN(); j++){
                int sqIndex = (i * getN()) + j;
                Square curSquare = getSquares().get(sqIndex);
                boolean s = curSquare.getS();

                textMaze += "+";

                // Check bottom wall
                if(s){
                    textMaze += "---";
                } else {
                    textMaze += "   ";
                }
            }

            textMaze += "+\n";

        }

        return textMaze;
    }

    // Getters and Setters
    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}
