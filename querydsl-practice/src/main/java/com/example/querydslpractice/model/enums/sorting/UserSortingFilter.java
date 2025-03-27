package com.example.querydslpractice.model.enums.sorting;

import com.example.querydslpractice.entity.QUser;
import com.example.querydslpractice.model.request.SortingFilter;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public enum UserSortingFilter implements SortingFilter<QUser> {
    id,
    name,
    email,
    balance;

    @Override
    public ComparableExpressionBase<?> getExpression(QUser qEntity) {
        return switch (this) {
            case id -> qEntity.id;
            case name -> qEntity.name;
            case email -> qEntity.email;
            case balance -> qEntity.balance;
        };
    }

}
