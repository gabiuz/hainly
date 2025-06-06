package com.hainly;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseUtil {
  private static final String USERS_FILE = "users.txt";
  private static final String INVENTORY_DIR = "inventories";
  private static Map<String, User> users = new HashMap<>();

  static {
    // Create inventories directory if it doesn't exist
    new File(INVENTORY_DIR).mkdirs();
    loadUsers();
  }

  public static boolean registerUser(String username, String password) {
    if (users.containsKey(username)) {
      return false;
    }

    users.put(username, new User(username, password));
    // Create empty inventory file for new user
    saveInventory(username, new HashMap<>());
    saveUsers();
    return true;
  }

  public static boolean authenticateUser(String username, String password) {
    User user = users.get(username);
    return user != null && user.getPassword().equals(password);
  }

  public static void addFoodItem(String username, FoodItem item) {
    Map<String, FoodItem> userInventory = loadInventory(username);
    userInventory.put(item.getId(), item);
    saveInventory(username, userInventory);
  }

  public static void updateFoodItem(String username, FoodItem item) {
    Map<String, FoodItem> userInventory = loadInventory(username);
    userInventory.put(item.getId(), item);
    saveInventory(username, userInventory);
  }

  public static void deleteFoodItem(String username, String id) {
    Map<String, FoodItem> userInventory = loadInventory(username);
    userInventory.remove(id);
    saveInventory(username, userInventory);
  }

  public static FoodItem[] getInventory(String username) {
    Map<String, FoodItem> userInventory = loadInventory(username);
    return userInventory.values().toArray(new FoodItem[0]);
  }

  private static void loadUsers() {
    try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 2) {
          users.put(parts[0], new User(parts[0], parts[1]));
        }
      }
    } catch (IOException e) {
      users = new HashMap<>();
    }
  }

  private static void saveUsers() {
    try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
      for (User user : users.values()) {
        writer.println(user.getUsername() + "|" + user.getPassword());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    public static FoodItem[] searchFoodItemsByName(String username, String searchTerm) {
        Map<String, FoodItem> userInventory = loadInventory(username);
        return userInventory.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toArray(FoodItem[]::new);
    }

    private static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            users = new HashMap<>();

  }

  private static Map<String, FoodItem> loadInventory(String username) {
    File inventoryFile = new File(INVENTORY_DIR, username + ".inventory");
    Map<String, FoodItem> inventory = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(inventoryFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
          String id = parts[0];
          String name = parts[1];
          int quantity = Integer.parseInt(parts[2]);
          LocalDate expiryDate = LocalDate.parse(parts[3]);
          String category = parts[4];

          FoodItem item = new FoodItem(name, quantity, expiryDate, category);
          item.setId(id); // Set the original ID
          inventory.put(id, item);
        }
      }
    } catch (IOException e) {
      // Return empty inventory if file doesn't exist or can't be read
      return new HashMap<>();
    }
    return inventory;
  }

  private static void saveInventory(String username, Map<String, FoodItem> inventory) {
    File inventoryFile = new File(INVENTORY_DIR, username + ".inventory");
    try (PrintWriter writer = new PrintWriter(new FileWriter(inventoryFile))) {
      for (FoodItem item : inventory.values()) {
        writer.println(String.format("%s|%s|%d|%s|%s",
            item.getId(),
            item.getName(),
            item.getQuantity(),
            item.getExpiryDate(),
            item.getCategory()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
