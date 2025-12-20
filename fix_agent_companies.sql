-- Assign first company to agents without company
UPDATE agents 
SET company_id = (SELECT id FROM companies LIMIT 1)
WHERE company_id IS NULL;
