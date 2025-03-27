package com.example.querydslpractice.utils;

import com.example.querydslpractice.model.enums.ExpressionType;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {

    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicates builder() {
        return new QPredicates();
    }

    public <T> QPredicates and(T object, Function<T, Predicate> predicateFunction) {
        if (object != null) {
            predicates.add(predicateFunction.apply(object));
        }
        return this;
    }

    public Predicate build(ExpressionType type) {
        return switch (type) {
            case ALL -> ExpressionUtils.allOf(this.predicates);
            case OR -> ExpressionUtils.anyOf(this.predicates);
        };
    }
}
