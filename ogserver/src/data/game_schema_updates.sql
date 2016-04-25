DROP TABLE IF EXISTS game_user;
CREATE TABLE game_user (
  id INTEGER NOT NULL AUTO_INCREMENT,
  active char(1) NOT NULL DEFAULT 'A',
  email varchar(255) NOT NULL,
  role varchar(255) NOT NULL,
  salt varchar(255) NOT NULL,
  hash varchar(255) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

