package app;

import model.Employee;
import util.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeListFrame extends JFrame {

    public EmployeeListFrame() {
        setTitle("All Employee Records");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"ID", "Full Name", "Position", "Rate per Hour"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        List<Employee> employees = FileHandler.loadEmployees();
        for (Employee emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getFullName(),
                emp.getPosition(),
                emp.getRatePerHour()
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
