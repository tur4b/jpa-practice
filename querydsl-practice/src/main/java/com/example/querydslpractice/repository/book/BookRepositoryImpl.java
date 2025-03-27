package com.example.querydslpractice.repository.book;

import com.example.querydslpractice.entity.Book;
import com.example.querydslpractice.model.BookDTO;
import com.example.querydslpractice.entity.QBook;
import com.example.querydslpractice.model.enums.sorting.BookSortingFilter;
import com.example.querydslpractice.model.request.DefaultFilter;
import com.example.querydslpractice.repository.skeleton.AbstractBaseRepository;
import com.example.querydslpractice.model.enums.ExpressionType;
import com.example.querydslpractice.utils.QPredicates;
import com.example.querydslpractice.utils.QSortingUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl extends AbstractBaseRepository<Book, Integer> implements BookRepository {

    public BookRepositoryImpl(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
        super(Book.class, entityManager, jpaQueryFactory);
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return Optional.ofNullable(
                this.jpaQueryFactory.select(QBook.book)
                        .from(QBook.book)
                        .where(QBook.book.id.eq(id))
                        .fetchFirst()
        );
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        JPAQuery<Book> query = this.jpaQueryFactory.selectFrom(QBook.book)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QBook.book.id.desc());
        List<Book> booksList = query.fetch();

        Long countOfBooks = this.jpaQueryFactory.select(QBook.book.count())
                .from(QBook.book)
                .fetchOne();
        return new PageImpl<>(booksList, pageable, countOfBooks != null ? countOfBooks : 0);
    }

    @Override
    public Optional<BookDTO> findInfoById(Integer id) {
        return Optional.ofNullable(
                this.jpaQueryFactory.select(Projections.bean(BookDTO.class, QBook.book.id, QBook.book.title, QBook.book.status))
                        .from(QBook.book)
                        .where(QBook.book.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<BookDTO> findInfoBySearchText(String searchText) {

        Predicate orOf = QPredicates.builder()
                .and("%" + searchText + "%", QBook.book.isbn::likeIgnoreCase)
                .and("%" + searchText + "%", QBook.book.title::likeIgnoreCase)
                .and("%" + searchText + "%", QBook.book.author::likeIgnoreCase)
                .build(ExpressionType.OR);

        return this.jpaQueryFactory
                .select(Projections.bean(BookDTO.class,
                        QBook.book.id, QBook.book.title, QBook.book.isbn, QBook.book.author, QBook.book.status)
                )
                .from(QBook.book)
                .where(orOf)
                .fetch();
    }

    @Override
    public Page<BookDTO> findAllInfo(DefaultFilter<BookSortingFilter> filter) {
        QBook book = QBook.book;
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        List<BookDTO> content = this.jpaQueryFactory.select(Projections.bean(BookDTO.class, book.id, book.title, book.author, book.isbn, book.status))
                .from(book)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QSortingUtil.getOrderSpecifierList(filter, book).toArray(new OrderSpecifier[0]))
                .fetch();
        Long countOfContent = this.jpaQueryFactory.select(book.count()).from(book).fetchOne();
        return PageableExecutionUtils.getPage(content, pageable, () -> countOfContent != null ? countOfContent : 0);
    }
}
