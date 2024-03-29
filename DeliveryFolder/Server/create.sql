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
	sex ENUM('MALE', 'FEMALE'),
	birth_country VARCHAR(40),
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
	name VARCHAR (40) NOT NULL,
	ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	frequency ENUM('WEEK', 'MONTH', 'QUARTER', 'SEMESTER', 'YEAR'),
	views SMALLINT NOT NULL,
	attributes SMALLINT NOT NULL,
	status ENUM('PENDING', 'ACCEPTED') NOT NULL,

	PRIMARY KEY (individual, third_party),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE, 
	FOREIGN KEY (third_party) REFERENCES ThirdParty(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE GroupMonitoring
(
	third_party VARCHAR (30) NOT NULL,
	name VARCHAR (40) NOT NULL,
	ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	frequency ENUM('WEEK', 'MONTH', 'QUARTER', 'SEMESTER', 'YEAR'),
	views SMALLINT NOT NULL,
	location VARCHAR (100),
	age_min TINYINT,
	age_max TINYINT,
	sex ENUM('MALE', 'FEMALE'),
	birth_country VARCHAR (40),
	found_location BLOB,

	PRIMARY KEY (third_party, name),
	FOREIGN KEY (third_party) REFERENCES ThirdParty(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Heartbeat
(
	individual VARCHAR (30),
	ts TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
	value SMALLINT NOT NULL,
	latitude DOUBLE,
	longitude DOUBLE,

	PRIMARY KEY (individual, ts),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE BloodPressure
(
	individual VARCHAR (30),
	ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	value_min SMALLINT NOT NULL,
	value_max SMALLINT NOT NULL,
	latitude DOUBLE,
	longitude DOUBLE,

	PRIMARY KEY (individual, ts),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE SleepTime
(
	individual VARCHAR (30),
	day date,
	value time NOT NULL,
	latitude DOUBLE,
	longitude DOUBLE,

	PRIMARY KEY (individual, day),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Steps
(
	individual VARCHAR (30),
	day date,
	value int NOT NULL,
	latitude DOUBLE,
	longitude DOUBLE,

	PRIMARY KEY (individual, day),
	FOREIGN KEY (individual) REFERENCES Individual(taxcode)ON UPDATE CASCADE ON DELETE CASCADE
);