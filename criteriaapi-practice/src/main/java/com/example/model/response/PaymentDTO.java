package com.example.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDTO {

    private Integer id;
//    private Integer userId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private Integer status;
}
