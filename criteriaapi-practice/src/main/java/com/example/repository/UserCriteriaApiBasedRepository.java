package com.example.repository;

import com.example.model.response.UserDTO;
import com.example.model.response.UserPaymentInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCriteriaApiBasedRepository {

    Page<UserDTO> findUsersWithPagination(Pageable pageable);
    Page<UserPaymentInfoDTO> findUsersPaymentInfoWithPagination(Pageable pageable);
}
