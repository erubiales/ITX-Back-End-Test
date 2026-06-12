package com.test.similarproducts.service;

import com.test.similarproducts.client.ProductClient;
import com.test.similarproducts.model.ProductDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class SimilarProductsService {

    private final ProductClient productClient;

    public SimilarProductsService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public List<ProductDetail> getSimilarProducts(String productId) {
        List<String> similarIds = productClient.getSimilarIds(productId);
        //Busca todos los detalles de productos en paralelo para mejorar el rendimiento
        //Esto deberia de reducir al minimo el tiempo de respuesta
        List<CompletableFuture<Optional<ProductDetail>>> futures = similarIds.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> productClient.getProductDetail(id)))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}