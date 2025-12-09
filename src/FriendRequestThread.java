import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

/**
 * Base thread for handling friend request
 */
public class FriendRequestThread implements Runnable {
    public static Map<UniversityStudent, List<UniversityStudent>> friendHistory = new ConcurrentHashMap<>();
    private UniversityStudent sender;
    private UniversityStudent reciever;
    // Static semaphor to ensure thread-safe friend request operation
    private static final Semaphore semaphore = new Semaphore(1);
    /**
     *
     * Creates a Thread that sends friend request to other students
     *
     * @param sender, the student who is sending the request
     * @param receiver, the student who is recieving the request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
        this.sender = sender;
        this.reciever = receiver;
    }

    private static void recordFriendRequest(UniversityStudent sender, UniversityStudent reciever){
        List<UniversityStudent> senderList = friendHistory.computeIfAbsent(reciever, k -> new CopyOnWriteArrayList<>());
        senderList.add(sender);
    }

    /**
     *
     * The loop that will run always while the thread
     * hasn't terminated
     *
     */
    @Override
    public void run() {
        // Method signature only
        // Method signature only
        try{
            recordFriendRequest(sender, reciever);

            semaphore.acquire();
            // Simulate sending friend request
            System.out.println("FriendRequest (Thread-Safe): " + sender.name + " sent a friend request to " + reciever.name);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("FriendRequest interrupt: " + e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}
