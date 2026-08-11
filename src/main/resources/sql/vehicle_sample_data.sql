-- Sample data for the Vehicle module.
-- Not auto-executed by Spring Boot (no spring.sql.init.mode is configured in this project);
-- run manually against a dev database after the schema has been created by ddl-auto=update.
--
-- Assumes the ADMIN user seeded by InitialSeeder already exists with email 'admin@example.com'
-- and that the 'OWNER' role row already exists (also seeded by InitialSeeder on startup).

-- Demo owner user (skipped if it already exists).
-- NOTE: the password hash below is a placeholder, not a valid BCrypt hash for any real
-- password. Replace it with `passwordEncoder.encode(...)` output if you need to log in as
-- this user; it is not required to view the seeded vehicles data.
INSERT INTO users (name, email, phone, password, role_id, user_type, is_active, created_at, updated_at, created_by, updated_by)
SELECT 'Demo Owner', 'owner.demo@example.com', '01700000000', '{noop}replace-with-bcrypt-hash',
       (SELECT id FROM roles WHERE alias = 'OWNER'), 'OWNER', true, NOW(), NOW(), 0, 0
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'owner.demo@example.com');

-- Vehicle #1 - awaiting review.
INSERT INTO vehicles (owner_id, name, registration_no, type, capacity, description, status, active, created_at, updated_at)
SELECT (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       'MV Sundarban Star', 'REG-DEMO-001', 'CARGO_SHIP', 1200.0,
       'Cargo vessel awaiting first review.', 'PENDING', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM vehicles WHERE registration_no = 'REG-DEMO-001');

-- Vehicle #2 - approved by the demo admin.
INSERT INTO vehicles (owner_id, name, registration_no, type, capacity, description, status, approved_by, reviewed_at, active, created_at, updated_at)
SELECT (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       'MV Padma Voyager', 'REG-DEMO-002', 'PASSENGER_FERRY', 300.0,
       'Passenger ferry cleared for bookings.', 'APPROVED',
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM vehicles WHERE registration_no = 'REG-DEMO-002');

-- Vehicle #3 - rejected by the demo admin.
INSERT INTO vehicles (owner_id, name, registration_no, type, capacity, description, status, rejection_reason, approved_by, reviewed_at, active, created_at, updated_at)
SELECT (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       'MV Karnaphuli Tug', 'REG-DEMO-003', 'TUG_BOAT', 80.0,
       'Tug boat submitted with incomplete documentation.', 'REJECTED',
       'Registration certificate photo is unreadable, please resubmit.',
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM vehicles WHERE registration_no = 'REG-DEMO-003');
