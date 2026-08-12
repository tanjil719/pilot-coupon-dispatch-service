-- Sample data for the Coupon module (CouponRequest + Coupon).
-- Not auto-executed by Spring Boot (no spring.sql.init.mode is configured in this project);
-- run manually against a dev database after the schema has been created by ddl-auto=update.
--
-- Depends on route_sample_data.sql (for the active route CTG-OUT-001, service_fee 5000.00) and the
-- demo owner user (owner.demo@example.com) seeded by vehicle_sample_data.sql, in addition to the
-- ADMIN user seeded by InitialSeeder, having already been run.
--
-- Matches the actual CouponRequest/Coupon entities as of the Booking module: CouponRequest has no
-- vehicle/route FK (only a routeCode string) and Coupon has no coupon_request_id FK; status values
-- are ACTIVE, RESERVED, USED, EXPIRED, CANCELLED and source is one of REQUEST, REFUND, MANUAL.

-- Coupon request #1 - PENDING, awaiting admin review.
INSERT INTO coupon_requests (owner_id, route_code, service_start, requested_amount, note, status, created_at, updated_at)
SELECT (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       'CTG-OUT-001', DATE_ADD(NOW(), INTERVAL 5 DAY), 5000.00,
       'First piloting request for this vessel.', 'PENDING', NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM coupon_requests
    WHERE owner_id = (SELECT id FROM users WHERE email = 'owner.demo@example.com')
      AND route_code = 'CTG-OUT-001' AND status = 'PENDING' AND requested_amount = 5000.00
);

-- Coupon request #2 - APPROVED, with its issued ACTIVE coupon below.
INSERT INTO coupon_requests (owner_id, route_code, service_start, requested_amount, note, status, reviewed_at, created_at, updated_at)
SELECT (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       'CTG-OUT-001', DATE_ADD(NOW(), INTERVAL 3 DAY), 5000.00,
       'Approved and coupon already issued.', 'APPROVED', NOW(), NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM coupon_requests
    WHERE owner_id = (SELECT id FROM users WHERE email = 'owner.demo@example.com')
      AND route_code = 'CTG-OUT-001' AND status = 'APPROVED' AND requested_amount = 5000.00
);

-- Coupon #1 - ACTIVE, issued against request #2 above (source REQUEST; no direct FK between the two
-- in the current schema, only chronology and amount tie them together).
INSERT INTO coupons (code, owner_id, amount, status, source, issued_at, expires_at, created_at, updated_at, created_by, updated_by)
SELECT 'CPN-202608-A1B2C3',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       5000.00, 'ACTIVE', 'REQUEST',
       NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW(), NOW(),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202608-A1B2C3');

-- Coupon request #3 - REJECTED.
INSERT INTO coupon_requests (owner_id, route_code, service_start, requested_amount, note, status, rejection_reason, reviewed_at, created_at, updated_at)
SELECT (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       'CTG-OUT-001', DATE_ADD(NOW(), INTERVAL 7 DAY), 5000.00,
       'Requested during a scheduling conflict window.', 'REJECTED',
       'Vehicle documentation could not be verified in time for this service date.', NOW(), NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM coupon_requests
    WHERE owner_id = (SELECT id FROM users WHERE email = 'owner.demo@example.com')
      AND route_code = 'CTG-OUT-001' AND status = 'REJECTED'
);

-- Coupon #2 - EXPIRED, issued manually (source MANUAL) and already past its expiry date, to
-- exercise the read-time expiry check without waiting on a scheduled job.
INSERT INTO coupons (code, owner_id, amount, status, source, issued_at, expires_at, used_at, created_at, updated_at, created_by, updated_by)
SELECT 'CPN-202607-Z9Y8X7',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       2000.00, 'EXPIRED', 'MANUAL',
       DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL,
       DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202607-Z9Y8X7');
