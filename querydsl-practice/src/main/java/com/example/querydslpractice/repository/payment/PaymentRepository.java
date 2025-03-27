package com.example.querydslpractice.repository.payment;

import com.example.querydslpractice.entity.Payment;
import com.example.querydslpractice.repository.skeleton.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends BaseRepository<Payment, Integer> {
}
