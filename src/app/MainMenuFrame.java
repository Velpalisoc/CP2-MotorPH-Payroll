package app;

import util.LoginSession;
import util.FileHandler;
import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("MotorPH Main Menu");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + LoginSession.getCurrentUser(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton viewAllBtn = new JButton("View All Employees");
        viewAllBtn.addActionListener(e -> new EmployeeListFrame().setVisible(true));

        JButton viewByIdBtn = new JButton("View Employee by ID");
        viewByIdBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter Employee ID:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(input.trim());
                    List<Employee> employees = FileHandler.loadEmployees();
                    var found = employees.stream().filter(emp -> emp.getId() == id).findFirst();

                    if (found.isPresent()) {
                        Employee emp = found.get();
                        String message = String.format("""
                            ID: %05d
                            Name: %s
                            Position: %s
                            Rate per Hour: %.2f
                        """, emp.getId(), emp.getFullName(), emp.getPosition(), emp.getRatePerHour());

                        JOptionPane.showMessageDialog(this, message, "Employee Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton viewSalaryBtn = new JButton("View Salary Details");
        viewSalaryBtn.addActionListener(e -> new SalaryFrame().setVisible(true));

        JButton addEmployeeBtn = new JButton("Add New Employee");
        addEmployeeBtn.addActionListener(e -> new EmployeeForm().setVisible(true));

        JButton updateDeleteBtn = new JButton("Update/Delete Employee");
        updateDeleteBtn.addActionListener(e -> new UpdateDeleteFrame().setVisible(true));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            LoginSession.logout();
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel btnPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        btnPanel.add(viewAllBtn);
        btnPanel.add(viewByIdBtn);
        btnPanel.add(viewSalaryBtn);
        btnPanel.add(addEmployeeBtn);
        btnPanel.add(updateDeleteBtn); // ✅ FIXED THIS LINE
        btnPanel.add(logoutBtn);

        add(welcomeLabel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
    }
}
