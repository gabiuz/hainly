package com.hainly;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class UserPage extends JPanel {

    private Main mainFrame;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton logoutButton;
    private JTextField searchField;
    private JButton searchButton;
    private String currentUsername;

    public UserPage(Main mainFrame, String username) {
        this.mainFrame = mainFrame;
        this.currentUsername = username;
        setLayout(new BorderLayout());
        initializeComponents();
        refreshInventory();

  private Main mainFrame;
  private JTable inventoryTable;
  private DefaultTableModel tableModel;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JButton logoutButton;
  private String currentUsername;

  public UserPage(Main mainFrame, String username) {
    this.mainFrame = mainFrame;
    this.currentUsername = username;
    setLayout(new BorderLayout());
    initializeComponents();
    refreshInventory();
  }

  private void initializeComponents() {
    // Create table model
    String[] columns = { "Name", "Quantity", "Expiry Date", "Time Until Expiry", "Category" };
    tableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    inventoryTable = new JTable(tableModel);

    // Set up custom renderer for expiry column
    inventoryTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String expiryText = (String) value;

        if (expiryText.startsWith("Expired")) {
          c.setBackground(new Color(220, 20, 60)); // crimson red for expired
        } else if (expiryText.equals("Expires today") || expiryText.equals("Expires tomorrow")) {
          c.setBackground(new Color(255, 165, 0)); // Light yellow for expiring soon
        } else {
          long days = Long.parseLong(expiryText.split(" ")[0]);
          if (days <= 3) {
            c.setBackground(new Color(255, 255, 200)); // orange for 3 days or less
          } else {
            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
          }
        }
        return c;
      }
    });

    JScrollPane scrollPane = new JScrollPane(inventoryTable);
    add(scrollPane, BorderLayout.CENTER);

    // Create button panel
    JPanel buttonPanel = new JPanel(new FlowLayout());
    addButton = new JButton("Add Item");
    editButton = new JButton("Edit Item");
    deleteButton = new JButton("Delete Item");
    logoutButton = new JButton("Logout");

    addButton.addActionListener(e -> showAddItemDialog());
    editButton.addActionListener(e -> showEditItemDialog());
    deleteButton.addActionListener(e -> deleteSelectedItem());
    logoutButton.addActionListener(e -> mainFrame.showLandingPage());

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(logoutButton);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void refreshInventory() {
    tableModel.setRowCount(0);
    FoodItem[] inventory = DatabaseUtil.getInventory(currentUsername);
    for (FoodItem item : inventory) {
      Object[] row = {
          item.getName(),
          item.getQuantity(),
          item.getExpiryDate(),
          item.getExpiryDisplay(),
          item.getCategory()
      };
      tableModel.addRow(row);
    }
  }

  private void showAddItemDialog() {
    JTextField nameField = new JTextField();
    JTextField quantityField = new JTextField();
    JTextField expiryField = new JTextField();
    JTextField categoryField = new JTextField();

    JPanel panel = new JPanel(new GridLayout(4, 2));
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Quantity:"));
    panel.add(quantityField);
    panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
    panel.add(expiryField);
    panel.add(new JLabel("Category:"));
    panel.add(categoryField);

    int result = JOptionPane.showOptionDialog(this, panel, "Add New Item",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
        new String[] { "Save", "Cancel" }, "Save");

    if (result == JOptionPane.OK_OPTION) {
      try {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        LocalDate expiryDate = LocalDate.parse(expiryField.getText());
        String category = categoryField.getText();

        if (name.isEmpty() || category.isEmpty()) {
          throw new IllegalArgumentException("Name and category cannot be empty");
        }


    private void initializeComponents() {
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            refreshInventory();
        });

        searchPanel.add(new JLabel("Search by name: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        add(searchPanel, BorderLayout.NORTH);

        // Create table model
        String[] columns = { "Name", "Quantity", "Expiry Date", "Time Until Expiry", "Category" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(tableModel);

        // Set up custom renderer for expiry column
        inventoryTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String expiryText = (String) value;

                if (expiryText.startsWith("Expired")) {
                    c.setBackground(new Color(255, 200, 200)); // Light red for expired
                } else if (expiryText.equals("Expires today") || expiryText.equals("Expires tomorrow")) {
                    c.setBackground(new Color(255, 255, 200)); // Light yellow for expiring soon
                } else {
                    long days = Long.parseLong(expiryText.split(" ")[0]);
                    if (days <= 3) {
                        c.setBackground(new Color(255, 255, 200)); // Light yellow for 3 days or less
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    }
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add Item");
        editButton = new JButton("Edit Item");
        deleteButton = new JButton("Delete Item");
        logoutButton = new JButton("Logout");

        addButton.addActionListener(e -> showAddItemDialog());
        editButton.addActionListener(e -> showEditItemDialog());
        deleteButton.addActionListener(e -> deleteSelectedItem());
        logoutButton.addActionListener(e -> mainFrame.showLandingPage());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        FoodItem item = new FoodItem(name, quantity, expiryDate, category);
        DatabaseUtil.addFoodItem(currentUsername, item);
        refreshInventory();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
      }

    }
  }


    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshInventory();
            return;
        }

        tableModel.setRowCount(0);
        FoodItem[] searchResults = DatabaseUtil.searchFoodItemsByName(currentUsername, searchTerm);
        for (FoodItem item : searchResults) {
            Object[] row = {
                    item.getName(),
                    item.getQuantity(),
                    item.getExpiryDate(),
                    item.getExpiryDisplay(),
                    item.getCategory()
            };
            tableModel.addRow(row);
        }
    }

    private void refreshInventory() {
        tableModel.setRowCount(0);
        FoodItem[] inventory = DatabaseUtil.getInventory(currentUsername);
        for (FoodItem item : inventory) {
            Object[] row = {
                    item.getName(),
                    item.getQuantity(),
                    item.getExpiryDate(),
                    item.getExpiryDisplay(),
                    item.getCategory()
            };
            tableModel.addRow(row);
        }

  private void showEditItemDialog() {
    int selectedRow = inventoryTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Please select an item to edit");
      return;

    }

    FoodItem[] inventory = DatabaseUtil.getInventory(currentUsername);
    FoodItem selectedItem = inventory[selectedRow];

    JTextField nameField = new JTextField(selectedItem.getName());
    JTextField quantityField = new JTextField(String.valueOf(selectedItem.getQuantity()));
    JTextField expiryField = new JTextField(selectedItem.getExpiryDate().toString());
    JTextField categoryField = new JTextField(selectedItem.getCategory());

    JPanel panel = new JPanel(new GridLayout(4, 2));
    panel.add(new JLabel("Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Quantity:"));
    panel.add(quantityField);
    panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
    panel.add(expiryField);
    panel.add(new JLabel("Category:"));
    panel.add(categoryField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Edit Item",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      try {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        LocalDate expiryDate = LocalDate.parse(expiryField.getText());
        String category = categoryField.getText();

        if (name.isEmpty() || category.isEmpty()) {
          throw new IllegalArgumentException("Name and category cannot be empty");
        }

        selectedItem.setName(name);
        selectedItem.setQuantity(quantity);
        selectedItem.setExpiryDate(expiryDate);
        selectedItem.setCategory(category);

        DatabaseUtil.updateFoodItem(currentUsername, selectedItem);
        refreshInventory();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
      }
    }
  }

  private void deleteSelectedItem() {
    int selectedRow = inventoryTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Please select an item to delete");
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete this item?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      FoodItem[] inventory = DatabaseUtil.getInventory(currentUsername);
      FoodItem selectedItem = inventory[selectedRow];
      DatabaseUtil.deleteFoodItem(currentUsername, selectedItem.getId());
      refreshInventory();
    }

}

  }
}

