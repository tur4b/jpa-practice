package com.example.querydslpractice.repository.user;

import com.example.querydslpractice.entity.User;
import com.example.querydslpractice.model.UserDTO;
import com.example.querydslpractice.model.UserPaymentInfoDTO;
import com.example.querydslpractice.repository.skeleton.BaseRepository;
import com.example.querydslpractice.model.request.DefaultFilter;
import com.example.querydslpractice.model.enums.sorting.UserSortingFilter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Integer> {

    Optional<UserDTO> findUserInfoWithPaymentDetails(Integer userId);
    List<UserDTO> findAllUsersWithPaymentsDetails();
    List<UserPaymentInfoDTO> findAllUserPaymentInfos();
    Page<UserDTO> findAllInfo(DefaultFilter<UserSortingFilter> filter);
}
