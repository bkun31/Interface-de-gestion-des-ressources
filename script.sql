DROP TABLE IF EXISTS message_read;
DROP TABLE IF EXISTS message_received;
DROP TABLE IF EXISTS send;
DROP TABLE IF EXISTS belong;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS discussion_thread;
DROP TABLE IF EXISTS user_group;
DROP TABLE IF EXISTS user_account;

CREATE TABLE user_account(
   userId INT AUTO_INCREMENT,
   username VARCHAR(50) UNIQUE NOT NULL,
   password VARCHAR(50) NOT NULL,
   name VARCHAR(50) NOT NULL,
   firstname VARCHAR(50) NOT NULL,
   campus_user BOOLEAN NOT NULL,
   PRIMARY KEY(userId)
);

CREATE TABLE user_group(
   groupId INT AUTO_INCREMENT,
   groupname VARCHAR(50) UNIQUE NOT NULL,
   campus_group BOOLEAN NOT NULL,
   PRIMARY KEY(groupId)
);

CREATE TABLE discussion_thread(
   threadId INT AUTO_INCREMENT,
   title VARCHAR(50) NOT NULL,
   groupId INT NOT NULL,
   userId INT NOT NULL,
   PRIMARY KEY(threadId),
   FOREIGN KEY(groupId) REFERENCES user_group(groupId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY(userId) REFERENCES user_account(userId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE message(
   messageId INT AUTO_INCREMENT,
   message_timestamp TIMESTAMP,
   text TEXT NOT NULL,
   threadId INT NOT NULL,
   PRIMARY KEY(messageId),
   FOREIGN KEY(threadId) REFERENCES discussion_thread(threadId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE belong(
   userId INT,
   groupId INT,
   PRIMARY KEY(userId, groupId),
   FOREIGN KEY(userId) REFERENCES user_account(userId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY(groupId) REFERENCES user_group(groupId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE send(
   userId INT,
   threadId INT,
   messageId INT,
   PRIMARY KEY(userId, threadId, messageId),
   FOREIGN KEY(userId) REFERENCES user_account(userId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY(threadId) REFERENCES discussion_thread(threadId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY(messageId) REFERENCES message(messageId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE message_received(
   userId INT,
   messageId INT,
   PRIMARY KEY(userId, messageId),
   FOREIGN KEY(userId) REFERENCES user_account(userId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY(messageId) REFERENCES message(messageId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE message_read(
   userId INT,
   messageId INT,
   PRIMARY KEY(userId, messageId),
   FOREIGN KEY(userId) REFERENCES user_account(userId) ON UPDATE CASCADE ON DELETE CASCADE,
   FOREIGN KEY(messageId) REFERENCES message(messageId) ON UPDATE CASCADE ON DELETE CASCADE
);


INSERT INTO `user_account` (`username`, `password`, `name`, `firstname`, `campus_user`)
    VALUES
    ('ah02', 'pass', 'Hamza', 'Asma', 0),
    ('bb03', 'pass', 'Besseghieur', 'Bilel', 0),
    ('ch01', 'pass', 'Monsieur Le', 'Chauffagiste', 1),
    ('km01', 'pass', 'Maachou', 'Khalil', 0),
    ('pm01', 'pass', 'Senior', 'Plombier', 1),
    ('pm02', 'pass', 'Padawan', 'Plombier', 1),
    ('bch01', 'pass', 'BCH', 'King', 0);


INSERT INTO user_group (`groupName`, `campus_group`)
    VALUES
    ('Chauffagiste', 1),
    ('Enseignant', 0),
    ('Plombier', 1),
    ('TDA2_INFO', 0);

INSERT INTO belong (`userId`, `groupId`)
    VALUES
    (1, 4),
    (2, 4),
    (3, 1),
    (4, 4),
    (5, 3),
    (6, 3),
    (7, 2),
    (7, 4);


INSERT INTO discussion_thread (title, groupId, userId)
	VALUES
    ('Chauffage U6-319', 1, 4),
    ('Sanitaire cassé', 3, 2),
    ('Fermeture sanitaire Prof', 2, 6),
    ('URGENT À LIRE !!!', 3, 7);

INSERT INTO message (message_timestamp, text, threadId)
	VALUES
    ('2021-12-12 17:45:32', 'Bonjour les chauffage en salle U6-319 ne fonctionnent pas !', 1),
    ('2021-10-24 12:24:09', 'Bonjour, il n\'y a plus d\'eau dans les toilettes homme du second \'étage du batiment U6.', 2),
    ('2021-11-30 09:49:02', 'Bonjour suite à un incident de plomberie dans les sanitaires enseignants, ces derniers seront momentanément indisponible pour réparation, nous vous tiendrons donc au courant de la réouverture des sanitaires', 3),
    ('2022-01-09 21:28:21', 'Bonjour,\nil n\'a absolument aucun problème,\nmerci de votre attention.\n\nBCH', 4);

INSERT INTO send (userId, threadId, messageId)
	VALUES
    (2, 2, 2),
    (4, 1, 1),
    (6, 3, 3),
    (7, 4, 4);

INSERT INTO message_received (userId, messageId)
	VALUES
    (2, 2),
    (3, 1),
    (4, 1),
    (5, 2),
    (6, 2),
    (6, 3),
    (7, 3),
    (7, 4);

INSERT INTO message_read (userId, messageId)
	VALUES
    (2, 2),
    (4, 1),
    (5, 2),
    (6, 3),
    (7, 4);
