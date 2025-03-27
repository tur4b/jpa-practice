package com.example;

import com.example.entity.Payment;
import com.example.entity.User;
import com.example.repository.PaymentRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class CriteriaapiPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CriteriaapiPracticeApplication.class, args);
    }

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

//            this.userRepository.save(User.builder()
//                            .name("turab1")
//                            .email("turab1@gmail.com")
//                            .balance(BigDecimal.TEN)
//                    .build()
//            );
//
//            this.userRepository.save(User.builder()
//                    .name("turab2")
//                    .email("turab2@gmail.com")
//                    .balance(BigDecimal.valueOf(45.99))
//                    .build()
//            );
//
//            this.userRepository.save(User.builder()
//                    .name("turab3")
//                    .email("turab3@gmail.com")
//                    .balance(BigDecimal.valueOf(25.6))
//                    .build()
//            );

//            this.paymentRepository.save(Payment.builder()
//                            .user(this.userRepository.findById(2).orElse(null))
//                            .amount(new BigDecimal("1.99"))
//                            .paymentDate(LocalDateTime.now())
//                            .status(1)
//                    .build());

            this.userRepository.findUsersPaymentInfoWithPagination(PageRequest.of(0, 10))
                    .forEach(System.out::println);

        };
    }
}
