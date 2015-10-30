CREATE DATABASE IF NOT EXISTS og;

USE og;

DROP TABLE IF EXISTS user_profile; 
CREATE TABLE user_profile (
  id INTEGER NOT NULL AUTO_INCREMENT,
  user_id varchar(255) NOT NULL,
  profile TEXT NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_key (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
DROP TABLE IF EXISTS user_login;
CREATE TABLE user_login (
  id INTEGER NOT NULL AUTO_INCREMENT,
  active char(1) NOT NULL DEFAULT 'Y',
  user_id varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  verified char(1) default 'N',
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_key (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS idsequence;
CREATE TABLE idsequence (
  sequencename varchar(64) NOT NULL,
  sequenceno INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY (sequencename)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Sequence Generator';


