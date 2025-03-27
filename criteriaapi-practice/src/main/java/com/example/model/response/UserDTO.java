package com.example.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {

    private Integer id;
    private String name;
    private String email;
    private BigDecimal balance;

    private List<PaymentDTO> payments = new ArrayList<>();

    public UserDTO(Integer id, String name, String email, BigDecimal balance, PaymentDTO payment) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
        if(payment != null) {
            this.payments.add(payment);
        }
    }
}
