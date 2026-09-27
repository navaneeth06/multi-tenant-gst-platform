package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UnitResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.entity.Unit;
import com.navaneeth.multitenantgstplatform.feature.master.mapper.UnitMapper;
import com.navaneeth.multitenantgstplatform.feature.master.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    @Transactional
    public UnitResponse createUnit(CreateUnitRequest request) {

        if (unitRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Unit already exists");
        }

        Unit unit = unitMapper.toEntity(request);

        Unit saved = unitRepository.save(unit);

        return unitMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitResponse> getUnits() {

        return unitRepository.findAll()
                .stream()
                .map(unitMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UnitResponse getUnit(UUID id) {

        Unit unit = unitRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Unit not found")
                );

        return unitMapper.toResponse(unit);
    }

    @Override
    @Transactional
    public UnitResponse updateUnit(UUID id, UpdateUnitRequest request) {

        Unit unit = unitRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Unit not found")
                );

        if (request.name() != null &&
                !request.name().equals(unit.getName()) &&
                unitRepository.existsByNameAndIdNot(request.name(), id)) {

            throw new IllegalArgumentException("Unit already exists");
        }

        unitMapper.updateUnit(request, unit);

        Unit updated = unitRepository.save(unit);

        return unitMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteUnit(UUID id) {

        Unit unit = unitRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Unit not found")
                );

        try {
            unitRepository.delete(unit);
            unitRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Cannot delete this unit — it is still referenced by one or more products"
            );
        }
    }
}