package com.hainly;

import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LandingPage landingPage;
    private UserPage userPage;
    private String currentUsername;

    public Main() {
        setTitle("Hain-ly Food Inventory Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        landingPage = new LandingPage(this);
        userPage = null; // Will be created when user logs in

        // Add panels to card layout
        mainPanel.add(landingPage, "LANDING");

        // Show landing page by default
        cardLayout.show(mainPanel, "LANDING");

        // Add main panel to frame
        add(mainPanel);
    }

    public void showUserPage(String username) {
        this.currentUsername = username;
        if (userPage == null) {
            userPage = new UserPage(this, username);
            mainPanel.add(userPage, "USER");
        }
        cardLayout.show(mainPanel, "USER");
    }

    public void showLandingPage() {
        cardLayout.show(mainPanel, "LANDING");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
} 