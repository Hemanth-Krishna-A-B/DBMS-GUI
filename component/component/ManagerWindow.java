package component;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ManagerWindow {
    JFrame window;
    JLabel title;
    JButton addOrUpdateEmployee, viewAllEmployees, inventoryStatus, parcelStatus, bankDetails;
    JPanel rightPanel;
    DatabaseHelper dbHelper;

    public ManagerWindow(int l, int w) {
        dbHelper = new DatabaseHelper();
        windowInit(l, w);

        window.setLayout(new GridBagLayout());

        title = new JLabel("Manager Console");
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

        addOrUpdateEmployee = new JButton("Add/Update Employee");
        viewAllEmployees = new JButton("View All Employees");
        inventoryStatus = new JButton("Inventory Status");
        parcelStatus = new JButton("Parcel Status");
        bankDetails = new JButton("Bank Details");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        leftPanel.add(addOrUpdateEmployee, gbc);
        gbc.gridy = 2; leftPanel.add(viewAllEmployees, gbc);
        gbc.gridy = 3; leftPanel.add(inventoryStatus, gbc);
        gbc.gridy = 4; leftPanel.add(parcelStatus, gbc);
        gbc.gridy = 5; leftPanel.add(bankDetails, gbc);

        rightPanel = new JPanel(new CardLayout());

        rightPanel.add(createViewAllEmployeesPanel(), "View All Employees");
        rightPanel.add(createInventoryStatusPanel(), "Inventory Status");
        rightPanel.add(createParcelStatusPanel(), "Parcel Status");
        rightPanel.add(createBankDetailsPanel(), "Bank Details");

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

        addOrUpdateEmployee.addActionListener(e -> showPanel("Add/Update Employee"));
        viewAllEmployees.addActionListener(e -> showPanel("View All Employees"));
        inventoryStatus.addActionListener(e -> showPanel("Inventory Status"));
        parcelStatus.addActionListener(e -> showPanel("Parcel Status"));
        bankDetails.addActionListener(e -> showPanel("Bank Details"));

        window.setVisible(true);
    }

    private void windowInit(int l, int w) {
        window = new JFrame("Manager");
        window.setSize(l, w);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.WHITE);
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (rightPanel.getLayout());
        cl.show(rightPanel, panelName);
    }

    

    private JPanel createViewAllEmployeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable employeeTable = createTable("SELECT * FROM employee");
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInventoryStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable inventoryTable = createTable("SELECT * FROM inventory");
        panel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createParcelStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Pending", "Error", "Successful"});
        JTable parcelTable = new JTable();

        statusDropdown.addActionListener(e -> {
            String selectedStatus = statusDropdown.getSelectedItem().toString();
            parcelTable.setModel(createTableModel("SELECT * FROM parcels WHERE status = '" + selectedStatus + "'"));
        });

        panel.add(statusDropdown, BorderLayout.NORTH);
        panel.add(new JScrollPane(parcelTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBankDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel accountNumberLabel = new JLabel("Account Number:");
        JTextField accountNumberField = new JTextField(15);
        JLabel bankNameLabel = new JLabel("Bank Name:");
        JTextField bankNameField = new JTextField(15);
        JLabel branchLabel = new JLabel("Branch:");
        JTextField branchField = new JTextField(15);
        JButton updateButton = new JButton("Update");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(accountNumberLabel, gbc);
        gbc.gridx = 1; panel.add(accountNumberField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(bankNameLabel, gbc);
        gbc.gridx = 1; panel.add(bankNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(branchLabel, gbc);
        gbc.gridx = 1; panel.add(branchField, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(updateButton, gbc);

        updateButton.addActionListener(e -> updateBankDetails(accountNumberField.getText(), bankNameField.getText(), branchField.getText()));
        loadBankDetails(accountNumberField, bankNameField, branchField);
        return panel;
    }

    private JTable createTable(String query) {
        DefaultTableModel model = createTableModel(query);
        return new JTable(model);
    }

    private DefaultTableModel createTableModel(String query) {
        DefaultTableModel model = new DefaultTableModel();

        try (ResultSet rs = dbHelper.getTableData(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    private void updateBankDetails(String accountNumber, String bankName, String branch) {
        
    }

    private void loadBankDetails(JTextField accountField, JTextField bankField, JTextField branchField) {
       
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerWindow(1000, 600));
    }
}
