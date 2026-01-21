package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.List;

public record PageDto<T>(
        @JsonProperty("currentPage")
        int pageNumber,

        @JsonProperty("pageSize")
        int pageSize,

        @JsonProperty("totalPages")
        int totalPages,

        @JsonProperty("totalElements")
        long totalElements,

        @JsonProperty("hasNext")
        boolean hasNext,

        @JsonProperty("hasPrevious")
        boolean hasPrevious,

        @JsonProperty("isFirst")
        boolean isFirst,

        @JsonProperty("isLast")
        boolean isLast,

        List<T> content

) {
    public static <T> PageDto<T> from(Page<T> page) {
        return new PageDto<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.hasNext(),
                page.hasPrevious(),
                page.isFirst(),
                page.isLast(),
                page.getContent()
        );
    }
}
