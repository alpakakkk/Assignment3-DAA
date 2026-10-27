
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
                broken[count++] = i;
            }
        }

        return Arrays.copyOf(broken, count);
    }

    // Mission 2: Kruskal algorithm
    public static int[] cheapestMaze(Input.Walls w) {

        Integer[] order = new Integer[w.count()];

        for (int i = 0; i < w.count(); i++) {
            order[i] = i;
        }

        // Sort walls by cost
        Arrays.sort(
            order,
            Comparator.comparingInt(i -> w.cost()[i])
        );

        DisjointSet dsu = new DisjointSet(w.cells());

        int[] broken = new int[w.cells() - 1];
        int count = 0;

        for (int i : order) {

            if (dsu.union(w.a()[i], w.b()[i])) {

                broken[count++] = i;

                if (count == broken.length) {
                    break;
                }
            }
        }

        return Arrays.copyOf(broken, count);
    }

    // Mission 2: Prim algorithm
    public static long primCost(Input.Walls w) {

        int n = w.cells();

        int[] degree = new int[n];

        // Count walls connected to each cell
        for (int i = 0; i < w.count(); i++) {
            degree[w.a()[i]]++;
            degree[w.b()[i]]++;
        }

        // Create adjacency lists
        int[][] edges = new int[n][];

        for (int i = 0; i < n; i++) {
            edges[i] = new int[degree[i]];
        }

        int[] position = new int[n];

        for (int i = 0; i < w.count(); i++) {

            int a = w.a()[i];
            int b = w.b()[i];

            edges[a][position[a]++] = i;
            edges[b][position[b]++] = i;
        }

        boolean[] visited = new boolean[n];

        PriorityQueue<Integer> pq =
            new PriorityQueue<>(
                Comparator.comparingInt(i -> w.cost()[i])
            );

        // Start from cell 0
        visited[0] = true;

        for (int edge : edges[0]) {
            pq.add(edge);
        }

        long totalCost = 0;
        int visitedCount = 1;

        while (!pq.isEmpty() && visitedCount < n) {

            int edge = pq.poll();

            int a = w.a()[edge];
            int b = w.b()[edge];

            if (visited[a] && visited[b]) {
                continue;
            }

            int next = visited[a] ? b : a;

            visited[next] = true;
            visitedCount++;

            totalCost += w.cost()[edge];

            for (int e : edges[next]) {
                pq.add(e);
            }
        }

        return totalCost;
    }
}
