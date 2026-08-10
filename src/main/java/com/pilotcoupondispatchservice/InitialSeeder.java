package com.pilotcoupondispatchservice;

import com.pilotcoupondispatchservice.dao.PermissionService;
import com.pilotcoupondispatchservice.enums.UserType;
import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import com.pilotcoupondispatchservice.modules.roles.repository.RoleRepository;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.pilotcoupondispatchservice.constants.Constant.*;

/**
 * Database seeder for initial data setup on application startup.
 * Creates default roles and admin user if they don't exist.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitialSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionService permissionService;

    @Value("${ADMIN_NAME:Admin User}")
    private String adminName;

    @Value("${ADMIN_EMAIL:admin@example.com}")
    private String adminEmail;

    @Value("${ADMIN_PHONE:1234567890}")
    private String adminPhone;

    @Value("${ADMIN_PASSWORD:Admin@123456}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        try {
            seedDefaultRoles();
            seedAdminUser();
            log.info("Database seeding completed successfully");
        } catch (Exception e) {
            log.error("Error during database seeding", e);
        }
    }

    private void seedDefaultRoles() {
        log.info("Starting to seed default roles...");

        // ADMIN Role
        if (!roleRepository.existsByAlias("ADMIN")) {
            Role ADMIN_ROLE = new Role(ADMIN_ROLE_ALIAS, permissionService.generatePermission(Arrays.asList(ADMIN_PERMISSION_LIST)), true);
            roleRepository.save(ADMIN_ROLE);
            log.info("Created ADMIN role");
        } else {
            log.info("ADMIN role already exists");
        }

        // OWNER Role
        if (!roleRepository.existsByAlias("OWNER")) {
            Role OWNER_ROLE = new Role(OWNER_ROLE_ALIAS, permissionService.generatePermission(Arrays.asList(OWNER_PERMISSION_LIST)), true);
            roleRepository.save(OWNER_ROLE);
            log.info("Created OWNER role");
        } else {
            log.info("OWNER role already exists");
        }
    }

    private void seedAdminUser() {
        log.info("Starting to seed admin user...");

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user already exists with email: {}", adminEmail);
            return;
        }

        if (userRepository.existsByPhone(adminPhone)) {
            log.info("Admin user already exists with phone: {}", adminPhone);
            return;
        }

        Role adminRole = roleRepository.findByAlias("ADMIN").get();

        User adminUser = new User();
        adminUser.setName(adminName);
        adminUser.setEmail(adminEmail);
        adminUser.setPhone(adminPhone);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setRole(adminRole);
        adminUser.setUserType(UserType.ADMIN);
        adminUser.setIsActive(true);

        userRepository.save(adminUser);
        log.info("Created admin user with email: {} and phone: {}", adminEmail);
    }
}
