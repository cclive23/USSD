package com.example.USSDA.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class Payment {
    @Id
    private int paymentId;
    private int menuId;
    private String paymentType;
}
