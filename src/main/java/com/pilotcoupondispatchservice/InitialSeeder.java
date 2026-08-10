package com.pilotcoupondispatchservice;

import com.pilotcoupondispatchservice.enums.RoleLevel;
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
            Role adminRole = new Role();
            adminRole.setAlias("ADMIN");
            adminRole.setRoleLevel(RoleLevel.ADMIN);
            adminRole.setPermission("*"); // All permissions
            adminRole.setPredefine(true);
            roleRepository.save(adminRole);
            log.info("Created ADMIN role");
        } else {
            log.info("ADMIN role already exists");
        }

        // CONTRIBUTOR Role
        if (!roleRepository.existsByAlias("CONTRIBUTOR")) {
            Role contributorRole = new Role();
            contributorRole.setAlias("CONTRIBUTOR");
            contributorRole.setRoleLevel(RoleLevel.CONTRIBUTOR);
            contributorRole.setPermission("train:create,train:update,train:read");
            contributorRole.setPredefine(true);
            roleRepository.save(contributorRole);
            log.info("Created CONTRIBUTOR role");
        } else {
            log.info("CONTRIBUTOR role already exists");
        }

        // CONSUMER Role
        if (!roleRepository.existsByAlias("CONSUMER")) {
            Role consumerRole = new Role();
            consumerRole.setAlias("CONSUMER");
            consumerRole.setRoleLevel(RoleLevel.CONSUMER);
            consumerRole.setPermission("train:read");
            consumerRole.setPredefine(true);
            roleRepository.save(consumerRole);
            log.info("Created CONSUMER role");
        } else {
            log.info("CONSUMER role already exists");
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

        User adminUser = new User();
        adminUser.setName(adminName);
        adminUser.setEmail(adminEmail);
        adminUser.setPhone(adminPhone);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setRole(RoleLevel.ADMIN);
        adminUser.setIsActive(true);

        userRepository.save(adminUser);
        log.info("Created admin user with email: {} and phone: {}", adminEmail, adminPhone);
    }
}
