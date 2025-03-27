package com.example.querydslpractice.repository.skeleton;

import com.example.querydslpractice.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public abstract class AbstractBaseRepository<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    protected final JPAQueryFactory jpaQueryFactory;

    public AbstractBaseRepository(Class<T> domainClass, EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
        super(domainClass, entityManager);
        this.jpaQueryFactory = jpaQueryFactory;
    }

}
