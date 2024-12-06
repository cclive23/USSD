package com.example.USSDA.controller;

import com.example.USSDA.model.*;
import com.example.USSDA.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.USSDA.repository.*;

import java.util.*;


@RestController
@RequestMapping("/ussd")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private SessionRepository sessionRepository;


    @PostMapping
    public String getMenu(@RequestBody Map<String, Integer> requestBody) {
        int sessionId = requestBody.get("sessionId");
        int newRequest = requestBody.get("newRequest");
        int input = requestBody.get("input");

        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) {
            // Create a new session
            session = new Session();
            session.setSessionId(sessionId);
            session.setCurrentMenu(0); // Initialize to the main menu

            sessionRepository.save(session);
        }

        Integer currentMenu = session.getCurrentMenu();
        Integer previousMenu = session.getPreviousMenu();
        int paymentChoice=0;

        //For every new request we display main menu and intial currentMenu to main
        if (newRequest == 1) {
            session.setCurrentMenu(0); // Initialize to the main menu
            session.setPreviousMenu(0);
            sessionRepository.save(session);
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
                    session.setCurrentMenu(currentMenu);
                    session.setPreviousMenu(previousMenu);
                    sessionRepository.save(session);
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
        session.setCurrentMenu(currentMenu);
        session.setPreviousMenu(previousMenu);
        sessionRepository.save(session);







        //Check Payment Status
        if(menuService.getMenuPaymentStatus(currentMenu)){

            return menuService.getPaymentDescription(currentMenu)+"\n" +
                    "1. Pay with Airtime\n"
                    + "2. Pay with MoMo\n"+
                    "0.Back";
        }



        return menuService.getSubMenu(currentMenu)+"\n0. Back";










    }
    private void resetSessionToMainMenu(Session session) {
        session.setCurrentMenu(0);
        session.setPreviousMenu(0);
        sessionRepository.save(session);
    }





}

