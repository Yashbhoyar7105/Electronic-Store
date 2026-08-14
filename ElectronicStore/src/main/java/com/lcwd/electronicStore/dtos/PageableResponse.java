package com.lcwd.electronicStore.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageableResponse<T> {

    public List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalElements;
    private int totalNoPages;
    private boolean lastPage;
}
