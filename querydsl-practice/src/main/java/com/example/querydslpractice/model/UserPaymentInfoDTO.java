package com.example.querydslpractice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class UserPaymentInfoDTO {

    private Integer userId;
    private BigDecimal balance;
    private BigDecimal totalPaymentAmount;
    private BigDecimal avgOfPaymentAmount;
    private Long totalPaymentCount;

}
