package com.example.repository;

import com.example.entity.Payment;
import com.example.entity.Payment_;
import com.example.entity.User;
import com.example.entity.User_;
import com.example.model.response.PaymentDTO;
import com.example.model.response.UserDTO;
import com.example.model.response.UserPaymentInfoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserCriteriaApiBasedRepositoryImpl implements UserCriteriaApiBasedRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserDTO> findUsersWithPagination(Pageable pageable) {

        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDTO> contentQuery = cb.createQuery(UserDTO.class);
        Root<User> contentRoot = contentQuery.from(User.class);
        Join<User, Payment> paymentsJoin = contentRoot.join("payments", JoinType.LEFT);

        contentQuery.select(
                cb.construct(
                        UserDTO.class,
                        contentRoot.get("id"),
                        contentRoot.get("name"),
                        contentRoot.get("email"),
                        contentRoot.get("balance"),
                        cb.construct(
                            PaymentDTO.class,
                            paymentsJoin.get("id"),
                            paymentsJoin.get("amount"),
                            paymentsJoin.get("paymentDate"),
                            paymentsJoin.get("status")
                        )
                )
        );

        List<UserDTO> content = this.entityManager.createQuery(contentQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        Join<User, Payment> paymentsJoinForCount = contentRoot.join("payments", JoinType.LEFT);
        countQuery.select(cb.count(countRoot));

        Long totalCount = this.entityManager.createQuery(countQuery).getSingleResult();


        List<UserDTO> finalResults = content.stream()
                .collect(Collectors.toMap(
                        UserDTO::getId,
                        u -> u,
                        (u1, u2) -> {
                            u1.getPayments().addAll(u2.getPayments());
                            return u1;
                        }
                ))
                .values()
                .stream()
                .toList();


        return PageableExecutionUtils.getPage(finalResults, pageable, () -> totalCount);
    }

    public Page<UserPaymentInfoDTO> findUsersPaymentInfoWithPagination(Pageable pageable) {

        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<UserPaymentInfoDTO> contentQuery = cb.createQuery(UserPaymentInfoDTO.class);
        Root<User> contentRoot = contentQuery.from(User.class);
        Join<User, Payment> paymentsJoin = contentRoot.join(User_.payments, JoinType.LEFT);

        contentQuery.select(
                cb.construct(
                        UserPaymentInfoDTO.class,
                        contentRoot.get(User_.id),
                        cb.coalesce(contentRoot.get(User_.balance), BigDecimal.ZERO),
                        cb.coalesce(cb.sum(paymentsJoin.get(Payment_.amount)), BigDecimal.ZERO),
                        cb.coalesce(
                                cb.selectCase()
                                        .when(cb.isNull(cb.avg(paymentsJoin.get(Payment_.amount))), BigDecimal.ZERO)
                                        .otherwise(cb.avg(paymentsJoin.get(Payment_.amount)).as(BigDecimal.class)),
                                BigDecimal.ZERO
                        ),
                        cb.count(paymentsJoin.get(Payment_.id))
                )
        ).groupBy(contentRoot.get(User_.id));

        List<UserPaymentInfoDTO> content = this.entityManager.createQuery(contentQuery)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // find total count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(cb.count(countRoot)).distinct(true);

        Long totalCount = this.entityManager.createQuery(countQuery).getSingleResult();

        return PageableExecutionUtils.getPage(content, pageable, () -> totalCount);
    }

}
