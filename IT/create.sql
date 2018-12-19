DROP DATABASE IF EXISTS trackme;

CREATE DATABASE trackme;
use trackme;

CREATE TABLE Individual 
(
	taxcode VARCHAR (30) PRIMARY KEY,
	username VARCHAR (40) UNIQUE NOT NULL,
	name VARCHAR (40),
	surname VARCHAR (40),
	email VARCHAR (320) UNIQUE NOT NULL,
	birth_date DATE,
	country VARCHAR(40),
	password VARCHAR(255) NOT NULL
);

CREATE TABLE ThirdParty
(
	username VARCHAR (30) PRIMARY KEY,
	email VARCHAR (320) UNIQUE NOT NULL,
	name VARCHAR (40),
	surname VARCHAR (40),
	password VARCHAR (255) NOT NULL
);

CREATE TABLE Monitoring
(
	individual VARCHAR (30),
	third_party VARCHAR (30),
	ts TIMESTAMP NOT NULL,
	frequency VARCHAR(20),
	views SMALLINT NOT NULL,

	PRIMARY KEY (individual, third_party),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE, 
	FOREIGN KEY (third_party) REFERENCES ThirdParty(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE GroupMonitoring
(
	id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	third_party VARCHAR (30) NOT NULL,
	ts TIMESTAMP NOT NULL,
	frequency VARCHAR(20),
	views SMALLINT NOT NULL,
	location VARCHAR (100),
	age_min TINYINT,
	age_max TINYINT,
	sex ENUM('male', 'female'),

	FOREIGN KEY (third_party) REFERENCES ThirdParty(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Heartbeat
(
	individual VARCHAR (30),
	ts TIMESTAMP,
	value SMALLINT NOT NULL,
	latitude VARCHAR (20),
	longitude VARCHAR (20),

	PRIMARY KEY (individual, ts),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE BloodPressure
(
	individual VARCHAR (30),
	ts TIMESTAMP,
	value SMALLINT NOT NULL,
	latitude VARCHAR (20),
	longitude VARCHAR (20),

	PRIMARY KEY (individual, ts),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE SleepTime
(
	individual VARCHAR (30),
	day date,
	value time NOT NULL,
	latitude VARCHAR (20),
	longitude VARCHAR (20),

	PRIMARY KEY (individual, day),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Steps
(
	individual VARCHAR (30),
	day date,
	value int NOT NULL,
	latitude VARCHAR (20),
	longitude VARCHAR (20),

	PRIMARY KEY (individual, day),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode)ON UPDATE CASCADE ON DELETE CASCADE
);