DROP TABLE IF EXISTS game_user;
CREATE TABLE game_user (
  id INTEGER NOT NULL AUTO_INCREMENT,
  active char(1) NOT NULL DEFAULT 'A',
  name varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  role varchar(255) NOT NULL,
  phone varchar(25) NULL,
  salt varchar(255) NOT NULL,
  hash varchar(255) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY unique_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-------------------------
--Proposal related tables
-------------------------
DROP TABLE IF EXISTS proposal;
CREATE TABLE proposal(
  id INTEGER NOT NULL AUTO_INCREMENT,
  status char(10) NOT NULL DEFAULT 'Active', -- Active, Submitted, Cancelled
  title varchar(255) NOT NULL,
  version char(10) NULL,
  crmId varchar(64) NULL,
  quoteNo varchar(64) NULL,
  customerId varchar(64) NULL,
  cname varchar(64) NULL,
  caddress1 varchar(128) NULL,
  caddress2 varchar(128) NULL,
  caddress3 varchar(128) NULL,
  ccity varchar(64) NULL,
  cemail varchar(128) NULL,
  cphone1 varchar(16) NULL,
  cphone2 varchar(16) NULL,
  projectName varchar(128) NULL,
  paddress1 varchar(128) NULL,
  paddress2 varchar(128) NULL,
  pcity varchar(64) NULL,
  salesName varchar(128) NULL,
  salesEmail varchar(128) NULL,
  salesPhone varchar(16) NULL,
  designerName varchar(128) NULL,
  designerEmail varchar(128) NULL,
  designerPhone varchar(16) NULL,
  amount int NOT NULL DEFAULT 0,
  folderPath varchar(255) NULL,
  createdOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  createdBy varchar(64) NULL,
  updatedOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updatedBy varchar(64) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Master';


DROP TABLE IF EXISTS proposal_product;
CREATE TABLE proposal_product(
  id INTEGER NOT NULL AUTO_INCREMENT,
  active char(1) NOT NULL DEFAULT 'A',
  proposalId INTEGER NOT NULL,
  title varchar(255) NOT NULL DEFAULT 'TITLE',
  seq int NOT NULL DEFAULT 0,
  type char(16) NOT NULL DEFAULT 'CUSTOMIZED',
  roomCode varchar(16) NOT NULL DEFAULT 'Kitchen',
  productCategoryCode varchar(16) NOT NULL DEFAULT 'Kitchen',
  catalogueId varchar(32) NULL,
  catalogueName varchar(128) NULL,
  makeTypeCode char(1) NOT NULL,
  baseCarcassCode varchar(8) NOT NULL DEFAULT 'PLY',
  wallCarcassCode varchar(8) NOT NULL DEFAULT 'PLY',
  finishTypeCode varchar(16) NOT NULL,
  finishCode varchar(8) NOT NULL,
  designCode varchar(32) NOT NULL,
  dimension varchar(64) NULL,
  quantity INTEGER NOT NULL DEFAULT 1,
  amount DOUBLE NOT NULL DEFAULT 0,
  quoteFilePath varchar(255) NULL,
  modules TEXT NULL,
  addons TEXT NULL,
  createdOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  createdBy varchar(64) NULL,
  updatedOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updatedBy varchar(64) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposal_key (proposalId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Products';


DROP TABLE IF EXISTS proposal_documents;
CREATE TABLE proposal_documents(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalId INTEGER NOT NULL,
  productId INTEGER NOT NULL DEFAULT 0,
  title varchar(255) NOT NULL,
  fileName varchar(255) NOT NULL,
  uploadedBy varchar(64) NULL,
  uploadedOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal documents';

DROP TABLE IF EXISTS kdmax_def_map;
CREATE TABLE kdmax_def_map(
  id INTEGER NOT NULL AUTO_INCREMENT,
  kdmcode varchar(64) NOT NULL,
  kdmdefcode varchar(64) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY kdmcode_key (kdmcode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='KDMax to KDMax default Module mapping';


DROP TABLE IF EXISTS kdmax_mg_map;
CREATE TABLE kdmax_mg_map(
  id INTEGER NOT NULL AUTO_INCREMENT,
  kdmcode varchar(64) NOT NULL,
  mgcode varchar(32) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY kdmcode_key (kdmcode, mgcode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='KDMax to Mygubbi Module mapping';

DROP TABLE IF EXISTS module_master;
CREATE TABLE module_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  description varchar(255) NOT NULL,
  imagePath varchar(255) NOT NULL,
  carcassCode char(8) NULL,
  width INTEGER NOT NULL DEFAULT 0,
  depth INTEGER NOT NULL DEFAULT 0,
  height INTEGER NOT NULL DEFAULT 0,
  dimension varchar(32) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module master';

DROP TABLE IF EXISTS module_components;
CREATE TABLE module_components(
  id INTEGER NOT NULL AUTO_INCREMENT,
  modulecode varchar(16) NOT NULL,
  comptype char(1) NOT NULL, -- C - Carcass, S- Shutter, A - Accessory, H- Hardware
  compcode varchar(32) NOT NULL, -- Component code - Carcass, Shutter, Hardware, Accessory
  quantity INTEGER NOT NULL DEFAULT 0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY mc_key (modulecode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module to Component mapping';

DROP TABLE IF EXISTS carcass_master;
CREATE TABLE carcass_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  title varchar(255) NOT NULL,
  plength INTEGER NOT NULL DEFAULT 0,
  breadth INTEGER NOT NULL DEFAULT 0,
  thickness INTEGER NOT NULL DEFAULT 0,
  edgebinding varchar(128) NOT NULL,
  area DOUBLE NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Carcass master';


DROP TABLE IF EXISTS shutter_master;
CREATE TABLE shutter_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  type varchar(64) NOT NULL DEFAULT 'NA',
  title varchar(255) NOT NULL,
  plength INTEGER NOT NULL DEFAULT 0,
  breadth INTEGER NOT NULL DEFAULT 0,
  thickness INTEGER NOT NULL DEFAULT 0,
  edgebinding varchar(128) NOT NULL,
  quantity INTEGER NOT NULL DEFAULT 0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Shutter master';


DROP TABLE IF EXISTS code_master;
CREATE TABLE code_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  lookupType varchar(16) NOT NULL DEFAULT 'NA', -- CM - Carcass Material, SF- Shutter Finish, FT - Finish Types
  levelType varchar(1) NOT NULL DEFAULT 'A', -- P - Product, M - Module, A - All
  additionalType varchar(16) NULL,
  code varchar(16) NOT NULL,
  title varchar(64) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Code Master';

DROP TABLE IF EXISTS finish_master;
CREATE TABLE finish_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  costCode char(5) NOT NULL DEFAULT 'NA',
  finishType varchar(64) NOT NULL DEFAULT 'NA',
  finishMaterial varchar(16) NOT NULL,
  design char(1) NOT NULL DEFAULT 'N',
  shutterMaterial char(5) NOT NULL DEFAULT 'N',
  finishCode char(5) NOT NULL DEFAULT 'NA',
  colorGroupCode char(3) NOT NULL DEFAULT 'NA',
  cuttingOffset int NOT NULL DEFAULT 0,
  title varchar(64) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (finishCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Finish Master';

DROP TABLE IF EXISTS color_master;
CREATE TABLE color_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  colorGroupCode char(3) NOT NULL DEFAULT 'NA',
  code varchar(64) NOT NULL DEFAULT 'NA',
  imagePath varchar(255) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Color Master';


DROP TABLE IF EXISTS acc_hw_master;
CREATE TABLE acc_hw_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type char(1) NOT NULL DEFAULT 'A', -- A - Accessory, H - Hardware
  code varchar(16) NOT NULL,
  catalogCode varchar(16) NOT NULL,
  title varchar(255) NOT NULL,
  makeType char(1) NOT NULL, -- Economy, Standard, Premium
  make varchar(16) NOT NULL,
  imagePath varchar(255) NOT NULL,
  uom char(10) NOT NULL DEFAULT 'N', -- N Numbers, S Set
  mrp DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Accessory and Hardware master';

DROP TABLE IF EXISTS addon_master;
CREATE TABLE addon_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  categoryCode varchar(32) NOT NULL DEFAULT 'NA',
  roomCode varchar(32) NOT NULL DEFAULT 'All',
  productTypeCode char(32) NOT NULL DEFAULT 'All',
  brandCode varchar(32) NULL,
  catalogueCode varchar(32) NOT NULL,
  rateReadOnly boolean NOT NULL,
  title varchar(255) NOT NULL,
  rate DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  mrp DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  uom char(5) NOT NULL DEFAULT 'N', -- N Numbers, S Set
  imagePath varchar(255) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Addon Master';

