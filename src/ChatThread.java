import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

/**
 * Base thread for handling chat messages
 */
public class ChatThread implements Runnable {
    public static Map<UniversityStudent, Map<UniversityStudent, List<String>>> chatHistory = new ConcurrentHashMap<>();
    private UniversityStudent sender;
    private UniversityStudent reciever;
    private String message;
    // Static semaphore to ensure thread-safe chat operation
    private static final Semaphore semaphore = new Semaphore(1);
    /**
     * Creates a thread that will handle chat message
     * Includes sending and receiving messages
     * @param sender, the student who is sending the message
     * @param receiver, the student who is receiving the message
     * @param message, the contents of the message
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        // Constructor
        this.sender = sender;
        this.reciever = receiver;
        this.message = message;
    }

    /**
     * Adds the message to the chat history
     * @param sender, the sender sending the message
     * @param reciever, the student recieving the message
     * @param message, the message being sent
     */
    public static void addMessage(UniversityStudent sender, UniversityStudent reciever, String message){
        Map<UniversityStudent, List<String>> senderHistory = chatHistory.computeIfAbsent(sender, k -> new ConcurrentHashMap<>());
        List<String> messages = senderHistory.computeIfAbsent(reciever, k -> new CopyOnWriteArrayList<>());
        messages.add(message);
    }

    /**
     *
     * The loop that will run always while the thread
     * hasn't terminated
     *
     */
    @Override
    public void run() {
        try{
            addMessage(sender, reciever, message);

            semaphore.acquire();
            // Simulate chat message
            System.out.println("Chat (Thread-Safe): " + sender.name + " to " + reciever.name + ": " + message);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Chat interrupted: " + e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}
