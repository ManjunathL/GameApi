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
  crmid varchar(64) NULL,
  quoteno varchar(64) NULL,
  customerid varchar(64) NULL,
  cname varchar(64) NULL,
  caddress1 varchar(128) NULL,
  caddress2 varchar(128) NULL,
  caddress3 varchar(128) NULL,
  ccity varchar(64) NULL,
  cemail varchar(128) NULL,
  cphone1 varchar(16) NULL,
  cphone2 varchar(16) NULL,
  projectname varchar(128) NULL,
  pmodeltype varchar(64) NULL,
  pdrawingno varchar(64) NULL,
  paddress1 varchar(128) NULL,
  paddress2 varchar(128) NULL,
  pcity varchar(64) NULL,
  salesperson varchar(128) NULL,
  salesemail varchar(128) NULL,
  salescontact varchar(16) NULL,
  designername varchar(128) NULL,
  designeremail varchar(128) NULL,
  designercontact varchar(16) NULL,
  totalamount int NOT NULL DEFAULT 0,
  docsfolder varchar(255) NULL,
  createdon timestamp NULL,
  createdby varchar(64) NULL,
  updatedon timestamp NULL,
  updatedby varchar(64) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Master';

DROP TABLE IF EXISTS proposal_product;
CREATE TABLE proposal_product(
  id INTEGER NOT NULL AUTO_INCREMENT,
  active char(1) NOT NULL DEFAULT 'A',
  proposalid INTEGER NOT NULL,
  title varchar(255) NOT NULL DEFAULT 'TITLE',
  type char(16) NOT NULL DEFAULT 'Assembled', -- Assembled, Bought, Service, Standard
  kdmax_file varchar(255) NULL,
  room varchar(16) NOT NULL DEFAULT 'Kitchen',
  product_type varchar(16) NOT NULL DEFAULT 'Kitchen',
  product_id varchar(32) NULL, -- valid only for catalog products
  product_name varchar(128) NULL, -- valid only for catalog products
  carcass_code varchar(8) NOT NULL DEFAULT 'PLY',
  finish_type varchar(16) NOT NULL,
  finish_code varchar(8) NOT NULL,
  make_type char(1) NOT NULL,
  dimension varchar(64) NULL,
  quantity INTEGER NOT NULL DEFAULT 1,
  amount INTEGER NOT NULL DEFAULT 0,
  notes TEXT NULL,
  modules TEXT NULL,
  addons TEXT NULL,
  createdon timestamp NULL,
  createdby varchar(64) NULL,
  updatedon timestamp NULL,
  updatedby varchar(64) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposal_key (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Products';


DROP TABLE IF EXISTS proposal_documents;
CREATE TABLE proposal_documents(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalid INTEGER NOT NULL,
  productid INTEGER NOT NULL DEFAULT 0,
  type char(16) NOT NULL, -- KDMax, Floor Plan
  title varchar(255) NOT NULL,
  url TEXT NOT NULL,
  userid varchar(128) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal documents';

DROP TABLE IF EXISTS kdmax_def_map;
CREATE TABLE kdmax_def_map(
  id INTEGER NOT NULL AUTO_INCREMENT,
  kdmcode varchar(16) NOT NULL,
  kdmdefcode varchar(16) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY kdmcode_key (kdmcode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='KDMax to KDMax default Module mapping';


DROP TABLE IF EXISTS kdmax_mg_map;
CREATE TABLE kdmax_mg_map(
  id INTEGER NOT NULL AUTO_INCREMENT,
  kdmcode varchar(16) NOT NULL,
  mgcode varchar(16) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY kdmcode_key (kdmcode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='KDMax to Mygubbi Module mapping';

DROP TABLE IF EXISTS module_master;
CREATE TABLE module_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  type varchar(16) NOT NULL DEFAULT 'NA',
  title varchar(255) NOT NULL,
  imageurl varchar(255) NOT NULL,
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
  type varchar(16) NOT NULL DEFAULT 'NA',
  groupname varchar(16) NOT NULL DEFAULT 'NA',
  title varchar(255) NOT NULL,
  plength INTEGER NOT NULL DEFAULT 0,
  breadth INTEGER NOT NULL DEFAULT 0,
  thickness INTEGER NOT NULL DEFAULT 0,
  edgebinding varchar(128) NOT NULL,
  area DECIMAL(10,10) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Carcass master';


DROP TABLE IF EXISTS shutter_master;
CREATE TABLE shutter_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  type varchar(16) NOT NULL DEFAULT 'NA',
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
  type varchar(2) NOT NULL DEFAULT 'NA', -- CM - Carcass Material, SF- Shutter Finish, FT - Finish Types
  groupid varchar(1) NOT NULL DEFAULT 'A', -- P - Product, M - Module, A - All
  code varchar(16) NOT NULL,
  title varchar(64) NOT NULL,
  finishType varchar(16) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Code Master';


DROP TABLE IF EXISTS acc_hw_master;
CREATE TABLE acc_hw_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type char(1) NOT NULL DEFAULT 'A', -- A - Accessory, H - Hardware
  code varchar(16) NOT NULL,
  catalogcode varchar(16) NOT NULL,
  title varchar(255) NOT NULL,
  maketype char(1) NOT NULL, -- Economy, Standard, Premium
  make varchar(16) NOT NULL,
  imageurl varchar(255) NOT NULL,
  uom char(1) NOT NULL DEFAULT 'N', -- N Numbers, S Set
  mrp DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Accessory and Hardware master';

DROP TABLE IF EXISTS addon_master;
CREATE TABLE addon_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  producttype char(1) NOT NULL DEFAULT 'A', -- K - Kitchen, W- Wardrobe
  addontype char(1) NOT NULL DEFAULT 'A', -- A Accessory, Service etc
  code varchar(16) NOT NULL,
  catalogcode varchar(16) NOT NULL,
  title varchar(255) NOT NULL,
  make varchar(16) NOT NULL,
  imageurl varchar(255) NOT NULL,
  uom char(1) NOT NULL DEFAULT 'N', -- N Numbers, S Set
  rate DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Addon Master';
