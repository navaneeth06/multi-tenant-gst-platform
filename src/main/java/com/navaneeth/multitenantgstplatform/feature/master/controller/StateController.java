package com.navaneeth.multitenantgstplatform.feature.master.controller;

import com.navaneeth.multitenantgstplatform.common.factory.ApiResponseFactory;
import com.navaneeth.multitenantgstplatform.common.response.ApiResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.StateResponse;
import com.navaneeth.multitenantgstplatform.feature.master.service.StateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/states")
public class StateController {

    private final StateService stateService;
    private final ApiResponseFactory apiResponseFactory;

    public StateController(
            StateService stateService,
            ApiResponseFactory apiResponseFactory
    ) {
        this.stateService = stateService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StateResponse>>> getStates() {

        List<StateResponse> response =
                stateService.getStates();

        return ResponseEntity.ok(
                apiResponseFactory.success(
                        response,
                        "States retrieved successfully"
                )
        );
    }
}