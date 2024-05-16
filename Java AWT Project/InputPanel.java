import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InputPanel extends JPanel implements ActionListener {
    private JTextField nameField;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> courseComboBox;
    private JTextField courseNumberField;
    private JButton courseSubmitButton;
    private List<JTextField> creditFields;
    private List<JTextField> gradeFields;
    private Connection connection;

    public InputPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 230, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel titleLabel = new JLabel("Enter Student Information: ", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, gbc);

        gbc.gridy++;
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(nameLabel, gbc);

        gbc.gridy++;
        nameField = new JTextField(10);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.setPreferredSize(new Dimension(200, 30));
        add(nameField, gbc);

        gbc.gridy++;
        JLabel yearLabel = new JLabel("Year: ");
        yearLabel.setForeground(Color.BLACK);
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(yearLabel, gbc);

        gbc.gridy++;
        yearComboBox = new JComboBox<>(new String[]{"1st", "2nd", "3rd", "4th"});
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        yearComboBox.setPreferredSize(new Dimension(200, 30));
        add(yearComboBox, gbc);

        gbc.gridy++;
        JLabel courseLabel = new JLabel("Course: ");
        courseLabel.setForeground(Color.BLACK);
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(courseLabel, gbc);

        gbc.gridy++;
        courseComboBox = new JComboBox<>(new String[]{"CSE", "AI&DS", "CSIT", "MECH", "ECE"});
        courseComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        courseComboBox.setPreferredSize(new Dimension(200, 30));
        add(courseComboBox, gbc);

        gbc.gridy++;
        JLabel courseNumberLabel = new JLabel("Number of Courses: ");
        courseNumberLabel.setForeground(Color.BLACK);
        courseNumberLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(courseNumberLabel, gbc);

        gbc.gridy++;
        courseNumberField = new JTextField(10);
        courseNumberField.setFont(new Font("Arial", Font.PLAIN, 18));
        courseNumberField.setPreferredSize(new Dimension(200, 30));
        add(courseNumberField, gbc);

        gbc.gridy++;
        courseSubmitButton = new JButton("Submit");
        courseSubmitButton.addActionListener(this);
        courseSubmitButton.setFont(new Font("Arial", Font.BOLD, 18));
        courseSubmitButton.setPreferredSize(new Dimension(150, 40));
        add(courseSubmitButton, gbc);

        creditFields = new ArrayList<>();
        gradeFields = new ArrayList<>();

        // Establish database connection
        String url = "jdbc:postgresql://localhost:5432/exam";
        String user = "postgres";
        String password = "12345";
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == courseSubmitButton) {
            String studentName = nameField.getText();
            String selectedYear = (String) yearComboBox.getSelectedItem();
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            int totalCourses = Integer.parseInt(courseNumberField.getText());

            removeAll();
            revalidate();
            repaint();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(20, 0, 20, 10);

            for (int i = 0; i < totalCourses; i++) {
                JLabel creditLabel = new JLabel("Credits for Course " + (i + 1) + ":");
                creditLabel.setForeground(Color.BLACK);
                creditLabel.setFont(new Font("Arial", Font.BOLD, 18));
                add(creditLabel, gbc);

                JTextField creditField = new JTextField(5);
                creditField.setFont(new Font("Arial", Font.PLAIN, 18));
                creditField.setPreferredSize(new Dimension(150, 30));
                creditFields.add(creditField);
                gbc.gridx++;
                add(creditField, gbc);

                JLabel gradeLabel = new JLabel("Grade for Course " + (i + 1) + " (1-10):");
                gradeLabel.setForeground(Color.BLACK);
                gradeLabel.setFont(new Font("Arial", Font.BOLD, 18));
                gbc.gridx++;
                add(gradeLabel, gbc);

                JTextField gradeField = new JTextField(5);
                gradeField.setFont(new Font("Arial", Font.PLAIN, 18));
                gradeField.setPreferredSize(new Dimension(150, 30));
                gradeFields.add(gradeField);
                gbc.gridx++;
                add(gradeField, gbc);

                gbc.gridy++;
                gbc.gridx = 0;
            }

            JButton calculateButton = new JButton("Calculate");
            calculateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calculateCGPA(studentName, selectedYear, selectedCourse);
                }
            });
            calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridwidth = GridBagConstraints.REMAINDER; 
            gbc.gridy++;
            add(calculateButton, gbc);

            JButton resetButton = new JButton("Reset");
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetFields();
                }
            });
            resetButton.setFont(new Font("Arial", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy++;
            add(resetButton, gbc);

            revalidate();
            repaint();
        }
    }

    private void calculateCGPA(String studentName, String selectedYear, String selectedCourse) {
        double totalCredits = 0;
        double totalGradePoints = 0;
        int totalCourses = creditFields.size();

        for (int i = 0; i < totalCourses; i++) {
            String creditText = creditFields.get(i).getText();
            String gradeText = gradeFields.get(i).getText();

            if (!creditText.isEmpty() && !gradeText.isEmpty()) {
                int credit = Integer.parseInt(creditText);
                int grade = Integer.parseInt(gradeText);
                double gradePoint = grade;

                totalCredits += credit;
                totalGradePoints += gradePoint * credit;
            }
        }

        double cgpa = totalCredits != 0 ? totalGradePoints / totalCredits : 0;

        try {
            // Insert student information into the respective year table
            String tableName = "year_" + selectedYear.toLowerCase(); // Table name based on selected year
            String insertQuery = "INSERT INTO " + tableName + " (name, branch, courses, cgpa) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, selectedCourse);
            preparedStatement.setInt(3, totalCourses);
            preparedStatement.setDouble(4, cgpa);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        showCongratulatoryMessage(cgpa);
    }

    private void resetFields() {
        for (JTextField creditField : creditFields) {
            creditField.setText("");
        }
        for (JTextField gradeField : gradeFields) {
            gradeField.setText("");
        }
    }

    private void showCongratulatoryMessage(double cgpa) {
        JFrame frame = new JFrame("Congratulations!");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Congratulations! Your CGPA is: " + cgpa);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(messageLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Close database connection when the panel is destroyed
    @Override
    public void finalize() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
