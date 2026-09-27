package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.HsnCodeResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.entity.GSTRate;
import com.navaneeth.multitenantgstplatform.feature.master.entity.HsnCode;
import com.navaneeth.multitenantgstplatform.feature.master.mapper.HsnCodeMapper;
import com.navaneeth.multitenantgstplatform.feature.master.repository.GSTRateRepository;
import com.navaneeth.multitenantgstplatform.feature.master.repository.HsnCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HsnCodeServiceImpl implements HsnCodeService {

    private final HsnCodeRepository hsnCodeRepository;
    private final GSTRateRepository gstRateRepository;
    private final HsnCodeMapper hsnCodeMapper;

    @Override
    @Transactional
    public HsnCodeResponse createHsnCode(CreateHsnCodeRequest request) {

        if (hsnCodeRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException(
                    "HSN code already exists"
            );
        }

        HsnCode hsnCode = hsnCodeMapper.toEntity(request);

        if (request.gstRateId() != null) {
            hsnCode.setGstRate(resolveGstRate(request.gstRateId()));
        }

        HsnCode saved = hsnCodeRepository.save(hsnCode);

        return hsnCodeMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HsnCodeResponse> getHsnCodes() {

        return hsnCodeRepository.findAll()
                .stream()
                .map(hsnCodeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HsnCodeResponse getHsnCode(UUID id) {

        HsnCode hsnCode = hsnCodeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("HSN code not found")
                );

        return hsnCodeMapper.toResponse(hsnCode);
    }

    @Override
    @Transactional
    public HsnCodeResponse updateHsnCode(UUID id, UpdateHsnCodeRequest request) {

        HsnCode hsnCode = hsnCodeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("HSN code not found")
                );

        if (request.code() != null &&
                !request.code().equals(hsnCode.getCode()) &&
                hsnCodeRepository.existsByCodeAndIdNot(request.code(), id)) {

            throw new IllegalArgumentException("HSN code already exists");
        }

        hsnCodeMapper.updateHsnCode(request, hsnCode);

        if (request.gstRateId() != null) {
            hsnCode.setGstRate(resolveGstRate(request.gstRateId()));
        }

        HsnCode updated = hsnCodeRepository.save(hsnCode);

        return hsnCodeMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteHsnCode(UUID id) {

        HsnCode hsnCode = hsnCodeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("HSN code not found")
                );

        try {
            hsnCodeRepository.delete(hsnCode);
            hsnCodeRepository.flush(); // force the DELETE now, so an FK violation surfaces here, not later
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Cannot delete this HSN code — it is still referenced by one or more products"
            );
        }
    }

    private GSTRate resolveGstRate(UUID gstRateId) {
        return gstRateRepository.findById(gstRateId)
                .orElseThrow(() ->
                        new IllegalArgumentException("GST rate not found")
                );
    }
}