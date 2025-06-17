package app;

import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeForm extends JFrame {
    private JTextField idField, nameField, positionField, rateField;

    public EmployeeForm() {
        setTitle("Add New Employee");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel idLabel = new JLabel("Employee ID:");
        JLabel nameLabel = new JLabel("Full Name:");
        JLabel positionLabel = new JLabel("Position:");
        JLabel rateLabel = new JLabel("Rate per Hour:");

        idField = new JTextField(10);
        nameField = new JTextField(20);
        positionField = new JTextField(15);
        rateField = new JTextField(10);

        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new SaveAction());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(idLabel); panel.add(idField);
        panel.add(nameLabel); panel.add(nameField);
        panel.add(positionLabel); panel.add(positionField);
        panel.add(rateLabel); panel.add(rateField);
        panel.add(new JLabel()); panel.add(saveButton);

        add(panel);
    }

    private class SaveAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String position = positionField.getText().trim();
                double rate = Double.parseDouble(rateField.getText().trim());

                if (name.isEmpty() || position.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Employee emp = new Employee(id, name, position, rate);
                saveToCSV(emp);
                JOptionPane.showMessageDialog(null, "Employee saved successfully!");
                dispose(); // close form
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. ID and Rate should be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void saveToCSV(Employee emp) {
            try (FileWriter writer = new FileWriter("employees.csv", true)) {
                writer.write(emp.getId() + "," + emp.getFullName() + "," + emp.getPosition() + "," + emp.getRatePerHour() + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving employee.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
