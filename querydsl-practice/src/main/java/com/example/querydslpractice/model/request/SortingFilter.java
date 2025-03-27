package com.example.querydslpractice.model.request;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.ComparableExpressionBase;

public interface SortingFilter<T extends EntityPath<?>> {

    ComparableExpressionBase<?> getExpression(T qEntity);

}
