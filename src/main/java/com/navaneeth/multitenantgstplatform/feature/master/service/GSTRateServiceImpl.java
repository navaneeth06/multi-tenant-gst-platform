package com.navaneeth.multitenantgstplatform.feature.master.service;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.GSTRateResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.entity.GSTRate;
import com.navaneeth.multitenantgstplatform.feature.master.mapper.GSTRateMapper;
import com.navaneeth.multitenantgstplatform.feature.master.repository.GSTRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GSTRateServiceImpl implements GSTRateService {

    private final GSTRateRepository gstRateRepository;
    private final GSTRateMapper gstRateMapper;

    @Override
    @Transactional
    public GSTRateResponse createRate(CreateGSTRateRequest request) {

        if (gstRateRepository.existsByRatePercent(request.ratePercent())) {
            throw new IllegalArgumentException(
                    "A GST rate with this percentage already exists"
            );
        }

        GSTRate gstRate = gstRateMapper.toEntity(request);
        gstRate.setActive(true);

        GSTRate saved = gstRateRepository.save(gstRate);

        return gstRateMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GSTRateResponse> getRates() {

        return gstRateRepository.findAll()
                .stream()
                .map(gstRateMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GSTRateResponse> getActiveRates() {

        return gstRateRepository.findByActiveTrue()
                .stream()
                .map(gstRateMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GSTRateResponse getRate(UUID id) {

        GSTRate gstRate = gstRateRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("GST rate not found")
                );

        return gstRateMapper.toResponse(gstRate);
    }

    @Override
    @Transactional
    public GSTRateResponse updateRate(UUID id, UpdateGSTRateRequest request) {

        GSTRate gstRate = gstRateRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("GST rate not found")
                );

        if (request.ratePercent() != null &&
                !request.ratePercent().equals(gstRate.getRatePercent()) &&
                gstRateRepository.existsByRatePercentAndIdNot(request.ratePercent(), id)) {

            throw new IllegalArgumentException(
                    "A GST rate with this percentage already exists"
            );
        }

        gstRateMapper.updateGSTRate(request, gstRate);

        GSTRate updated = gstRateRepository.save(gstRate);

        return gstRateMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deactivateRate(UUID id) {

        GSTRate gstRate = gstRateRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("GST rate not found")
                );

        gstRate.setActive(false);

        gstRateRepository.save(gstRate);
    }
}