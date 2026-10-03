package com.navaneeth.multitenantgstplatform.feature.customer.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.CreateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.CustomerResponse;
import com.navaneeth.multitenantgstplatform.feature.customer.dto.UpdateCustomerRequest;
import com.navaneeth.multitenantgstplatform.feature.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ApiResponseFactory apiResponseFactory;

    public CustomerController(
            CustomerService customerService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.customerService = customerService;
        this.apiResponseFactory = apiResponseFactory;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request
    ) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        apiResponseFactory.success(
                                response,
                                "Customer created successfully"
                        )
                );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getCustomers(
            @RequestParam(required = false) String search
    ) {

        List<CustomerResponse> response =
                customerService.getCustomers(search);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Customers retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
            @PathVariable UUID id
    ) {

        CustomerResponse response =
                customerService.getCustomer(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Customer retrieved successfully"
                )
        );
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {

        CustomerResponse response =
                customerService.updateCustomer(id, request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "Customer updated successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(
            @PathVariable UUID id
    ) {

        customerService.deleteCustomer(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "Customer deleted successfully"
                )
        );
    }
}