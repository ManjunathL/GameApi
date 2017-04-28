CREATE TABLE `game_user` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`active` CHAR(1) NOT NULL DEFAULT 'A',
	`name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL,
	`role` VARCHAR(255) NOT NULL,
	`phone` VARCHAR(25) NULL DEFAULT NULL,
	`salt` VARCHAR(255) NOT NULL,
	`hash` VARCHAR(255) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	UNIQUE INDEX `unique_email` (`email`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;




-------------------------
--Proposal related tables
-------------------------
CREATE TABLE `proposal` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`status` CHAR(10) NOT NULL DEFAULT 'Active',
	`title` VARCHAR(255) NOT NULL,
	`version` CHAR(10) NULL DEFAULT NULL,
	`crmId` VARCHAR(64) NULL DEFAULT NULL,
	`quoteNo` VARCHAR(64) NULL DEFAULT NULL,
	`customerId` VARCHAR(64) NULL DEFAULT NULL,
	`cname` VARCHAR(64) NULL DEFAULT NULL,
	`caddress1` VARCHAR(128) NULL DEFAULT NULL,
	`caddress2` VARCHAR(128) NULL DEFAULT NULL,
	`caddress3` VARCHAR(128) NULL DEFAULT NULL,
	`ccity` VARCHAR(64) NULL DEFAULT NULL,
	`cemail` VARCHAR(128) NULL DEFAULT NULL,
	`cphone1` VARCHAR(16) NULL DEFAULT NULL,
	`cphone2` VARCHAR(16) NULL DEFAULT NULL,
	`projectName` VARCHAR(128) NULL DEFAULT NULL,
	`paddress1` VARCHAR(128) NULL DEFAULT NULL,
	`paddress2` VARCHAR(128) NULL DEFAULT NULL,
	`pcity` VARCHAR(64) NULL DEFAULT NULL,
	`salesName` VARCHAR(128) NULL DEFAULT NULL,
	`salesEmail` VARCHAR(128) NULL DEFAULT NULL,
	`salesPhone` VARCHAR(16) NULL DEFAULT NULL,
	`designerName` VARCHAR(128) NULL DEFAULT NULL,
	`designerEmail` VARCHAR(128) NULL DEFAULT NULL,
	`designerPhone` VARCHAR(16) NULL DEFAULT NULL,
	`amount` INT(11) NOT NULL DEFAULT '0',
	`folderPath` VARCHAR(255) NULL DEFAULT NULL,
	`createdOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`createdBy` VARCHAR(64) NULL DEFAULT NULL,
	`updatedOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`updatedBy` VARCHAR(64) NULL DEFAULT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`quoteNoNew` VARCHAR(64) NULL DEFAULT NULL,
	`designPartnerName` VARCHAR(128) NULL DEFAULT NULL,
	`designPartnerEmail` VARCHAR(128) NULL DEFAULT NULL,
	`designPartnerPhone` VARCHAR(16) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='Proposal Master'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `proposal_product` (
 `id` INT(11) NOT NULL AUTO_INCREMENT,
 `active` CHAR(1) NOT NULL DEFAULT 'A',
 `proposalId` INT(11) NOT NULL,
 `title` VARCHAR(255) NOT NULL DEFAULT 'TITLE',
 `seq` INT(11) NOT NULL DEFAULT '0',
 `type` CHAR(16) NOT NULL DEFAULT 'CUSTOMIZED',
 `roomCode` VARCHAR(255) NULL DEFAULT NULL,
 `productCategoryCode` VARCHAR(16) NOT NULL DEFAULT 'Kitchen',
 `catalogueId` VARCHAR(32) NULL DEFAULT NULL,
 `catalogueName` VARCHAR(128) NULL DEFAULT NULL,
 `makeTypeCode` CHAR(1) NOT NULL,
 `baseCarcassCode` VARCHAR(32) NOT NULL DEFAULT 'PLY',
 `wallCarcassCode` VARCHAR(8) NOT NULL DEFAULT 'PLY',
 `finishTypeCode` VARCHAR(16) NOT NULL,
 `finishCode` VARCHAR(32) NOT NULL,
 `designCode` VARCHAR(32) NOT NULL,
 `dimension` VARCHAR(64) NULL DEFAULT NULL,
 `quantity` INT(11) NOT NULL DEFAULT '1',
 `amount` DOUBLE NOT NULL DEFAULT '0',
 `quoteFilePath` VARCHAR(255) NULL DEFAULT NULL,
 `modules` LONGTEXT NULL,
 `addons` TEXT NULL,
 `createdOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `createdBy` VARCHAR(64) NULL DEFAULT NULL,
 `updatedOn` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 `updatedBy` VARCHAR(64) NULL DEFAULT NULL,
 `touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 `costWoAccessories` DOUBLE NULL DEFAULT '0',
 `fromVersion` VARCHAR(4) NULL DEFAULT NULL,
 `profit` DOUBLE NULL DEFAULT '0',
 `margin` DOUBLE NULL DEFAULT '0',
 `amountWoTax` DOUBLE NULL DEFAULT '0',
 `manufactureAmount` DOUBLE NULL DEFAULT '0',
 `manualSeq` INT(11) NULL DEFAULT NULL,
 `source` VARCHAR(11) NULL DEFAULT NULL,
 `spaceName` varchar(64) not null default 'default',
 `subSpaceName` varchar(64) not null default 'room',
 `description` varchar(2048) not null default '',
 `imageUrl` varchar(128) not null default '',
PRIMARY KEY (`id`),
 INDEX `proposal_key` (`proposalId`)
)
COMMENT='Proposal Products'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=16326
;


DROP TABLE IF EXISTS proposal_documents;
CREATE TABLE proposal_documents(
  id INTEGER NOT NULL AUTO_INCREMENT,
  proposalId INTEGER NOT NULL,
  productId INTEGER NOT NULL DEFAULT 0,
  title varchar(255) NOT NULL,
  fileName varchar(255) NOT NULL,
  attachmentType varchar(32) NOT NULL,
  uploadedBy varchar(64) NULL,
  uploadedOn timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY proposalid_key (proposalId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal documents';


CREATE TABLE `proposal_addon` (
 `id` INT(11) NOT NULL AUTO_INCREMENT,
 `seq` INT(4) NOT NULL DEFAULT '0',
 `proposalId` INT(11) NOT NULL,
 `fromVersion` VARCHAR(50) NOT NULL,
 `code` VARCHAR(16) NOT NULL,
 `categoryCode` VARCHAR(32) NOT NULL DEFAULT 'NA',
 `productTypeCode` CHAR(32) NOT NULL DEFAULT 'All',
 `productSubtypeCode` CHAR(64) NULL DEFAULT NULL,
 `product` CHAR(32) NOT NULL DEFAULT 'All',
 `brandCode` VARCHAR(32) NULL DEFAULT NULL,
 `catalogueCode` VARCHAR(32) NOT NULL,
 `title` VARCHAR(255) NOT NULL,
 `rate` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
 `quantity` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
 `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
 `uom` CHAR(10) NOT NULL DEFAULT 'N',
 `updatedBy` VARCHAR(64) NULL DEFAULT NULL,
 `touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 `profit` DOUBLE NULL DEFAULT NULL,
 `margin` DOUBLE NULL DEFAULT NULL,
 `remarks` VARCHAR(255) NULL DEFAULT NULL,
 `sourcePrice` DECIMAL(10,2) NULL DEFAULT NULL,
 `amountWOTax` DECIMAL(10,2) NULL DEFAULT NULL,
 `spaceName` varchar(64) not null default 'default',
 `subSpaceName` varchar(64) not null default 'room',
 `description` varchar(2048) not null default '',
 `imageUrl` varchar(128) not null default '',
 PRIMARY KEY (`id`),
 INDEX `proposalid_key` (`proposalId`)
)
COMMENT='Proposal Addons'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3571
;

CREATE TABLE `module_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(255) NOT NULL,
	`description` VARCHAR(255) NOT NULL,
	`imagePath` VARCHAR(255) NOT NULL,
	`width` INT(11) NOT NULL DEFAULT '0',
	`depth` INT(11) NOT NULL DEFAULT '0',
	`height` INT(11) NOT NULL DEFAULT '0',
	`dimension` VARCHAR(32) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`moduleType` VARCHAR(2) NOT NULL COMMENT 'standard - S and non standard/customized - C',
	`moduleCategory` VARCHAR(64) NOT NULL,
	`productCategory` VARCHAR(24) NULL DEFAULT 'NULL',
	`material` CHAR(8) NULL DEFAULT NULL,
	`unitType` CHAR(16) NULL DEFAULT NULL,
	`accessoryPackDefault` CHAR(4) NULL DEFAULT NULL,
	`remarks` VARCHAR(64) NULL DEFAULT 'Add Remarks',
	PRIMARY KEY (`id`),
	INDEX `code_key` (`code`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



CREATE TABLE `module_components` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`modulecode` VARCHAR(32) NULL DEFAULT NULL,
	`comptype` CHAR(1) NOT NULL,
	`compcode` VARCHAR(32) NOT NULL,
	`quantity` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`quantityFlag` CHAR(1) NULL DEFAULT NULL,
	`quantityFormula` CHAR(5) NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	INDEX `mc_key` (`modulecode`)
)
COMMENT='Module to Component mapping'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `code_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`lookupType` CHAR(16) NULL DEFAULT NULL,
	`levelType` VARCHAR(1) NOT NULL DEFAULT 'A',
	`additionalType` VARCHAR(16) NULL DEFAULT NULL,
	`code` CHAR(64) NULL DEFAULT NULL,
	`title` VARCHAR(64) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`code`)
)
COMMENT='Code Master'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



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


--------------------------------------------------------------------------------------------------------------------

-- $$$$$$$$$$ NEW TABLES $$$$$$$$$$$$$$$$$

DROP TABLE IF EXISTS carcass_master;
CREATE TABLE carcass_master(
  id INTEGER NOT NULL AUTO_INCREMENT,
  code varchar(16) NOT NULL,
  type char(1) NOT NULL, -- Left, Right, Top, Bottom
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


---------------------------------------------------------------------------------------------------------------

CREATE TABLE `acc_hw_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`type` CHAR(1) NOT NULL DEFAULT 'A',
	`code` VARCHAR(16) NOT NULL,
	`catalogCode` VARCHAR(16) NOT NULL,
	`title` VARCHAR(255) NOT NULL,
	`make` VARCHAR(16) NOT NULL,
	`category` VARCHAR(32) NULL DEFAULT NULL,
	`imagePath` VARCHAR(255) NULL DEFAULT NULL,
	`uom` CHAR(10) NOT NULL DEFAULT 'N',
	`cp` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`mrp` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`msp` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`code`)
)
COMMENT='Accessory and Hardware master'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `addon_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(16) NOT NULL,
	`categoryCode` VARCHAR(32) NOT NULL DEFAULT 'NA',
	`roomCode` VARCHAR(32) NOT NULL DEFAULT 'All',
	`productTypeCode` CHAR(32) NOT NULL DEFAULT 'All',
	`productSubtypeCode` CHAR(32) NOT NULL DEFAULT 'All',
	`brandCode` VARCHAR(32) NULL DEFAULT NULL,
	`product` VARCHAR(32) NULL DEFAULT NULL,
	`catalogueCode` VARCHAR(32) NULL DEFAULT NULL,
	`rateReadOnly` TINYINT(1) NULL DEFAULT NULL,
	`title` VARCHAR(255) NULL DEFAULT NULL,
	`rate` DECIMAL(10,2) NULL DEFAULT '0.00',
	`mrp` DECIMAL(10,2) NULL DEFAULT '0.00',
	`dealerPrice` DOUBLE NULL DEFAULT '0',
	`uom` CHAR(5) NOT NULL DEFAULT 'N',
	`imagePath` VARCHAR(255) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`code`)
)
COMMENT='Addon Master'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



----------------------------------------------------------------------------------------------------------------

CREATE TABLE `acc_pack_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(16) NOT NULL,
	`title` VARCHAR(255) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`code`)
)
COMMENT='Accessory pack master'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



----------------------------------------------------------------------------------------------------------------------

CREATE TABLE `acc_pack_components` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`apcode` VARCHAR(16) NOT NULL,
	`type` CHAR(1) NOT NULL,
	`code` VARCHAR(16) NOT NULL,
	`qty` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`apcode`)
)
COMMENT='Accessory pack components'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



----------------------------------------------------------------------------------------------------------------

CREATE TABLE `module_acc_pack` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`apcode` VARCHAR(16) NOT NULL,
	`mgcode` VARCHAR(32) NULL DEFAULT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`mgcode`)
)
COMMENT='module accessory pack'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;




CREATE TABLE `accpack_addon_map` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`apcode` VARCHAR(16) NOT NULL,
	`addoncode` VARCHAR(16) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`),
	INDEX `code_key` (`apcode`)
)
COMMENT='addon accessory map'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



--DROP TABLE IF EXISTS module_category_master;
--CREATE TABLE module_category_master(
--  id INTEGER NOT NULL AUTO_INCREMENT,
--  moduleType varchar(4) NOT NULL,
--  moduleCategory varchar(64) NOT NULL,
--  moduleCode varchar(32) NOT NULL,
--  touchtime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--  PRIMARY KEY (id),
--  KEY code_key (moduleCode)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='module Category Master';

CREATE TABLE `panel_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`code` VARCHAR(16) NOT NULL,
	`type` CHAR(1) NOT NULL,
	`side` CHAR(1) NOT NULL,
	`title` VARCHAR(255) NOT NULL,
	`subtitle` VARCHAR(64) NOT NULL DEFAULT 'NA',
	`plength` INT(11) NOT NULL DEFAULT '0',
	`breadth` INT(11) NOT NULL DEFAULT '0',
	`thickness` INT(11) NOT NULL DEFAULT '0',
	`edgebinding` VARCHAR(128) NOT NULL,
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`dimensionFormula` CHAR(4) NULL DEFAULT NULL,
	`exposed` CHAR(1) NULL DEFAULT NULL COMMENT 'D or S',
	PRIMARY KEY (`id`),
	INDEX `code_key` (`code`)
)
COMMENT='Panel master'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;



CREATE TABLE `city_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`city` VARCHAR(50) NOT NULL DEFAULT '0',
	`curmonth` INT(2) NOT NULL,
	`proposalId` INT(11) NOT NULL DEFAULT '0',
	`quoteNo` VARCHAR(50) NOT NULL DEFAULT '0',
	`touchtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`cityLocked` VARCHAR(4) NULL DEFAULT 'Yes',
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;


CREATE TABLE `version_master` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`proposalId` INT(11) NOT NULL,
	`version` VARCHAR(50) NOT NULL,
	`fromVersion` VARCHAR(4) NULL DEFAULT NULL,
	`title` VARCHAR(255) NOT NULL,
	`finalAmount` DOUBLE NOT NULL,
	`status` CHAR(64) NOT NULL,
	`internalStatus` CHAR(64) NULL DEFAULT NULL,
	`date` VARCHAR(64) NULL DEFAULT NULL,
	`remarks` VARCHAR(255) NULL DEFAULT NULL,
	`discountPercentage` DOUBLE NOT NULL,
	`discountAmount` DOUBLE NOT NULL,
	`amount` DOUBLE NOT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

delimiter $$
drop function if exists get_addon_price;
CREATE FUNCTION get_addon_price (code varchar(30), priceDate date) returns double
BEGIN
declare addon_price double;
select price into addon_price from price_master where rateId = code and priceDate between fromDate and toDate;
return addon_price;
END$$
