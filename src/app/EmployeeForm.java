package app;

import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EmployeeForm extends JFrame {
    private JTextField nameField, positionField, rateField;

    public EmployeeForm() {
        setTitle("Add New Employee");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Full Name:");
        JLabel positionLabel = new JLabel("Position:");
        JLabel rateLabel = new JLabel("Rate per Hour:");

        nameField = new JTextField(20);
        positionField = new JTextField(15);
        rateField = new JTextField(10);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new SaveAction());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(nameLabel); panel.add(nameField);
        panel.add(positionLabel); panel.add(positionField);
        panel.add(rateLabel); panel.add(rateField);
        panel.add(new JLabel()); panel.add(saveButton);

        add(panel);
    }

    private class SaveAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String position = positionField.getText().trim();
                double rate = Double.parseDouble(rateField.getText().trim());

                if (name.isEmpty() || position.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String generatedId = generateNextEmployeeId();
                Employee emp = new Employee(Integer.parseInt(generatedId), name, position, rate);
                saveToCSV(emp);
                JOptionPane.showMessageDialog(null, "Employee saved successfully! ID: " + generatedId);
                dispose(); // close form
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Rate should be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveToCSV(Employee emp) {
            try (FileWriter writer = new FileWriter("employees.csv", true)) {
                writer.write(String.format("%05d,%s,%s,%.2f\n", emp.getId(), emp.getFullName(), emp.getPosition(), emp.getRatePerHour()));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving employee.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private String generateNextEmployeeId() {
            String filePath = "employees.csv";
            int maxId = 0;

            File file = new File(filePath);
            if (!file.exists()) return "00001"; // If file doesn't exist yet

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        try {
                            int id = Integer.parseInt(parts[0]);
                            if (id > maxId) maxId = id;
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return String.format("%05d", maxId + 1);
        }
    }
}
