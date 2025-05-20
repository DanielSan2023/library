package com.example.library.controller.utils;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SortUtils {

    public static List<Sort.Order> parseSortParams(String[] sortParams) {
        if (sortParams == null || sortParams.length == 0) {
            return List.of();
        }

        return Arrays.stream(sortParams)
                .map(sortParam -> {
                    String[] parts = sortParam.split(",");
                    if (parts.length == 2) {
                        return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                    } else {
                        return new Sort.Order(Sort.Direction.ASC, parts[0]);
                    }
                })
                .collect(Collectors.toList());
    }
}
