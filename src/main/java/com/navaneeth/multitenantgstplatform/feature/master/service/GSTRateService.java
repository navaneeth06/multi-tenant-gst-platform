package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.GSTRateResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateGSTRateRequest;

import java.util.List;
import java.util.UUID;

public interface GSTRateService {

    GSTRateResponse createRate(CreateGSTRateRequest request);

    List<GSTRateResponse> getRates();

    List<GSTRateResponse> getActiveRates();

    GSTRateResponse getRate(UUID id);

    GSTRateResponse updateRate(UUID id, UpdateGSTRateRequest request);

    void deactivateRate(UUID id);
}