CREATE TABLE Participants (
  companyName varchar,
  name varchar,
  surname varchar,
  PRIMARY KEY (companyName, name)
);

CREATE TABLE Meetups (
  name varchar,
  date timestamp,
  places double,
  pressure double,
  PRIMARY KEY (stationName, measurement)
);

CREATE TABLE Organizers (
  name varchar,
  surname varchar,
);

CREATE TABLE PageViewCounts (
  url varchar,
  views counter,
  PRIMARY KEY(url)
);