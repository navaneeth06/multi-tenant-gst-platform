package com.navaneeth.multitenantgstplatform.infrastructure.seeder;

import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Module;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Permission;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.ModuleRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.PermissionRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.RoleRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AuthorizationSeeder {

    private final ModuleRepository moduleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public AuthorizationSeeder(
            ModuleRepository moduleRepository,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository
    ) {
        this.moduleRepository = moduleRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void seed() {

        seedModules();
        seedPermissions();
        seedRoles();
        seedSuperAdminPermissions();
    }

    private void seedModules() {

        List<String[]> modules = List.of(
                new String[]{"Tenant", "tenant"},
                new String[]{"User", "user"},
                new String[]{"Authentication", "authentication"},
                new String[]{"Role & Permission", "role-permission"},
                new String[]{"Masters", "masters"},
                new String[]{"Customer", "customer"},
                new String[]{"Product", "product"},
                new String[]{"Invoice", "invoice"},
                new String[]{"Reports", "reports"}
        );

        for (String[] moduleData : modules) {

            String name = moduleData[0];
            String slug = moduleData[1];

            if (moduleRepository.existsBySlug(slug)) {
                continue;
            }

            moduleRepository.save(
                    Module.builder()
                            .name(name)
                            .slug(slug)
                            .build()
            );
        }
    }

    private void seedPermissions() {

        List<String> actions = List.of(
                "view",
                "add",
                "edit",
                "delete"
        );

        for (Module module : moduleRepository.findAll()) {

            for (String action : actions) {

                if (permissionRepository
                        .existsByModuleAndAction(module, action)) {
                    continue;
                }

                permissionRepository.save(
                        Permission.builder()
                                .module(module)
                                .action(action)
                                .build()
                );
            }
        }
    }

    private void seedRoles() {

        createGlobalRole(
                "Super Admin",
                "super-admin",
                "Full system access"
        );
    }

    private void createGlobalRole(
            String name,
            String slug,
            String description
    ) {

        if (roleRepository
                .findByTenantIdIsNullAndSlug(slug)
                .isPresent()) {
            return;
        }

        roleRepository.save(
                Role.builder()
                        .name(name)
                        .slug(slug)
                        .description(description)
                        .build()
        );
    }

    private void seedSuperAdminPermissions() {

        Role superAdmin = roleRepository
                .findByTenantIdIsNullAndSlug("super-admin")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Super Admin role not found"
                        )
                );

        Set<Permission> allPermissions =
                new HashSet<>(permissionRepository.findAll());

        superAdmin.setPermissions(allPermissions);

        roleRepository.save(superAdmin);
    }
}