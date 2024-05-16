import javax.swing.*;
import java.awt.*;

public class CGPACalculator extends JFrame {

    public CGPACalculator() {
        setTitle("CGPA Calculator");
        setSize(400, 300);
        getContentPane().setBackground(new Color(0, 0, 0)); // Setting background color to black
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("CGPA Calculator", SwingConstants.CENTER);
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        InputPanel inputPanel = new InputPanel();
        JScrollPane scrollPane = new JScrollPane(inputPanel);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0)); // Setting scroll pane background color to black

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(0, 0, 0)); // Setting title panel background color to black
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        ResultPanel resultPanel = new ResultPanel();
        resultPanel.setBackground(new Color(0, 0, 0)); // Setting result panel background color to black
        add(resultPanel, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CGPACalculator());
    }
}
