-- Sample data for the Coupon module (CouponRequest + Coupon).
-- Not auto-executed by Spring Boot (no spring.sql.init.mode is configured in this project);
-- run manually against a dev database after the schema has been created by ddl-auto=update.
--
-- Depends on vehicle_sample_data.sql (for the APPROVED vehicle REG-DEMO-002 owned by
-- owner.demo@example.com) and route_sample_data.sql (for the active route CTG-OUT-001) having
-- already been run, in addition to the ADMIN user seeded by InitialSeeder.

-- Coupon request #1 - PENDING, awaiting admin review.
INSERT INTO coupon_requests (request_no, owner_id, vehicle_id, route_id, service_start, service_end, requested_amount, note, status, created_at, updated_at)
SELECT 'CR-202608-000001',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       DATE_ADD(NOW(), INTERVAL 5 DAY),
       DATE_ADD(DATE_ADD(NOW(), INTERVAL 5 DAY), INTERVAL 45 MINUTE),
       5000.00, 'First piloting request for this vessel.', 'PENDING', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupon_requests WHERE request_no = 'CR-202608-000001');

-- Coupon request #2 - APPROVED, with its issued ACTIVE coupon below.
INSERT INTO coupon_requests (request_no, owner_id, vehicle_id, route_id, service_start, service_end, requested_amount, note, status, reviewed_by, reviewed_at, created_at, updated_at)
SELECT 'CR-202608-000002',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       DATE_ADD(NOW(), INTERVAL 3 DAY),
       DATE_ADD(DATE_ADD(NOW(), INTERVAL 3 DAY), INTERVAL 45 MINUTE),
       5000.00, 'Approved and coupon already issued.', 'APPROVED',
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupon_requests WHERE request_no = 'CR-202608-000002');

INSERT INTO coupons (code, owner_id, amount, status, source, coupon_request_id, issued_by, issued_at, expires_at, version, created_at, updated_at)
SELECT 'CPN-202608-A1B2C3',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       5000.00, 'ACTIVE', 'REQUEST',
       (SELECT id FROM coupon_requests WHERE request_no = 'CR-202608-000002'),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202608-A1B2C3');

-- Coupon request #3 - REJECTED.
INSERT INTO coupon_requests (request_no, owner_id, vehicle_id, route_id, service_start, service_end, requested_amount, note, status, rejection_reason, reviewed_by, reviewed_at, created_at, updated_at)
SELECT 'CR-202608-000003',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       DATE_ADD(NOW(), INTERVAL 7 DAY),
       DATE_ADD(DATE_ADD(NOW(), INTERVAL 7 DAY), INTERVAL 45 MINUTE),
       5000.00, 'Requested during a scheduling conflict window.', 'REJECTED',
       'Vehicle documentation could not be verified in time for this service date.',
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM coupon_requests WHERE request_no = 'CR-202608-000003');

-- Coupon #2 - EXPIRED, issued manually (source MANUAL, no linked request) and already past its
-- expiry date, to exercise the read-time expiry check without waiting on the scheduled job.
INSERT INTO coupons (code, owner_id, amount, status, source, coupon_request_id, issued_by, issued_at, expires_at, version, created_at, updated_at)
SELECT 'CPN-202607-Z9Y8X7',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       2000.00, 'EXPIRED', 'MANUAL', NULL,
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 0,
       DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202607-Z9Y8X7');
