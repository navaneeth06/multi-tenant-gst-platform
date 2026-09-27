package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.HsnCodeResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateHsnCodeRequest;

import java.util.List;
import java.util.UUID;

public interface HsnCodeService {

    HsnCodeResponse createHsnCode(CreateHsnCodeRequest request);

    List<HsnCodeResponse> getHsnCodes();

    HsnCodeResponse getHsnCode(UUID id);

    HsnCodeResponse updateHsnCode(UUID id, UpdateHsnCodeRequest request);

    void deleteHsnCode(UUID id);
}