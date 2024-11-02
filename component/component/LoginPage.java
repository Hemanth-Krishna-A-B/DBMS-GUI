package component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginPage {

    JLabel title;
    JLabel usrIdLabel;
    JLabel usernamLabel;
    JLabel pasLabel;
    JTextField usrId;
    JTextField userName;
    JPasswordField password;
    JFrame window;
    JPanel formPanel;
    JButton login;
    JButton cancel;

    int person;

    public LoginPage(int l, int w, int id) {
        person = id;
        window = new JFrame("Login Window");
        window.setSize(l, w);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        setTitle(id);

        ImageIcon originalIcon = new ImageIcon("icon2.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(resizedImage));

        JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        window.add(titlePanel, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        Font labelFont = new Font("Arial", Font.PLAIN, 16);

        usrId = new JTextField();
        userName = new JTextField();
        password = new JPasswordField();

        Dimension fieldSize = new Dimension(200, 30);
        usrId.setPreferredSize(fieldSize);
        userName.setPreferredSize(fieldSize);
        password.setPreferredSize(fieldSize);

        usrIdLabel = new JLabel("USER ID    :   ");
        usernamLabel = new JLabel("USER NAME   :   ");
        pasLabel = new JLabel("PASSWORD    :   ");

        usrIdLabel.setFont(labelFont);
        usernamLabel.setFont(labelFont);
        pasLabel.setFont(labelFont);

        formPanel.add(usrIdLabel);
        formPanel.add(usrId);
        formPanel.add(usernamLabel);
        formPanel.add(userName);
        formPanel.add(pasLabel);
        formPanel.add(password);
        formPanel.setBackground(Color.white);

        login = new JButton("Login");
        cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RootWindow(l, w);
                window.dispose();
            }
        });
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userName.getText();
                String pass = new String(password.getPassword());
                if (name.equals("admin") && pass.equals("admin")) {
                    window.dispose();
                    switch (person) {
                        case 0 -> new AdminWindow(1000, 600);
                        case 1 -> new Office(1000, 600);
                        case 2 -> new JustSupplier(1000, 600);
                    }
                } else {
                    JOptionPane.showMessageDialog(window, "Wrong Credentials");
                }
            }
        });

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(imageLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(formPanel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(login, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(cancel, gbc);

        window.add(centerPanel, BorderLayout.CENTER);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
    }

    private void setTitle(int id) {
        switch (id) {
            case 0 -> title = new JLabel("Login as Administrator", JLabel.CENTER);
            case 1 -> title = new JLabel("Login as Post Office", JLabel.CENTER);
            case 2 -> title = new JLabel("Login as Supplier", JLabel.CENTER);
        }
        title.setFont(new Font("Arial", Font.BOLD, 16));
    }

}
