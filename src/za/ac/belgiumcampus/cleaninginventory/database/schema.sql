-- ============================================================
-- Public schema (matches current Supabase database state)
-- ============================================================


drop table if exists public.stock_issue_items;
drop table if exists public.stock_issue_headers;
drop table if exists public.materials;
drop table if exists public.cleaners;
drop table if exists public.suppliers;
drop table if exists public.users;

drop sequence if exists public.stock_issue_items_issue_item_id_seq;
drop sequence if exists public.stock_issue_headers_issue_id_seq;
drop sequence if exists public.materials_material_id_seq;
drop sequence if exists public.cleaners_cleaner_id_seq;
drop sequence if exists public.suppliers_supplier_id_seq;
drop sequence if exists public.users_user_id_seq;

create sequence public.users_user_id_seq;
create sequence public.suppliers_supplier_id_seq;
create sequence public.materials_material_id_seq;
create sequence public.cleaners_cleaner_id_seq;
create sequence public.stock_issue_headers_issue_id_seq;
create sequence public.stock_issue_items_issue_item_id_seq;

-- -------------------------
-- public.users
-- -------------------------
create table public.users (
  user_id bigint primary key default nextval('public.users_user_id_seq'::regclass),
  username varchar(50) unique not null,
  password_hash text not null,
  full_name varchar(100),
  email varchar(100) unique not null,
  role varchar(20),
  created_at timestamp not null default CURRENT_TIMESTAMP
);

-- -------------------------
-- public.suppliers
-- -------------------------
create table public.suppliers (
  supplier_id bigint primary key default nextval('public.suppliers_supplier_id_seq'::regclass),
  supplier_name varchar(100) not null,
  contact_person varchar(100),
  phone varchar(30),
  email varchar(100),
  address text
);

-- -------------------------
-- public.materials
-- -------------------------
create table public.materials (
  material_id bigint primary key default nextval('public.materials_material_id_seq'::regclass),
  material_name varchar(100),
  description text,
  quantity integer not null default 0,
  unit varchar(20),
  reorder_level integer not null default 0,
  supplier_id bigint
);

alter table public.materials
  add constraint materials_supplier_id_fkey
  foreign key (supplier_id)
  references public.suppliers(supplier_id);

-- -------------------------
-- public.cleaners
-- -------------------------
create table public.cleaners (
  cleaner_id bigint primary key default nextval('public.cleaners_cleaner_id_seq'::regclass),
  first_name varchar(100),
  last_name varchar(100),
  phone varchar(30),
  email varchar(100),
  department varchar(100)
);

-- -------------------------
-- public.stock_issue_headers
-- -------------------------
create table public.stock_issue_headers (
  issue_id bigint primary key default nextval('public.stock_issue_headers_issue_id_seq'::regclass),
  cleaner_id bigint not null,
  user_id bigint not null,
  issue_date timestamp not null default CURRENT_TIMESTAMP,
  remarks text
);

alter table public.stock_issue_headers
  add constraint stock_issue_headers_cleaner_id_fkey
  foreign key (cleaner_id)
  references public.cleaners(cleaner_id);

alter table public.stock_issue_headers
  add constraint stock_issue_headers_user_id_fkey
  foreign key (user_id)
  references public.users(user_id);

-- -------------------------
-- public.stock_issue_items
-- -------------------------
create table public.stock_issue_items (
  issue_item_id bigint primary key default nextval('public.stock_issue_items_issue_item_id_seq'::regclass),
  issue_id bigint not null,
  material_id bigint not null,
  quantity integer not null,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  constraint stock_issue_items_quantity_check check (quantity > 0)
);

alter table public.stock_issue_items
  add constraint stock_issue_items_material_id_fkey
  foreign key (material_id)
  references public.materials(material_id);

alter table public.stock_issue_items
  add constraint stock_issue_items_issue_id_fkey
  foreign key (issue_id)
  references public.stock_issue_headers(issue_id);

-- (No extra indexes/unique constraints were present in your current introspection)