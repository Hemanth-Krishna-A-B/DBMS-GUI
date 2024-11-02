package component;

import java.awt.*;
import javax.swing.*;
import java.util.List;

public class Office {
    JFrame window;
    JLabel title;
    JButton transactionDet, updateTrans, managerCon, employeCon;
    DatabaseHelper dbHelper;
    JPanel rightPanel;

    public Office(int l, int w) {
        dbHelper = new DatabaseHelper();
        windowInit(l, w);

        window.setLayout(new GridBagLayout());
        title = new JLabel("Office Administration Console", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.insets = new Insets(10, 10, 10, 10);
        window.add(title, titleConstraints);

        JPanel leftPanel = createLeftPanel();
        rightPanel = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);

        GridBagConstraints splitPaneConstraints = new GridBagConstraints();
        splitPaneConstraints.gridx = 0;
        splitPaneConstraints.gridy = 1;
        splitPaneConstraints.weightx = 1.0;
        splitPaneConstraints.weighty = 1.0;
        splitPaneConstraints.fill = GridBagConstraints.BOTH;
        window.add(splitPane, splitPaneConstraints);

        transactionDet.addActionListener(e -> showParcelDetails());
        updateTrans.addActionListener(e -> updateParcelPopup());
        window.setVisible(true);
    }

    private void windowInit(int l, int w) {
        window = new JFrame("Administrator");
        window.setSize(l, w);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.WHITE);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        transactionDet = new JButton("Parcel Details");
        updateTrans = new JButton("Update Parcel");
        managerCon = new JButton("Manager Console");
        employeCon = new JButton("Employee Console");

        gbc.gridy = 0;
        leftPanel.add(transactionDet, gbc);
        gbc.gridy++;
        leftPanel.add(updateTrans, gbc);
        gbc.gridy++;
        leftPanel.add(managerCon, gbc);
        gbc.gridy++;
        leftPanel.add(employeCon, gbc);

        return leftPanel;
    }

    private void showParcelDetails() {
        rightPanel.removeAll();
        List<Mail> mailList = dbHelper.getAllMails();

        String[] columnNames = { "MID", "Track ID", "Weight", "Serial No", "CID", "Recipient Name", "Recipient Address", "Recipient PIN", "Sender Name", "Sender ID" };
        Object[][] data = new Object[mailList.size()][columnNames.length];

        for (int i = 0; i < mailList.size(); i++) {
            Mail mail = mailList.get(i);
            data[i] = new Object[]{ mail.getMid(), mail.getTrackId(), mail.getWeight(), mail.getSerNo(), mail.getCid(), 
                                     mail.getRecName(), mail.getRecAdd(), mail.getRecPin(), mail.getSenderName(), mail.getSenderId() };
        }

        JTable mailTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(mailTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void updateParcelPopup() {
        JTextField midField = new JTextField(10);
        JTextField trackIdField = new JTextField(10);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter MID:"));
        inputPanel.add(midField);
        inputPanel.add(Box.createHorizontalStrut(15));
        inputPanel.add(new JLabel("Enter Track ID:"));
        inputPanel.add(trackIdField);

        int result = JOptionPane.showConfirmDialog(window, inputPanel, "Find Parcel to Update", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String mid = midField.getText();
            String trackId = trackIdField.getText();

            Parcel parcel = dbHelper.getParcel(mid, trackId);
            if (parcel != null) {
                showUpdateForm(parcel);
            } else {
                JOptionPane.showMessageDialog(window, "No parcel found with given MID and Track ID.");
            }
        }
    }

    private void showUpdateForm(Parcel parcel) {
        JTextField weightField = new JTextField(String.valueOf(parcel.getWeight()));
        JTextField serNoField = new JTextField(parcel.getSerNo());
        JTextField recNameField = new JTextField(parcel.getRecName());
        JTextField recAddField = new JTextField(parcel.getRecAdd());
        JTextField recPinField = new JTextField(parcel.getRecPin());
        JTextField senderNameField = new JTextField(parcel.getSenderName());

        JPanel updatePanel = new JPanel(new GridLayout(6, 2, 10, 10));
        updatePanel.add(new JLabel("Weight:"));
        updatePanel.add(weightField);
        updatePanel.add(new JLabel("Serial No:"));
        updatePanel.add(serNoField);
        updatePanel.add(new JLabel("Recipient Name:"));
        updatePanel.add(recNameField);
        updatePanel.add(new JLabel("Recipient Address:"));
        updatePanel.add(recAddField);
        updatePanel.add(new JLabel("Recipient PIN:"));
        updatePanel.add(recPinField);
        updatePanel.add(new JLabel("Sender Name:"));
        updatePanel.add(senderNameField);

        int updateResult = JOptionPane.showConfirmDialog(window, updatePanel, "Update Parcel Details", JOptionPane.OK_CANCEL_OPTION);
        if (updateResult == JOptionPane.OK_OPTION) {
            parcel.setWeight(Double.parseDouble(weightField.getText()));
            parcel.setSerNo(serNoField.getText());
            parcel.setRecName(recNameField.getText());
            parcel.setRecAdd(recAddField.getText());
            parcel.setRecPin(recPinField.getText());
            parcel.setSenderName(senderNameField.getText());

            dbHelper.updateParcel(parcel);
            JOptionPane.showMessageDialog(window, "Parcel details updated successfully!");
        }
    }
}
