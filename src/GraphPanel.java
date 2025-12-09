import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphPanel extends JPanel {
    private UniversityStudent hovered = null;

    private final int NODE_RADIUS = 15;
    private final int HOVER_RADIUS = 22;

    private StudentGraph graph;

    private List<UniversityStudent> students = new ArrayList<>();

    // color per student
    private Map<UniversityStudent, Color> nodeColors = new HashMap<>();

    public GraphPanel(){
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e){
                updateHover(e.getX(), e.getY());
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e){
                if (hovered != null){
                    showStudentPopup(hovered);
                }
            }
        });


    }

    public void setGraph(StudentGraph graph, List<UniversityStudent> students){
        this.graph = graph;
        this.students = students;
        System.out.println(this.students);

        // set default colors
        nodeColors.clear();
        for (UniversityStudent s : this.students){
            nodeColors.put(s, Color.WHITE);
        }
        // refresh page
        repaint();
        //generateGraph();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        // if no graph, don't do anything
        if (graph == null || students == null) return;
        int width = getWidth();
        int height = getHeight();
        // pick radius sos name fits in
        int r = Math.min(width, height) / 3;
        // get center
        int cx = width / 2;
        int cy = height / 2;

        Map<UniversityStudent, Point> coords = new HashMap<>();
        int n = students.size();

        for (int i = 0; i < n; i++){
            double angle = 2 * Math.PI * i / n;
            int x = cx + (int) (r * Math.cos(angle));
            int y = cy + (int) (r * Math.sin(angle));
            UniversityStudent student = students.get(i);
            coords.put(student, new Point(x,y));
        }
        Graphics2D g2 = (Graphics2D) g;
        // turns on antialiasing, makes shapes smoother
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Draw Edges
        for (UniversityStudent s : students){
            for (StudentGraph.Edge e : graph.getNeighbors(s)){
                UniversityStudent t = e.neighbor;
                if (students.indexOf(t) <= students.indexOf(s)) continue;
                Point p1 = coords.get(s);
                Point p2 = coords.get(t);

                g2.setColor(Color.WHITE);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                int mx = (p1.x + p2.x) / 2;
                int my = (p1.y + p2.y) / 2;
                g2.drawString(String.valueOf(e.weight), mx, my);
            }
        }
        // Draw nodes
        for (UniversityStudent s : students) {
            Point p = coords.get(s);

            int radius = (s == hovered ? HOVER_RADIUS : NODE_RADIUS);
            int diameter = radius * 2;

            Color nodeColor = nodeColors.getOrDefault(s, Color.WHITE);

            g2.setColor(nodeColor);
            g2.fillOval(p.x - radius, p.y - radius, diameter, diameter);

            g2.setColor(Color.BLACK);
            g2.drawString(s.getName(), p.x - radius + 3, p.y + 4);

            g2.setColor(Color.WHITE);
        }
    }

    /**
     * Change student node color
     * @param student the node you want to change
     * @param color the new color you want to set
     */
    public void setNodeColor(UniversityStudent student, Color color){
        if (student == null || color == null) return;
        nodeColors.put(student, color);
        repaint();
    }

    public Color getNodeColor(UniversityStudent student){ return nodeColors.get(student);}

    public void clearGraphColor(){
        for (UniversityStudent s : nodeColors.keySet()){
            nodeColors.put(s, Color.WHITE);
        }
        repaint();
    }

    private boolean inCircle(int mx, int my, int cx, int cy, int radius){
        int dx = mx - cx;
        int dy = my - cy;
        return dx*dx + dy*dy <= radius*radius;
    }

    private void updateHover(int mx, int my){
        if (graph == null || students == null) return;

        UniversityStudent newHover = null;

        // get coordinates
        int width = getWidth();
        int height = getHeight();
        int r = Math.min(width, height) / 3;
        int cx = width / 2;
        int cy = height / 2;
        for (int i = 0; i < students.size(); i++){
            double angle = 2 * Math.PI * i / students.size();
            int x = cx + (int) (r * Math.cos(angle));
            int y = cy + (int) (r * Math.sin(angle));

            if (inCircle(mx,my,x,y,NODE_RADIUS)){
                newHover = students.get(i);
                break;
            }
        }

        if (newHover != hovered){
            hovered = newHover;
            repaint();
        }
    }

    private void showStudentPopup(UniversityStudent student){
        String roommate = student.getRoommate() != null ? student.getRoommate().getName() : "None";
        String message = "Student: " + student.getName() + "\nAge: " + student.getAge() + "\nGender: " + student.getGender() +
        "\nYear: " + student.getYear() + "\nMajor: " + student.getMajor() + "\nGPA: " + student.getGpa() + "\nRoommate Preferences: " +
                (student.getRoommatePreferences() == null ? "None" : student.getRoommatePreferences())
                + "\nPrevious Internships: " + (student.getPreviousInternships() == null ? "None" : student.getPreviousInternships())
                + "\nRoommate: " + roommate + "\n\nChat History:\n";
        if (ChatThread.chatHistory.containsKey(student) && !ChatThread.chatHistory.get(student).isEmpty()) {
            Set<UniversityStudent> chatRecievers = ChatThread.chatHistory.get(student).keySet();
            for (UniversityStudent s : chatRecievers){
                message += s.getName() + ": " + ChatThread.chatHistory.get(student).get(s) + "\n";
            }
        } else{
            message += "None\n";
        }

        message += "\nFriend Request:\n";
        if (FriendRequestThread.friendHistory.containsKey(student) && !FriendRequestThread.friendHistory.get(student).isEmpty()){
            for (UniversityStudent s : FriendRequestThread.friendHistory.get(student)){
                message += s.getName() + "\n";
            }
        } else {
            message += "None\n";
        }
        JOptionPane.showMessageDialog(
                this,
                message,
                student.getName(),
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
