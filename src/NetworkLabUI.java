import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class NetworkLabUI extends JFrame {
    private JComboBox<String> testCaseSelected;
    private JTextArea testOutputArea;
    private GraphPanel graphPanel;
    private JButton runTestsButton;
    private JButton generateGraphButton;
    private JButton generatePair;
    private JButton pathFinderButton;
    private JTextField targetCompany;
    private JComboBox startStudent;
    private JButton clearButton;

    private JPanel panel;
    private JPanel top;
    private JPanel wrapperPannel1;
    private JPanel wrapperPannel2;

    private List<List<UniversityStudent>> testCases;
    private final String[] testCaseSelection = {"Test Case 1", "Test Case 2", "Test Case 3", "All Test Cases"};

    private StudentGraph graph;
    private Map<UniversityStudent, Color> nodeColors = new HashMap<>();
    private List<UniversityStudent> pathColors = new ArrayList<>();

    public NetworkLabUI(){
        // Set up for the window
        super("Longhorn Network Lab UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        // get all the test case results
        testCases = Arrays.asList(
                Main.generateTestCase1(),
                Main.generateTestCase2(),
                Main.generateTestCase3()
        );
        // create the UI buttons, panels, etc
        // Colors
        Color bgDark = new Color(30,30,30);
        Color bgPanel = new Color (45,45,45);
        Color bgTextArea = new Color (20,20,20);
        Color textColor = Color.WHITE;
        Color accent = new Color(191, 87, 0);

        // Panels
        panel = new JPanel(new BorderLayout());
        wrapperPannel1 = new JPanel();
        wrapperPannel2 = new JPanel();
        top = new JPanel();
        top.setLayout(new GridLayout(2,1,5,5));

        // ComboBox for test cases
        testCaseSelected = new JComboBox<>(testCaseSelection);

        // Text area for output
        testOutputArea = new JTextArea();
        testOutputArea.setEditable(false);

        // Buttons
        runTestsButton = new JButton("Run Tests");
        runTestsButton.addActionListener(e -> onRunTest());
        generateGraphButton = new JButton("Generate Graph");
        generateGraphButton.addActionListener(e -> generateGraph());
        generatePair = new JButton("Generate Pair");
        generatePair.addActionListener(e -> generateRoommatePairings());
        pathFinderButton = new JButton("Find Path");
        pathFinderButton.addActionListener(e -> generatePathFinder());
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            graphPanel.clearGraphColor();
            nodeColors.clear();
            pathColors.clear();
        });

        // Disable certain buttons on start
        generateGraphButton.setEnabled(false);
        generatePair.setEnabled(false);
        pathFinderButton.setEnabled(false);
        clearButton.setEnabled(false);


        // Text field
        targetCompany = new JTextField(15);

        // Graph Panel
        graphPanel = new GraphPanel();

        // Applying style
        panel.setBackground(bgDark);
        top.setOpaque(true);
        top.setBackground(bgPanel);
        wrapperPannel1.setOpaque(true);
        wrapperPannel1.setBackground(bgPanel);
        wrapperPannel2.setOpaque(true);
        wrapperPannel2.setBackground(bgPanel);

        graphPanel.setBackground(bgDark);
        testOutputArea.setBackground(bgTextArea);

        testOutputArea.setForeground(textColor);

        runTestsButton.setBackground(accent);
        generateGraphButton.setBackground(accent);
        generatePair.setBackground(accent);
        pathFinderButton.setBackground(accent);
        clearButton.setBackground(accent);

        runTestsButton.setForeground(Color.WHITE);
        generateGraphButton.setForeground(Color.WHITE);
        generatePair.setForeground(Color.WHITE);
        pathFinderButton.setForeground(Color.WHITE);
        clearButton.setForeground(Color.WHITE);

        // Styling Buttons
        JLabel testLabel = new JLabel("Selected Test Case:");
        testLabel.setForeground(textColor);
        runTestsButton.setForeground(textColor);
        generateGraphButton.setForeground(textColor);
        generatePair.setForeground(textColor);
        pathFinderButton.setForeground(textColor);
        clearButton.setForeground(textColor);

        Font uiFont = new Font("Segoe UI", Font.PLAIN, 13);
        testOutputArea.setFont(uiFont);
        runTestsButton.setFont(uiFont);
        generateGraphButton.setFont(uiFont);

        // Layout top panel
        wrapperPannel1.add(testLabel);
        wrapperPannel1.add(testCaseSelected);
        wrapperPannel1.add(runTestsButton);
        wrapperPannel1.add(generateGraphButton);
        wrapperPannel1.add(generatePair);
        wrapperPannel1.add(pathFinderButton);
        wrapperPannel1.add(clearButton);

        // Add wrapper panels to center buttons
        top.add(wrapperPannel1);
        top.add(wrapperPannel2);

        // Add top panel to main panel
        panel.add(top, BorderLayout.NORTH);
        // Adding text area as default
        panel.add(testOutputArea, BorderLayout.CENTER);

        // Set final content pane
        setContentPane(panel);

    }
    private void onRunTest(){
        if (graphPanel != null && graphPanel.getParent() == panel){
            graph = null;
            panel.remove(graphPanel);
            panel.add(testOutputArea, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
        // Clear text area and get selected test case
        testOutputArea.setText("");
        String sel = (String) testCaseSelected.getSelectedItem();
        if (sel.equals("All Test Cases")){
            for (int i = 1; i <= testCases.size(); i++){
                runTest(i);
            }
            return;
        } else{
            int num = Integer.parseInt(sel.split(" ")[2]);
            runTest(num);
        }
        // add UI elements when graph is generated
        // enable buttons as well
        if (startStudent == null){
            JLabel targetLabel = new JLabel("Target Company: ");
            targetLabel.setForeground(Color.WHITE);
            wrapperPannel2.add(targetLabel);
            wrapperPannel2.add(targetCompany);
            JLabel startLabel = new JLabel("Start Student: ");
            startLabel.setForeground(Color.WHITE);
            startStudent = new JComboBox();
            wrapperPannel2.add(startLabel);
            wrapperPannel2.add(startStudent);
            generateGraphButton.setEnabled(true);
            generatePair.setEnabled(true);
            pathFinderButton.setEnabled(true);
            clearButton.setEnabled(true);
        }
        startStudent.removeAllItems();
        int index = testCaseSelected.getSelectedIndex();
        List<UniversityStudent> data = testCases.get(index);
        for (UniversityStudent s : data){
            startStudent.addItem(s.getName());
        }
    }

    private void runTest(int n){
        testOutputArea.append("=== Test Case " + n + "===\n");
        List<UniversityStudent> data = testCases.get(n - 1);
        // Print the data onto screen
        data.forEach(s -> testOutputArea.append(s + "\n"));
        testOutputArea.append("\n");
        int score = Main.gradeLab(data, n);
        testOutputArea.append("Test Case " + n + " Score: " + score + "\n\n");
    }

    private void generateGraph(){
        if (testOutputArea.getParent() == panel){
            panel.remove(testOutputArea);
        }
        // Remove old graph
        if (graph != null && graphPanel.getParent() == panel){
            panel.remove(graphPanel);
        }
        int index = testCaseSelected.getSelectedIndex();
        List<UniversityStudent> data = testCases.get(index);
        graph = new StudentGraph(data);
        graphPanel = new GraphPanel();
        graphPanel.setGraph(graph, data);
        graphPanel.setBackground(new Color(30,30,30));
        for(UniversityStudent s : nodeColors.keySet()) nodeColors.put(s, Color.BLACK);
        panel.add(graphPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void generateRoommatePairings(){
        int idx = testCaseSelected.getSelectedIndex();
        List<UniversityStudent> data = testCases.get(idx);

        data.forEach(s -> s.setRoommate(null));
        GaleShapley.assignRoommates(data);
        // Create random so each pairing has a unique color
        Random rand = new Random();

        for (UniversityStudent s : data){
            if (s.getRoommate() != null && s.getName().compareTo(s.getRoommate().getName()) < 0){
                Color randColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                graphPanel.setNodeColor(s, randColor);
                graphPanel.setNodeColor(s.getRoommate(), randColor);
                nodeColors.put(s, randColor);
                nodeColors.put(s.getRoommate(), randColor);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private void generatePathFinder(){
        if (startStudent == null) return;
        int idx = testCaseSelected.getSelectedIndex();
        List<UniversityStudent> data = testCases.get(idx);
        String selectedName = (String) startStudent.getSelectedItem();
        UniversityStudent start = data.stream().filter(s -> s.getName().equals(selectedName)).findFirst().orElse(null);
        String target = targetCompany.getText().trim();
        for(UniversityStudent s : nodeColors.keySet()) graphPanel.setNodeColor(s, nodeColors.get(s));
        if (start != null && !target.isEmpty()){
            ReferralPathFinder finder = new ReferralPathFinder(graph);
            List<UniversityStudent> path = finder.findReferralPath(start, target);
            for (UniversityStudent s : path){
                graphPanel.setNodeColor(s, new Color(0, 14, 120));
                pathColors.add(s);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new NetworkLabUI().setVisible(true));
    }

}
