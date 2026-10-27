
import java.util.*;

public class Maze {

    // Mission 1: Build the maze
    public static int[] breakWalls(Input.Walls w) {

        DisjointSet dsu = new DisjointSet(w.cells());

        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int i = 0; i < w.count(); i++) {

            int a = w.a()[i];
            int b = w.b()[i];

            if (dsu.union(a, b)) {
                broken[count] = i;
                count++;
            }
        }

        return Arrays.copyOf(broken, count);
    }

    // Mission 2 will be implemented later
    public static int[] cheapestMaze(Input.Walls w) {
        return new int[0];
    }

    public static long primCost(Input.Walls w) {
        return 0;
    }
}
