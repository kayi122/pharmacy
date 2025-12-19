-- Add sample medicines to database
INSERT INTO medicine (name, category, description, purchase_price, selling_price, quantity, manufacture_date, expiry_date, batch_number, is_expired, created_at, updated_at)
VALUES 
('Paracetamol 500mg', 'PAINKILLER', 'Effective pain relief and fever reducer', 50.00, 100.00, 500, '2024-01-01', '2026-01-01', 'BATCH001', false, NOW(), NOW()),
('Amoxicillin 250mg', 'ANTIBIOTIC', 'Antibiotic for bacterial infections', 150.00, 250.00, 300, '2024-02-01', '2025-12-01', 'BATCH002', false, NOW(), NOW()),
('Ibuprofen 400mg', 'PAINKILLER', 'Anti-inflammatory pain reliever', 80.00, 150.00, 400, '2024-01-15', '2026-06-15', 'BATCH003', false, NOW(), NOW()),
('Vitamin C 1000mg', 'VITAMINS', 'Immune system support supplement', 100.00, 180.00, 600, '2024-03-01', '2027-03-01', 'BATCH004', false, NOW(), NOW()),
('Metformin 500mg', 'DIABETES', 'Blood sugar control medication', 120.00, 200.00, 350, '2024-01-20', '2026-01-20', 'BATCH005', false, NOW(), NOW()),
('Aspirin 100mg', 'CARDIOVASCULAR', 'Blood thinner and pain relief', 60.00, 120.00, 450, '2024-02-10', '2026-02-10', 'BATCH006', false, NOW(), NOW()),
('Vitamin D3 1000IU', 'VITAMINS', 'Bone health and immune support', 90.00, 160.00, 500, '2024-03-05', '2027-03-05', 'BATCH007', false, NOW(), NOW()),
('Cetirizine 10mg', 'ANTIHISTAMINE', 'Allergy relief medication', 70.00, 130.00, 400, '2024-01-25', '2026-01-25', 'BATCH008', false, NOW(), NOW());
