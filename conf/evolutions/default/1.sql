# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table patient_address (
  id                            bigserial not null,
  patient_id                    bigint not null,
  nickname                      varchar(255) not null,
  province                      varchar(87) not null,
  postalcode                    varchar(40) not null,
  streetaddress                 varchar(255) not null,
  city                          varchar(58) not null,
  country                       varchar(4),
  when_created                  timestamptz not null,
  enabled                       boolean default false not null,
  when_modified                 timestamptz not null,
  who_created                   bigint not null,
  who_modified                  bigint not null,
  version                       bigint not null,
  constraint pk_patient_address primary key (id)
);

create index ix_patient_address_patient_id_enabled on patient_address (patient_id,enabled);
create index ix_patient_address_patient_id on patient_address (patient_id);
create index ix_patient_address_enabled on patient_address (enabled);

# --- !Downs

drop table if exists patient_address cascade;

drop index if exists ix_patient_address_patient_id_enabled;
drop index if exists ix_patient_address_patient_id;
drop index if exists ix_patient_address_enabled;
