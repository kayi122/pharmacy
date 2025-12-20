-- Check if agents have company_id
SELECT id, first_name, last_name, email, company_id FROM agents;

-- Check available companies
SELECT id, name FROM companies;

-- Assign first company to agents without company
UPDATE agents 
SET company_id = (SELECT id FROM companies LIMIT 1)
WHERE company_id IS NULL;

-- Verify the update
SELECT a.id, a.first_name, a.last_name, a.email, a.company_id, c.name as company_name
FROM agents a
LEFT JOIN companies c ON a.company_id = c.id;
