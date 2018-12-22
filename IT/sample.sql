use trackme;
DELETE from Individual;
DELETE from ThirdParty;
DELETE from Monitoring;
DELETE from GroupMonitoring;
DELETE from HeartBeat;
DELETE from SleepTime;

INSERT INTO Individual (taxcode, username, name, surname, email, sex, birth_date, birth_country, password)
	VALUES	('ABCDE', 'spyro', 'Mario', 'Rossi', 'mario.rossi@mail.com', 'MALE', '1990-10-20', 'Italy' ,'aaish'),
			('FGHI', 'turing', 'John', 'Doe', 'jdoe@lol.com', NULL ,'1955-11-02', 'USA','qjqn'),
			('AHJ28', 'boole', NULL , 'Smith', 'smit@me.org', 'FEMALE', NULL, 'Italy', 'isksi');

INSERT INTO ThirdParty (username, email, name, surname, password)
	VALUES	('mydoc', 'doc@mail.com', NULL, NULL, 'aanjanjn'),
			('cardiologist', 'heart@mail.org', 'Luigi', 'Verdi', 'quiqhi');

INSERT INTO Monitoring (individual, third_party, ts, views, attributes, status)
	VALUES	('ABCDE', 'mydoc', '2018-12-04 11:22:00', 5, 1, 'ACCEPTED');

INSERT INTO GroupMonitoring (third_party, name, ts, frequency, views, location, age_min, age_max, sex)
	VALUES ('cardiologist', 'Italians', '2018-12-04 11:22:00', NULL, 3, 'Milano', '18', '35', 'FEMALE');

INSERT INTO HeartBeat (individual, ts, value, latitude, longitude)
	VALUES 	('ABCDE', '2018-12-01 16:39:00', 150, '45.4640976', '9.191926500000022'),
			('FGHI', '2018-12-04 11:22:00', 120, '45.4640976', '9.191926500000022');

INSERT INTO SleepTime (individual, day, value, latitude, longitude)
	VALUES	('ABCDE', '2018-12-01 16:39:00', '8:30:00', '45.4640976', '9.191926500000022');

INSERT INTO BloodPressure (individual, ts, value)
	VALUES	('ABCDE', '2018-12-01 11:39:00', 111),
			('ABCDE', '2018-12-01 12:39:00', 115),
			('ABCDE', '2018-12-01 13:39:00', 120),
			('AHJ28', '2018-12-01 3:39:00', 104),
			('FGHI', '2018-12-01 10:39:00', 99);