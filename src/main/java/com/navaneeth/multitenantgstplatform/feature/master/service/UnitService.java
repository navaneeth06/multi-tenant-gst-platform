package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UnitResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateUnitRequest;

import java.util.List;
import java.util.UUID;

public interface UnitService {

    UnitResponse createUnit(CreateUnitRequest request);

    List<UnitResponse> getUnits();

    UnitResponse getUnit(UUID id);

    UnitResponse updateUnit(UUID id, UpdateUnitRequest request);

    void deleteUnit(UUID id);
}