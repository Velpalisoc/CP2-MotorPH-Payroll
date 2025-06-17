package util;

import model.Employee;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "employees.csv";

    public static List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4) {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String position = data[2].trim();
                    double rate = Double.parseDouble(data[3].trim());

                    employees.add(new Employee(id, name, position, rate));
                }
            }
        } catch (FileNotFoundException e) {
            // If file doesn't exist yet, we just return an empty list.
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return employees;
    }
}
