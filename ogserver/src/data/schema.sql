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


DROP TABLE IF EXISTS proposal;
CREATE TABLE proposal(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalid varchar(64) NOT NULL,
  status char(1) NOT NULL DEFAULT 'C',
  createdon timestamp NULL,
  expectedby timestamp NULL,
  completedon timestamp NULL,
  wfstatus varchar(32) NOT NULL,
  wfassignedto varchar(128) NOT NULL,
  wfintime timestamp NOT NULL,
  wfexpectedby timestamp NULL,
  priority char(1) NOT NULL DEFAULT 'N',
  attention char(1) NOT NULL DEFAULT 'N',
  customerid varchar(64) NOT NULL,
  customeremail varchar(255) NOT NULL,
  crmid varchar(64) NULL,
  quoteno varchar(64) NULL,
  modeltype varchar(16) NULL,
  drawingno varchar(64) NULL,
  title varchar(255) NOT NULL,
  designerid varchar(64) NULL,
  designername varchar(64) NULL,
  projectname varchar(64) NULL,
  city varchar(64) NULL,
  salesid varchar(64) NULL,
  salesperson varchar(128) NULL,
  budget int NOT NULL DEFAULT 0,
  proposalamount int NOT NULL DEFAULT 0,
  customernotes TEXT NULL,
  salesnotes TEXT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_proposalid (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Master';

DROP TABLE IF EXISTS proposal_summary;
CREATE TABLE proposal_summary(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type char(1) NOT NULL,
  code varchar(128) NOT NULL,
  proposalcount INTEGER NOT NULL,
  proposalamount INTEGER NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Statistics';


DROP TABLE IF EXISTS proposal_wf_status;
CREATE TABLE proposal_wf_status(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalid varchar(64) NOT NULL,
  status varchar(32) NOT NULL,
  assignedto varchar(128) NULL,
  expintime timestamp NULL,
  expouttime timestamp NULL,
  intime timestamp NOT NULL,
  outtime timestamp NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal workflow status';

DROP TABLE IF EXISTS proposal_comments;
CREATE TABLE proposal_comments(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalid varchar(64) NOT NULL,
  comment TEXT NOT NULL,
  userid varchar(128) NULL,
  username varchar(128) NULL,
  createdon timestamp NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal comments';

DROP TABLE IF EXISTS proposal_documents;
CREATE TABLE proposal_comments(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalid varchar(64) NOT NULL,
  title varchar(255) NOT NULL,
  description TEXT NULL,
  url TEXT NOT NULL,
  userid varchar(128) NOT NULL,
  username varchar(128) NOT NULL,
  uploadedon timestamp NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal documents';


DROP TABLE IF EXISTS user_login;
CREATE TABLE user_login(
  id INTEGER NOT NULL AUTO_INCREMENT,
  email varchar(128) NOT NULL,
  password varchar(128) NOT NULL,
  roles varchar(64) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY email_key (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='User Login Table';


`   `