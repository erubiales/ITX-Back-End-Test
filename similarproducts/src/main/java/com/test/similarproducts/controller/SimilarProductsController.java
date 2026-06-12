package com.test.similarproducts.controller;

import com.test.similarproducts.model.ProductDetail;
import com.test.similarproducts.service.SimilarProductsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
public class SimilarProductsController {

    private final SimilarProductsService similarProductsService;

    public SimilarProductsController(SimilarProductsService similarProductsService) {
        this.similarProductsService = similarProductsService;
    }

    @GetMapping("/product/{productId}/similar")
    public ResponseEntity<List<ProductDetail>> getSimilarProducts(@PathVariable String productId) {
        try {
            List<ProductDetail> products = similarProductsService.getSimilarProducts(productId);
            return ResponseEntity.ok(products);
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}