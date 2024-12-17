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
    public String getMenu(HttpSession session, @RequestBody Map<String, String> requestBody) {
        String newRequest = requestBody.get("newRequest");
        String input = requestBody.get("input");

        // Ensure the key is always initialized, even for new requests
        String key = (session.getAttribute("key") != null) ? session.getAttribute("key").toString() : "menu:0";

        if ("1".equals(newRequest)) {
            // Reset session and initialize key to "menu:0" for a new request
            resetSession(session);

            key="menu:0";
        }else if("0".equals(input)){
            key=menuService.getPreviousMenu(key);
            session.setAttribute("key", key);
        }
        else {
            // Append the input to the key
            key += ":" + input;
            session.setAttribute("key", key);
        }
        // Call the service method to get the menu
        if(key.equals("menu:0")){
            return menuService.getMenu(key);
        }
        return menuService.getMenu(key)+"\n0.Back";
    }

    public void resetSession(HttpSession session) {
        session.setAttribute("key", "menu:0");
    }






}

