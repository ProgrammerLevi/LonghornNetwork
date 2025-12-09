import java.util.*;

/**
 * Base class for a university student
 */
public class UniversityStudent extends Student {
    private UniversityStudent roommate;
    /**
     *
     * Constructor for the UniversityStudent class
     *
     * @param name, the name of the student
     * @param age, the age of the student
     * @param gender, the gender of the student
     * @param year, the current year the student is in
     * @param major, the major the student is taking
     * @param gpa, the current gpa of the student
     * @param roommatePreferences, a list of preferences for what roommate they want
     * @param previousInternships, a list of internships the student took
     */
    public UniversityStudent(String name, int age, String gender, int year,
                             String major, double gpa, List<String> roommatePreferences, List<String> previousInternships){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.year = year;
        this.major = major;
        this.gpa = gpa;
        this.roommatePreferences = roommatePreferences;
        this.previousInternships = previousInternships;
        this.roommate = null;
    }

    /**
     * Sets the students roommate.
     * @param student the student to be the roommate
     */
    public void setRoommate(UniversityStudent student){
        this.roommate = student;
    }

    /**
     * Returns the students roommate if they have one.
     * @return the roommate of the student, null if no roommate
     */

    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public int getYear() { return year; }
    public String getMajor() { return major; }
    public double getGpa() { return gpa; }
    public List<String> getRoommatePreferences() { return roommatePreferences; }
    public List<String> getPreviousInternships() { return previousInternships; }
    public UniversityStudent getRoommate(){
        return this.roommate;
    }
    /**
     * Generates a university student from a HashMap
     * This is used for parsing data
     * @param data a hash map of parameters for university student
     * @return a university student based on data
     */
    // Factory method to create a UniversityStudent from a map o key/value pairs
    public static UniversityStudent fromMap(Map<String, String> data){
        // Retrieve required fields
        String name = data.get("Name");
        String ageStr = data.get("Age");
        String gender = data.get("Gender");
        String yearStr = data.get("Year");
        String major = data.get("Major");
        String gpaStr = data.get("GPA");
        String roommatePrefStr = data.get("RoommatePreferences");
        String previousInternshipsStr = data.get("PreviousInternships");

        // Check if any fields are missing
        if (name == null){
            throw new IllegalArgumentException("Missing required field 'Name' in student entry.");
        }
        if (ageStr == null){
            throw new IllegalArgumentException("Missing required field 'Age' in student entry for " + name + ".");
        }
        if (gender == null){
            throw new IllegalArgumentException("Missing required field 'Gender' in student entry for " + name + ".");
        }
        if (yearStr == null){
            throw new IllegalArgumentException("Missing required field 'Year' in student entry for " + name + ".");
        }
        if (major == null){
            throw new IllegalArgumentException("Missing required field 'Major' in student entry for " + name + ".");
        }
        if (gpaStr == null){
            throw new IllegalArgumentException("Missing required field 'GPA' in student entry for " + name + ".");
        }
        if (roommatePrefStr == null){
            throw new IllegalArgumentException("Missing required field 'RommmatePreferences' in student entry for " + name + ".");
        }
        if (previousInternshipsStr == null){
            throw new IllegalArgumentException("Missing required field 'PreviousInternships' in student entry for " + name + ".");
        }

        int age;
        int year;
        double gpa;
        // Try to parse numerical values
        try{
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Invalid number format for age: '" + ageStr + "' in student entry for " + name + ".");
        }
        try{
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Invalid number format for year: '" + yearStr + "' in student entry for " + name + ".");
        }
        try{
            gpa = Double.parseDouble(ageStr);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Invalid number format for gpa: '" + gpaStr + "' in student entry for " + name + ".");
        }

        // Parse comma-separated fields
        List<String> roommatePreferences = new ArrayList<>();
        for (String pref : roommatePrefStr.split(",")){
            pref = pref.trim();
            if (!pref.isEmpty()){
                roommatePreferences.add(pref);
            }
        }

        List<String> previousInternships = new ArrayList<>();
        for (String intern : previousInternshipsStr.split(",")){
            intern = intern.trim();
            if (!intern.isEmpty()){
                previousInternships.add(intern);
            }
        }

        return new UniversityStudent(name, age, gender, year, major, gpa, roommatePreferences, previousInternships);
    }

    @Override
    public String toString() {
        return "UniversityStudent{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", year=" + year +
                ", major='" + major + '\'' +
                ", gpa=" + gpa +
                ", roommatePreferences=" + roommatePreferences +
                ", previousInternships=" + previousInternships +
                ", roommate=" + (roommate == null ? "none" : roommate.getName()) +
                '}';
    }


    /**
     *
     * Compute the compatibility of this student with another
     * as potential roommates
     *
     * @param other, the other student being measured
     * @return the compatibility score, the higher, the greater compatibility
     */
    public int calculateConnectionStrength(Student other){
        int strength = 0;
        // see if student is a university student
        if (other instanceof UniversityStudent){
            UniversityStudent o = (UniversityStudent) other;
            // +4 for roommate that is assigned
            if (this.roommate != null && this.roommate.equals(o)){
                strength += 4;
            }
            // + 3 for each internship that's the same
            for (String internship : this.previousInternships){
                if (o.previousInternships.contains(internship)){
                    strength += 3;
                }
            }
            // +2 for same major
            if (this.major.equals(o.major)){
                strength += 2;
            }
            // +1 for same age
            if (this.age == o.age){
                strength += 1;
            }

        }
        return strength;
    }

}

