import java.util.*;

/**
 * A Graph representing the connections each student has with each other
 */
public class StudentGraph {
    /**
     * Inner class representing the edge between nodes
     */
    public static class Edge{
        public UniversityStudent neighbor;
        public int weight;

        /**
         * Constructor representing the edge
         * @param neighbor the student
         * @param weight the connection strength
         */
        public Edge(UniversityStudent neighbor, int weight){
            this.neighbor = neighbor;
            this.weight = weight;
        }

        @Override
        public String toString(){
            return "(" + neighbor.name + ", " + weight + ")";
        }
    }

    private Map<UniversityStudent, List<Edge>> adjacencyList;


    /**
     * Builds an undirected graph with each edge being a pairing of students
     * @param students a list of university students to build the graph
     */
    public StudentGraph(List<UniversityStudent> students){
        adjacencyList = new HashMap<>();

        // Initialize nodes
        for (UniversityStudent s: students){
            adjacencyList.put(s, new ArrayList<>());
        }

        // Create graph with every pair of students
        // Weight being calculated by calculateWeight method
        for (int i = 0; i < students.size(); i++){
            for (int j = i + 1; j < students.size(); j++){
                UniversityStudent s1 = students.get(i);
                UniversityStudent s2 = students.get(j);
                int weight = s1.calculateConnectionStrength(s2);
                if (weight > 0){
                    addEdge(s1, s2, weight);
                }
            }
        }

    }

    /**
     * Adds a weighted edge between two students
     * @param s1 student 1
     * @param s2 student 2
     * @param weight the connection strength between the two
     */
    public void addEdge(UniversityStudent s1, UniversityStudent s2, int weight){
        adjacencyList.get(s1).add(new Edge(s2, weight));
        adjacencyList.get(s2).add(new Edge(s1, weight));
    }

    /**
     * Returns all the edges (students and weights) of a student
     * @param s the student to get all edges
     * @return a list of all the edges
     */
    public List<Edge> getNeighbors(UniversityStudent s){
        return adjacencyList.get(s);
    }

    /**
     * Return all the students in the graph
     * @return a set of all the nodes (set use to remove duplicates)
     */
    public Set<UniversityStudent> getAllNodes(){
        return adjacencyList.keySet();
    }

    /**
     * Displays the graph
     */
    public void displayGraph(){
        System.out.println("\nStudent Graph:");
        for (UniversityStudent s : adjacencyList.keySet()){
            System.out.println(s.name + " -> " + adjacencyList.get(s));
        }
    }
}
