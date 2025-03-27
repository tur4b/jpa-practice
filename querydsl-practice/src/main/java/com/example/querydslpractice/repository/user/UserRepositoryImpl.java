package com.example.querydslpractice.repository.user;

import com.example.querydslpractice.entity.QPayment;
import com.example.querydslpractice.entity.QUser;
import com.example.querydslpractice.entity.User;
import com.example.querydslpractice.model.PaymentDTO;
import com.example.querydslpractice.model.UserDTO;
import com.example.querydslpractice.model.UserPaymentInfoDTO;
import com.example.querydslpractice.repository.skeleton.AbstractBaseRepository;
import com.example.querydslpractice.model.request.DefaultFilter;
import com.example.querydslpractice.utils.QSortingUtil;
import com.example.querydslpractice.model.enums.sorting.UserSortingFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl extends AbstractBaseRepository<User, Integer> implements UserRepository{

    public UserRepositoryImpl(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
        super(User.class, entityManager, jpaQueryFactory);
    }

    @Override
    public Optional<UserDTO> findUserInfoWithPaymentDetails(Integer userId) {
        QUser user = QUser.user;
        QPayment payment = QPayment.payment;

        QBean<UserDTO> userDTOQBean = Projections.bean(UserDTO.class, user.id, user.name, user.email, user.balance);
        QBean<PaymentDTO> paymentDTOQBean = Projections.bean(PaymentDTO.class, payment.id, payment.user.id.as("userId"), payment.paymentDate, payment.amount, payment.status);

        UserDTO userDTO = this.jpaQueryFactory.select(userDTOQBean)
                .from(user)
                .where(user.id.eq(userId))
                .fetchFirst();

        return Optional.ofNullable(userDTO)
                .map(u -> {
                    List<PaymentDTO> payments = this.jpaQueryFactory.select(paymentDTOQBean)
                            .from(payment)
                            .where(payment.user.id.eq(u.getId()))
                            .fetch();
                    u.setPayments(payments);
                    return u;
                });
    }

    @Override
    public List<UserDTO> findAllUsersWithPaymentsDetails() {
        QUser user = QUser.user;
        QPayment payment = QPayment.payment;

        QBean<UserDTO> userDTOQBean = Projections.bean(UserDTO.class, user.id, user.name, user.email, user.balance);
        QBean<PaymentDTO> paymentDTOQBean = Projections.bean(PaymentDTO.class, payment.id, payment.user.id.as("userId"), payment.paymentDate, payment.amount, payment.status);

        List<UserDTO> users =  this.jpaQueryFactory
                .select(userDTOQBean)
                .from(user)
                .fetch();

        List<PaymentDTO> payments =  this.jpaQueryFactory
                .select(paymentDTOQBean)
                .from(payment)
                .join(user).on(payment.user.id.eq(user.id))
                .fetch();

        Map<Integer, List<PaymentDTO>> paymentMap = payments.stream()
                .collect(Collectors.groupingBy(PaymentDTO::getUserId));

        return users.stream()
                .peek(userDTO -> userDTO.setPayments(paymentMap.get(userDTO.getId())))
                .toList();
    }

    @Override
    public List<UserPaymentInfoDTO> findAllUserPaymentInfos() {
        QUser user = QUser.user;
        QPayment payment = QPayment.payment;

        QBean<UserPaymentInfoDTO> userDTOBean = Projections.bean(UserPaymentInfoDTO.class, user.id.as("userId"), user.balance,
                payment.amount.sum().as("totalPaymentAmount"),
                payment.amount.avg().castToNum(BigDecimal.class).as("avgOfPaymentAmount"),
                payment.amount.count().as("totalPaymentCount")
        );

        return this.jpaQueryFactory.select(userDTOBean)
                .from(user)
                .leftJoin(payment).on(payment.user.id.eq(user.id))
                .groupBy(user.id)
                .fetch();
    }

    @Override
    public Page<UserDTO> findAllInfo(DefaultFilter<UserSortingFilter> filter) {
        QUser user = QUser.user;
        QPayment payment = QPayment.payment;
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        QBean<UserDTO> userDTOQBean = Projections.bean(UserDTO.class, user.id, user.name, user.email, user.balance);
        QBean<PaymentDTO> paymentDTOQBean = Projections.bean(PaymentDTO.class, payment.id, payment.user.id.as("userId"), payment.paymentDate, payment.amount, payment.status);

        List<OrderSpecifier<?>> orderSpecifierList = QSortingUtil.getOrderSpecifierList(filter, user);

        List<UserDTO> content = this.jpaQueryFactory.select(userDTOQBean)
                .from(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifierList.toArray(new OrderSpecifier[0]))
                .fetch();

        List<Integer> listOfContentIds = content.stream().map(UserDTO::getId).toList();

        List<PaymentDTO> payments = this.jpaQueryFactory.select(paymentDTOQBean)
                .from(payment)
                .where(payment.user.id.in(listOfContentIds))
                .fetch();

        // total count of users
        Long totalCount = this.jpaQueryFactory.select(user.count())
                .from(user)
                .fetchFirst();

        Map<Integer, List<PaymentDTO>> paymentMap = payments.stream()
                .collect(Collectors.groupingBy(PaymentDTO::getUserId));

        List<UserDTO> contentWithPaymentDetails = content.stream()
                .peek(userDTO -> userDTO.setPayments(paymentMap.get(userDTO.getId())))
                .toList();

        return PageableExecutionUtils.getPage(contentWithPaymentDetails, pageable, () -> totalCount != null ? totalCount : 0);
    }
}
