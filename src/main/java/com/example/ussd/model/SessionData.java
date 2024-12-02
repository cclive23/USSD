package com.example.ussd.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class SessionData {
    @Id
    private String sessionId;
    private String currentMenu;

}