package component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class AdminWindow {
    JFrame window;
    JLabel title;
    JButton updateEmp, paymentDet, pendingPayment, listEmployee;
    DatabaseHelper dbHelper;
    JPanel rightPanel;

    public AdminWindow(int l, int w) {
        dbHelper = new DatabaseHelper();
        windowInit(l, w);

        window.setLayout(new GridBagLayout());

        title = new JLabel("Administrative Console");
        title.setFont(title.getFont().deriveFont(16f));
        title.setForeground(Color.BLACK);
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.insets = new Insets(10, 10, 10, 10);
        window.add(title, titleConstraints);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.white);
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        updateEmp = new JButton("Update Entry");
        paymentDet = new JButton("Payment Details");
        pendingPayment = new JButton("Pending Payments");
        listEmployee = new JButton("List Employees");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        leftPanel.add(updateEmp, gbc);

        gbc.gridy = 2;
        leftPanel.add(paymentDet, gbc);

        gbc.gridy = 3;
        leftPanel.add(pendingPayment, gbc);

        gbc.gridy = 4;
        leftPanel.add(listEmployee, gbc);

        rightPanel = new JPanel(new CardLayout());

        rightPanel.add(createListEmployeesPanel(), "List Employees");
        rightPanel.add(createPaymentDetailsPanel(), "Payment Details");
        rightPanel.add(createPendingPaymentsPanel(), "Pending Payments");
        rightPanel.add(createUpdateEntryPanel(), "Update Entry");

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);

        GridBagConstraints splitPaneConstraints = new GridBagConstraints();
        splitPaneConstraints.gridx = 0;
        splitPaneConstraints.gridy = 5;
        splitPaneConstraints.weightx = 1.0;
        splitPaneConstraints.weighty = 1.0;
        splitPaneConstraints.fill = GridBagConstraints.BOTH;

        window.add(splitPane, splitPaneConstraints);

        updateEmp.addActionListener(e -> showPanel("Update Entry"));
        paymentDet.addActionListener(e -> showPanel("Payment Details"));
        pendingPayment.addActionListener(e -> showPanel("Pending Payments"));
        listEmployee.addActionListener(e -> showPanel("List Employees"));

        window.setVisible(true);
    }

    private void windowInit(int l, int w) {
        window = new JFrame("Administrator");
        window.setSize(l, w);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.WHITE);
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (rightPanel.getLayout());
        cl.show(rightPanel, panelName);
    }

    private JPanel createUpdateEntryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JRadioButton employeeRadio = new JRadioButton("Employee");
        JRadioButton supplierRadio = new JRadioButton("Supplier");
        JRadioButton branchRadio = new JRadioButton("Branch");

        ButtonGroup group = new ButtonGroup();
        group.add(employeeRadio);
        group.add(supplierRadio);
        group.add(branchRadio);

        JPanel radioPanel = new JPanel();
        radioPanel.add(employeeRadio);
        radioPanel.add(supplierRadio);
        radioPanel.add(branchRadio);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 10, 5);
        panel.add(radioPanel, gbc);

        JTextField empIdField = new JTextField(15);
        JTextField empNameField = new JTextField(15);
        JTextField empPositionField = new JTextField(15);
        JTextField empSalaryField = new JTextField(15);
        JTextField empJoinDateField = new JTextField(15);
        JTextField empPhoneField = new JTextField(15);
        JTextField empBranchIdField = new JTextField(15);

        JTextField supIdField = new JTextField(15);
        JTextField supNameField = new JTextField(15);
        JTextField supItemField = new JTextField(15);
        JTextField supContactField = new JTextField(15);

        JTextField branchIdField = new JTextField(15);
        JTextField branchNameField = new JTextField(15);
        JTextField branchPincodeField = new JTextField(15);
        JTextField branchPhoneField = new JTextField(15);
        JTextField branchCityField = new JTextField(15);
        JTextField branchStateField = new JTextField(15);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        gbc.gridwidth = 1;

        employeeRadio.addActionListener(e -> {
            updateInputPanel(inputPanel, "Employee", empIdField, empNameField, empPositionField, empSalaryField,
                    empJoinDateField, empPhoneField, empBranchIdField);
        });

        supplierRadio.addActionListener(e -> {
            updateInputPanel(inputPanel, "Supplier", supIdField, supNameField, supItemField, supContactField);
        });

        branchRadio.addActionListener(e -> {
            updateInputPanel(inputPanel, "Branch", branchIdField, branchNameField, branchPincodeField, branchPhoneField,
                    branchCityField, branchStateField);
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(inputPanel, gbc);

        JButton addUpdateButton = new JButton("Add/Update");
        JButton cancelButton = new JButton("Cancel");

        addUpdateButton.addActionListener(e -> {
            if (employeeRadio.isSelected()) {
                String empId = empIdField.getText();
                String name = empNameField.getText();
                String position = empPositionField.getText();
                String salary = empSalaryField.getText();
                String joinDate = empJoinDateField.getText();
                String phone = empPhoneField.getText();
                String branchId = empBranchIdField.getText();

                java.sql.Date sqlDate = null;
                try {
                    LocalDate localDate = LocalDate.parse(joinDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    sqlDate = java.sql.Date.valueOf(localDate);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid date format. Please use 'yyyy-MM-dd'.");
                    return; 
                }

                String query = empId.isEmpty() ? 
                    "INSERT INTO employee (name, position, salary, joinDate, phone, branch_id) VALUES (?, ?, ?, ?, ?, ?)" :
                    "UPDATE employee SET name=?, position=?, salary=?, joinDate=?, phone=?, branch_id=? WHERE emp_id=?";

                try (PreparedStatement stmt = dbHelper.getConnection().prepareStatement(query)) {
                    if (!empId.isEmpty()) {
                        stmt.setInt(7, Integer.parseInt(empId));
                    }
                    stmt.setString(1, name);
                    stmt.setString(2, position);
                    stmt.setDouble(3, Double.parseDouble(salary));
                    stmt.setDate(4, sqlDate);
                    stmt.setString(5, phone);
                    stmt.setInt(6, Integer.parseInt(branchId));
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Employee added/updated successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            } else if (supplierRadio.isSelected()) {
                String supId = supIdField.getText();
                String name = supNameField.getText();
                String item = supItemField.getText();
                String contact = supContactField.getText();

                String query = supId.isEmpty() ? 
                    "INSERT INTO supplier (name, item, contact) VALUES (?, ?, ?)" :
                    "UPDATE supplier SET name=?, item=?, contact=? WHERE sup_id=?";

                try (PreparedStatement stmt = dbHelper.getConnection().prepareStatement(query)) {
                    if (!supId.isEmpty()) {
                        stmt.setInt(4, Integer.parseInt(supId));
                    }
                    stmt.setString(1, name);
                    stmt.setString(2, item);
                    stmt.setString(3, contact);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Supplier added/updated successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            } else if (branchRadio.isSelected()) {
                String branchId = branchIdField.getText();
                String name = branchNameField.getText();
                String pincode = branchPincodeField.getText();
                String phone = branchPhoneField.getText();
                String city = branchCityField.getText();
                String state = branchStateField.getText();

                String query = branchId.isEmpty() ? 
                    "INSERT INTO branch (name, pincode, phone, add_city, add_state) VALUES (?, ?, ?, ?, ?)" :
                    "UPDATE branch SET name=?, pincode=?, phone=?, add_city=?, add_state=? WHERE branch_id=?";

                try (PreparedStatement stmt = dbHelper.getConnection().prepareStatement(query)) {
                    if (!branchId.isEmpty()) {
                        stmt.setInt(6, Integer.parseInt(branchId));
                    }
                    stmt.setString(1, name);
                    stmt.setString(2, pincode);
                    stmt.setString(3, phone);
                    stmt.setString(4, city);
                    stmt.setString(5, state);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Branch added/updated successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(addUpdateButton, gbc);

        gbc.gridx = 1;
        panel.add(cancelButton, gbc);

        return panel;
    }

    private void updateInputPanel(JPanel inputPanel, String type, JTextField... fields) {
        inputPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (JTextField field : fields) {
            JLabel label = new JLabel(field.getName());
            inputPanel.add(label, gbc);
            gbc.gridx++;
            inputPanel.add(field, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
        }
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private JPanel createListEmployeesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"Emp ID", "Name", "Position", "Salary", "Join Date", "Phone"}, 0);
        JTable table = new JTable(model);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            try (Statement stmt = dbHelper.getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM employee")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("emp_id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getDate("joinDate"),
                        rs.getString("phone")
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPaymentDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"Payment ID", "Amount", "Method", "Date", "Status", "MID"}, 0);
        JTable table = new JTable(model);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            try (Statement stmt = dbHelper.getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM payment")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("pay_id"),
                        rs.getDouble("amount"),
                        rs.getString("method"),
                        rs.getDate("tranDate"),
                        rs.getString("status"),
                        rs.getInt("mid")
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPendingPaymentsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"Payment ID", "Amount", "Method", "Date", "Status"}, 0);
        JTable table = new JTable(model);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            try (Statement stmt = dbHelper.getConnection().createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM pending_payments WHERE status='p'")) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("pay_id"),
                        rs.getDouble("amount"),
                        rs.getString("method"),
                        rs.getDate("tranDate"),
                        rs.getString("status")
                    });
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

}
