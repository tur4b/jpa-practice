package com.example.querydslpractice;

import com.example.querydslpractice.entity.Book;
import com.example.querydslpractice.model.BookDTO;
import com.example.querydslpractice.model.enums.sorting.BookSortingFilter;
import com.example.querydslpractice.repository.book.BookRepository;
import com.example.querydslpractice.repository.payment.PaymentRepository;
import com.example.querydslpractice.repository.user.UserRepository;
import com.example.querydslpractice.model.request.DefaultFilter;
import com.example.querydslpractice.model.request.SortRequest;
import com.example.querydslpractice.model.enums.sorting.UserSortingFilter;
import com.querydsl.core.types.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@SpringBootApplication
public class QuerydslPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuerydslPracticeApplication.class, args);
    }

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            bookRepository.save(Book.builder()
                            .title(UUID.randomUUID().toString())
                            .isbn(UUID.randomUUID().toString())
                            .status(1)
                            .author("turab")
                    .build());

//            Pageable pageable = PageRequest.of(0, 15);
//            bookRepository.findAll(pageable)
//                    .forEach(System.out::println);
//
//            Optional<BookDTO> bookInfo = bookRepository.findInfoById(7);
//            bookInfo.ifPresent(System.out::println);

            List<BookDTO> bookDTOList = bookRepository.findInfoBySearchText("48a83622");
            bookDTOList.forEach(System.out::println);


            System.out.println("--------------------------------------");


//            User user1 = userRepository.save(
//                    User.builder()
//                            .name("turab1")
//                            .email("turab1@gmail.com")
//                            .balance(BigDecimal.valueOf(100.59))
//                            .build()
//            );
//
//            User user2 = userRepository.save(
//                    User.builder()
//                            .name("turab2")
//                            .email("turab2@gmail.com")
//                            .balance(BigDecimal.valueOf(599.99))
//                            .build()
//            );
//
//            this.paymentRepository.save(
//                    Payment.builder()
//                            .amount(BigDecimal.valueOf(24.29))
//                            .paymentDate(LocalDateTime.now())
//                            .user(user1)
//                            .status(1)
//                            .build()
//            );
//
//            this.paymentRepository.save(
//                    Payment.builder()
//                            .amount(BigDecimal.valueOf(5))
//                            .paymentDate(LocalDateTime.now())
//                            .user(user1)
//                            .status(2)
//                            .build()
//            );
//
//            this.paymentRepository.save(
//                    Payment.builder()
//                            .amount(BigDecimal.valueOf(23))
//                            .paymentDate(LocalDateTime.now())
//                            .user(user1)
//                            .status(1)
//                            .build()
//            );
//
//            this.paymentRepository.save(
//                    Payment.builder()
//                            .amount(BigDecimal.valueOf(99))
//                            .paymentDate(LocalDateTime.now())
//                            .user(user2)
//                            .status(1)
//                            .build()
//            );


//            this.userRepository.findAllUsersWithPaymentsDetails().forEach(System.out::println);

            System.out.println("--------------------------------");
//            this.userRepository.findUserInfoWithPaymentDetails(1).ifPresent(System.out::println);

//            this.userRepository.findAllUserPaymentInfos().forEach(System.out::println);

            this.userRepository.findAllInfo(
                DefaultFilter.<UserSortingFilter>builder()
                    .page(0)
                    .size(10)
                    .sorts(
                        List.of(
                            SortRequest.<UserSortingFilter>builder().order(Order.DESC).sort(UserSortingFilter.name).build(),
                            SortRequest.<UserSortingFilter>builder().order(Order.DESC).sort(UserSortingFilter.email).build(),
                            SortRequest.<UserSortingFilter>builder().order(Order.ASC).sort(UserSortingFilter.balance).build()
                        )
                    )
                    .build()
                ).forEach(System.out::println);

            System.out.println("--------------------------------");

            this.bookRepository.findAllInfo(
                    DefaultFilter.<BookSortingFilter>builder()
                            .page(0)
                            .size(10)
                            .sorts(
                                    List.of(
                                            SortRequest.<BookSortingFilter>builder().order(Order.DESC).sort(BookSortingFilter.title).build(),
                                            SortRequest.<BookSortingFilter>builder().order(Order.DESC).sort(BookSortingFilter.author).build(),
                                            SortRequest.<BookSortingFilter>builder().order(Order.DESC).sort(BookSortingFilter.id).build()
                                    )
                            )
                            .build()
            ).forEach(System.out::println);

        };

    }
}
