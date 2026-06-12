package com.test.similarproducts.client;

import com.test.similarproducts.model.ProductDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductClient(RestTemplate restTemplate,
                         @Value("${product.service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<String> getSimilarIds(String productId) {
        String[] ids = restTemplate.getForObject(
                baseUrl + "/product/{id}/similarids", String[].class, productId);
        return ids != null ? Arrays.asList(ids) : List.of();
    }

    public Optional<ProductDetail> getProductDetail(String productId) {
        try {
            ProductDetail detail = restTemplate.getForObject(
                    baseUrl + "/product/{id}", ProductDetail.class, productId);
            return Optional.ofNullable(detail);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            //Devuelve empty si el producto no se encuentra o 
            //si ocurre cualquier error al obtener los detalles del producto
            //Garantiza la resiliencia, si falla uno no afecta al resto
            return Optional.empty();
        }
    }
}