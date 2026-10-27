
import java.util.*;

public class Escape {

    public record Result(
        int[] path,
        int visited,
        int deadEnds
    ) {}

    public static Result escape(Input.Walls w, int[] broken) {

        int n = w.cells();
        int cols = w.cols();

        // Get the graph of open doors
        int[][] graph = Graphs.open(w, broken);

        // Count dead ends
        int deadEnds = 0;

        for (int[] neighbours : graph) {
            if (neighbours.length == 1) {
                deadEnds++;
            }
        }

        // Sort neighbours:
        // up, right, down, left
        for (int u = 0; u < n; u++) {

            for (int i = 1; i < graph[u].length; i++) {

                int v = graph[u][i];
                int j = i - 1;

                while (
                    j >= 0 &&
                    rank(u, graph[u][j], cols)
                    > rank(u, v, cols)
                ) {
                    graph[u][j + 1] = graph[u][j];
                    j--;
                }

                graph[u][j + 1] = v;
            }
        }

        boolean[] seen = new boolean[n];

        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        int[] cursor = new int[n];
        int[] stack = new int[n];

        int top = 0;
        int marked = 1;

        stack[top] = 0;
        seen[0] = true;

        // Iterative DFS
        while (top >= 0 && !seen[n - 1]) {

            int u = stack[top];

            if (cursor[u] == graph[u].length) {
                top--;
                continue;
            }

            int v = graph[u][cursor[u]++];

            if (!seen[v]) {

                seen[v] = true;
                parent[v] = u;

                marked++;
                stack[++top] = v;
            }
        }

        if (!seen[n - 1]) {
            return new Result(new int[0], marked, deadEnds);
        }

        // Calculate path length
        int length = 0;

        for (int v = n - 1; v != -1; v = parent[v]) {
            length++;
        }

        int[] path = new int[length];

        int index = length - 1;

        // Restore path from exit to start
        for (int v = n - 1; v != -1; v = parent[v]) {
            path[index--] = v;
        }

        return new Result(path, marked, deadEnds);
    }

    // Direction priority
    private static int rank(int u, int v, int cols) {

        if (v == u - cols) {
            return 0; // Up
        }

        if (v == u + 1) {
            return 1; // Right
        }

        if (v == u + cols) {
            return 2; // Down
        }

        return 3; // Left
    }
}
