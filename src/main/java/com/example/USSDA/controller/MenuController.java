package com.example.USSDA.controller;


import com.example.USSDA.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/ussd")
public class MenuController {

    @Autowired
    private MenuService menuService;


    @PostMapping
    public String getMenu(HttpSession session, @RequestBody Map<String, Integer> requestBody) {
        int newRequest = requestBody.get("newRequest");
        int input = requestBody.get("input");

        int paymentChoice=0;

        int currentMenu = session.getAttribute("currentMenu") == null ? 0 : (int) session.getAttribute("currentMenu");
        int previousMenu = session.getAttribute("previousMenu") == null ? 0 : (int) session.getAttribute("previousMenu");

        //For every new request we display main menu and intial currentMenu to main
        if (newRequest == 1) {
            resetSessionToMainMenu(session);// Initialize menu
            return menuService.getMainMenu();
        }

        //In case user press 0 to go back
        if( input==0) {
            switch (previousMenu) {
                case 0:
                    resetSessionToMainMenu(session);
                    return menuService.getMainMenu();
                default:
                    currentMenu = previousMenu;
                    previousMenu = menuService.getParentIdByMenuId(currentMenu);
                    session.setAttribute("currentMenu", currentMenu);
                    session.setAttribute("previousMenu", previousMenu);
                    return menuService.getSubMenu(currentMenu);

            }
        }
        // Validate the user input
        if (!menuService.isValidInput(currentMenu, input)) {
            // Invalid input
            resetSessionToMainMenu(session);
            return "Invalid input. Your session has been reset to the main menu.\n" + menuService.getMainMenu();
        }




        //In case it's not a new request it should get the submenus of the menu picked as input
        Integer get= menuService.getMenuIdByParentAndAction(currentMenu, input);
        previousMenu = currentMenu;
        currentMenu = get;
        session.setAttribute("currentMenu", currentMenu);
        session.setAttribute("previousMenu", previousMenu);



        //Check Payment Status
        if(menuService.getMenuPaymentStatus(currentMenu)){

            return menuService.getPaymentDescription(currentMenu)+"\n" +
                    "1. Pay with Airtime\n"
                    + "2. Pay with MoMo\n"+
                    "0.Back";
        }



        return menuService.getSubMenu(currentMenu)+"\n0. Back";




    }
    //To effectively initialize the session menu attributes.
    private void resetSessionToMainMenu(HttpSession session) {
        session.setAttribute("currentMenu",0);
        session.setAttribute("previousMenu",0);
    }





}

