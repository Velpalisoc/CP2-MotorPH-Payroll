package app;

import model.Employee;
import util.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SalaryFrame extends JFrame {

    public SalaryFrame() {
        setTitle("View Salary Details");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel idLabel = new JLabel("Employee ID:");
        JTextField idField = new JTextField();

        JLabel monthLabel = new JLabel("Month:");
        JTextField monthField = new JTextField();

        JButton computeBtn = new JButton("Compute Salary");

        computeBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String month = monthField.getText().trim();

                List<Employee> employees = FileHandler.loadEmployees();
                Employee emp = employees.stream().filter(em -> em.getId() == id).findFirst().orElse(null);

                if (emp != null) {
                    int workingDays = 22;
                    int hoursPerDay = 8;
                    double salary = emp.getRatePerHour() * workingDays * hoursPerDay;

                    String result = String.format("""
                        Name: %s
                        Month: %s
                        Daily Hours: %d
                        Working Days: %d
                        Total Salary: PHP %.2f
                        """, emp.getFullName(), month, hoursPerDay, workingDays, salary);

                    JOptionPane.showMessageDialog(this, result, "Salary Details", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(idLabel); panel.add(idField);
        panel.add(monthLabel); panel.add(monthField);
        panel.add(new JLabel()); panel.add(computeBtn);

        add(panel);
    }
}
