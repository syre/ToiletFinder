-- delete existing tables
drop table if exists toilets;
drop table if exists toilet_group;
drop table if exists events;

-- create schema
create table toilets (
id integer primary key,
group_id integer,
description text,
location text,
occupied integer,
methane_level integer,
lat real,
lng real,
FOREIGN KEY(group_id) references toilet_group(id)
);

create table toilet_group (
id integer primary key,
name text,
lat real,
lng real
);

create table events (
id integer primary key,
group_id integer,
toilet_id integer,
methane_level integer,
occupied integer,
time_stamp text,
FOREIGN KEY(group_id) references toilet_group(id),
FOREIGN KEY(id) references toilet(id)
);

--- insert data
--- toilet group 1
insert into toilet_group (id, name, lat, lng) values (1, "Test Ø 1", 55.621727, 12.078388);

insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
	values (NULL, 1,"Toilet 1 ved Test Ø 1", 55.621803, 12.078233, "Roskilde Festival", 0, 50);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
	values (NULL, 1, "Toilet 2 ved Test Ø 1", 55.621803, 12.078362, "Roskilde Festival", 1, 30);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 1, "Toilet 3 ved Test Ø 1", 55.621786, 12.078533, "Roskilde Festival", 0, 20);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 1, "Toilet 4 ved Test Ø 1", 55.621580, 12.078214, "Roskilde Festival", 1, 10);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 1, "Toilet 5 ved Test Ø 1", 55.621609, 12.078337, "Roskilde Festival", 0, 90);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 1, "Toilet 6 ved Test Ø 1", 55.621576, 12.078576, "Roskilde Festival", 1, 65);
--- toilet group 2
insert into toilet_group (id, name, lat, lng) values (2, "Test Ø 2", 55.621700, 12.078300);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 2,"Toilet 1 ved Test Ø 2", 55.621750, 12.078200, "Roskilde Festival", 1, 5);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 2, "Toilet 2 ved Test Ø 2", 55.621740, 12.078300, "Roskilde Festival", 1, 0);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 2, "Toilet 3 ved Test Ø 2", 55.621700, 12.078500, "Roskilde Festival", 0, 20);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 2, "Toilet 4 ved Test Ø 2", 55.621500, 12.078200, "Roskilde Festival", 0, 20);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 2, "Toilet 5 ved Test Ø 2", 55.621550, 12.078300, "Roskilde Festival", 0, 15);
insert into toilets (id, group_id, description, lat, lng, location, occupied, methane_level)
    values (NULL, 2, "Toilet 6 ved Test Ø 2", 55.621525, 12.078500, "Roskilde Festival", 0, 10);
