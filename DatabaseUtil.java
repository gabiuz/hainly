package com.hainly;

import java.io.*;
import java.util.*;

public class DatabaseUtil {
    private static final String USERS_FILE = "users.txt";
    private static final String INVENTORY_FILE = "inventory.txt";
    private static User[] users = new User[0];
    private static FoodItem[] inventory = new FoodItem[0];

    static {
        loadUsers();
        loadInventory();
    }

    public static boolean registerUser(String username, String password) {
        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        
        // Create new array with increased size
        User[] newUsers = new User[users.length + 1];
        System.arraycopy(users, 0, newUsers, 0, users.length);
        newUsers[users.length] = new User(username, password);
        users = newUsers;
        
        saveUsers();
        return true;
    }

    public static boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public static void addFoodItem(FoodItem item) {
        FoodItem[] newInventory = new FoodItem[inventory.length + 1];
        System.arraycopy(inventory, 0, newInventory, 0, inventory.length);
        newInventory[inventory.length] = item;
        inventory = newInventory;
        saveInventory();
    }

    public static void updateFoodItem(FoodItem item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getId().equals(item.getId())) {
                inventory[i] = item;
                break;
            }
        }
        saveInventory();
    }

    public static void deleteFoodItem(String id) {
        int indexToRemove = -1;
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getId().equals(id)) {
                indexToRemove = i;
                break;
            }
        }
        
        if (indexToRemove != -1) {
            FoodItem[] newInventory = new FoodItem[inventory.length - 1];
            System.arraycopy(inventory, 0, newInventory, 0, indexToRemove);
            System.arraycopy(inventory, indexToRemove + 1, newInventory, indexToRemove, inventory.length - indexToRemove - 1);
            inventory = newInventory;
            saveInventory();
        }
    }

    public static FoodItem[] getInventory() {
        return Arrays.copyOf(inventory, inventory.length);
    }

    private static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (User[]) ois.readObject();
        } catch (Exception e) {
            users = new User[0];
        }
    }

    private static void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
            inventory = (FoodItem[]) ois.readObject();
        } catch (Exception e) {
            inventory = new FoodItem[0];
        }
    }

    private static void saveInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 