package app;

import model.Employee;
import util.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateDeleteFrame extends JFrame {

    private JTextField idField, nameField, positionField, rateField;
    private JButton searchBtn, updateBtn, deleteBtn;

    public UpdateDeleteFrame() {
        setTitle("Update or Delete Employee");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        idField = new JTextField();
        nameField = new JTextField();
        positionField = new JTextField();
        rateField = new JTextField();

        searchBtn = new JButton("Search");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");

        panel.add(new JLabel("Employee ID:")); panel.add(idField);
        panel.add(searchBtn); panel.add(new JLabel());

        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Position:")); panel.add(positionField);
        panel.add(new JLabel("Rate per Hour:")); panel.add(rateField);
        panel.add(updateBtn); panel.add(deleteBtn);

        add(panel);

        nameField.setEnabled(false);
        positionField.setEnabled(false);
        rateField.setEnabled(false);
        updateBtn.setEnabled(false);
        deleteBtn.setEnabled(false);

        searchBtn.addActionListener(e -> searchEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
    }

    private void searchEmployee() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            List<Employee> employees = FileHandler.loadEmployees();
            Employee emp = employees.stream().filter(e -> e.getId() == id).findFirst().orElse(null);

            if (emp != null) {
                nameField.setText(emp.getFullName());
                positionField.setText(emp.getPosition());
                rateField.setText(String.valueOf(emp.getRatePerHour()));

                nameField.setEnabled(true);
                positionField.setEnabled(true);
                rateField.setEnabled(true);
                updateBtn.setEnabled(true);
                deleteBtn.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Employee not found.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String position = positionField.getText().trim();
            double rate = Double.parseDouble(rateField.getText().trim());

            List<Employee> employees = FileHandler.loadEmployees();
            for (Employee emp : employees) {
                if (emp.getId() == id) {
                    emp.setFullName(name);
                    emp.setPosition(position);
                    emp.setRatePerHour(rate);
                    break;
                }
            }

            saveEmployees(employees);
            JOptionPane.showMessageDialog(this, "Employee updated successfully.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating employee.");
        }
    }

    private void deleteEmployee() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                List<Employee> employees = FileHandler.loadEmployees();
                List<Employee> updatedList = employees.stream().filter(e -> e.getId() != id).collect(Collectors.toList());

                saveEmployees(updatedList);
                JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting employee.");
            }
        }
    }

    private void saveEmployees(List<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("employees.csv"))) {
            for (Employee emp : employees) {
                writer.write(String.format("%d,%s,%s,%.2f\n",
                        emp.getId(), emp.getFullName(), emp.getPosition(), emp.getRatePerHour()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
