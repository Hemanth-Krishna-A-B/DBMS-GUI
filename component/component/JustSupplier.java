package component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

public class JustSupplier {
    JFrame frame;
    JTextField supIdField, nameField, itemField, contactField;
    JButton actionButton, cancelButton, addButton;
    DatabaseHelper dbHelper;
    boolean isUpdateMode = false;

    public JustSupplier(int l, int w) {
        dbHelper = new DatabaseHelper();
        frame = new JFrame("Supplier Console");
        frame.setSize(l, w);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Supplier Console");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        JLabel supIdLabel = new JLabel("Supplier ID:");
        supIdLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        supIdField = new JTextField(15);
        supIdField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(supIdLabel, gbc);
        gbc.gridx = 1;
        frame.add(supIdField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(nameLabel, gbc);
        gbc.gridx = 1;
        frame.add(nameField, gbc);

        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        itemField = new JTextField(15);
        itemField.setEditable(false);
        itemField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(itemLabel, gbc);
        gbc.gridx = 1;
        frame.add(itemField, gbc);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contactField = new JTextField(15);
        contactField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(contactLabel, gbc);
        gbc.gridx = 1;
        frame.add(contactField, gbc);

        actionButton = new JButton("Fetch");
        actionButton.setFont(new Font("Arial", Font.PLAIN, 18));
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 18));
        addButton = new JButton("Add New Supplier");
        addButton.setFont(new Font("Arial", Font.PLAIN, 18));

        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(actionButton, gbc);

        gbc.gridx = 1;
        frame.add(cancelButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Span across both columns
        frame.add(addButton, gbc);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdateMode) {
                    fetchSupplierDetails();
                } else {
                    updateSupplierDetails();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
                isUpdateMode = false;
                actionButton.setText("Add");
                supIdField.setEditable(true);
                itemField.setEditable(true);
            }
        });

        frame.setVisible(true);
    }

    private void clearFields() {
        supIdField.setText("");
        nameField.setText("");
        itemField.setText("");
        contactField.setText("");
    }

    private void fetchSupplierDetails() {
        String supId = supIdField.getText();
        String name = nameField.getText();

        if (supId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Supplier ID and Name cannot be empty.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT sup_id, name, item, contact FROM supplier WHERE sup_id = ? AND name = ?";

        try (Connection conn = dbHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, supId);
            stmt.setString(2, name);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                supIdField.setText(rs.getString("sup_id"));
                nameField.setText(rs.getString("name"));
                itemField.setText(rs.getString("item"));
                contactField.setText(rs.getString("contact"));

                actionButton.setText("Update");
                isUpdateMode = true;
                supIdField.setEditable(false);
                itemField.setEditable(true);
            } else {
                JOptionPane.showMessageDialog(frame, "No supplier found with the provided ID and name.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSupplierDetails() {
        String supId = supIdField.getText();
        String name = nameField.getText();
        String item = itemField.getText();
        String contact = contactField.getText();

        if (supId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Supplier ID and Name cannot be empty.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updateQuery = "UPDATE supplier SET item = ?, contact = ?, name = ? WHERE sup_id = ?";

        try (Connection conn = dbHelper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, item);
            stmt.setString(2, contact);
            stmt.setString(3, name);
            stmt.setString(4, supId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Supplier details updated successfully.", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                isUpdateMode = false;
                actionButton.setText("Fetch");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to update supplier details.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
