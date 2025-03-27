package com.example.querydslpractice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private Integer id;
    private String name;
    private String email;
    private BigDecimal balance;

    private List<PaymentDTO> payments = new ArrayList<>();

}
