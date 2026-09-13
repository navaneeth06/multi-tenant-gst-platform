package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.StateResponse;
import com.navaneeth.multitenantgstplatform.feature.master.repository.StateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    public StateServiceImpl(
            StateRepository stateRepository
    ) {
        this.stateRepository = stateRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponse> getStates() {

        return stateRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(state -> new StateResponse(
                        state.getId(),
                        state.getName(),
                        state.getCode(),
                        state.getGstCode()
                ))
                .toList();
    }
}