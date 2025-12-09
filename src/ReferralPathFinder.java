import java.util.*;

/**
 * Base class for path finding algorithms with Student graph
 */
public class ReferralPathFinder {
    private StudentGraph graph;
    /**
     * Helps with finding connections between students using a graph
     *
     * @param graph, a graph of students with their connections to other students
     */
    public ReferralPathFinder(StudentGraph graph) {
        // Constructor
        this.graph = graph;
    }

    /**
     * Finds the shortest path to a student who interned at a
     * specific company.
     * Uses Dijkstra's algorithm
     *
     * @param start, the student to start from
     * @param targetCompany, what company to look for
     * @return a list of university students who had the desired company
     */

    public List<UniversityStudent> findReferralPath(UniversityStudent start, String targetCompany) {
        // Method signature only
        // Maps to store distance of nodes and previous nodes
        Map<UniversityStudent, Double> dist = new HashMap<>();
        Map<UniversityStudent, UniversityStudent> prev = new HashMap<>();
        Set<UniversityStudent> visited = new HashSet<>();

        // Initialize distance with infinity
        for (UniversityStudent s : graph.getAllNodes()){
            dist.put(s, Double.MAX_VALUE);
            prev.put(s, null);
        }
        dist.put(start, 0.0);

        // Priority Queue orders nodes by their current distance
        PriorityQueue<UniversityStudent> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while (!pq.isEmpty()){
            UniversityStudent u = pq.poll();
            if (visited.contains(u)){
                continue;
            }
            visited.add(u);

            // Check if student has a target company
            for (String internship : u.previousInternships){
                if (internship.equalsIgnoreCase(targetCompany)){
                    // Reconstruct the path from star to u
                    List<UniversityStudent> path = new ArrayList<>();
                    UniversityStudent cur = u;
                    while (cur != null){
                        path.add(cur);
                        cur = prev.get(cur);
                    }
                    Collections.reverse(path);
                    return path;
                }
            }
            // Relaxation for neighbors
            for (StudentGraph.Edge edge : graph.getNeighbors(u)){
                UniversityStudent v = edge.neighbor;
                if (visited.contains(v)){
                    continue;
                }

                // Calculate new distance
                double newDist = dist.get(u) + (1.0 / edge.weight);
                if (newDist < dist.get(v)){
                    dist.put(v, newDist);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }

        // Return empty if no student with target company was found
        return new ArrayList<>();
    }
}
