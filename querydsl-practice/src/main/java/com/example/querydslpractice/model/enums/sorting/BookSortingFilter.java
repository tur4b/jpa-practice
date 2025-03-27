package com.example.querydslpractice.model.enums.sorting;

import com.example.querydslpractice.entity.QBook;
import com.example.querydslpractice.model.request.SortingFilter;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public enum BookSortingFilter implements SortingFilter<QBook> {
    id,
    title,
    author,
    isbn;

    @Override
    public ComparableExpressionBase<?> getExpression(QBook qEntity) {
        return switch (this) {
            case id -> qEntity.id;
            case title -> qEntity.title;
            case author -> qEntity.author;
            case isbn -> qEntity.isbn;
        };
    }
}
