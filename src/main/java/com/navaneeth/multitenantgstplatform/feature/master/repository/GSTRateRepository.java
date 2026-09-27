package com.navaneeth.multitenantgstplatform.feature.master.repository;

import com.navaneeth.multitenantgstplatform.feature.master.entity.GSTRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GSTRateRepository extends JpaRepository<GSTRate, UUID> {

    List<GSTRate> findByActiveTrue();

    boolean existsByRatePercent(BigDecimal ratePercent);

    // used on update, to exclude the row being edited from the uniqueness check
    boolean existsByRatePercentAndIdNot(BigDecimal ratePercent, UUID id);
}