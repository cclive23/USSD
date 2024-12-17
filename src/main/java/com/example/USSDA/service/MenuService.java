package com.example.USSDA.service;

import com.example.USSDA.model.Menu;
import com.example.USSDA.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Retrieve a menu ID based on parentId and actionId (DB)
    public Integer getMenuIdByParentAndAction(int currentMenu, int input) {
        return menuRepository.findMenuIdByParentIdAndActionId(currentMenu, input);
    }

    // Validate if an input is valid for the current menu
    public boolean isValidInput(int parentId, int input) {
        List<Menu> subMenus = menuRepository.findByParentId(parentId);
        return input > 0 && input <= subMenus.size(); // Valid if input is within range of submenus
    }

    // Check if menu has reached payment status
    public boolean getMenuPaymentStatus(int menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu with ID " + menuId + " not found"));
        return menu.isMenuPayment();
    }

    public String getPaymentDescription(int menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu with ID " + menuId + " not found"));
        return menu.getDescription();
    }

    public Integer getParentIdByMenuId(int menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu with ID " + menuId + " not found"));
        return menu.getParentId();
    }

    // Get submenus based on a parent menu ID from the DB and then store it in Redis
    public String getMenu(String key) {
        // Try to retrieve the value from Redis
        Map<Object, Object> value = redisTemplate.opsForHash().entries(key); // Fetch as a hash structure
        if (!value.isEmpty()) {
            // If value exists in Redis, build and return the menu string
            return buildRedisMenuString(value);
        } else {
            // Split the key to extract the menu hierarchy
            String[] use = key.split(":");
            int parentId;

            if (use.length == 2) {
                // If the key is "menu:0", handle it as the main menu
                parentId = Integer.parseInt(use[1]);
            } else {
                // For other keys (e.g., "menu:x:y"), find the parent menu ID
                int currentMenu = Integer.parseInt(use[use.length - 2]);
                int nextMenu = Integer.parseInt(use[use.length - 1]);
                parentId = getMenuIdByParentAndAction(currentMenu, nextMenu);
            }

            // Retrieve the submenu from the database
            List<Menu> subMenus = menuRepository.findByParentId(parentId);

            if (subMenus.isEmpty()) {
                return "No submenus found for the given menu.";
            }

            // Convert the submenu list to a map for storing in Redis
            Map<Object, Object> subMenuMap = new HashMap<>();
            for (Menu menu : subMenus) {
                subMenuMap.put(String.valueOf(menu.getActionId()), menu.getMenuName());
            }

            // Store the submenu in Redis
            redisTemplate.opsForHash().putAll(key, subMenuMap);

            // Build and return the menu string
            return buildRedisMenuString(subMenuMap);
        }
    }


    // Previous menu method
    public String getPreviousMenu(String key) {
        String[] use = key.split(":");
        if (use.length == 2) {
            return getMenu(key);
        } else {
            StringBuilder newKey = new StringBuilder();
            for (int i = 0; i < use.length - 1; i++) {
                if (i > 0) {
                    newKey.append(":");
                }
                newKey.append(use[i]);
            }

            // Return "menu:0" explicitly if the result would be empty
            return !newKey.isEmpty() ? newKey.toString() : "menu:0";
        }
    }


    // Helper method to construct the menu display string for entity types
    private String buildMenuString(List<Menu> menus) {
        StringBuilder menuString = new StringBuilder();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            menuString.append(i + 1).append(". ").append(menu.getMenuName()).append("\n");
        }
        return menuString.toString().trim(); // Remove trailing newline
    }

    private String buildRedisMenuString(Map<Object, Object> redisMenu) {
        StringBuilder menuString = new StringBuilder();

        // Iterate over the menu entries
        for (Map.Entry<Object, Object> entry : redisMenu.entrySet()) {
            menuString.append(entry.getKey()) // Add the key (e.g., 1, 2)
                    .append(". ")          // Append the numbering format
                    .append(entry.getValue()) // Add the value (e.g., "YOLO Voice")
                    .append("\n");         // Add a newline for formatting
        }

        return menuString.toString().trim();
    }
}
