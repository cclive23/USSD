package com.example.ussd.controller;

import com.example.ussd.model.SessionData;

import org.springframework.web.bind.annotation.*;
import com.example.ussd.service.UssdService;
import com.example.ussd.repository.SessionDataRepository;
import java.util.*;


@RestController
@RequestMapping("/ussd")
public class UssdController {

    private final SessionDataRepository sessionDataRepository;

    public UssdController(SessionDataRepository sessionDataRepository) {
        this.sessionDataRepository = sessionDataRepository;
    }

    @PostMapping
    public List<String> getMainMenu(@RequestBody Map<String, String> requestBody) {
        String sessionId = requestBody.get("sessionId");
        String newRequest = requestBody.get("newRequest");
        String input = requestBody.get("input");

        SessionData sessionData = sessionDataRepository.findById(sessionId).orElse(new SessionData());
        String currentMenu = sessionData.getCurrentMenu();

        if ("1".equals(newRequest) && (currentMenu == null || "".equals(currentMenu))) {
            currentMenu = "main";
            sessionData.setSessionId(sessionId);
            sessionData.setCurrentMenu(currentMenu);
            sessionDataRepository.save(sessionData);
            return UssdService.menu.get("main");
        } else if ("1".equals(input) && "main".equals(currentMenu)) {
            currentMenu = "voice";
            sessionData.setCurrentMenu(currentMenu);
            sessionDataRepository.save(sessionData);
            return UssdService.menu.get("voice");
        } else if ("1".equals(input)  && "voice".equals(currentMenu)) {
            currentMenu = "voice1";
            sessionData.setCurrentMenu(currentMenu);
            sessionDataRepository.save(sessionData);
            return UssdService.menu.get("voice1");
        } else {
            return UssdService.menu.get("");
        }
    }

}
