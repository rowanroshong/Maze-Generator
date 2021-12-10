import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MazeGenerator {

    public static void main(String[] args){
        // set up scanner
        Scanner s = new Scanner(System.in);
        String save = "";
        String repeat = "";
        int n;
        int m;

        mazeloop:
        while(true) {
            System.out.println("--------------------------------------------------");
            System.out.println("Please select how many rows you would like in your maze:");

            // Retrieve valid input for m
            mLoop:
            while(true){
                try{
                    m = s.nextInt();
                    break mLoop;
                }
                catch(InputMismatchException e) {
                    System.out.println("Please input an integer");
                    s.nextLine();
                }
            }

            System.out.println("Please select how many columns you would like in your maze:");

            // Retrieve valid input for n
            nLoop:
            while(true){
                try{
                    n = s.nextInt();
                    break nLoop;
                }
                catch(InputMismatchException e) {
                    System.out.println("Please input an integer");
                    s.nextLine();
                }
            }

            System.out.println("--------------------------------------------------\n");

            // Calculate number of edges
            int horizontalEdges = n * (m - 1);
            int verticalEdges = m * (n - 1);
            int numEdges = verticalEdges + horizontalEdges;

            // Instantiate Maze
            Maze maze = new Maze(m, n);

            // Create maze with checkEdge() function and for loop
            for (int i = 0; i < numEdges; i++) {
                maze.checkEdge();
            }

            // Print maze and ask for new input
            System.out.println(maze.toString() + "\n");
            System.out.println("Would you like to save your maze? (yes or no)");

            // Retrieve valid input
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

            // Retrieve valid input
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

        // Exit with message
        System.out.println("Thank You!");
    }


}