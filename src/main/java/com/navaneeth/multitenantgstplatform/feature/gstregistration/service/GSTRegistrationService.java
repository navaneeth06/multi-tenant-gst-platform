package com.navaneeth.multitenantgstplatform.feature.gstregistration.service;

import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.CreateGSTRegistrationRequest;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.GSTRegistrationResponse;

import java.util.List;
import java.util.UUID;

public interface GSTRegistrationService {

    GSTRegistrationResponse createRegistration(CreateGSTRegistrationRequest request);

    List<GSTRegistrationResponse> getRegistrations();

    GSTRegistrationResponse getRegistration(UUID id);

    void deactivateRegistration(UUID id);
}