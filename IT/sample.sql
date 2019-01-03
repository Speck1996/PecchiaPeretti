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

INSERT INTO Monitoring (individual, third_party, name, ts, views, attributes, status)
	VALUES	('ABCDE', 'mydoc', 'Paziente99','2018-12-04 11:22:00', 5, 1, 'ACCEPTED');

INSERT INTO GroupMonitoring (third_party, name, ts, frequency, views, location, age_min, age_max, sex)
	VALUES ('cardiologist', 'Italians', '2018-12-04 11:22:00', NULL, 3, 'Milano', '18', '35', 'FEMALE');

INSERT INTO HeartBeat (individual, ts, value, latitude, longitude)
	VALUES 	('ABCDE', '2018-12-01 16:39:00', 150, '45.4640976', '9.191926500000022'),
			('FGHI', '2018-12-04 11:22:00', 120, '45.4640976', '9.191926500000022');

INSERT INTO SleepTime (individual, day, value, latitude, longitude)
	VALUES	('ABCDE', '2018-12-01 16:39:00', '8:30:00', '45.4640976', '9.191926500000022'),
			('ABCDE', '2018-12-10', '4:20:30', '87.1', '-9.191926500000022'),
			('ABCDE', '2018-11-01 16:39:00', '6:30:00', '45.4640976', '9.191926500000022');

INSERT INTO BloodPressure (individual, ts, value, latitude, longitude)
	VALUES	('ABCDE', '2018-12-01 11:39:00', 111, '45.4640976', '9.191926500000022'),
			('ABCDE', '2018-12-01 12:39:00', 115, NULL, NULL),
			('ABCDE', '2018-12-01 13:39:00', 120, '45.4640976', '9.191926500000022'),
			('AHJ28', '2018-12-01 3:39:00', 104, NULL, NULL),
			('FGHI', '2018-12-01 10:39:00', 99, '45.4640976', '9.191926500000022');

INSERT INTO Steps (individual, day, value, latitude, longitude)
	VALUES	('ABCDE', '2018-12-03', '3401', '45.4640976', '9.191926500000022'),
			('ABCDE', '2018-12-04', '2401', '45.4640976', '9.191926500000022'),	
			('ABCDE', '2018-12-02', '3401', NULL, NULL),
			('FGHI', '2018-12-03', '300', '87.1', '-9.191926500000022');				