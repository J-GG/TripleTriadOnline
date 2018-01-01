# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table board (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  constraint pk_board primary key (uid)
);

create table card_in_deck (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  player_uid                    uuid not null,
  card_uid                      uuid not null,
  constraint pk_card_in_deck primary key (uid)
);

create table card (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  name                          varchar(255),
  level                         integer,
  top_value                     integer,
  right_value                   integer,
  bottom_value                  integer,
  left_value                    integer,
  constraint uq_card_name unique (name),
  constraint pk_card primary key (uid)
);

create table card_on_case (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  player_uid                    uuid not null,
  card_uid                      uuid not null,
  flipped_by_rule               varchar(6),
  flipping_step                 integer,
  constraint ck_card_on_case_flipped_by_rule check ( flipped_by_rule in ('SAME','SIMPLE','WAR','COMBO','OPEN','PLUS')),
  constraint pk_card_on_case primary key (uid)
);

create table "case" (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  card_on_case_uid              uuid,
  board_uid                     uuid not null,
  row                           integer not null,
  col                           integer not null,
  constraint uq_case_card_on_case_uid unique (card_on_case_uid),
  constraint pk_case primary key (uid)
);

create table default_game_settings (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  difficulty                    varchar(6) not null,
  enabled_rules                 json not null,
  constraint ck_default_game_settings_difficulty check ( difficulty in ('EASY','HARD','NORMAL')),
  constraint pk_default_game_settings primary key (uid)
);

create table game (
  uid                           uuid not null,
  created_at                    timestamptz not null,
  board_uid                     uuid not null,
  player_turn_uid               uuid,
  difficulty                    varchar(6) not null,
  enabled_rules                 json not null,
  game_over                     boolean default false not null,
  constraint ck_game_difficulty check ( difficulty in ('EASY','HARD','NORMAL')),
  constraint uq_game_board_uid unique (board_uid),
  constraint pk_game primary key (uid)
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
  member_uid                    uuid,
  game_uid                      uuid not null,
  constraint pk_player primary key (uid)
);

alter table card_in_deck add constraint fk_card_in_deck_player_uid foreign key (player_uid) references player (uid) on delete restrict on update restrict;
create index ix_card_in_deck_player_uid on card_in_deck (player_uid);

alter table card_in_deck add constraint fk_card_in_deck_card_uid foreign key (card_uid) references card (uid) on delete restrict on update restrict;
create index ix_card_in_deck_card_uid on card_in_deck (card_uid);

alter table card_on_case add constraint fk_card_on_case_player_uid foreign key (player_uid) references player (uid) on delete restrict on update restrict;
create index ix_card_on_case_player_uid on card_on_case (player_uid);

alter table card_on_case add constraint fk_card_on_case_card_uid foreign key (card_uid) references card (uid) on delete restrict on update restrict;
create index ix_card_on_case_card_uid on card_on_case (card_uid);

alter table "case" add constraint fk_case_card_on_case_uid foreign key (card_on_case_uid) references card_on_case (uid) on delete restrict on update restrict;

alter table "case" add constraint fk_case_board_uid foreign key (board_uid) references board (uid) on delete restrict on update restrict;
create index ix_case_board_uid on "case" (board_uid);

alter table game add constraint fk_game_board_uid foreign key (board_uid) references board (uid) on delete restrict on update restrict;

alter table game add constraint fk_game_player_turn_uid foreign key (player_turn_uid) references player (uid) on delete restrict on update restrict;
create index ix_game_player_turn_uid on game (player_turn_uid);

alter table member add constraint fk_member_member_settings_uid foreign key (member_settings_uid) references member_settings (uid) on delete restrict on update restrict;

alter table member_owns_card add constraint fk_member_owns_card_member foreign key (member_uid) references member (uid) on delete restrict on update restrict;
create index ix_member_owns_card_member on member_owns_card (member_uid);

alter table member_owns_card add constraint fk_member_owns_card_card foreign key (card_uid) references card (uid) on delete restrict on update restrict;
create index ix_member_owns_card_card on member_owns_card (card_uid);

alter table member_settings add constraint fk_member_settings_default_game_settings_uid foreign key (default_game_settings_uid) references default_game_settings (uid) on delete restrict on update restrict;

alter table player add constraint fk_player_member_uid foreign key (member_uid) references member (uid) on delete restrict on update restrict;
create index ix_player_member_uid on player (member_uid);

alter table player add constraint fk_player_game_uid foreign key (game_uid) references game (uid) on delete restrict on update restrict;
create index ix_player_game_uid on player (game_uid);


# --- !Downs

alter table if exists card_in_deck drop constraint if exists fk_card_in_deck_player_uid;
drop index if exists ix_card_in_deck_player_uid;

alter table if exists card_in_deck drop constraint if exists fk_card_in_deck_card_uid;
drop index if exists ix_card_in_deck_card_uid;

alter table if exists card_on_case drop constraint if exists fk_card_on_case_player_uid;
drop index if exists ix_card_on_case_player_uid;

alter table if exists card_on_case drop constraint if exists fk_card_on_case_card_uid;
drop index if exists ix_card_on_case_card_uid;

alter table if exists "case" drop constraint if exists fk_case_card_on_case_uid;

alter table if exists "case" drop constraint if exists fk_case_board_uid;
drop index if exists ix_case_board_uid;

alter table if exists game drop constraint if exists fk_game_board_uid;

alter table if exists game drop constraint if exists fk_game_player_turn_uid;
drop index if exists ix_game_player_turn_uid;

alter table if exists member drop constraint if exists fk_member_member_settings_uid;

alter table if exists member_owns_card drop constraint if exists fk_member_owns_card_member;
drop index if exists ix_member_owns_card_member;

alter table if exists member_owns_card drop constraint if exists fk_member_owns_card_card;
drop index if exists ix_member_owns_card_card;

alter table if exists member_settings drop constraint if exists fk_member_settings_default_game_settings_uid;

alter table if exists player drop constraint if exists fk_player_member_uid;
drop index if exists ix_player_member_uid;

alter table if exists player drop constraint if exists fk_player_game_uid;
drop index if exists ix_player_game_uid;

drop table if exists board cascade;

drop table if exists card_in_deck cascade;

drop table if exists card cascade;

drop table if exists card_on_case cascade;

drop table if exists "case" cascade;

drop table if exists default_game_settings cascade;

drop table if exists game cascade;

drop table if exists member cascade;

drop table if exists member_owns_card cascade;

drop table if exists member_settings cascade;

drop table if exists player cascade;

