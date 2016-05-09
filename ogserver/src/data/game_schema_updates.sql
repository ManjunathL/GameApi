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
  status char(10) NOT NULL DEFAULT 'Draft', -- Draft, Submitted, Cancelled
  title varchar(255) NOT NULL,
  version char(10) NOT NULL,
  crmid varchar(64) NULL,
  quoteno varchar(64) NULL,
  customerid varchar(64) NOT NULL,
  cname varchar(64) NOT NULL,
  caddress1 varchar(128) NULL,
  caddress2 varchar(128) NULL,
  caddress3 varchar(128) NULL,
  ccity varchar(64) NULL,
  cemail varchar(128) NOT NULL,
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
  createdon timestamp NULL,
  createdby varchar(64) NULL,
  updatedon timestamp NULL,
  updatedby varchar(64) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Master';

DROP TABLE IF EXISTS product_lineitem;
CREATE TABLE product_lineitem(
  id INTEGER NOT NULL AUTO_INCREMENT,
  active char(1) NOT NULL DEFAULT 'A',
  proposalid INTEGER NOT NULL,
  title varchar(255) NOT NULL DEFAULT 'TITLE',
  type char(16) NOT NULL DEFAULT 'Assembled', --Assembled, Bought, Service, Standard
  kdmax_file varchar(255) NULL,
  product_type varchar(16) NOT NULL DEFAULT 'Kitchen',
  product_id varchar(32) NULL, -- valid only for catalog products
  product_name varchar(128) NULL, -- valid only for catalog products
  carcass_id varchar(8) NOT NULL DEFAULT 'PLY',
  carcass_name varchar(32) NOT NULL DEFAULT 'PLY',
  shutter_id varchar(8) NOT NULL DEFAULT 'MDF',
  shutter_name varchar(32) NOT NULL DEFAULT 'MDF',
  finish_id varchar(8) NOT NULL,
  finish_name varchar(32) NOT NULL,
  color_id varchar(8) NOT NULL,
  color_name varchar(32) NOT NULL,
  make varchar(64) NOT NULL DEFAULT 'HETTICH',
  dimension varchar(64) NULL,
  quantity INTEGER NOT NULL DEFAULT 1,
  amount INTEGER NOT NULL DEFAULT 0,
  notes TEXT NULL,
  moduleJson TEXT NULL,
  addonJson TEXT NULL,
  createdon timestamp NULL,
  createdby varchar(64) NULL,
  updatedon timestamp NULL,
  updatedby varchar(64) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposal_key (proposalid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Products';


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
CREATE TABLE proposal_documents(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalid INTEGER NOT NULL,
  productid INTEGER NOT NULL DEFAULT 0,
  type char(16) NOT NULL, -- KDMax, Floor Plan
  title varchar(255) NOT NULL,
  url TEXT NOT NULL,
  userid varchar(128) NULL,
  username varchar(128) NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalid)
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
  mgcode varchar(64) NOT NULL,
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
  labour_cost DECIMAL(10,2) NOT NULL DEFAULT 0.0,
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
  labour_cost DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  area DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Carcass master';


DROP TABLE IF EXISTS carcass_material_xref;
CREATE TABLE carcass_material_xref(
  id INTEGER NOT NULL AUTO_INCREMENT,
  carcasscode varchar(16) NOT NULL,
  mfcode varchar(16) NOT NULL,
  carcassxcode varchar(16) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (carcasscode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Carcass Material xref';

DROP TABLE IF EXISTS shutter_master;
CREATE TABLE shutter_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  type varchar(16) NOT NULL DEFAULT 'NA',
  title varchar(255) NOT NULL,
  plength INTEGER NOT NULL DEFAULT 0,
  breadth INTEGER NOT NULL DEFAULT 0,
  height INTEGER NOT NULL DEFAULT 0,
  edgebinding varchar(128) NOT NULL,
  quantity INTEGER NOT NULL DEFAULT 0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Shutter master';


DROP TABLE IF EXISTS shutter_mf_xref;
CREATE TABLE shutter_mf_xref(
  id INTEGER NOT NULL AUTO_INCREMENT,
  shuttercode varchar(16) NOT NULL,
  mfcode varchar(16) NOT NULL,
  shutterxcode varchar(16) NOT NULL,
  cuttingl INTEGER NOT NULL DEFAULT 0,
  cuttingb INTEGER NOT NULL DEFAULT 0,
  cuttingt INTEGER NOT NULL DEFAULT 0,
  cuttingarea DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (shuttercode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Shutter Finish xref';

DROP TABLE IF EXISTS mf_code_master;
CREATE TABLE mf_code_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type varchar(2) NOT NULL DEFAULT 'NA', -- M - Material, F- Finish, MF - Material+Finish
  code varchar(16) NOT NULL,
  title varchar(64) NOT NULL,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Material Finish Code Master';


DROP TABLE IF EXISTS mf_rate_master;
CREATE TABLE mf_code_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type char(2) NOT NULL DEFAULT 'NA', -- M - Material, F- Finish, MF - Material+Finish
  mfcode varchar(16) NOT NULL,
  thickness INTEGER NOT NULL DEFAULT 0,
  rate DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (shuttercode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Material Finish Rate Master';


DROP TABLE IF EXISTS loading_factor;
CREATE TABLE loading_factor(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type varchar(16) NOT NULL DEFAULT 'NA',
  title varchar(64) NOT NULL,
  factor DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Loading factors';


DROP TABLE IF EXISTS acs_hw_master;
CREATE TABLE acs_hw_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  type char(1) NOT NULL DEFAULT 'A', -- A - Accessory, H - Hardware
  code varchar(16) NOT NULL,
  catalogcode varchar(16) NOT NULL,
  title varchar(255) NOT NULL,
  make varchar(16) NOT NULL,
  imageurl varchar(255) NOT NULL,
  uom char(1) NOT NULL DEFAULT 'N', -- N Numbers, S Set
  mrp DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY code_key (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Accessory and Hardware master';

