package com.navaneeth.multitenantgstplatform.feature.master.repository;

import com.navaneeth.multitenantgstplatform.feature.master.entity.HsnCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HsnCodeRepository extends JpaRepository<HsnCode, UUID> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID id);

    Optional<HsnCode> findByCode(String code);
}