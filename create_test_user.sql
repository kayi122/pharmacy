-- First, check if location data exists
SELECT COUNT(*) FROM locations;

-- If locations exist, get a village ID to use
SELECT id, name, type FROM locations WHERE type = 'VILLAGE' LIMIT 1;

-- Create a test user (update location_id with an actual village ID from above query)
-- Password is: password123
INSERT INTO users (first_name, last_name, email, phone, password, role, location_id, active, email_verified, two_factor_enabled, created_at, updated_at)
VALUES (
    'Test',
    'User',
    'test@example.com',
    '0781234567',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    (SELECT id FROM locations WHERE type = 'VILLAGE' LIMIT 1),
    true,
    true,
    false,
    NOW(),
    NOW()
)
ON CONFLICT (email) DO UPDATE SET
    password = EXCLUDED.password,
    active = true,
    email_verified = true,
    two_factor_enabled = false;

-- Verify the user was created
SELECT id, first_name, last_name, email, role, active, email_verified FROM users WHERE email = 'test@example.com';
