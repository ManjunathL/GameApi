CREATE DATABASE IF NOT EXISTS mg;

USE mg;

DROP TABLE IF EXISTS user_profile; 
CREATE TABLE user_profile (
  id INTEGER NOT NULL AUTO_INCREMENT,
  fbid varchar(64) NOT NULL,
  active char(1) NOT NULL DEFAULT 'A',
  email varchar(255) NOT NULL,
  profile TEXT NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
DROP TABLE IF EXISTS event_log;
CREATE TABLE event_log (
  id INTEGER NOT NULL AUTO_INCREMENT,
  fbid varchar(64) NOT NULL,
  event_type varchar(64) NOT NULL,
  event_data TEXT NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_fbid (fbid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS event_errors;
CREATE TABLE event_errors (
  id INTEGER NOT NULL AUTO_INCREMENT,
  fbid varchar(64) NOT NULL,
  event_type varchar(64) NOT NULL,
  event_data TEXT NOT NULL,
  reason varchar(255) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS enquiries;
CREATE TABLE enquiries (
  id INTEGER NOT NULL AUTO_INCREMENT,
  fbid varchar(64) NOT NULL,
  uid varchar(64) NOT NULL,
  email varchar(255) NOT NULL,
  contactNumber varchar(16) NULL,
  fullName varchar(128) NULL,
  propertyName varchar(255) NULL,
  requirements TEXT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS product;
CREATE TABLE product (
  id INTEGER NOT NULL AUTO_INCREMENT,
  productId varchar(64) NOT NULL,
  name varchar(64) NOT NULL,
  description varchar(2048) NOT NULL,
  category varchar(64) NULL,
  categoryId varchar(16) NULL,
  subcategory varchar(64) NULL,
  subcategoryId varchar(16) NULL,
  styleId varchar(24) NULL,
  productShortJson TEXT NULL,
  productJson TEXT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_productid (productId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS shortlisted;
CREATE TABLE shortlisted (
  id INTEGER NOT NULL AUTO_INCREMENT,
  uid varchar(64) NOT NULL,
  email varchar(255) NOT NULL,
  productId varchar(64) NOT NULL,
  product TEXT NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY email (email),
  KEY uid (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS idsequence;
CREATE TABLE idsequence (
  sequencename varchar(64) NOT NULL,
  sequenceno INTEGER NOT NULL DEFAULT 1,
  PRIMARY KEY (sequencename)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Sequence Generator';

DROP TABLE IF EXISTS blog;
CREATE TABLE blog (
  id INTEGER NOT NULL AUTO_INCREMENT,
  blogId varchar(256) NOT NULL,
  tags TEXT NOT NULL,
  blog_categories TEXT NULL,
  blogJson TEXT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_blogId (blogId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



