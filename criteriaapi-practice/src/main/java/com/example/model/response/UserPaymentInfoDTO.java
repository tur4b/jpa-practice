package com.example.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPaymentInfoDTO {

    private Integer userId;
    private BigDecimal balance;
    private BigDecimal totalPaymentAmount;
    private BigDecimal avgOfPaymentAmount;
    private Long totalPaymentCount;

}
