package com.example.USSDA.model;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private int sessionId;

    private int currentMenu=0;


    private int previousMenu;



}
