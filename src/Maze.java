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

    // Checks an edge from the maze to see if it has been used yet and if it's valid
    public void checkEdge(){
        List<Edge> edges = this.getEdges();
        int index = getRandomIndex(edges.size());
        Edge currentEdge = edges.get(index);

        Square s1 = currentEdge.getS1();
        Square s2 = currentEdge.getS2();
        Path p1 = s1.getPath();
        Path p2 = s2.getPath();

        // If no paths exist yet, make a new one and connect them
        if(p1 == null && p2 == null){
            Path newPath = new Path();
            newPath.addEdge(currentEdge);
            s1.setPath(newPath);
            s2.setPath(newPath);

            // Set the sides of the affected squares
            s1.setSides(s2);
        }

        // If Square 1 is on a path and Square 2 isn't, add Square 2 to Square 1's path
        if (p1 != null && p2 == null){
            p1.addEdge(currentEdge);
            s2.setPath(p1);

            // Set the sides of the affected squares
            s1.setSides(s2);
        }

        // If Square 2 is on a path and Square 1 isn't, add Square 1 to Square 2's path
        if (p1 == null && p2 != null){
            p2.addEdge(currentEdge);
            s1.setPath(p2);

            // Set the sides of the affected squares
            s1.setSides(s2);
        }

        // If both squares are on their own paths, there are two outcomes
        // First, check if they are on the same path. If so, do nothing and do not add edge to path
        if (p1 != null && p2 != null){
            if(p1.equals(p2)){
                // nothing
            } else {
                // If they aren't on the same path, combine them.
                p1.combinePaths(p2);

                // Set the sides of the affected squares
                s1.setSides(s2);
            }
        }
        // remove the edge so it will not be found again
        edges.remove(index);
    }

    public int getRandomIndex(int size) {
        int index = (int)(Math.random() * size);
        return index;
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
