package com.navaneeth.multitenantgstplatform.feature.product.service;

import com.navaneeth.multitenantgstplatform.feature.product.dto.CreateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.dto.ProductResponse;
import com.navaneeth.multitenantgstplatform.feature.product.dto.UpdateProductRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    List<ProductResponse> getProducts(String search, boolean activeOnly);

    ProductResponse getProduct(UUID id);

    ProductResponse updateProduct(UUID id, UpdateProductRequest request);

    void deleteProduct(UUID id);
}