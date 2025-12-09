import java.io.*;
import java.util.*;

/**
 * Base class for recieving data from file for student
 */
public class DataParser {
    /**
     * reads all the students in a file and creates a list of them
     * @param filename, the file path for the file to be read
     * @return a list of students from the file
     * @throws IOException checks if the file can be read or not
     */
    public static List<UniversityStudent> parseStudents(String filename) throws IOException {
        List<UniversityStudent> students = new ArrayList<>();
        // try to open the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            // Create map to hold data
            Map<String, String> currentData = new HashMap<>();
            String line;
            // Read line
            line = reader.readLine();
            // Keep reading until there's no more lines
            while (line != null){
                String trimmed = line.trim();
                // if line is blank, then end of student
                if (trimmed.isEmpty()){
                    if(!currentData.isEmpty()){
                        // create the student class
                        UniversityStudent student = UniversityStudent.fromMap(currentData);
                        students.add(student);
                        currentData.clear();
                    }
                    line = reader.readLine();
                    continue;
                }

                // Split data
                String[] parts = trimmed.split(":", 2);

                String key = parts[0].trim();
                String value = parts[1].trim();

                // for "Student", if we have data, then get rid of data
                if (key.equals("Student")){
                    if(!currentData.isEmpty()){
                        UniversityStudent student = UniversityStudent.fromMap(currentData);
                        students.add(student);
                        currentData.clear();
                    }
                } else{
                    // Store data from file into hash map
                    currentData.put(key, value);
                }
                line = reader.readLine();
            }
            // add last student
            if (!currentData.isEmpty()){
                UniversityStudent student = UniversityStudent.fromMap(currentData);
                students.add(student);
            }
        }
        return students;
    }
}
