use trackme;
DELETE from Individual;
DELETE from ThirdParty;
DELETE from Monitoring;
DELETE from GroupMonitoring;
DELETE from HeartBeat;
DELETE from SleepTime;

INSERT INTO Individual (taxcode, username, name, surname, email, birth_date, password)
	VALUES	('ABCDE', 'spyro', 'Mario', 'Rossi', 'mario.rossi@mail.com', '1990-10-20', 'aaish'),
			('FGHI', 'turing', 'John', 'Doe', 'jdoe@lol.com', '1955-11-02', 'qjqn'),
			('AHJ28', 'boole', NULL , 'Smith', 'smit@me.org', NULL, 'isksi');

INSERT INTO ThirdParty (username, email, name, surname, password)
	VALUES	('mydoc', 'doc@mail.com', NULL, NULL, 'aanjanjn'),
			('cardiologist', 'heart@mail.org', 'Luigi', 'Verdi', 'quiqhi');

INSERT INTO Monitoring (individual, third_party, ts, views)
	VALUES	('ABCDE', 'mydoc', '2018-12-04 11:22:00', 5);

INSERT INTO GroupMonitoring (third_party, ts, views, location, age_min, age_max, sex)
	VALUES ('cardiologist', '2018-12-04 11:22:00', 3, 'Milano', '18', '35', 'female');

INSERT INTO HeartBeat (individual, ts, value, latitude, longitude)
	VALUES 	('ABCDE', '2018-12-01 16:39:00', 150, '45.4640976', '9.191926500000022'),
			('FGHI', '2018-12-04 11:22:00', 120, '45.4640976', '9.191926500000022');

INSERT INTO SleepTime (individual, day, value, latitude, longitude)
	VALUES	('ABCDE', '2018-12-01 16:39:00', '8:30:00', '45.4640976', '9.191926500000022');