package com.example.USSDA.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    @Id
    private int id;
    private Integer parentId;
    private int actionId;
    private boolean menuPayment;
    private String menuName;
    private String description;
}