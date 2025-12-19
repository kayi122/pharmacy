-- Run this SQL in your PostgreSQL database to fix the address column issue
ALTER TABLE companies DROP COLUMN IF EXISTS address;
