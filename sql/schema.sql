create database pushdown;

create type invoice_status as enum('DRAFT','CONFIRMED','PAID');

create table invoice(
    id serial primary key,
    customer_name varchar(255) not null,
    status invoice_status
);

create table invoice_line (
    id serial primary key,
    invoice_id int not null references invoice(id),
    label varchar(255) not null,
    quantity int not null,
    unit_price numeric(10,2) not null
);