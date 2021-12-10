import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class MazeGenerator {
    private static Maze maze;

    public MazeGenerator(){
    }

    public void generateMaze(){

    }

    // Checks an edge from the maze to see if it has been used yet and if it's valid
    public static void checkEdge(){
        List<Edge> edges = maze.getEdges();
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
            setSides(s1, s2);
        }

        // If Square 1 is on a path and Square 2 isn't, add Square 2 to Square 1's path
        if (p1 != null && p2 == null){
            p1.addEdge(currentEdge);
            s2.setPath(p1);

            // Set the sides of the affected squares
            setSides(s1, s2);
        }

        // If Square 2 is on a path and Square 1 isn't, add Square 1 to Square 2's path
        if (p1 == null && p2 != null){
            p2.addEdge(currentEdge);
            s1.setPath(p2);

            // Set the sides of the affected squares
            setSides(s1, s2);
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
                setSides(s1, s2);
            }
        }

        // remove the edge so it will not be found again
        edges.remove(index);
    }

    public static void setSides(Square s1, Square s2){
        int diff = s2.getIndex() - s1.getIndex();

        if(diff == 1){
            s1.setE(false);
            s2.setW(false);
        }else{
            s1.setS(false);
            s2.setN(false);
        }
    }

    public static int getRandomIndex(int size) {
        int index = (int)(Math.random() * size);
        return index;
    }

    public static void main(String[] args){
        // set up scanner
        Scanner s = new Scanner(System.in);
        String save = "";
        String repeat = "";

        mazeloop:
        while(true) {
            System.out.println("--------------------------------------------------");
            System.out.println("Please select how many rows you would like in your maze:");
            int m = s.nextInt();

            System.out.println("Please select how many columns you would like in your maze:");
            int n = s.nextInt();
            System.out.println("--------------------------------------------------\n");

            int horizontalEdges = n * (m - 1);
            int verticalEdges = m * (n - 1);
            int numEdges = verticalEdges + horizontalEdges;

            maze = new Maze(m, n);

            for (int i = 0; i < numEdges; i++) {
                checkEdge();
            }

            System.out.println(maze.toString() + "\n");
            System.out.println("Would you like to save your maze? (yes or no)");

            saveloop:
            while(true) {
                save = s.next().toLowerCase();
                if(save.equals("yes")){

                    System.out.println("Please type in the filename of your maze:");
                    String filename = s.next() + ".txt";

                    try (PrintWriter out = new PrintWriter(filename)) {
                        out.println(maze.mazeToTextFile());
                        break saveloop;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if(save.equals("no")){
                    break saveloop;
                }
            }

            System.out.println("Would you like make another maze? (yes or no)");

            againloop:
            while(true) {
                repeat = s.next().toLowerCase();
                if(repeat.equals("yes")){
                    break againloop;
                }

                if(repeat.equals("no")){
                    break mazeloop;
                }
            }
        }

        System.out.println("Thank You!");
    }


}