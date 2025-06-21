package MotorPHGUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CreateNewAccount extends JFrame {
    private JTextField empNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField phoneNumberField;
    private JComboBox<String> statusCombo;
    private JComboBox<String> positionCombo;
    private JButton createButton;
    private JButton cancelButton;
    private final EmployeeManagementPanel parentPanel;

    public CreateNewAccount(EmployeeManagementPanel parent) {
        this.parentPanel = parent;
        setupFrame();
        createComponents();
        setupLayout();
        setupListeners();
    }

    private void setupFrame() {
        setTitle("Create New Employee Account");
        setSize(500, 600); // Increased frame size
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    private void createComponents() {
        // Initialize text fields with larger size
        empNumberField = new JTextField(20);
        lastNameField = new JTextField(20);
        firstNameField = new JTextField(20);
        phoneNumberField = new JTextField(20);
        
        // Set preferred size for text fields
        Dimension fieldSize = new Dimension(250, 35);
        empNumberField.setPreferredSize(fieldSize);
        lastNameField.setPreferredSize(fieldSize);
        firstNameField.setPreferredSize(fieldSize);
        phoneNumberField.setPreferredSize(fieldSize);
        
        // Status dropdown options
        String[] statusOptions = {
            "Active",
            "Inactive",
            "On Leave",
            "Terminated"
        };
        statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setPreferredSize(fieldSize);
        
        // Position dropdown options
        String[] positionOptions = {
            "Chief Executive Officer",
            "Chief Operating Officer",
            "Chief Finance Officer",
            "Chief Marketing Officer",
            "IT Operations and Systems",
            "HR Manager",
            "HR Team Leader",
            "Accounting Head",
            "Payroll Manager",
            "Payroll Team Leader",
            "Account Manager",
            "Sales & Marketing",
            "Supply Chain and Logistics",
            "Customer Service and Relations",
            "HR Rank and File",
            "Payroll Rank and File"
        };
        positionCombo = new JComboBox<>(positionOptions);
        positionCombo.setPreferredSize(fieldSize);
        
        // Initialize buttons with larger size
        createButton = new JButton("Create");
        cancelButton = new JButton("Cancel");
        Dimension buttonSize = new Dimension(120, 40);
        createButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        
        // Set fonts for all components
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        empNumberField.setFont(fieldFont);
        lastNameField.setFont(fieldFont);
        firstNameField.setFont(fieldFont);
        phoneNumberField.setFont(fieldFont);
        statusCombo.setFont(fieldFont);
        positionCombo.setFont(fieldFont);
        createButton.setFont(labelFont);
        cancelButton.setFont(labelFont);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title Label
        JLabel titleLabel = new JLabel("Create New Employee");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Add form fields
        mainPanel.add(createFormField("Employee Number:", empNumberField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createFormField("Last Name:", lastNameField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createFormField("First Name:", firstNameField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createFormField("Phone Number:", phoneNumberField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createFormField("Status:", statusCombo));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createFormField("Position:", positionCombo));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(440, 40));
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel.setPreferredSize(new Dimension(140, 35));
        panel.add(jLabel);
        panel.add(field);
        
        return panel;
    }

    private void setupListeners() {
        createButton.addActionListener(e -> handleCreate());
        cancelButton.addActionListener(e -> dispose());
    }

    private void handleCreate() {
        String csvFilePath = System.getProperty("csv.filepath");
        if (csvFilePath == null) {
            JOptionPane.showMessageDialog(this,
                "Please import a CSV file first",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate fields
        if (empNumberField.getText().isEmpty() || 
            lastNameField.getText().isEmpty() || 
            firstNameField.getText().isEmpty() || 
            phoneNumberField.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create new employee record
        String[] newRecord = {
            empNumberField.getText().trim(),
            lastNameField.getText().trim(),
            firstNameField.getText().trim(),
            phoneNumberField.getText().trim(),
            statusCombo.getSelectedItem().toString(),
            positionCombo.getSelectedItem().toString()
        };

        try {
            File csvFile = new File(csvFilePath);
            // Read all existing content
            StringBuilder content = new StringBuilder();
            content.append(System.getProperty("csv.headers")).append("\n");
            
            DefaultTableModel model = (DefaultTableModel) parentPanel.getEmployeeTable().getModel();
            // Add existing records
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col > 0) content.append(",");
                    content.append(model.getValueAt(row, col));
                }
                content.append("\n");
            }
            
            // Add new record
            StringBuilder newLine = new StringBuilder();
            for (int i = 0; i < newRecord.length; i++) {
                if (i > 0) newLine.append(",");
                newLine.append(newRecord[i]);
            }
            content.append(newLine).append("\n");
            
            // Write everything back to file
            try (FileWriter fw = new FileWriter(csvFile);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(content.toString());
            }

            // Update table model
            model.addRow(newRecord);

            JOptionPane.showMessageDialog(this, 
                "Employee account created successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            parentPanel.refreshTable();
            dispose();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error saving employee data: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

