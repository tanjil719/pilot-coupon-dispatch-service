-- Sample data for the Booking module (Booking + Payment + BookingStatusHistory).
-- Not auto-executed by Spring Boot (no spring.sql.init.mode is configured in this project);
-- run manually against a dev database after the schema has been created by ddl-auto=update, and
-- after vehicle_sample_data.sql and route_sample_data.sql have already been run.
--
-- Covers the four booking lifecycles: PENDING_APPROVAL with a RESERVED coupon, APPROVED with a
-- USED coupon and PAID payment, REJECTED with its coupon released back to ACTIVE, and COMPLETED.
-- Each booking gets its own dedicated demo coupon and a full status history trail.

-- ===================== Booking #1 - PENDING_APPROVAL, coupon RESERVED =====================

INSERT INTO coupons (code, owner_id, amount, status, source, issued_at, expires_at, created_at, updated_at, created_by, updated_by)
SELECT 'CPN-202608-BK0001',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       5000.00, 'RESERVED', 'REQUEST',
       NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW(), NOW(),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202608-BK0001');

INSERT INTO bookings (booking_no, owner_id, vehicle_id, route_id, coupon_id, service_start, service_end,
                       fee_amount, status, payment_status, note, version, created_at, updated_at)
SELECT 'BK-202608-000001',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0001'),
       DATE_ADD(NOW(), INTERVAL 10 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 10 DAY), INTERVAL 45 MINUTE),
       5000.00, 'PENDING_APPROVAL', 'RESERVED', 'Awaiting admin review.', 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE booking_no = 'BK-202608-000001');

INSERT INTO payments (booking_id, coupon_id, fee_amount, coupon_amount, excess_amount, status, reserved_at, created_at, updated_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000001'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0001'),
       5000.00, 5000.00, 0.00, 'RESERVED', NOW(), NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM payments WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000001'));

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000001'), NULL, 'PENDING_APPROVAL',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'), 'Booking created', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000001') AND to_status = 'PENDING_APPROVAL'
);

-- ===================== Booking #2 - APPROVED, coupon USED, payment PAID =====================

INSERT INTO coupons (code, owner_id, amount, status, source, issued_at, expires_at, used_at, created_at, updated_at, created_by, updated_by)
SELECT 'CPN-202608-BK0002',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       5000.00, 'USED', 'REQUEST',
       DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 29 DAY), NOW(),
       DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202608-BK0002');

INSERT INTO bookings (booking_no, owner_id, vehicle_id, route_id, coupon_id, service_start, service_end,
                       fee_amount, status, payment_status, reviewed_by, reviewed_at, version, created_at, updated_at)
SELECT 'BK-202608-000002',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0002'),
       DATE_ADD(NOW(), INTERVAL 8 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 8 DAY), INTERVAL 45 MINUTE),
       5000.00, 'APPROVED', 'PAID',
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), 1, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE booking_no = 'BK-202608-000002');

INSERT INTO payments (booking_id, coupon_id, fee_amount, coupon_amount, excess_amount, status, reserved_at, paid_at, created_at, updated_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000002'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0002'),
       5000.00, 5000.00, 0.00, 'PAID', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()
WHERE NOT EXISTS (SELECT 1 FROM payments WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000002'));

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000002'), NULL, 'PENDING_APPROVAL',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'), 'Booking created', DATE_SUB(NOW(), INTERVAL 1 DAY)
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000002') AND to_status = 'PENDING_APPROVAL'
);

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000002'), 'PENDING_APPROVAL', 'APPROVED',
       (SELECT id FROM users WHERE email = 'admin@example.com'), 'Booking approved', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000002') AND to_status = 'APPROVED'
);

-- ===================== Booking #3 - REJECTED, coupon released back to ACTIVE =====================

INSERT INTO coupons (code, owner_id, amount, status, source, issued_at, expires_at, created_at, updated_at, created_by, updated_by)
SELECT 'CPN-202608-BK0003',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       5000.00, 'ACTIVE', 'REQUEST',
       DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 28 DAY),
       DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202608-BK0003');

INSERT INTO bookings (booking_no, owner_id, vehicle_id, route_id, coupon_id, service_start, service_end,
                       fee_amount, status, payment_status, rejection_reason, reviewed_by, reviewed_at, version, created_at, updated_at)
SELECT 'BK-202608-000003',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0003'),
       DATE_ADD(NOW(), INTERVAL 6 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 6 DAY), INTERVAL 45 MINUTE),
       5000.00, 'REJECTED', 'RELEASED',
       'Vehicle documentation could not be verified in time for this service date.',
       (SELECT id FROM users WHERE email = 'admin@example.com'), NOW(), 1, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE booking_no = 'BK-202608-000003');

INSERT INTO payments (booking_id, coupon_id, fee_amount, coupon_amount, excess_amount, status, reserved_at, released_at, created_at, updated_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000003'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0003'),
       5000.00, 5000.00, 0.00, 'RELEASED', DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()
WHERE NOT EXISTS (SELECT 1 FROM payments WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000003'));

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000003'), NULL, 'PENDING_APPROVAL',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'), 'Booking created', DATE_SUB(NOW(), INTERVAL 2 DAY)
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000003') AND to_status = 'PENDING_APPROVAL'
);

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000003'), 'PENDING_APPROVAL', 'REJECTED',
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       'Vehicle documentation could not be verified in time for this service date.', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000003') AND to_status = 'REJECTED'
);

-- ===================== Booking #4 - COMPLETED =====================

INSERT INTO coupons (code, owner_id, amount, status, source, issued_at, expires_at, used_at, created_at, updated_at, created_by, updated_by)
SELECT 'CPN-202608-BK0004',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       5000.00, 'USED', 'REQUEST',
       DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY),
       DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(),
       (SELECT id FROM users WHERE email = 'admin@example.com'),
       (SELECT id FROM users WHERE email = 'admin@example.com')
WHERE NOT EXISTS (SELECT 1 FROM coupons WHERE code = 'CPN-202608-BK0004');

INSERT INTO bookings (booking_no, owner_id, vehicle_id, route_id, coupon_id, service_start, service_end,
                       fee_amount, status, payment_status, reviewed_by, reviewed_at, completed_at, version, created_at, updated_at)
SELECT 'BK-202608-000004',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'),
       (SELECT id FROM vehicles WHERE registration_no = 'REG-DEMO-002'),
       (SELECT id FROM routes WHERE route_code = 'CTG-OUT-001'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0004'),
       DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(DATE_SUB(NOW(), INTERVAL 2 DAY), INTERVAL 45 MINUTE),
       5000.00, 'COMPLETED', 'PAID',
       (SELECT id FROM users WHERE email = 'admin@example.com'), DATE_SUB(NOW(), INTERVAL 4 DAY),
       DATE_SUB(NOW(), INTERVAL 2 DAY), 2, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()
WHERE NOT EXISTS (SELECT 1 FROM bookings WHERE booking_no = 'BK-202608-000004');

INSERT INTO payments (booking_id, coupon_id, fee_amount, coupon_amount, excess_amount, status, reserved_at, paid_at, created_at, updated_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004'),
       (SELECT id FROM coupons WHERE code = 'CPN-202608-BK0004'),
       5000.00, 5000.00, 0.00, 'PAID', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY),
       DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()
WHERE NOT EXISTS (SELECT 1 FROM payments WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004'));

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004'), NULL, 'PENDING_APPROVAL',
       (SELECT id FROM users WHERE email = 'owner.demo@example.com'), 'Booking created', DATE_SUB(NOW(), INTERVAL 5 DAY)
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004') AND to_status = 'PENDING_APPROVAL'
);

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004'), 'PENDING_APPROVAL', 'APPROVED',
       (SELECT id FROM users WHERE email = 'admin@example.com'), 'Booking approved', DATE_SUB(NOW(), INTERVAL 4 DAY)
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004') AND to_status = 'APPROVED'
);

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004'), 'APPROVED', 'IN_PROGRESS',
       (SELECT id FROM users WHERE email = 'admin@example.com'), 'Booking started', DATE_SUB(NOW(), INTERVAL 2 DAY)
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004') AND to_status = 'IN_PROGRESS'
);

INSERT INTO booking_status_history (booking_id, from_status, to_status, changed_by, note, changed_at)
SELECT (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004'), 'IN_PROGRESS', 'COMPLETED',
       (SELECT id FROM users WHERE email = 'admin@example.com'), 'Booking completed', DATE_SUB(NOW(), INTERVAL 2 DAY)
WHERE NOT EXISTS (
    SELECT 1 FROM booking_status_history
    WHERE booking_id = (SELECT id FROM bookings WHERE booking_no = 'BK-202608-000004') AND to_status = 'COMPLETED'
);
