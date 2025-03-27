package com.example.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultFilter { // <T extends SortingFilter<?>> {

    private int page;
    private int size;
//    private List<SortRequest<T>> sorts;

}
