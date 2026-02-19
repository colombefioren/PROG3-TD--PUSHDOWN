create table tax_config(
    id serial primary key,
    label varchar(255) not null,
    rate numeric(5,2) not null
);

insert into tax_config (label, rate) values
    ('TVA STANDARD',20);

alter table invoice
    add column tax_config_id int references tax_config(id);

select setval('tax_config_id_seq',(select max(id) from tax_config));