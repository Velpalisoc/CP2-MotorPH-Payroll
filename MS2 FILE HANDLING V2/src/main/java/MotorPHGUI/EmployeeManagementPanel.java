package MotorPHGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class EmployeeManagementPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;
    private JButton refreshButton; // Add this field
    private JButton importButton;
    private JButton createAccountButton;
    private JTable employeeTable;

    private DefaultTableModel originalModel;
    private final String[] columns = {"ID no.", "Last Name", "First Name", "Phone Number", "Status", "Position", "Actions"};

    public EmployeeManagementPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        createComponents();
        setupLayout();
        setupListeners();
        // Remove syncWithCSV() call - start with empty table
    }

    private void createComponents() {
        searchField = new JTextField(18); // Shorter field
        searchButton = new JButton("Search");
        refreshButton = new JButton("Refresh");
        importButton = new JButton("Import CSV File");
        createAccountButton = new JButton("Create New Account");

        // Set button sizes: width 85, height 33 (same as importButton)
        Dimension buttonSize = new Dimension(85, 33);
        searchButton.setPreferredSize(buttonSize);
        refreshButton.setPreferredSize(buttonSize);

        // Add Actions column
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Actions column is editable (for buttons)
                return column == 6;
            }
        };
        employeeTable = new JTable(model);
        employeeTable.setRowHeight(36);
        employeeTable.setGridColor(new Color(64, 64, 64));
        employeeTable.setShowGrid(true);

        // Adjust column widths for readability
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID no.
        employeeTable.getColumnModel().getColumn(0).setMinWidth(50);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(70); // Last Name
        employeeTable.getColumnModel().getColumn(1).setMinWidth(70);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(70); // First Name
        employeeTable.getColumnModel().getColumn(2).setMinWidth(70);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Phone Number
        employeeTable.getColumnModel().getColumn(3).setMinWidth(80);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(50); // Status
        employeeTable.getColumnModel().getColumn(4).setMinWidth(50);
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(130); // Position
        employeeTable.getColumnModel().getColumn(5).setMinWidth(130);

        // Style table headers
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(240, 240, 240));
        employeeTable.getTableHeader().setForeground(new Color(64, 64, 64));
        employeeTable.getTableHeader().setOpaque(true);
        employeeTable.setBackground(Color.WHITE);

        // Set custom renderer and editor for Actions column
        employeeTable.getColumn("Actions").setCellRenderer(new ActionCellRenderer());
        employeeTable.getColumn("Actions").setCellEditor(new ActionCellEditor(this));

        // Store original model
        originalModel = copyTableModel(model);
    }

    private void setupLayout() {
        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setMaximumSize(new Dimension(1000, 40));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setMaximumSize(new Dimension(1000, 40));

        // Left side search components
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchField.setPreferredSize(new Dimension(110, 28)); // Shorter width
        leftPanel.add(searchField);
        leftPanel.add(searchButton);
        leftPanel.add(refreshButton);

        // Right side buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        importButton.setPreferredSize(new Dimension(120, 33));
        createAccountButton.setPreferredSize(new Dimension(150, 33));
        rightPanel.add(importButton);
        rightPanel.add(createAccountButton);

        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(rightPanel, BorderLayout.EAST);

        // Table ScrollPane
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        // Add components to main panel
        add(titlePanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(searchPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(scrollPane);
    }

    private void setupListeners() {
        importButton.addActionListener(e -> {
            handleImportCSV();
            originalModel = copyTableModel((DefaultTableModel) employeeTable.getModel());
        });
        
        // Add document listener for real-time filtering
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { handleSearchChange(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { handleSearchChange(); }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { handleSearchChange(); }
        });

        // Add action listener for Enter key
        searchField.addActionListener(e -> handleSearch());

        createAccountButton.addActionListener(e -> {
            CreateNewAccount createAccount = new CreateNewAccount(this);
            createAccount.setVisible(true);
        });

        refreshButton.addActionListener(e -> refreshTable()); // Add listener for refresh
    }

    private void handleImportCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
                model.setRowCount(0);

                String line = br.readLine(); // Read header line
                System.setProperty("csv.headers", line);

                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] row = line.split(",");
                        // Add empty string for Actions column
                        Object[] rowWithActions = new Object[7];
                        System.arraycopy(row, 0, rowWithActions, 0, Math.min(row.length, 6));
                        rowWithActions[6] = ""; // Placeholder for Actions
                        model.addRow(rowWithActions);
                    }
                }

                System.setProperty("csv.filepath", selectedFile.getAbsolutePath());
                System.setProperty("last.imported.csv", selectedFile.getAbsolutePath());
                originalModel = copyTableModel(model);

                JOptionPane.showMessageDialog(this,
                        "CSV file imported successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error reading CSV file: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Add this new method for search functionality
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        DefaultTableModel filteredModel = new DefaultTableModel(
            new String[]{"ID no.", "First Name", "Surname", "Date Hired", "Status", "Actions"}, 
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int row = 0; row < model.getRowCount(); row++) {
            boolean matchFound = false;
            for (int col = 0; col < model.getColumnCount(); col++) {
                String cellValue = model.getValueAt(row, col).toString().toLowerCase();
                if (cellValue.contains(searchText)) {
                    matchFound = true;
                    break;
                }
            }
            if (matchFound) {
                Object[] rowData = new Object[model.getColumnCount()];
                for (int col = 0; col < model.getColumnCount(); col++) {
                    rowData[col] = model.getValueAt(row, col);
                }
                filteredModel.addRow(rowData);
            }
        }
        
        employeeTable.setModel(filteredModel);
    }

    private void handleSearchChange() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty() && originalModel != null) {
            // Reset to original data
            employeeTable.setModel(copyTableModel(originalModel));
        } else if (!searchText.isEmpty()) {
            handleSearch();
        }
    }

    // Utility method to deep copy a DefaultTableModel
    private DefaultTableModel copyTableModel(DefaultTableModel model) {
        DefaultTableModel newModel = new DefaultTableModel();
        // Copy column identifiers
        for (int i = 0; i < model.getColumnCount(); i++) {
            newModel.addColumn(model.getColumnName(i));
        }
        // Copy rows
        for (int row = 0; row < model.getRowCount(); row++) {
            Object[] rowData = new Object[model.getColumnCount()];
            for (int col = 0; col < model.getColumnCount(); col++) {
                rowData[col] = model.getValueAt(row, col);
            }
            newModel.addRow(rowData);
        }
        return newModel;
    }


    public void refreshTable() {
        String lastImportedFile = System.getProperty("last.imported.csv");
        if (lastImportedFile == null) return;

        File csvFile = new File(lastImportedFile);
        if (csvFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
                model.setRowCount(0);

                String line;
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false; // Skip header
                        continue;
                    }
                    if (!line.trim().isEmpty()) {
                        String[] data = line.split(",");
                        if (data.length == 6) {
                            model.addRow(data);
                        }
                    }
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error refreshing table: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JTable getEmployeeTable() {
        return employeeTable;
    }

    // --- Custom Renderer and Editor for Actions column ---

    private static class ActionCellRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton editButton = new JButton("Edit");
        private final JButton deleteButton = new JButton("Delete");

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            editButton.setMargin(new Insets(2, 8, 2, 8));
            editButton.setFocusable(false);
            deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            deleteButton.setMargin(new Insets(2, 8, 2, 8));
            deleteButton.setFocusable(false);
            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private static class ActionCellEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton editButton = new JButton("Edit");
        private final JButton deleteButton = new JButton("Delete");
        private JTable table;
        public ActionCellEditor(EmployeeManagementPanel parent) {
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            editButton.setMargin(new Insets(2, 8, 2, 8));
            deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            deleteButton.setMargin(new Insets(2, 8, 2, 8));
            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    parent.showEditDialog(row);
                }
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    parent.handleDelete(row);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.table = table;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // --- Edit Dialog ---
    public void showEditDialog(int row) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        JDialog dialog = new JDialog((java.awt.Frame)SwingUtilities.getWindowAncestor(this), "Edit Employee", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        JTextField empNumberField = new JTextField(model.getValueAt(row, 0).toString(), 20);
        JTextField lastNameField = new JTextField(model.getValueAt(row, 1).toString(), 20);
        JTextField firstNameField = new JTextField(model.getValueAt(row, 2).toString(), 20);
        JTextField phoneNumberField = new JTextField(model.getValueAt(row, 3).toString(), 20);
        JTextField statusField = new JTextField(model.getValueAt(row, 4).toString(), 20);
        JTextField positionField = new JTextField(model.getValueAt(row, 5).toString(), 20);

        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.insets = new Insets(8, 8, 4, 8);
        dialog.add(new JLabel("ID no.:"), gbc);
        gbc.gridy++; dialog.add(new JLabel("Last Name:"), gbc);
        gbc.gridy++; dialog.add(new JLabel("First Name:"), gbc);
        gbc.gridy++; dialog.add(new JLabel("Phone Number:"), gbc);
        gbc.gridy++; dialog.add(new JLabel("Status:"), gbc);
        gbc.gridy++; dialog.add(new JLabel("Position:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(empNumberField, gbc);
        gbc.gridy++; dialog.add(lastNameField, gbc);
        gbc.gridy++; dialog.add(firstNameField, gbc);
        gbc.gridy++; dialog.add(phoneNumberField, gbc);
        gbc.gridy++; dialog.add(statusField, gbc);
        gbc.gridy++; dialog.add(positionField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        saveButton.addActionListener(e -> {
            // Log before updating
            String[] oldData = new String[6];
            for (int i = 0; i < 6; i++) oldData[i] = model.getValueAt(row, i).toString();
            String[] newData = {
                empNumberField.getText().trim(),
                lastNameField.getText().trim(),
                firstNameField.getText().trim(),
                phoneNumberField.getText().trim(),
                statusField.getText().trim(),
                positionField.getText().trim()
            };
            logActivity("Admin", "Edit", "Edited employee: " + oldData[0] + " " + oldData[1] + ", " + oldData[2],
                concatArrays(oldData, newData));

            // Update table model
            model.setValueAt(empNumberField.getText().trim(), row, 0);
            model.setValueAt(lastNameField.getText().trim(), row, 1);
            model.setValueAt(firstNameField.getText().trim(), row, 2);
            model.setValueAt(phoneNumberField.getText().trim(), row, 3);
            model.setValueAt(statusField.getText().trim(), row, 4);
            model.setValueAt(positionField.getText().trim(), row, 5);
            syncTableToCSV();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // --- Delete Handler ---
    public void handleDelete(int row) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        String[] employeeData = new String[6];
        for (int i = 0; i < 6; i++) employeeData[i] = model.getValueAt(row, i).toString();

        // Remove from main table and CSV
        model.removeRow(row);
        syncTableToCSV();

        // Append to activitylog.csv (recycle bin) with date, time, user, action, and employee data
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.toLocalTime().withNano(0).toString();
        String user = "Admin"; // Or get from session
        String action = "Delete";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("activitylog.csv", true))) {
            bw.write(date + "," + time + "," + user + "," + action + "," + String.join(",", employeeData));
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing to activitylog.csv: " + ex.getMessage());
        }
    }

    // --- Sync Table to CSV ---
    private void syncTableToCSV() {
        String csvFilePath = System.getProperty("csv.filepath");
        if (csvFilePath == null) return;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write headers
            String headers = System.getProperty("csv.headers");
            if (headers != null) {
                bw.write(headers);
                bw.newLine();
            }
            DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
            for (int row = 0; row < model.getRowCount(); row++) {
                StringBuilder line = new StringBuilder();
                for (int col = 0; col < 6; col++) { // Only first 6 columns
                    if (col > 0) line.append(",");
                    Object val = model.getValueAt(row, col);
                    line.append(val == null ? "" : val.toString());
                }
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving to CSV: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logActivity(String user, String action, String details, String[] employeeData) {
        String logFilePath = "activitylog.csv";
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.toLocalTime().withNano(0).toString();
        StringBuilder sb = new StringBuilder();
        sb.append(date).append(",").append(time).append(",").append(user).append(",").append(action).append(",").append(details);
        // Append full employee data for restore
        if (employeeData != null) {
            for (String field : employeeData) {
                sb.append(",").append(field.replace(",", " ")); // Avoid breaking CSV
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFilePath, true))) {
            bw.write(sb.toString());
            bw.newLine();
        } catch (IOException ex) {
            // Optionally show error or ignore
        }
    }

    private String[] concatArrays(String[] a, String[] b) {
        String[] result = new String[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static class RestoreButtonRenderer extends JButton implements TableCellRenderer {
        public RestoreButtonRenderer() {
            setFont(new Font("Segoe UI", Font.PLAIN, 11));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Restore");
            // Enable if there are at least 10 columns and employee number is not empty
            boolean canRestore = table.getColumnCount() >= 10
                    && table.getValueAt(row, 4) != null
                    && !table.getValueAt(row, 4).toString().trim().isEmpty();
            setEnabled(canRestore);
            if (!canRestore) {
                setToolTipText("Restore unavailable: log entry does not contain full employee data.");
            } else {
                setToolTipText(null);
            }
            return this;
        }
    }

    private static class RestoreButtonEditor extends DefaultCellEditor {
        private JButton button;
        private ActivityLogPanel parent;
        private int row;
        private boolean canRestore;

        public RestoreButtonEditor(JCheckBox checkBox, ActivityLogPanel parent) {
            super(checkBox);
            this.parent = parent;
            button = new JButton("Restore");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            button.addActionListener(e -> {
                if (canRestore) {
                    parent.handleRestore(row);
                } else {
                    JOptionPane.showMessageDialog(parent, "Restore unavailable: log entry does not contain full employee data.", "Restore", JOptionPane.WARNING_MESSAGE);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            canRestore = table.getColumnCount() >= 10
                    && table.getValueAt(row, 4) != null
                    && !table.getValueAt(row, 4).toString().trim().isEmpty();
            button.setEnabled(canRestore);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Restore";
        }
    }
}
