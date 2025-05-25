package com.hainly;

import java.io.*;
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
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (Exception e) {
            users = new HashMap<>();
        }
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, FoodItem> loadInventory(String username) {
        File inventoryFile = new File(INVENTORY_DIR, username + ".inventory");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inventoryFile))) {
            return (Map<String, FoodItem>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private static void saveInventory(String username, Map<String, FoodItem> inventory) {
        File inventoryFile = new File(INVENTORY_DIR, username + ".inventory");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(inventoryFile))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 