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

create table default_game_settings (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  difficulty                    varchar(6) not null,
  enabled_rules                 varchar[],
  constraint ck_default_game_settings_difficulty check ( difficulty in ('EASY','HARD','NORMAL')),
  constraint pk_default_game_settings primary key (uid)
);

create table in_game_settings (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  difficulty                    varchar(6) not null,
  enabled_rules                 varchar[],
  constraint ck_in_game_settings_difficulty check ( difficulty in ('EASY','HARD','NORMAL')),
  constraint pk_in_game_settings primary key (uid)
);

create table member (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  username                      varchar(10) not null,
  password                      varchar(255) not null,
  member_settings_uid           uuid not null,
  constraint uq_member_username unique (username),
  constraint uq_member_member_settings_uid unique (member_settings_uid),
  constraint pk_member primary key (uid)
);

create table member_owns_card (
  member_uid                    uuid not null,
  card_uid                      uuid not null,
  constraint pk_member_owns_card primary key (member_uid,card_uid)
);

create table member_settings (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  language                      varchar(255) not null,
  audio_enabled                 boolean default false not null,
  default_game_settings_uid     uuid not null,
  constraint uq_member_settings_default_game_settings_uid unique (default_game_settings_uid),
  constraint pk_member_settings primary key (uid)
);

create table player (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  constraint pk_player primary key (uid)
);

alter table member add constraint fk_member_member_settings_uid foreign key (member_settings_uid) references member_settings (uid) on delete restrict on update restrict;

alter table member_owns_card add constraint fk_member_owns_card_member foreign key (member_uid) references member (uid) on delete restrict on update restrict;
create index ix_member_owns_card_member on member_owns_card (member_uid);

alter table member_owns_card add constraint fk_member_owns_card_card foreign key (card_uid) references card (uid) on delete restrict on update restrict;
create index ix_member_owns_card_card on member_owns_card (card_uid);

alter table member_settings add constraint fk_member_settings_default_game_settings_uid foreign key (default_game_settings_uid) references default_game_settings (uid) on delete restrict on update restrict;


# --- !Downs

alter table if exists member drop constraint if exists fk_member_member_settings_uid;

alter table if exists member_owns_card drop constraint if exists fk_member_owns_card_member;
drop index if exists ix_member_owns_card_member;

alter table if exists member_owns_card drop constraint if exists fk_member_owns_card_card;
drop index if exists ix_member_owns_card_card;

alter table if exists member_settings drop constraint if exists fk_member_settings_default_game_settings_uid;

drop table if exists card cascade;

drop table if exists default_game_settings cascade;

drop table if exists in_game_settings cascade;

drop table if exists member cascade;

drop table if exists member_owns_card cascade;

drop table if exists member_settings cascade;

drop table if exists player cascade;

