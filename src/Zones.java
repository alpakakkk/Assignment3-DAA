
import java.util.*;

public class Zones {

    // Mission 4: Kosaraju Algorithm
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

    // Mission 5: Fastest Route
    public static long[] routes(
        Input.Slides s,
        int[] zoneOf,
        int zoneCount
    ) {

        // Cheapest slide between two zones
        HashMap<Long, Integer> minEdge = new HashMap<>();

        for (int i = 0; i < s.count(); i++) {

            int a = zoneOf[s.from()[i]];
            int b = zoneOf[s.to()[i]];

            if (a == b) {
                continue;
            }

            long key = ((long) a << 32)
                | (b & 0xffffffffL);

            minEdge.merge(
                key,
                s.time()[i],
                Math::min
            );
        }

        // Build graph of zones
        @SuppressWarnings("unchecked")
        ArrayList<long[]>[] outgoing =
            new ArrayList[zoneCount];

        for (int i = 0; i < zoneCount; i++) {
            outgoing[i] = new ArrayList<>();
        }

        for (Map.Entry<Long, Integer> entry
                : minEdge.entrySet()) {

            long key = entry.getKey();

            int a = (int) (key >>> 32);
            int b = (int) key;

            int cost = entry.getValue();

            outgoing[a].add(new long[]{b, cost});
        }

        // Longest chain of zones
        long[] longest = new long[zoneCount];
        Arrays.fill(longest, 1);

        // Shortest route
        long INF = Long.MAX_VALUE / 4;

        long[] shortest = new long[zoneCount];
        Arrays.fill(shortest, INF);

        int startZone = zoneOf[0];
        int exitZone = zoneOf[s.n() - 1];

        shortest[startZone] = 0;

        // Process zones in topological order
        for (int a = 0; a < zoneCount; a++) {

            for (long[] edge : outgoing[a]) {

                int b = (int) edge[0];
                long cost = edge[1];

                longest[b] = Math.max(
                    longest[b],
                    longest[a] + 1
                );

                if (shortest[a] != INF) {

                    shortest[b] = Math.min(
                        shortest[b],
                        shortest[a] + cost
                    );
                }
            }
        }

        long stages = 0;

        for (long length : longest) {
            stages = Math.max(stages, length);
        }

        return new long[]{
            stages,
            shortest[exitZone]
        };
    }
}
