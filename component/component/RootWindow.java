package component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RootWindow {

    JButton adminLogin, bankLogin, supplierLogin;
    JFrame root;

    public RootWindow(int l, int w) {
        root = new JFrame();
        root.setSize(l, w);
        root.setTitle("Parcel Management System");
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setResizable(false);
        root.setLocationRelativeTo(null);
        root.getContentPane().setBackground(Color.WHITE);

        buttonConfig();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Parcel Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon originalIcon = new ImageIcon("icon1.jpg");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        buttonPanel.add(adminLogin);
        buttonPanel.add(bankLogin);
        buttonPanel.add(supplierLogin);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(imageLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        root.add(mainPanel);
        root.setVisible(true);
    }

    private void buttonConfig() {
        adminLogin = new JButton("Admin Login");
        bankLogin = new JButton("Bank Login");
        supplierLogin = new JButton("Supplier Login");

        adminLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                root.dispose();
                new LoginPage(1000,600,0);
            }
        });

        bankLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                root.dispose();
                new LoginPage(1000,600,1);
            }
        });

        supplierLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                root.dispose();
                new LoginPage(1000,600,2);
            }
        });
    }

    
}
