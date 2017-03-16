INSERT INTO PUBLIC.EVENT (ID, CLOSING_TIME, CREATED, DESCRIPTION, END_TIME, NAME, IMAGE, START_TIME, UPDATED) VALUES (1, '2017-07-16 16:45:58', '2017-03-16 16:46:04', 'The Greenfield Festival is an annual rock music festival held on the outskirts of the town of Interlaken, in the Swiss canton of Bern', '2017-06-10 16:48:13', 'Greenfield Festival', 'https://f3.blick.ch/img/incoming/origs5130194/8880145312-w980-h653/Greenfield.jpg', '2017-06-08 16:48:23', '2017-03-16 16:48:34');
INSERT INTO PUBLIC.EVENT (ID, CLOSING_TIME, CREATED, DESCRIPTION, END_TIME, NAME, IMAGE, START_TIME, UPDATED) VALUES (2, '2017-09-16 16:46:10', '2017-03-16 16:46:08', 'The Open Air Gampel is a music festival held in Gampel-Bratsch, Switzerland, over a time period of four days.', '2017-08-20 16:49:17', 'OpenAir Gampel', 'http://www.openairgampel.ch/2015/templates/openairgampel/img/standard_bg.jpg', '2017-08-18 16:49:28', '2017-03-16 16:48:40');

insert into file (name, event_id) values ('File 1', 1);
insert into file (name, event_id) values ('File 2', 1);

INSERT INTO PUBLIC.USER (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, INTERNAL) VALUES ('schoenbaechler.lukas@gmail.com', 'Lukas', 'Schönbächler', 'ASDF', 0);
INSERT INTO PUBLIC.USER (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, INTERNAL) VALUES ('andreas.gassmann@students.fhnw.ch', 'Andreas', 'Gassmann', 'ASDF', 0);
INSERT INTO PUBLIC.USER (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, INTERNAL) VALUES ('johnas.frehner@students.fhnw.ch', 'Johnas', 'Frehner', 'ASDF', 0);

INSERT INTO PUBLIC.ATTENDEES (IDUSER, IDEVENT) VALUES (1, 1);
INSERT INTO PUBLIC.ATTENDEES (IDUSER, IDEVENT) VALUES (2, 2);
INSERT INTO PUBLIC.ATTENDEES (IDUSER, IDEVENT) VALUES (3, 2);

INSERT INTO PUBLIC.SPEAKERS (IDUSER, IDEVENT) VALUES (1, 1);
INSERT INTO PUBLIC.SPEAKERS (IDUSER, IDEVENT) VALUES (1, 2);
