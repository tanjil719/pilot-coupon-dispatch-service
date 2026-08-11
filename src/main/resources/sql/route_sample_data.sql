-- Sample data for the Route module.
-- Not auto-executed by Spring Boot (no spring.sql.init.mode is configured in this project);
-- run manually against a dev database after the schema has been created by ddl-auto=update.
--
-- Assumes the ADMIN user seeded by InitialSeeder already exists with email 'admin@example.com'.

-- Route #1 - short in-harbor route, active.
INSERT INTO routes (route_code, name, start_point, end_point, distance_km, estimated_duration_minutes, service_fee, description, active, created_by, updated_by, created_at, updated_at)
SELECT 'CTG-OUT-001', 'Chittagong Outer Anchorage', 'Chittagong Port', 'Outer Anchorage', 12.5, 45, 5000.00,
       'Standard inbound piloting from the outer anchorage to Chittagong Port.', true,
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE route_code = 'CTG-OUT-001');

-- Route #2 - longer route, higher fee, active.
INSERT INTO routes (route_code, name, start_point, end_point, distance_km, estimated_duration_minutes, service_fee, description, active, created_by, updated_by, created_at, updated_at)
SELECT 'MGL-CTG-001', 'Mongla to Chittagong', 'Mongla Port', 'Chittagong Port', 210.0, 480, 45000.00,
       'Long-haul coastal piloting between Mongla and Chittagong.', true,
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE route_code = 'MGL-CTG-001');

-- Route #3 - short low-fee route, active.
INSERT INTO routes (route_code, name, start_point, end_point, distance_km, estimated_duration_minutes, service_fee, description, active, created_by, updated_by, created_at, updated_at)
SELECT 'CTG-JTY-001', 'Chittagong Jetty Transfer', 'Chittagong Port', 'Jetty No. 4', 3.2, 20, 1500.00,
       'Short jetty-to-anchorage transfer within the port limits.', true,
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE route_code = 'CTG-JTY-001');

-- Route #4 - retired route, inactive.
INSERT INTO routes (route_code, name, start_point, end_point, distance_km, estimated_duration_minutes, service_fee, description, active, created_by, updated_by, created_at, updated_at)
SELECT 'PAY-CTG-001', 'Payra to Chittagong', 'Payra Port', 'Chittagong Port', 165.0, 360, 38000.00,
       'Retired route, superseded by a shorter corridor.', false,
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM routes WHERE route_code = 'PAY-CTG-001');
