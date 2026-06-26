package com.navaneeth.multitenantgstplatform.tenant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TenantController {

    @GetMapping("/")
    public String home() {
        return "GST Platform is running!";
    }
}