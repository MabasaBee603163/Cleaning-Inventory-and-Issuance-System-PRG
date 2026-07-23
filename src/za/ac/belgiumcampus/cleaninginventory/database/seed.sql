-- ============================================================
-- Seed data for Cleaning Inventory and Issuance System
-- Run after schema.sql against your Supabase PostgreSQL database.
--
-- Demo login:
--   username: admin
--   password: admin123
--   (password stored as SHA-256 hex digest)
-- ============================================================

-- Clear dependent data first (safe re-seed)
delete from public.stock_issue_items;
delete from public.stock_issue_headers;
delete from public.materials;
delete from public.cleaners;
delete from public.suppliers;
delete from public.users;

-- Admin user (password = admin123)
-- SHA-256('admin123') = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
insert into public.users (username, password_hash, full_name, email, role)
values (
  'admin',
  '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
  'System Administrator',
  'admin@belgiumcampus.ac.za',
  'ADMIN'
);

insert into public.users (username, password_hash, full_name, email, role)
values (
  'storekeeper',
  '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
  'Store Keeper',
  'storekeeper@belgiumcampus.ac.za',
  'STOREKEEPER'
);

insert into public.suppliers (supplier_name, contact_person, phone, email, address)
values
  ('CleanPro Supplies', 'Thabo Molefe', '0115551001', 'thabo@cleanpro.co.za', '12 Industrial Road, Johannesburg'),
  ('Hygiene Hub', 'Sarah Naidoo', '0215552002', 'sarah@hygienehub.co.za', '45 Harbour Street, Cape Town');

insert into public.materials (material_name, description, quantity, unit, reorder_level, supplier_id)
values
  ('All-Purpose Cleaner', 'Multi-surface liquid cleaner', 50, 'litre', 10,
    (select supplier_id from public.suppliers where supplier_name = 'CleanPro Supplies')),
  ('Disinfectant Wipes', 'Antibacterial wipes pack', 30, 'pack', 8,
    (select supplier_id from public.suppliers where supplier_name = 'CleanPro Supplies')),
  ('Floor Polish', 'Commercial floor polish', 20, 'litre', 5,
    (select supplier_id from public.suppliers where supplier_name = 'Hygiene Hub')),
  ('Garbage Bags', 'Heavy duty black bags', 100, 'roll', 20,
    (select supplier_id from public.suppliers where supplier_name = 'Hygiene Hub')),
  ('Rubber Gloves', 'Nitrile cleaning gloves', 15, 'pair', 15,
    (select supplier_id from public.suppliers where supplier_name = 'CleanPro Supplies'));

insert into public.cleaners (first_name, last_name, phone, email, department)
values
  ('Lerato', 'Dlamini', '0825553003', 'lerato.dlamini@belgiumcampus.ac.za', 'Facilities'),
  ('Johan', 'van Wyk', '0835554004', 'johan.vanwyk@belgiumcampus.ac.za', 'Residences');
