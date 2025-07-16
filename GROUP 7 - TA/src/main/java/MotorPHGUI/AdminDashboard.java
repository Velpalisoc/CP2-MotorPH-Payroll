package MotorPHGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AdminDashboard extends JFrame {
    private JPanel sidePanel;
    private JTabbedPane mainContent;
    public AdminDashboard() {
        // Frame setup
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1014, 708);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Create panels
        createSidePanel();
        createMainContent();

        // Add panels to frame
        add(sidePanel, BorderLayout.WEST); 
        add(mainContent, BorderLayout.CENTER);
    }

    private void createSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));
        sidePanel.setBackground(new Color(51, 153, 255));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        try {
            // Logo section
            ImageIcon logoIcon = new ImageIcon("src\\Icons\\MotorPHLogo.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(180, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));
            sidePanel.add(logoLabel);
            
            // System Dashboard text with minimal spacing
            JLabel titleLabel = new JLabel("System Dashboard");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Reduced bottom padding
            sidePanel.add(titleLabel);

            String[] menuItems = {
                "Home", "Employee Management", "Payroll Management",
                "Attendance Management", "Deduction Management",
                "Reports Management", "Import & Export",
                "Account Management", "Activity Logs",
                "System Settings"
            };

            // Add main menu buttons
            for (String item : menuItems) {
                JButton btn = new JButton(item);
                btn.setMaximumSize(new Dimension(180, 35));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setMargin(new Insets(5, 10, 5, 10));
                btn.setBackground(new Color(240, 240, 240));
                btn.setFocusPainted(false);
                btn.addActionListener(e -> switchTab(item));
                sidePanel.add(Box.createRigidArea(new Dimension(0, 2)));
                sidePanel.add(btn);
            }

            // Add separator
            sidePanel.add(Box.createRigidArea(new Dimension(0, 5)));
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(160, 1));
            separator.setForeground(Color.WHITE);
            sidePanel.add(separator);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 5)));

            // Add remaining buttons
            String[] bottomMenuItems = {"Call Center", "Help", "Logout"};
            for (String item : bottomMenuItems) {
                JButton btn = new JButton(item);
                btn.setMaximumSize(new Dimension(180, 35));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.setMargin(new Insets(5, 10, 5, 10));
                btn.setBackground(new Color(240, 240, 240));
                btn.setFocusPainted(false);
                btn.addActionListener(e -> switchTab(item));
                sidePanel.add(Box.createRigidArea(new Dimension(0, 2)));
                sidePanel.add(btn);
            }

        } catch (Exception e) {
            System.err.println("Error loading logo image: " + e.getMessage());
            JLabel fallbackLabel = new JLabel("MotorPH");
            fallbackLabel.setForeground(Color.WHITE);
            fallbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            fallbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidePanel.add(fallbackLabel);
        }
    }

    private void createMainContent() {
        mainContent = new JTabbedPane();
        mainContent.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainContent.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0;
            }
        });

        JPanel homePanel = new JPanel();
        homePanel.add(new JLabel("Welcome to Home"));

        // Create EmployeeManagementPanel instance
        EmployeeManagementPanel empPanel = new EmployeeManagementPanel();
        JPanel payrollPanel = new JPanel();
        payrollPanel.add(new JLabel("Payroll Management"));

        // Pass empPanel to ActivityLogPanel
        JPanel activityLogPanel = new ActivityLogPanel(empPanel);

        mainContent.addTab("Home", homePanel);
        mainContent.addTab("Employee Management", empPanel);
        mainContent.addTab("Payroll Management", payrollPanel);
        mainContent.addTab("Attendance Management", new JPanel());
        mainContent.addTab("Deduction Management", new JPanel());
        mainContent.addTab("Reports Management", new JPanel());
        mainContent.addTab("Import & Export", new JPanel());
        mainContent.addTab("Account Management", new JPanel());
        mainContent.addTab("Activity Logs", activityLogPanel);
        mainContent.addTab("System Settings", new JPanel());
        mainContent.addTab("Call Center", new JPanel());
        mainContent.addTab("Help", new JPanel());
        mainContent.addTab("Logout", new JPanel());
    }


    private void switchTab(String tabName) {
        for (int i = 0; i < mainContent.getTabCount(); i++) {
            if (mainContent.getTitleAt(i).equals(tabName)) {
                mainContent.setSelectedIndex(i);
                return;
            }
        }

        // Handle logout separately
        if (tabName.equals("Logout")) {
            int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (choice == javax.swing.JOptionPane.YES_OPTION) {
                dispose();
                // Add code to show login screen
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            }
            new AdminDashboard().setVisible(true);
        });
    }
}
