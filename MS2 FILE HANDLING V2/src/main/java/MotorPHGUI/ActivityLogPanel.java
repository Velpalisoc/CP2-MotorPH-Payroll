package MotorPHGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class ActivityLogPanel extends JPanel {
    private JTable logTable;
    private JButton exportButton;
    private JButton refreshButton;
    private DefaultTableModel tableModel;
    private EmployeeManagementPanel employeePanel; // Add this field

    // Add a constructor that accepts EmployeeManagementPanel
    public ActivityLogPanel(EmployeeManagementPanel employeePanel) {
        this();
        this.employeePanel = employeePanel;
    }

    // Keep the default constructor for manual mode
    public ActivityLogPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Activity Log");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setMaximumSize(new Dimension(1000, 40));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        exportButton = new JButton("Export Data");
        exportButton.setPreferredSize(new Dimension(120, 33));
        exportButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        buttonPanel.add(exportButton);

        refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(120, 33));
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        buttonPanel.add(refreshButton);

        buttonPanel.setMaximumSize(new Dimension(1000, 40));

        // Only show these columns:
        String[] columns = {"Date", "Time", "User", "Action", "ID no.", "Last Name", "First Name", "Restore"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only Restore button is editable
            }
        };
        logTable = new JTable(tableModel);
        logTable.setRowHeight(36);
        logTable.setGridColor(new Color(64, 64, 64));
        logTable.setShowGrid(true);

        // Style table headers
        logTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        logTable.getTableHeader().setBackground(new Color(240, 240, 240));
        logTable.getTableHeader().setForeground(new Color(64, 64, 64));
        logTable.getTableHeader().setOpaque(true);
        logTable.setBackground(Color.WHITE);

        // Set preferred width for the "Restore" column to center the button and fit the label
        logTable.getColumnModel().getColumn(7).setPreferredWidth(140);
        logTable.getColumnModel().getColumn(7).setMinWidth(120);
        logTable.getColumnModel().getColumn(7).setMaxWidth(160);

        // Restore button in table
        logTable.getColumn("Restore").setCellRenderer(new RestoreButtonRenderer());
        logTable.getColumn("Restore").setCellEditor(new RestoreButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setPreferredSize(new Dimension(900, 300));

        add(titlePanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(scrollPane);

        // Load activity logs
        loadActivityLogs();

        // Export Data button handler
        exportButton.addActionListener(e -> handleExport());
        refreshButton.addActionListener(e -> refreshLogs());
    }

    private void loadActivityLogs() {
        String logFilePath = "activitylog.csv";
        File logFile = new File(logFilePath);
        if (!logFile.exists()) return;

        tableModel.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                // Only add rows with enough columns
                if (parts.length >= 7) {
                    Object[] row = new Object[8];
                    row[0] = parts[0]; // Date
                    row[1] = parts[1]; // Time
                    row[2] = parts[2]; // User
                    row[3] = parts[3]; // Action
                    row[4] = parts[4]; // Employee Number
                    row[5] = parts[5]; // Last Name
                    row[6] = parts[6]; // First Name
                    row[7] = "Restore";
                    tableModel.addRow(row);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading activity logs: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleRestore(int row) {
        // Get action type to determine how to parse the row
        String action = tableModel.getValueAt(row, 3).toString();
        String logFilePath = "activitylog.csv";
        String csvFilePath = System.getProperty("csv.filepath");
        if (csvFilePath == null) {
            JOptionPane.showMessageDialog(this, "No employee CSV loaded.", "Restore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Read the original line from activitylog.csv
        String logLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            int currentRow = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (currentRow == row) {
                    logLine = line;
                    break;
                }
                currentRow++;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading activity log: " + ex.getMessage());
            return;
        }
        if (logLine == null) {
            JOptionPane.showMessageDialog(this, "Log entry not found.", "Restore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] parts = logLine.split(",", -1);

        // For "Delete" actions, columns 4-9 are the employee data
        if (action.equals("Delete") && parts.length >= 10) {
            String[] employeeData = new String[6];
            System.arraycopy(parts, 4, employeeData, 0, 6);

            // Append to main employee CSV
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath, true))) {
                bw.write(String.join(",", employeeData));
                bw.newLine();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error restoring employee: " + ex.getMessage());
                return;
            }

            removeRowFromActivityLog(row);
            if (employeePanel != null) {
                employeePanel.refreshTable();
                JOptionPane.showMessageDialog(this, "Employee restored and Employee Management panel refreshed automatically.");
            } else {
                JOptionPane.showMessageDialog(this, "Employee restored. Please refresh Employee Management panel.");
            }
            refreshLogs();
        } else {
            JOptionPane.showMessageDialog(this, "Restore is only supported for deleted employees with full data.", "Restore", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeRowFromActivityLog(int rowToRemove) {
        String logFilePath = "activitylog.csv";
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            int currentRow = 0;
            while ((line = br.readLine()) != null) {
                if (currentRow != rowToRemove) {
                    lines.add(line);
                }
                currentRow++;
            }
        } catch (IOException ex) {
            // handle error
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFilePath))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException ex) {
            // handle error
        }
    }

    private void handleExport() {
        int result = JOptionPane.showConfirmDialog(this,
                "Do you want to download the data?", "Export Data", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Activity Log");
            fileChooser.setSelectedFile(new File("activitylog_export.csv"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToSave))) {
                    // Write headers
                    for (int i = 0; i < tableModel.getColumnCount() - 1; i++) {
                        if (i > 0) bw.write(",");
                        bw.write(tableModel.getColumnName(i));
                    }
                    bw.newLine();
                    // Write data
                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        for (int col = 0; col < tableModel.getColumnCount() - 1; col++) {
                            if (col > 0) bw.write(",");
                            Object val = tableModel.getValueAt(row, col);
                            bw.write(val == null ? "" : val.toString());
                        }
                        bw.newLine();
                    }
                    JOptionPane.showMessageDialog(this, "Activity log exported successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting log: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private static class RestoreButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton button;

        public RestoreButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Center with no extra gap
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // Add spacing on all sides
            button = new JButton("Restore");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Readable but smaller
            button.setPreferredSize(new Dimension(75, 26)); // Smaller button
            button.setFocusable(false);
            add(button);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                      boolean isSelected, boolean hasFocus, int row, int column) {
            boolean canRestore = table.getValueAt(row, 4) != null
                    && !table.getValueAt(row, 4).toString().trim().isEmpty();
            button.setEnabled(canRestore);
            if (!canRestore) {
                button.setToolTipText("Restore unavailable: log entry does not contain full employee data.");
            } else {
                button.setToolTipText(null);
            }
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    private static class RestoreButtonEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton button;
        private ActivityLogPanel parent;
        private int row;
        private boolean canRestore;

        public RestoreButtonEditor(JCheckBox checkBox, ActivityLogPanel parent) {
            super(checkBox);
            this.parent = parent;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // Add spacing on all sides
            button = new JButton("Restore");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // Readable but smaller
            button.setPreferredSize(new Dimension(70, 26)); // Smaller button
            button.setFocusable(false);
            panel.add(button);
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
            canRestore = table.getValueAt(row, 4) != null
                    && !table.getValueAt(row, 4).toString().trim().isEmpty();
            button.setEnabled(canRestore);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Restore";
        }
    }

    private static class ActionCellRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton editButton = new JButton("Edit");
        private final JButton deleteButton = new JButton("Delete");

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
            Dimension buttonSize = new Dimension(60, 28);
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            editButton.setPreferredSize(buttonSize);
            editButton.setFocusable(false);
            deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            deleteButton.setPreferredSize(buttonSize);
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

    private static class ActionCellEditor extends DefaultCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        private final JButton editButton = new JButton("Edit");
        private final JButton deleteButton = new JButton("Delete");
        private JTable table;
        public ActionCellEditor(ActivityLogPanel parent) {
            super(new JCheckBox());
            Dimension buttonSize = new Dimension(60, 28);
            editButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            editButton.setPreferredSize(buttonSize);
            deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            deleteButton.setPreferredSize(buttonSize);
            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    //parent.showEditDialog(row);
                }
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    //parent.handleDelete(row);
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

    public void refreshLogs() {
        loadActivityLogs();
    }
}
