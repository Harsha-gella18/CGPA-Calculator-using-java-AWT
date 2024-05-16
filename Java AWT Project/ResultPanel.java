import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class ResultPanel extends JPanel {
    private Deque<Double> cgpaHistory;
    private JLabel resultLabel;

    public ResultPanel() {
        setBackground(new Color(0, 0, 0)); // Setting background color to black
        setLayout(new BorderLayout());

        cgpaHistory = new ArrayDeque<>();
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(resultLabel, BorderLayout.CENTER);
    }

    public void setResult(double cgpa) {
        // Add the new CGPA to the history
        cgpaHistory.addFirst(cgpa);

        // Keep only the last 5 entries in the history
        while (cgpaHistory.size() > 5) {
            cgpaHistory.removeLast();
        }

        // Update the result label with the latest CGPA and history
        updateResultLabel();
    }

    private void updateResultLabel() {
        StringBuilder sb = new StringBuilder("<html><center>");
        sb.append("Current CGPA: ").append(cgpaHistory.getFirst()).append("<br>");
        sb.append("Past CGPAs: <br>");
        for (Double cgpa : cgpaHistory) {
            sb.append(cgpa).append("<br>");
        }
        sb.append("</center></html>");
        resultLabel.setText(sb.toString());
    }
}
