package hainly;

import java.awt.*;
import javax.swing.*;

public class LandingPage extends JPanel {
    private Main mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LandingPage(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        initializeComponents();
    }

    private void initializeComponents() {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Load and scale the logo
        int targetWidth = 150;
        ImageIcon logoIcon = new ImageIcon("logo.png");
        Image originalImage = logoIcon.getImage();

        // Compute height while preserving aspect ratio
        int originalWidth = logoIcon.getIconWidth();
        int originalHeight = logoIcon.getIconHeight();
        int targetHeight = (originalHeight * targetWidth) / originalWidth;

        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledImage);


        // Create logo and title panel
        JLabel logoLabel = new JLabel(logoIcon);
        JLabel titleLabel = new JLabel("Welcome to Hain-ly");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Use a vertical BoxLayout to stack logo and title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(logoLabel);
        titlePanel.add(Box.createVerticalStrut(10)); // Space between logo and title
        titlePanel.add(titleLabel);

        // Add title panel to main layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titlePanel, gbc);


        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
            return;
        }

        if (DatabaseUtil.authenticateUser(username, password)) {
            mainFrame.showUserPage(username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
            return;
        }

        if (DatabaseUtil.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            usernameField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists");
        }
    }
} 