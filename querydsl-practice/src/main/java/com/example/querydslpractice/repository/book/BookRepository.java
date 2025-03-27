package com.example.querydslpractice.repository.book;

import com.example.querydslpractice.entity.Book;
import com.example.querydslpractice.model.BookDTO;
import com.example.querydslpractice.model.enums.sorting.BookSortingFilter;
import com.example.querydslpractice.model.request.DefaultFilter;
import com.example.querydslpractice.repository.skeleton.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends BaseRepository<Book, Integer> {

    Optional<BookDTO> findInfoById(Integer id);
    List<BookDTO> findInfoBySearchText(String searchText);
    Page<BookDTO> findAllInfo(DefaultFilter<BookSortingFilter> filter);

}
