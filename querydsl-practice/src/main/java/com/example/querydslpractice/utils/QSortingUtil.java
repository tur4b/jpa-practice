package com.example.querydslpractice.utils;

import com.example.querydslpractice.model.request.DefaultFilter;
import com.example.querydslpractice.model.request.SortRequest;
import com.example.querydslpractice.model.request.SortingFilter;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QSortingUtil {

    public static <T extends EntityPathBase<?>, F extends SortingFilter<T>> OrderSpecifier<?> getOrderSpecifier(SortRequest<F> sortRequest, T qEntity) {
        return new OrderSpecifier<>(sortRequest.getOrder(), sortRequest.getSort().getExpression(qEntity));
    }

    public static <T extends EntityPathBase<?>, F extends SortingFilter<T>> List<OrderSpecifier<?>> getOrderSpecifierList(DefaultFilter<F> filter, T qEntity) {
        return filter.getSorts()
                .stream()
                .map(sortRequest -> getOrderSpecifier(sortRequest, qEntity))
                .collect(Collectors.toList());
    }
}
