package hainly;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
        // If userpage from a previous session already exists, remove it
        if (userPage != null) {
            mainPanel.remove(userPage);
        }

        // create a new UserPage instance
        // guaranteeing that the userPage is always fresh
        userPage = new UserPage(this, username);

        // add the new userPage to the mainPanel
        mainPanel.add(userPage, "USER");

        // switch to the user page view
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