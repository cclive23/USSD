package com.example.USSDA.service;

import com.example.USSDA.model.Menu;
import com.example.USSDA.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;



    // Retrieve a menu ID based on parentId and actionId
    public Integer getMenuIdByParentAndAction(int currentMenu, int input) {
        return menuRepository.findMenuIdByParentIdAndActionId(currentMenu, input);
    }

    // Get the main menu (root menus where parentId is null)
    public String getMainMenu() {
        List<Menu> mainMenu = menuRepository.findAll()
                .stream()
                .filter(menu -> menu.getParentId() == 0) // Filter root menus
                .toList();

        return buildMenuString(mainMenu);
    }
    // Validate if an input is valid for the current menu
    public boolean isValidInput(int parentId, int input) {
        List<Menu> subMenus = menuRepository.findByParentId(parentId);
        return input > 0 && input <= subMenus.size(); // Valid if input is within range of submenus
    }


    //Check if menu has reached payment status
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


    // Get submenus based on a parent menu ID
    public String getSubMenu(int parentId) {
        List<Menu> subMenu = menuRepository.findByParentId(parentId);
        return buildMenuString(subMenu);
    }

    // Helper method to construct the menu display string
    private String buildMenuString(List<Menu> menus) {
        StringBuilder menuString = new StringBuilder();
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            menuString.append(i + 1).append(". ").append(menu.getMenuName()).append("\n");
        }
        return menuString.toString().trim(); // Remove trailing newline
    }
}
