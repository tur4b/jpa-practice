package com.example.querydslpractice.model;

import lombok.*;

@Getter
@Setter
@ToString
public class BookDTO {

    private Integer id;
    private String title;
    private String author;
    private String isbn;

}
