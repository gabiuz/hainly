package com.hainly;

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

        // Title
        JLabel titleLabel = new JLabel("Welcome to Hain-ly");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

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
            mainFrame.showUserPage();
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