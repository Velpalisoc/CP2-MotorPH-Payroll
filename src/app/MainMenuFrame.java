package app;

import util.LoginSession;
import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("MotorPH Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + LoginSession.getCurrentUser(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton viewAllBtn = new JButton("View All Employees");
        JButton viewByIdBtn = new JButton("View Employee by ID");
        JButton viewSalaryBtn = new JButton("View Salary Details");
        JButton addEmployeeBtn = new JButton("Add New Employee");
        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            LoginSession.logout();
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel btnPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        btnPanel.add(viewAllBtn);
        btnPanel.add(viewByIdBtn);
        btnPanel.add(viewSalaryBtn);
        btnPanel.add(addEmployeeBtn);
        btnPanel.add(logoutBtn);

        add(welcomeLabel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
    }
}
