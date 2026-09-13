package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.StateResponse;

import java.util.List;

public interface StateService {

    List<StateResponse> getStates();
}