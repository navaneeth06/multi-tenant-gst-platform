package com.navaneeth.multitenantgstplatform.feature.gstregistration.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.CreateGSTRegistrationRequest;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.GSTRegistrationResponse;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.service.GSTRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/gst-registrations")
public class GSTRegistrationController {

    private final GSTRegistrationService gstRegistrationService;
    private final ApiResponseFactory apiResponseFactory;

    public GSTRegistrationController(
            GSTRegistrationService gstRegistrationService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.gstRegistrationService = gstRegistrationService;
        this.apiResponseFactory = apiResponseFactory;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<GSTRegistrationResponse>> createRegistration(
            @Valid @RequestBody CreateGSTRegistrationRequest request
    ) {

        GSTRegistrationResponse response =
                gstRegistrationService.createRegistration(request);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST registration created successfully"
                )
        );
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<GSTRegistrationResponse>>> getRegistrations() {

        List<GSTRegistrationResponse> response =
                gstRegistrationService.getRegistrations();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST registrations retrieved successfully"
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GSTRegistrationResponse>> getRegistration(
            @PathVariable UUID id
    ) {

        GSTRegistrationResponse response =
                gstRegistrationService.getRegistration(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "GST registration retrieved successfully"
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivateRegistration(
            @PathVariable UUID id
    ) {

        gstRegistrationService.deactivateRegistration(id);

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        null,
                        "GST registration deactivated successfully"
                )
        );
    }
}