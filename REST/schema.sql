create table toilets (
id integer primary key,
description text,
location text,
occupied integer,
methane_level integer,
lat real,
lng real
);
insert into toilets (id, description, lat, lng, location, occupied, methane_level)
	values (NULL, "Toilet ved grønnegade", 55.722353, 12.54459, "grønnegade 1", 0, 50);
insert into toilets (id, description, lat, lng, location, occupied, methane_level)
	values (NULL, "Toilet ved skippervej", 55.722353, 12.54459, "skippervej 1 1", 1, 20);