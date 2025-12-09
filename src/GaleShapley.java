import java.util.*;

/**
 * Class to implement the Gale Shapley Algorithm
 */
public class GaleShapley {
    /**
     * Creates a class that implements the Gale-Shapley matching
     * algorithm, which pairs students based on roommate preferences
     *
     * @param students, the list of students being paired
     */
    public static void assignRoommates(List<UniversityStudent> students) {
        // Map that holds final pairings: each student paired with a roommate
        Map<UniversityStudent, UniversityStudent> roommatePairs = new HashMap<>();
        // Tracks the proposals
        Map<UniversityStudent, Integer> nextProposalIndex = new HashMap<>();
        // Lookup map by student name
        Map<String, UniversityStudent> nameToStudent = new HashMap<>();

        for (UniversityStudent s : students){
            nameToStudent.put(s.name, s);
            nextProposalIndex.put(s, 0);
        }

        // Queue for students who are free and still have preference to propose
        Queue<UniversityStudent> freeStudents = new LinkedList<>();
        for (UniversityStudent s: students){
            if (!s.roommatePreferences.isEmpty()){
                freeStudents.offer(s);
            }
        }

        while (!freeStudents.isEmpty()){
            UniversityStudent s = freeStudents.poll();
            // Skip if s already has a roommate
            if (s.getRoommate() != null){
                continue;
            }

            int index = nextProposalIndex.get(s);
            // check if student has any more preferences
            if (index >= s.roommatePreferences.size()){
                continue;
            }

            String preferredName = s.roommatePreferences.get(index);
            nextProposalIndex.put(s, index + 1);
            UniversityStudent t = nameToStudent.get(preferredName);
            // Check if student exist
            if (t == null){
                if (nextProposalIndex.get(s) < s.roommatePreferences.size()){
                    freeStudents.offer(s);
                }
                continue;
            }

            // If t does not have a preference, continue
            if (!t.roommatePreferences.contains(s.name)){
                if (nextProposalIndex.get(s) < s.roommatePreferences.size()){
                    freeStudents.offer(s);
                }
                continue;
            }
            // if t doesn't have a roommate, pair s with t
            if (t.getRoommate() == null){
                roommatePairs.put(s,t);
                roommatePairs.put(t,s);
                t.setRoommate(s);
                s.setRoommate(t);
            } else{
                // if t is already paired, check if t prefers s over another partner
                UniversityStudent currentPartner = t.getRoommate();
                int currentIndex = t.roommatePreferences.indexOf(currentPartner.name);
                int newIndex = t.roommatePreferences.indexOf(s.name);
                // check if t prefers s over current roommate
                if (newIndex < currentIndex){
                    // pair t with s, get rid of old partner
                    roommatePairs.put(s,t);
                    roommatePairs.put(t,s);
                    roommatePairs.remove(currentPartner);
                    freeStudents.offer(currentPartner);
                    t.setRoommate(s);
                    s.setRoommate(t);
                } else{
                    // reject s
                    if (nextProposalIndex.get(s) < s.roommatePreferences.size()){
                        freeStudents.offer(s);
                    }
                }
            }
        }
        // Print the roommate pairs
        System.out.println("\nRoommate Pairings (Gale-Shapley):");
        Set<UniversityStudent> printed = new HashSet<>();
        for (UniversityStudent s : roommatePairs.keySet()){
            UniversityStudent partner = roommatePairs.get(s);
            if (!printed.contains(s) && !printed.contains(partner)){
                System.out.println(s.name + " paired with " + partner.name);
                printed.add(s);
                printed.add(partner);
            }
        }
    }
}
