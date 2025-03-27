package com.example.querydslpractice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class PaymentDTO {

    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private Integer status;
}
