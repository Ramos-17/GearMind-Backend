-- SQL Script to create admin user manually
-- Run this script in your database if you prefer manual admin creation

-- Note: The password below is 'admin123' encoded with BCrypt
-- You should change this password after first login!

INSERT INTO users (username, password, role) 
VALUES (
    'admin', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', -- admin123
    'ROLE_ADMIN'
) ON DUPLICATE KEY UPDATE username = username;

-- Alternative for PostgreSQL:
-- INSERT INTO users (username, password, role) 
-- VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ROLE_ADMIN')
-- ON CONFLICT (username) DO NOTHING; 