package component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

public class JustEmployee {
    JFrame frame;
    JTextField empIdField, nameField, positionField, salaryField, joinDateField, phoneField, branchIdField;
    JButton actionButton, cancelButton;
    DatabaseHelper dbHelper;
    boolean isUpdateMode = false;

    public JustEmployee(int l,int w) {
        dbHelper = new DatabaseHelper(); 
        frame = new JFrame("Employee Details");
        frame.setSize(l,w);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(empIdLabel, gbc);
        gbc.gridx = 1;
        frame.add(empIdField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(nameLabel, gbc);
        gbc.gridx = 1;
        frame.add(nameField, gbc);

        JLabel positionLabel = new JLabel("Position:");
        positionField = new JTextField(15);
        positionField.setEditable(false); 
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(positionLabel, gbc);
        gbc.gridx = 1;
        frame.add(positionField, gbc);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField(15);
        salaryField.setEditable(false);  
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(salaryLabel, gbc);
        gbc.gridx = 1;
        frame.add(salaryField, gbc);

        JLabel joinDateLabel = new JLabel("Join Date:");
        joinDateField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(joinDateLabel, gbc);
        gbc.gridx = 1;
        frame.add(joinDateField, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(phoneLabel, gbc);
        gbc.gridx = 1;
        frame.add(phoneField, gbc);

        JLabel branchIdLabel = new JLabel("Branch ID:");
        branchIdField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(branchIdLabel, gbc);
        gbc.gridx = 1;
        frame.add(branchIdField, gbc);

        actionButton = new JButton("Fetch");
        cancelButton = new JButton("Cancel");

        gbc.gridx = 0;
        gbc.gridy = 7;
        frame.add(actionButton, gbc);
        
        gbc.gridx = 1;
        frame.add(cancelButton, gbc);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdateMode) {
                    fetchEmployeeDetails();
                } else {
                    updateEmployeeDetails();
                }
            }
        });
        
        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void fetchEmployeeDetails() {
        String empId = empIdField.getText();
        String name = nameField.getText();
        
        if (empId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Employee ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT emp_id, name, position, salary, joinDate, phone, branch_id FROM employees WHERE emp_id = ? AND name = ?";
        
        try (Connection conn = dbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, empId);
            stmt.setString(2, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                empIdField.setText(rs.getString("emp_id"));
                nameField.setText(rs.getString("name"));
                positionField.setText(rs.getString("position"));
                salaryField.setText(rs.getString("salary"));
                joinDateField.setText(rs.getString("joinDate"));
                phoneField.setText(rs.getString("phone"));
                branchIdField.setText(rs.getString("branch_id"));
                
                actionButton.setText("Update");
                isUpdateMode = true;
                empIdField.setEditable(false); 
            } else {
                JOptionPane.showMessageDialog(frame, "No employee found with the provided ID and name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEmployeeDetails() {
        String empId = empIdField.getText();
        String name = nameField.getText();
        String position = positionField.getText();
        String joinDate = joinDateField.getText();
        String phone = phoneField.getText();
        String branchId = branchIdField.getText();

        if (empId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Employee ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updateQuery = "UPDATE employees SET position = ?, joinDate = ?, phone = ?, branch_id = ? WHERE emp_id = ? AND name = ?";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, position);
            stmt.setString(2, joinDate);
            stmt.setString(3, phone);
            stmt.setString(4, branchId);
            stmt.setString(5, empId);
            stmt.setString(6, name);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Employee details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update employee details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
