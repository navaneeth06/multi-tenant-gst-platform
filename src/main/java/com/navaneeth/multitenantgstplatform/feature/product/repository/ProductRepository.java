package com.navaneeth.multitenantgstplatform.feature.product.repository;

import com.navaneeth.multitenantgstplatform.feature.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByTenantId(UUID tenantId);

    List<Product> findByTenantIdAndActiveTrue(UUID tenantId);

    Optional<Product> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByTenantIdAndSku(UUID tenantId, String sku);

    boolean existsByTenantIdAndSkuAndIdNot(UUID tenantId, String sku, UUID id);

    @Query("""
            SELECT p FROM Product p
            WHERE p.tenant.id = :tenantId
              AND (
                   LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    List<Product> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("search") String search
    );
}