import java.util.*;

/**
 * Base class for all students
 */
public abstract class Student {
    protected String name;
    protected int age;
    protected String gender;
    protected int year;
    protected String major;
    protected double gpa;
    public List<String> roommatePreferences;
    protected List<String> previousInternships;

    /**
     * Calculates the connection for roommmate with another student
     * @param other, the other student being prepared
     * @return
     */
    public abstract int calculateConnectionStrength(Student other);
}
