-- Migration script to update Agent table to use Company reference instead of companyName

-- Step 1: Add company_id column
ALTER TABLE agents ADD COLUMN company_id BIGINT;

-- Step 2: Add foreign key constraint
ALTER TABLE agents ADD CONSTRAINT fk_agent_company 
    FOREIGN KEY (company_id) REFERENCES companies(id);

-- Step 3: Migrate existing data (optional - if you have existing agents with companyName)
-- This will try to match existing companyName strings to actual companies
-- UPDATE agents a 
-- SET company_id = (SELECT c.id FROM companies c WHERE c.name = a.company_name LIMIT 1)
-- WHERE a.company_name IS NOT NULL;

-- Step 4: Drop the old companyName column (after verifying data migration)
-- ALTER TABLE agents DROP COLUMN company_name;

-- Note: Uncomment Step 3 and 4 after verifying the migration works correctly
