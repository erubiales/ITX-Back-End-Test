package com.test.similarproducts.model;

public record ProductDetail(
        String id,
        String name,
        Double price,
        Boolean availability
) {}