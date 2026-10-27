
import java.util.*;

public class Zones {

    // Mission 4: Kosaraju algorithm
    public static int[] kosaraju(Input.Slides s) {

        int n = s.n();

        int[][] outgoing = Graphs.slidesFrom(s);
        int[][] incoming = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];

        int[] cursor = new int[n];
        int[] stack = new int[n];
        int[] finish = new int[n];

        int finished = 0;

        // First DFS pass
        for (int start = 0; start < n; start++) {

            if (visited[start]) {
                continue;
            }

            int top = 0;

            stack[top] = start;
            visited[start] = true;

            while (top >= 0) {

                int u = stack[top];

                if (cursor[u] == outgoing[u].length) {

                    finish[finished++] = u;
                    top--;

                } else {

                    int edge = outgoing[u][cursor[u]++];
                    int v = s.to()[edge];

                    if (!visited[v]) {

                        visited[v] = true;
                        stack[++top] = v;
                    }
                }
            }
        }

        // Second DFS pass: reversed graph
        int[] zone = new int[n];

        Arrays.fill(zone, -1);

        int zoneCount = 0;

        for (int i = finished - 1; i >= 0; i--) {

            int start = finish[i];

            if (zone[start] != -1) {
                continue;
            }

            int top = 0;

            stack[top] = start;
            zone[start] = zoneCount;

            while (top >= 0) {

                int u = stack[top--];

                for (int edge : incoming[u]) {

                    int v = s.from()[edge];

                    if (zone[v] == -1) {

                        zone[v] = zoneCount;
                        stack[++top] = v;
                    }
                }
            }

            zoneCount++;
        }

        return zone;
    }

    // Mission 5 will be implemented later
    public static long[] routes(
        Input.Slides s,
        int[] zoneOf,
        int zoneCount
    ) {
        return new long[]{0, 0};
    }
}
