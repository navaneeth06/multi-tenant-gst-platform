package com.navaneeth.multitenantgstplatform.feature.gstregistration.service;

import com.navaneeth.multitenantgstplatform.common.exception.AppException;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.CreateGSTRegistrationRequest;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.dto.GSTRegistrationResponse;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.entity.GSTRegistration;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.mapper.GSTRegistrationMapper;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.repository.GSTRegistrationRepository;
import com.navaneeth.multitenantgstplatform.feature.master.entity.State;
import com.navaneeth.multitenantgstplatform.feature.master.repository.StateRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import com.navaneeth.multitenantgstplatform.infrastructure.security.SecurityContextService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GSTRegistrationServiceImpl implements GSTRegistrationService {

    // PAN occupies characters 3–12 (0-indexed 2–12) of a 15-character GSTIN
    private static final int PAN_START_INDEX = 2;
    private static final int PAN_END_INDEX = 12;

    private final GSTRegistrationRepository gstRegistrationRepository;
    private final StateRepository stateRepository;
    private final TenantRepository tenantRepository;
    private final GSTRegistrationMapper gstRegistrationMapper;
    private final SecurityContextService securityContextService;

    public GSTRegistrationServiceImpl(
            GSTRegistrationRepository gstRegistrationRepository,
            StateRepository stateRepository,
            TenantRepository tenantRepository,
            GSTRegistrationMapper gstRegistrationMapper,
            SecurityContextService securityContextService
    ) {
        this.gstRegistrationRepository = gstRegistrationRepository;
        this.stateRepository = stateRepository;
        this.tenantRepository = tenantRepository;
        this.gstRegistrationMapper = gstRegistrationMapper;
        this.securityContextService = securityContextService;
    }

    @Override
    @Transactional
    public GSTRegistrationResponse createRegistration(CreateGSTRegistrationRequest request) {

        UUID tenantId = securityContextService.getCurrentTenantId();

        gstRegistrationRepository.findByTenantIdAndStateId(tenantId, request.stateId())
                .ifPresent(existing -> {
                    throw new AppException(
                            "This tenant already has a GST registration for the selected state."
                            , HttpStatus.BAD_REQUEST);
                });

        validatePanMatchesExistingRegistrations(tenantId, request.gstin());

        if (gstRegistrationRepository.existsByTenantIdAndGstin(tenantId, request.gstin())) {
            throw new AppException("This GSTIN is already registered for this tenant.", HttpStatus.BAD_REQUEST);
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new AppException("Tenant not found", HttpStatus.NOT_FOUND));

        State state = stateRepository.findById(request.stateId())
                .orElseThrow(() -> new AppException("State not found", HttpStatus.NOT_FOUND));

        GSTRegistration registration = gstRegistrationMapper.toEntity(request);
        registration.setTenant(tenant);
        registration.setState(state);
        registration.setStatus("ACTIVE");

        GSTRegistration saved = gstRegistrationRepository.save(registration);

        return gstRegistrationMapper.toResponse(saved);
    }

    @Override
    public List<GSTRegistrationResponse> getRegistrations() {

        UUID tenantId = securityContextService.getCurrentTenantId();

        return gstRegistrationRepository.findByTenantId(tenantId).stream()
                .map(gstRegistrationMapper::toResponse)
                .toList();
    }

    @Override
    public GSTRegistrationResponse getRegistration(UUID id) {

        UUID tenantId = securityContextService.getCurrentTenantId();

        GSTRegistration registration = gstRegistrationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new AppException("GST registration not found", HttpStatus.NOT_FOUND));

        return gstRegistrationMapper.toResponse(registration);
    }

    @Override
    @Transactional
    public void deactivateRegistration(UUID id) {

        UUID tenantId = securityContextService.getCurrentTenantId();

        GSTRegistration registration = gstRegistrationRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new AppException("GST registration not found", HttpStatus.NOT_FOUND));

        registration.setStatus("SUSPENDED");
        gstRegistrationRepository.save(registration);
    }

    /**
     * Enforces the rule: every GSTIN a tenant holds must belong to the same
     * legal business (same PAN), even across different states. A mismatch
     * means this is genuinely a different business and should be a new
     * tenant account, not a new registration under this one.
     */
    private void validatePanMatchesExistingRegistrations(UUID tenantId, String newGstin) {

        String newPan = extractPan(newGstin);

        List<GSTRegistration> existingRegistrations = gstRegistrationRepository.findByTenantId(tenantId);

        boolean hasMismatch = existingRegistrations.stream()
                .map(GSTRegistration::getGstin)
                .map(this::extractPan)
                .anyMatch(existingPan -> !existingPan.equals(newPan));

        if (hasMismatch) {
            throw new AppException(
                    "This GSTIN belongs to a different business (PAN mismatch). " +
                            "To manage a separate business, please create a new tenant account."
                    , HttpStatus.BAD_REQUEST);
        }
    }

    private String extractPan(String gstin) {
        return gstin.substring(PAN_START_INDEX, PAN_END_INDEX).toUpperCase();
    }
}