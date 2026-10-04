package com.navaneeth.multitenantgstplatform.feature.product.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.product.dto.CreateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.dto.ProductResponse;
import com.navaneeth.multitenantgstplatform.feature.product.dto.UpdateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ApiResponseFactory apiResponseFactory;

    public ProductController(
            ProductService productService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.productService = productService;
        this.apiResponseFactory = apiResponseFactory;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {

        ProductResponse response =
                productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        apiResponseFactory.success(
                                response,
                                "Product created successfully"
                        )
                );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    ) {

        List<ProductResponse> response =
                productService.getProducts(search, activeOnly);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Products retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @PathVariable UUID id
    ) {

        ProductResponse response =
                productService.getProduct(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Product retrieved successfully"
                )
        );
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request
    ) {

        ProductResponse response =
                productService.updateProduct(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Product updated successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable UUID id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "Product deleted successfully"
                )
        );
    }
}