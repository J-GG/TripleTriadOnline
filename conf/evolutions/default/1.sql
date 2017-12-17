# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table card (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  name                          varchar(255),
  level                         integer,
  up_number                     integer,
  right_number                  integer,
  down_number                   integer,
  left_number                   integer,
  constraint pk_card primary key (uid)
);

create table member (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  username                      varchar(255),
  password                      varchar(255),
  constraint pk_member primary key (uid)
);

create table member_owns_card (
  member_uid                    uuid not null,
  card_uid                      uuid not null,
  constraint pk_member_owns_card primary key (member_uid,card_uid)
);

create table player (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  constraint pk_player primary key (uid)
);

alter table member_owns_card add constraint fk_member_owns_card_member foreign key (member_uid) references member (uid) on delete restrict on update restrict;
create index ix_member_owns_card_member on member_owns_card (member_uid);

alter table member_owns_card add constraint fk_member_owns_card_card foreign key (card_uid) references card (uid) on delete restrict on update restrict;
create index ix_member_owns_card_card on member_owns_card (card_uid);


# --- !Downs

alter table if exists member_owns_card drop constraint if exists fk_member_owns_card_member;
drop index if exists ix_member_owns_card_member;

alter table if exists member_owns_card drop constraint if exists fk_member_owns_card_card;
drop index if exists ix_member_owns_card_card;

drop table if exists card cascade;

drop table if exists member cascade;

drop table if exists member_owns_card cascade;

drop table if exists player cascade;

