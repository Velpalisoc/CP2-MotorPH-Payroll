package MotorPHGUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LoginDashboard extends JFrame {
    private final JPanel mainPanel;
    private final JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordBox;
    private final JButton loginButton;
    private final JLabel forgotPasswordLabel;
    
    public LoginDashboard() {
        // Frame setup
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 450);
        setLocationRelativeTo(null);
        
        // Initialize main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setBounds(100, 40, 200, 40);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setBounds(50, 100, 100, 20);
        
        usernameField = new JTextField();
        usernameField.setBounds(50, 125, 300, 32);
        
        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setBounds(50, 170, 100, 20);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(50, 195, 300, 32);
        
        // Show password checkbox
        showPasswordBox = new JCheckBox("Show Password");
        showPasswordBox.setBounds(50, 235, 120, 20);
        showPasswordBox.setBackground(Color.WHITE);
        showPasswordBox.addActionListener(e -> {
            if (showPasswordBox.isSelected()) {
                passwordField.setEchoChar((char)0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        
        // Forgot password link
        forgotPasswordLabel = new JLabel("Forgot password?");
        forgotPasswordLabel.setBounds(250, 235, 100, 20);
        forgotPasswordLabel.setForeground(Color.GRAY);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 280, 100, 35);
        loginButton.addActionListener(e -> handleLogin());
        
        // Demo accounts section
        JLabel demoLabel = new JLabel("Demo Accounts");
        demoLabel.setBounds(150, 340, 100, 20);
        demoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel adminLabel = new JLabel("Admin | Admin123");
        adminLabel.setBounds(95, 365, 105, 20);
        adminLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel userLabel2 = new JLabel("User | User123");
        userLabel2.setBounds(205, 365, 100, 20);
        
        // Add components to panel
        mainPanel.add(welcomeLabel);
        mainPanel.add(userLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passLabel);
        mainPanel.add(passwordField);
        mainPanel.add(showPasswordBox);
        mainPanel.add(forgotPasswordLabel);
        mainPanel.add(loginButton);
        mainPanel.add(demoLabel);
        mainPanel.add(adminLabel);
        mainPanel.add(userLabel2);
        
        // Add panel to frame
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if ((username.equals("Admin") && password.equals("Admin123")) || 
            (username.equals("User") && password.equals("User123"))) {
            
            dispose(); // Close login window
            
            // Open appropriate dashboard
            if (username.equals("Admin")) {
                new AdminDashboard().setVisible(true);
            } else {
                new UserDashboard().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid Username or Password", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginDashboard().setVisible(true);
        });
    }
}


