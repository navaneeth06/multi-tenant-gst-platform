package com.navaneeth.multitenantgstplatform.feature.product.service;

import com.navaneeth.multitenantgstplatform.feature.master.entity.GSTRate;
import com.navaneeth.multitenantgstplatform.feature.master.entity.HsnCode;
import com.navaneeth.multitenantgstplatform.feature.master.entity.Unit;
import com.navaneeth.multitenantgstplatform.feature.master.repository.GSTRateRepository;
import com.navaneeth.multitenantgstplatform.feature.master.repository.HsnCodeRepository;
import com.navaneeth.multitenantgstplatform.feature.master.repository.UnitRepository;
import com.navaneeth.multitenantgstplatform.feature.product.dto.CreateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.dto.ProductResponse;
import com.navaneeth.multitenantgstplatform.feature.product.dto.UpdateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.entity.Product;
import com.navaneeth.multitenantgstplatform.feature.product.mapper.ProductMapper;
import com.navaneeth.multitenantgstplatform.feature.product.repository.ProductRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import com.navaneeth.multitenantgstplatform.infrastructure.security.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TenantRepository tenantRepository;
    private final HsnCodeRepository hsnCodeRepository;
    private final GSTRateRepository gstRateRepository;
    private final UnitRepository unitRepository;
    private final ProductMapper productMapper;
    private final SecurityContextService securityContextService;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {

        UUID tenantId = getCurrentTenantId();

        if (productRepository.existsByTenantIdAndSku(tenantId, request.sku())) {
            throw new IllegalArgumentException("A product with this SKU already exists");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        Product product = productMapper.toEntity(request);
        product.setTenant(tenant);
        product.setHsnCode(resolveHsnCode(request.hsnCodeId()));
        product.setGstRate(resolveGstRate(request.gstRateId()));
        product.setUnit(resolveUnit(request.unitId()));
        product.setActive(true);

        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(String search, boolean activeOnly) {

        UUID tenantId = getCurrentTenantId();

        List<Product> products;
        if (StringUtils.hasText(search)) {
            products = productRepository.searchByTenantId(tenantId, search);
        } else if (activeOnly) {
            products = productRepository.findByTenantIdAndActiveTrue(tenantId);
        } else {
            products = productRepository.findByTenantId(tenantId);
        }

        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {

        UUID tenantId = getCurrentTenantId();

        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID id, UpdateProductRequest request) {

        UUID tenantId = getCurrentTenantId();

        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (request.sku() != null &&
                !request.sku().equals(product.getSku()) &&
                productRepository.existsByTenantIdAndSkuAndIdNot(tenantId, request.sku(), id)) {

            throw new IllegalArgumentException("A product with this SKU already exists");
        }

        productMapper.updateProduct(request, product);

        if (request.hsnCodeId() != null) {
            product.setHsnCode(resolveHsnCode(request.hsnCodeId()));
        }
        if (request.gstRateId() != null) {
            product.setGstRate(resolveGstRate(request.gstRateId()));
        }
        if (request.unitId() != null) {
            product.setUnit(resolveUnit(request.unitId()));
        }

        Product updated = productRepository.save(product);

        return productMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {

        UUID tenantId = getCurrentTenantId();

        Product product = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        try {
            productRepository.delete(product);
            productRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "Cannot delete this product — it is referenced by one or more invoices"
            );
        }
    }

    private HsnCode resolveHsnCode(UUID id) {
        return hsnCodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("HSN code not found"));
    }

    private GSTRate resolveGstRate(UUID id) {
        return gstRateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GST rate not found"));
    }

    private Unit resolveUnit(UUID id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));
    }

    private UUID getCurrentTenantId() {
        UUID tenantId = securityContextService.getCurrentTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("No authenticated tenant context");
        }
        return tenantId;
    }
}