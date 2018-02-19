-- --------------------------------------------------------
-- Host:                         106.51.67.223
-- Server version:               5.6.33-0ubuntu0.14.04.1 - (Ubuntu)
-- Server OS:                    debian-linux-gnu
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table prod_copy.accpack_addon_map
DROP TABLE IF EXISTS `accpack_addon_map`;
CREATE TABLE IF NOT EXISTS `accpack_addon_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apcode` varchar(16) NOT NULL,
  `addoncode` varchar(16) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`apcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='addon accessory map';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.acc_hw_master
DROP TABLE IF EXISTS `acc_hw_master`;
CREATE TABLE IF NOT EXISTS `acc_hw_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` char(1) NOT NULL DEFAULT 'A',
  `code` varchar(16) NOT NULL,
  `ERPCode` varchar(16) DEFAULT NULL,
  `catalogCode` varchar(50) NOT NULL,
  `boqDisplayOrder` int(11) NOT NULL DEFAULT '1000',
  `procurementType` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `make` varchar(16) NOT NULL,
  `category` varchar(32) DEFAULT NULL,
  `imagePath` varchar(255) DEFAULT NULL,
  `uom` char(10) NOT NULL DEFAULT 'NA',
  `cp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `msp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Accessory and Hardware master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.acc_pack_components
DROP TABLE IF EXISTS `acc_pack_components`;
CREATE TABLE IF NOT EXISTS `acc_pack_components` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apcode` varchar(32) NOT NULL,
  `type` char(4) NOT NULL DEFAULT 'HI',
  `code` varchar(16) NOT NULL,
  `qty` decimal(10,2) NOT NULL DEFAULT '0.00',
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`apcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Accessory pack components';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.acc_pack_master
DROP TABLE IF EXISTS `acc_pack_master`;
CREATE TABLE IF NOT EXISTS `acc_pack_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `title` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Accessory pack master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.addon_master
DROP TABLE IF EXISTS `addon_master`;
CREATE TABLE IF NOT EXISTS `addon_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `categoryCode` varchar(32) NOT NULL DEFAULT 'NA',
  `roomCode` varchar(32) NOT NULL DEFAULT 'All',
  `productTypeCode` char(64) NOT NULL DEFAULT 'All',
  `productSubtypeCode` char(64) NOT NULL DEFAULT 'All',
  `brandCode` varchar(32) DEFAULT NULL,
  `product` varchar(64) DEFAULT NULL,
  `catalogueCode` varchar(32) DEFAULT NULL,
  `rateReadOnly` tinyint(1) NOT NULL DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `rate` decimal(10,2) DEFAULT '0.00',
  `mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `dealerPrice` double DEFAULT '0',
  `uom` char(5) NOT NULL DEFAULT 'N',
  `imagePath` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `scopeDisplayFlag` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Addon Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.areference
DROP TABLE IF EXISTS `areference`;
CREATE TABLE IF NOT EXISTS `areference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `code_key` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='areference';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.audit_master
DROP TABLE IF EXISTS `audit_master`;
CREATE TABLE IF NOT EXISTS `audit_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `priceDate` datetime NOT NULL,
  `oldAmountProduct` double DEFAULT NULL,
  `oldAmountAddon` double DEFAULT NULL,
  `newAmountProduct` double DEFAULT NULL,
  `newAmountAddon` double DEFAULT NULL,
  `updateDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.blog
DROP TABLE IF EXISTS `blog`;
CREATE TABLE IF NOT EXISTS `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blogId` varchar(255) NOT NULL,
  `tags` text NOT NULL,
  `blog_categories` text,
  `blogJson` text,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_blogId` (`blogId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.blog_bkp_210717
DROP TABLE IF EXISTS `blog_bkp_210717`;
CREATE TABLE IF NOT EXISTS `blog_bkp_210717` (
  `id` int(11) NOT NULL DEFAULT '0',
  `blogId` varchar(255) CHARACTER SET utf8 NOT NULL,
  `tags` text CHARACTER SET utf8 NOT NULL,
  `blog_categories` text CHARACTER SET utf8,
  `blogJson` text CHARACTER SET utf8,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.breference
DROP TABLE IF EXISTS `breference`;
CREATE TABLE IF NOT EXISTS `breference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='breference';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.carcass_master
DROP TABLE IF EXISTS `carcass_master`;
CREATE TABLE IF NOT EXISTS `carcass_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `type` char(1) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `plength` int(11) NOT NULL DEFAULT '0',
  `breadth` int(11) NOT NULL DEFAULT '0',
  `thickness` int(11) NOT NULL DEFAULT '0',
  `edgebinding` varchar(128) NOT NULL,
  `area` double NOT NULL DEFAULT '0',
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='carcass master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.categorySpaceMap
DROP TABLE IF EXISTS `categorySpaceMap`;
CREATE TABLE IF NOT EXISTS `categorySpaceMap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customAddoncode` varchar(64) NOT NULL,
  `spaceType` varchar(64) DEFAULT NULL,
  `category` varchar(128) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_addonCode` (`customAddoncode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.city_master
DROP TABLE IF EXISTS `city_master`;
CREATE TABLE IF NOT EXISTS `city_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(50) NOT NULL DEFAULT '0',
  `curmonth` int(2) NOT NULL,
  `proposalId` int(11) NOT NULL DEFAULT '0',
  `quoteNo` varchar(50) NOT NULL DEFAULT '0',
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cityLocked` varchar(4) DEFAULT 'Yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.code_master
DROP TABLE IF EXISTS `code_master`;
CREATE TABLE IF NOT EXISTS `code_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lookupType` char(32) DEFAULT NULL,
  `levelType` varchar(1) NOT NULL DEFAULT 'A',
  `additionalType` varchar(16) DEFAULT NULL,
  `code` char(64) DEFAULT NULL,
  `title` varchar(64) NOT NULL,
  `seq` int(11) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Code Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.color_master
DROP TABLE IF EXISTS `color_master`;
CREATE TABLE IF NOT EXISTS `color_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `colorGroupCode` varchar(50) NOT NULL DEFAULT 'NA',
  `code` varchar(64) NOT NULL DEFAULT 'NA',
  `imagePath` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fromDate` datetime DEFAULT NULL,
  `toDate` datetime DEFAULT NULL,
  `priceCode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Color Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.customer_master
DROP TABLE IF EXISTS `customer_master`;
CREATE TABLE IF NOT EXISTS `customer_master` (
  `customerId` int(11) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `phone` int(15) NOT NULL,
  `alt_phone` int(15) NOT NULL,
  `email` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.design_master
DROP TABLE IF EXISTS `design_master`;
CREATE TABLE IF NOT EXISTS `design_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `spaceName` varchar(32) NOT NULL,
  `webDisplayName` varchar(32) DEFAULT NULL,
  `dFileLocation` varchar(128) DEFAULT NULL,
  `primaryImageLocation` varchar(128) NOT NULL,
  `secondaryImageLocationJson` text NOT NULL,
  `l0Location` varchar(128) DEFAULT NULL,
  `moodBoardLocation` varchar(128) DEFAULT NULL,
  `conceptNote` varchar(50) DEFAULT NULL,
  `spaceType` varchar(32) NOT NULL,
  `spaceSize` varchar(32) NOT NULL,
  `propertyType` varchar(32) NOT NULL,
  `spaceDesignStyle` varchar(128) NOT NULL,
  `spaceColorStyle` varchar(128) NOT NULL,
  `furnitureJson` varchar(50) NOT NULL,
  `decorJson` varchar(50) NOT NULL,
  `wallTreatmentType` varchar(32) NOT NULL,
  `wallMaterial` char(16) NOT NULL,
  `wallFinishMaterial` char(16) NOT NULL,
  `wallFinishSwatch` char(16) NOT NULL,
  `ceilingType` varchar(32) NOT NULL,
  `ceilingMaterial` char(16) NOT NULL,
  `ceilingFinishMaterial` char(16) NOT NULL,
  `ceilingFinishSwatch` char(16) NOT NULL,
  `flooringMaterial` char(16) NOT NULL,
  `flooringFinishSwatch` char(16) NOT NULL,
  `lighting` char(32) NOT NULL,
  `designElement` char(64) NOT NULL,
  `userPersonaJson` char(50) NOT NULL,
  `designerDetailsJson` text NOT NULL,
  `taggedProductJson` text NOT NULL,
  `designerstory` char(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`spaceName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='design_master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.developer
DROP TABLE IF EXISTS `developer`;
CREATE TABLE IF NOT EXISTS `developer` (
  `developerId` int(11) NOT NULL AUTO_INCREMENT,
  `developer_name` varchar(255) NOT NULL,
  `developer_address` text NOT NULL,
  `developer_url` text NOT NULL,
  `developer_logo` text,
  `champion_name` varchar(255) NOT NULL,
  `champion_role` varchar(255) NOT NULL,
  `champion_image` text NOT NULL,
  `champion_message` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`developerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_activity
DROP TABLE IF EXISTS `directus_activity`;
CREATE TABLE IF NOT EXISTS `directus_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(100) DEFAULT NULL,
  `action` varchar(100) NOT NULL,
  `identifier` varchar(100) DEFAULT NULL,
  `table_name` varchar(100) NOT NULL DEFAULT '',
  `row_id` int(11) unsigned DEFAULT '0',
  `user` int(11) unsigned NOT NULL DEFAULT '0',
  `data` text,
  `delta` text,
  `parent_id` int(11) unsigned DEFAULT NULL,
  `parent_table` varchar(100) DEFAULT NULL,
  `parent_changed` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Did the top-level record in the change set alter (scalar values/many-to-one relationships)? Or only the data within its related foreign collection records? (*toMany)',
  `datetime` datetime DEFAULT NULL,
  `logged_ip` varchar(20) DEFAULT NULL,
  `user_agent` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_bookmarks
DROP TABLE IF EXISTS `directus_bookmarks`;
CREATE TABLE IF NOT EXISTS `directus_bookmarks` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user` int(11) unsigned DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `icon_class` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `section` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_columns
DROP TABLE IF EXISTS `directus_columns`;
CREATE TABLE IF NOT EXISTS `directus_columns` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(64) NOT NULL DEFAULT '',
  `column_name` varchar(64) NOT NULL DEFAULT '',
  `data_type` varchar(64) DEFAULT NULL,
  `ui` varchar(64) DEFAULT NULL,
  `relationship_type` varchar(20) DEFAULT NULL,
  `related_table` varchar(64) DEFAULT NULL,
  `junction_table` varchar(64) DEFAULT NULL,
  `junction_key_left` varchar(64) DEFAULT NULL,
  `junction_key_right` varchar(64) DEFAULT NULL,
  `hidden_input` tinyint(1) NOT NULL DEFAULT '0',
  `hidden_list` tinyint(1) NOT NULL DEFAULT '0',
  `required` tinyint(1) NOT NULL DEFAULT '0',
  `sort` int(11) DEFAULT NULL,
  `comment` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `table-column` (`table_name`,`column_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_files
DROP TABLE IF EXISTS `directus_files`;
CREATE TABLE IF NOT EXISTS `directus_files` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) DEFAULT '1',
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT '',
  `location` varchar(200) DEFAULT NULL,
  `caption` text,
  `type` varchar(255) DEFAULT '',
  `charset` varchar(50) DEFAULT '',
  `tags` varchar(255) DEFAULT '',
  `width` int(11) unsigned DEFAULT '0',
  `height` int(11) unsigned DEFAULT '0',
  `size` int(11) unsigned DEFAULT '0',
  `embed_id` varchar(200) DEFAULT NULL,
  `user` int(11) unsigned NOT NULL,
  `date_uploaded` datetime DEFAULT NULL,
  `storage_adapter` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_groups
DROP TABLE IF EXISTS `directus_groups`;
CREATE TABLE IF NOT EXISTS `directus_groups` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `restrict_to_ip_whitelist` text,
  `nav_override` text,
  `show_activity` tinyint(1) NOT NULL DEFAULT '1',
  `show_messages` tinyint(1) NOT NULL DEFAULT '1',
  `show_users` tinyint(1) NOT NULL DEFAULT '1',
  `show_files` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_messages
DROP TABLE IF EXISTS `directus_messages`;
CREATE TABLE IF NOT EXISTS `directus_messages` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from` int(11) unsigned DEFAULT NULL,
  `subject` varchar(255) NOT NULL DEFAULT '',
  `message` text NOT NULL,
  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `attachment` int(11) unsigned DEFAULT NULL,
  `response_to` int(11) unsigned DEFAULT NULL,
  `comment_metadata` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_messages_recipients
DROP TABLE IF EXISTS `directus_messages_recipients`;
CREATE TABLE IF NOT EXISTS `directus_messages_recipients` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `message_id` int(11) unsigned NOT NULL,
  `recipient` int(11) unsigned NOT NULL,
  `read` tinyint(1) NOT NULL,
  `group` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_preferences
DROP TABLE IF EXISTS `directus_preferences`;
CREATE TABLE IF NOT EXISTS `directus_preferences` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user` int(11) unsigned DEFAULT NULL,
  `table_name` varchar(64) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `columns_visible` varchar(300) DEFAULT NULL,
  `sort` varchar(64) DEFAULT 'id',
  `sort_order` varchar(5) DEFAULT 'ASC',
  `status` varchar(5) DEFAULT '3',
  `search_string` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`user`,`table_name`,`title`),
  UNIQUE KEY `pref_title_constraint` (`user`,`table_name`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_privileges
DROP TABLE IF EXISTS `directus_privileges`;
CREATE TABLE IF NOT EXISTS `directus_privileges` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(255) CHARACTER SET latin1 NOT NULL,
  `allow_view` tinyint(1) NOT NULL DEFAULT '0',
  `allow_add` tinyint(1) NOT NULL DEFAULT '0',
  `allow_edit` tinyint(1) NOT NULL DEFAULT '0',
  `allow_delete` tinyint(1) NOT NULL DEFAULT '0',
  `allow_alter` tinyint(1) NOT NULL DEFAULT '0',
  `group_id` int(11) unsigned NOT NULL,
  `read_field_blacklist` varchar(1000) CHARACTER SET latin1 DEFAULT NULL,
  `write_field_blacklist` varchar(1000) CHARACTER SET latin1 DEFAULT NULL,
  `nav_listed` tinyint(1) NOT NULL DEFAULT '1',
  `status_id` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_schema_migrations
DROP TABLE IF EXISTS `directus_schema_migrations`;
CREATE TABLE IF NOT EXISTS `directus_schema_migrations` (
  `version` varchar(255) DEFAULT NULL,
  UNIQUE KEY `idx_directus_schema_migrations_version` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_settings
DROP TABLE IF EXISTS `directus_settings`;
CREATE TABLE IF NOT EXISTS `directus_settings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `collection` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `value` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Unique Collection and Name` (`collection`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_tables
DROP TABLE IF EXISTS `directus_tables`;
CREATE TABLE IF NOT EXISTS `directus_tables` (
  `table_name` varchar(64) NOT NULL DEFAULT '',
  `hidden` tinyint(1) NOT NULL DEFAULT '0',
  `single` tinyint(1) NOT NULL DEFAULT '0',
  `default_status` tinyint(1) NOT NULL DEFAULT '1',
  `footer` tinyint(1) DEFAULT '0',
  `list_view` varchar(200) DEFAULT NULL,
  `column_groupings` varchar(255) DEFAULT NULL,
  `primary_column` varchar(255) DEFAULT NULL,
  `user_create_column` varchar(64) DEFAULT NULL,
  `user_update_column` varchar(64) DEFAULT NULL,
  `date_create_column` varchar(64) DEFAULT NULL,
  `date_update_column` varchar(64) DEFAULT NULL,
  `filter_column_blacklist` text,
  PRIMARY KEY (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_ui
DROP TABLE IF EXISTS `directus_ui`;
CREATE TABLE IF NOT EXISTS `directus_ui` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_name` varchar(64) DEFAULT NULL,
  `column_name` varchar(64) DEFAULT NULL,
  `ui_name` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `value` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`table_name`,`column_name`,`ui_name`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.directus_users
DROP TABLE IF EXISTS `directus_users`;
CREATE TABLE IF NOT EXISTS `directus_users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `active` tinyint(1) DEFAULT '1',
  `first_name` varchar(50) DEFAULT '',
  `last_name` varchar(50) DEFAULT '',
  `email` varchar(255) DEFAULT '',
  `password` varchar(255) DEFAULT '',
  `salt` varchar(255) DEFAULT '',
  `token` varchar(255) NOT NULL,
  `access_token` varchar(255) DEFAULT '',
  `reset_token` varchar(255) DEFAULT '',
  `reset_expiration` datetime DEFAULT NULL,
  `position` varchar(500) DEFAULT '',
  `email_messages` tinyint(1) DEFAULT '1',
  `last_login` datetime DEFAULT NULL,
  `last_access` datetime DEFAULT NULL,
  `last_page` varchar(255) DEFAULT '',
  `ip` varchar(50) DEFAULT '',
  `group` int(11) unsigned DEFAULT NULL,
  `avatar` varchar(500) DEFAULT NULL,
  `avatar_file_id` int(11) unsigned DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  `zip` varchar(10) DEFAULT NULL,
  `language` varchar(8) DEFAULT 'en',
  `timezone` varchar(32) DEFAULT 'America/New_York',
  PRIMARY KEY (`id`),
  UNIQUE KEY `directus_users_token_unique` (`token`),
  UNIQUE KEY `directus_users_email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dir_diy
DROP TABLE IF EXISTS `dir_diy`;
CREATE TABLE IF NOT EXISTS `dir_diy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` int(11) NOT NULL DEFAULT '2',
  `diyId` varchar(1000) DEFAULT NULL,
  `diy_heading` varchar(1000) DEFAULT NULL,
  `diy_main_image` varchar(1000) DEFAULT NULL,
  `diy_content` text,
  `author` varchar(1000) DEFAULT NULL,
  `tags` varchar(1000) DEFAULT NULL,
  `date` varchar(1000) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dir_story
DROP TABLE IF EXISTS `dir_story`;
CREATE TABLE IF NOT EXISTS `dir_story` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` int(11) NOT NULL DEFAULT '2',
  `blogId` varchar(100) DEFAULT NULL,
  `blog_heading` varchar(1000) DEFAULT NULL,
  `blog_main_image` varchar(1000) DEFAULT NULL,
  `blog_content` text,
  `tags` text,
  `author` varchar(100) DEFAULT NULL,
  `date` varchar(100) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.diy
DROP TABLE IF EXISTS `diy`;
CREATE TABLE IF NOT EXISTS `diy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `diyId` varchar(255) NOT NULL,
  `tags` text NOT NULL,
  `diy_categories` text,
  `diyJson` text,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_diyId` (`diyId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dw_module_component
DROP TABLE IF EXISTS `dw_module_component`;
CREATE TABLE IF NOT EXISTS `dw_module_component` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `quoteNo` varchar(50) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `crmId` varchar(64) NOT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `version` varchar(8) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `region` varchar(128) NOT NULL,
  `status` varchar(128) NOT NULL,
  `discountAmount` double NOT NULL,
  `discountPercentage` double NOT NULL,
  `spaceType` varchar(128) NOT NULL,
  `room` varchar(128) NOT NULL,
  `prId` int(11) NOT NULL,
  `prTitle` varchar(255) NOT NULL,
  `prPrice` double DEFAULT NULL,
  `prPriceAfterDiscount` double DEFAULT NULL,
  `prArea` double DEFAULT NULL,
  `prCategory` varchar(255) NOT NULL,
  `moduleType` varchar(255) NOT NULL,
  `moduleSeq` int(11) NOT NULL,
  `moduleCode` varchar(64) NOT NULL,
  `moduleCategory` varchar(64) NOT NULL,
  `erpCode` varchar(16) DEFAULT NULL,
  `articleId` varchar(50) DEFAULT NULL,
  `accPackCode` varchar(64) DEFAULT NULL,
  `carcass` varchar(64) DEFAULT NULL,
  `finish` varchar(64) DEFAULT NULL,
  `finishMaterial` varchar(64) DEFAULT NULL,
  `color` varchar(128) DEFAULT NULL,
  `height` varchar(64) DEFAULT NULL,
  `width` varchar(64) DEFAULT NULL,
  `depth` varchar(64) DEFAULT NULL,
  `panelArea` double NOT NULL,
  `compType` char(8) NOT NULL,
  `compCode` varchar(32) NOT NULL,
  `compQty` double NOT NULL,
  `compTitle` varchar(255) NOT NULL,
  `compPrice` double NOT NULL,
  `compPriceAfterDiscount` double NOT NULL,
  `compWoTax` double NOT NULL,
  `compCost` double NOT NULL,
  `compProfit` double NOT NULL,
  `compMargin` double NOT NULL,
  `compUom` varchar(32) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module Component Price Info\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dw_product_module
DROP TABLE IF EXISTS `dw_product_module`;
CREATE TABLE IF NOT EXISTS `dw_product_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` double NOT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `region` varchar(128) NOT NULL,
  `crmId` varchar(128) NOT NULL,
  `quoteNo` varchar(128) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `spaceType` varchar(128) NOT NULL,
  `room` varchar(128) NOT NULL,
  `prId` int(11) NOT NULL,
  `prTitle` varchar(255) NOT NULL,
  `prCategoryCode` varchar(16) DEFAULT NULL,
  `moduleSeq` int(11) NOT NULL,
  `moduleCode` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `width` double NOT NULL,
  `depth` double NOT NULL,
  `height` double NOT NULL,
  `moduleCategory` varchar(128) NOT NULL,
  `handleSize` double NOT NULL,
  `handleQty` double NOT NULL,
  `carcass` varchar(128) NOT NULL,
  `finish` varchar(128) NOT NULL,
  `finishMaterial` varchar(128) NOT NULL,
  `color` varchar(128) NOT NULL,
  `exposedLeft` char(8) NOT NULL,
  `exposedRight` char(8) NOT NULL,
  `exposedBottom` char(8) NOT NULL,
  `exposedTop` char(8) NOT NULL,
  `exposedBack` char(8) NOT NULL,
  `exposedOpen` char(8) NOT NULL,
  `noOfAccPacks` int(11) NOT NULL,
  `moduleArea` double NOT NULL,
  `ccPrice` double NOT NULL,
  `ccWoTax` double NOT NULL,
  `ccCost` double NOT NULL,
  `ccProfit` double NOT NULL,
  `ccMargin` double NOT NULL,
  `shPrice` double NOT NULL,
  `shWoTax` double NOT NULL,
  `shCost` double NOT NULL,
  `shProfit` double NOT NULL,
  `shMargin` double NOT NULL,
  `hwPrice` double NOT NULL,
  `hwWoTax` double NOT NULL,
  `hwCost` double NOT NULL,
  `hwProfit` double NOT NULL,
  `hwMargin` double NOT NULL,
  `accPrice` double NOT NULL,
  `accWoTax` double NOT NULL,
  `accCost` double NOT NULL,
  `accProfit` double NOT NULL,
  `accMargin` double NOT NULL,
  `handlePrice` double NOT NULL,
  `handleWoTax` double NOT NULL,
  `handleCost` double NOT NULL,
  `handleProfit` double NOT NULL,
  `handleMargin` double NOT NULL,
  `hingePrice` double NOT NULL,
  `hingeWoTax` double NOT NULL,
  `hingeCost` double NOT NULL,
  `hingeProfit` double NOT NULL,
  `hingeMargin` double NOT NULL,
  `lrPrice` double NOT NULL,
  `lrWoTax` double NOT NULL,
  `lrCost` double NOT NULL,
  `lrProfit` double NOT NULL,
  `lrMargin` double NOT NULL,
  `modulePrice` double NOT NULL,
  `moduleWoTax` double NOT NULL,
  `moduleCost` double NOT NULL,
  `moduleProfit` double NOT NULL,
  `moduleMargin` double NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `knobType` varchar(255) DEFAULT NULL,
  `knobFinish` varchar(255) DEFAULT NULL,
  `handleType` varchar(255) DEFAULT NULL,
  `handleFinish` varchar(255) DEFAULT NULL,
  `knobQty` double DEFAULT NULL,
  `handleTypeSelection` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module Price information\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dw_proposal
DROP TABLE IF EXISTS `dw_proposal`;
CREATE TABLE IF NOT EXISTS `dw_proposal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) DEFAULT NULL,
  `version` double DEFAULT NULL,
  `proposalTitle` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `crmId` varchar(255) DEFAULT NULL,
  `quoteNo` varchar(255) DEFAULT NULL,
  `projectName` varchar(255) DEFAULT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `designPartnerName` varchar(128) DEFAULT NULL,
  `proposalUpdatedBy` varchar(128) DEFAULT NULL,
  `proposalCreateDate` datetime DEFAULT NULL,
  `proposalPriceDate` datetime DEFAULT NULL,
  `businessDate` datetime DEFAULT NULL,
  `versionCreatedBy` varchar(128) DEFAULT NULL,
  `versionCreatedOn` datetime DEFAULT NULL,
  `versionUpdatedBy` varchar(128) DEFAULT NULL,
  `versionUpdatedOn` datetime DEFAULT NULL,
  `stdModuleCount` int(11) DEFAULT NULL,
  `nStdModuleCount` int(11) DEFAULT NULL,
  `hikeModuleCount` int(11) DEFAULT NULL,
  `vrPrice` double DEFAULT NULL,
  `vrPriceAfterDiscount` double DEFAULT NULL,
  `vrPriceAfterTax` double DEFAULT NULL,
  `vrCost` double DEFAULT NULL,
  `vrProfit` double DEFAULT NULL,
  `vrMargin` double DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `prPrice` double DEFAULT NULL,
  `prPriceAfterDiscount` double DEFAULT NULL,
  `prPriceAfterTax` double DEFAULT NULL,
  `prCost` double DEFAULT NULL,
  `prProfit` double DEFAULT NULL,
  `prMargin` double DEFAULT NULL,
  `wwPrice` double DEFAULT NULL,
  `wwPriceAfterDiscount` double DEFAULT NULL,
  `wwPriceAfterTax` double DEFAULT NULL,
  `wwCost` double DEFAULT NULL,
  `wwProfit` double DEFAULT NULL,
  `wwMargin` double DEFAULT NULL,
  `bpPrice` double DEFAULT NULL,
  `bpPriceAfterDiscount` double DEFAULT NULL,
  `bpPriceAfterTax` double DEFAULT NULL,
  `bpCost` double DEFAULT NULL,
  `bpProfit` double DEFAULT NULL,
  `bpMargin` double DEFAULT NULL,
  `svPrice` double DEFAULT NULL,
  `svPriceAfterDiscount` double DEFAULT NULL,
  `svPriceAfterTax` double DEFAULT NULL,
  `svCost` double DEFAULT NULL,
  `svProfit` double DEFAULT NULL,
  `svMargin` double DEFAULT NULL,
  `kitchenCount` int(11) DEFAULT NULL,
  `wardrobeCount` int(11) DEFAULT NULL,
  `NSproductCount` int(11) DEFAULT NULL,
  `bpCount` int(11) DEFAULT NULL,
  `svCount` int(11) DEFAULT NULL,
  `stdModulePrice` double DEFAULT NULL,
  `nStdModulePrice` double DEFAULT NULL,
  `hikeModulePrice` double DEFAULT NULL,
  `hikeModuleCost` double DEFAULT NULL,
  `hwPrice` double DEFAULT NULL,
  `hwPriceAfterTax` double DEFAULT NULL,
  `hwPriceAfterDiscount` double DEFAULT NULL,
  `hwCost` double DEFAULT NULL,
  `hwProfit` double DEFAULT NULL,
  `hwMargin` double DEFAULT NULL,
  `accPrice` double DEFAULT NULL,
  `accPriceAfterTax` double DEFAULT NULL,
  `accPriceAfterDiscount` double DEFAULT NULL,
  `accCost` double DEFAULT NULL,
  `accProfit` double DEFAULT NULL,
  `accMargin` double DEFAULT NULL,
  `hkPrice` double DEFAULT NULL,
  `hkPriceAfterTax` double DEFAULT NULL,
  `hkPriceAfterDiscount` double DEFAULT NULL,
  `hkCost` double DEFAULT NULL,
  `hkProfit` double DEFAULT NULL,
  `hkMargin` double DEFAULT NULL,
  `hingePrice` double DEFAULT NULL,
  `hingePriceAfterTax` double DEFAULT NULL,
  `hingePriceAfterDiscount` double DEFAULT NULL,
  `hingeCost` double DEFAULT NULL,
  `hingeProfit` double DEFAULT NULL,
  `hingeMargin` double DEFAULT NULL,
  `lcPrice` double DEFAULT NULL,
  `lcPriceAfterTax` double DEFAULT NULL,
  `lcPriceAfterDiscount` double DEFAULT NULL,
  `lcCost` double DEFAULT NULL,
  `lcProfit` double DEFAULT NULL,
  `lcMargin` double DEFAULT NULL,
  `laPrice` double DEFAULT NULL,
  `laPriceAfterTax` double DEFAULT NULL,
  `laPriceAfterDiscount` double DEFAULT NULL,
  `laCost` double DEFAULT NULL,
  `laProfit` double DEFAULT NULL,
  `laMargin` double DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dw_proposal_addon
DROP TABLE IF EXISTS `dw_proposal_addon`;
CREATE TABLE IF NOT EXISTS `dw_proposal_addon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` double NOT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `crmId` varchar(255) NOT NULL,
  `quoteNo` varchar(255) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `region` varchar(64) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `category` varchar(64) NOT NULL,
  `subCategory` varchar(64) NOT NULL,
  `addonId` int(11) NOT NULL,
  `spaceType` varchar(64) NOT NULL,
  `roomcode` varchar(64) NOT NULL,
  `code` varchar(16) NOT NULL,
  `productTypeCode` char(32) NOT NULL DEFAULT 'All',
  `productSubtypeCode` char(64) DEFAULT NULL,
  `product` varchar(256) DEFAULT NULL,
  `brandCode` varchar(32) DEFAULT NULL,
  `catalogueCode` varchar(32) NOT NULL,
  `quantity` decimal(10,2) NOT NULL DEFAULT '0.00',
  `uom` char(10) NOT NULL DEFAULT 'N',
  `updatedBy` varchar(64) NOT NULL,
  `unitPrice` double NOT NULL,
  `unitSourceCost` double NOT NULL,
  `price` double NOT NULL,
  `priceWoTax` double NOT NULL,
  `sourceCost` double NOT NULL,
  `addonProfit` double NOT NULL,
  `addonMargin` double NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Addon reporting\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.dw_proposal_product
DROP TABLE IF EXISTS `dw_proposal_product`;
CREATE TABLE IF NOT EXISTS `dw_proposal_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` varchar(8) DEFAULT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `region` varchar(128) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `crmId` varchar(255) NOT NULL,
  `quoteNo` varchar(255) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `category` varchar(128) NOT NULL,
  `subCategory` varchar(128) NOT NULL,
  `prId` int(11) NOT NULL,
  `prTitle` varchar(255) NOT NULL,
  `spaceType` varchar(255) NOT NULL,
  `room` varchar(255) NOT NULL,
  `baseCarcass` varchar(255) NOT NULL,
  `wallCarcass` varchar(255) NOT NULL,
  `finishMaterial` varchar(255) NOT NULL,
  `finish` varchar(255) NOT NULL,
  `shutterDesign` varchar(255) NOT NULL,
  `hinge` varchar(128) NOT NULL,
  `glass` varchar(128) NOT NULL,
  `handleSelection` varchar(128) NOT NULL,
  `noOfLengths` int(11) NOT NULL,
  `handleType` varchar(128) NOT NULL,
  `handleFinish` varchar(128) NOT NULL,
  `handleSize` varchar(128) NOT NULL,
  `knobType` varchar(128) NOT NULL,
  `knobFinish` varchar(128) NOT NULL,
  `prArea` varchar(128) NOT NULL,
  `prCreatedBy` varchar(128) NOT NULL,
  `prCreatedOn` datetime NOT NULL,
  `prUpdatedBy` varchar(50) NOT NULL,
  `prUpdatedOn` datetime NOT NULL,
  `prPrice` double NOT NULL,
  `prPriceAfterDiscount` double NOT NULL,
  `prPriceAfterTax` double NOT NULL,
  `prCost` double NOT NULL,
  `prProfit` double NOT NULL,
  `prMargin` double NOT NULL,
  `wwPrice` double NOT NULL,
  `wwPriceAfterTax` double NOT NULL,
  `wwCost` double NOT NULL,
  `wwProfit` double NOT NULL,
  `wwMargin` double NOT NULL,
  `hwPrice` double NOT NULL,
  `hwPriceAfterTax` double NOT NULL,
  `hwCost` double NOT NULL,
  `hwProfit` double NOT NULL,
  `hwMargin` double NOT NULL,
  `accPrice` double NOT NULL,
  `accPriceAfterTax` double NOT NULL,
  `accCost` double NOT NULL,
  `accProfit` double NOT NULL,
  `accMargin` double NOT NULL,
  `laPrice` double NOT NULL,
  `laPriceAfterTax` double NOT NULL,
  `laCost` double NOT NULL,
  `laProfit` double NOT NULL,
  `laMargin` double NOT NULL,
  `hkPrice` double NOT NULL,
  `hkPriceAfterTax` double NOT NULL,
  `hkCost` double NOT NULL,
  `hkProfit` double NOT NULL,
  `hkMargin` double NOT NULL,
  `hingePrice` double NOT NULL,
  `hingePriceAfterTax` double NOT NULL,
  `hingeCost` double NOT NULL,
  `hingeProfit` double NOT NULL,
  `hingeMargin` double NOT NULL,
  `lcPrice` double NOT NULL,
  `lcPriceAfterTax` double NOT NULL,
  `lcCost` double NOT NULL,
  `lcProfit` double NOT NULL,
  `lcMargin` double NOT NULL,
  `installationCost` double NOT NULL,
  `transportationCost` double NOT NULL,
  `stdModuleCount` int(11) NOT NULL,
  `stdMPrice` double NOT NULL,
  `nStdModuleCount` int(11) NOT NULL,
  `nStdMPrice` double NOT NULL,
  `hikeModuleCount` int(11) NOT NULL,
  `hikeMPrice` double NOT NULL,
  `hikeMCost` double DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `color` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Product price Info\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.enquiries
DROP TABLE IF EXISTS `enquiries`;
CREATE TABLE IF NOT EXISTS `enquiries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(64) NOT NULL,
  `uid` varchar(64) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contactNumber` varchar(16) DEFAULT NULL,
  `fullName` varchar(128) DEFAULT NULL,
  `propertyName` varchar(255) DEFAULT NULL,
  `requirements` text,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.erp_master
DROP TABLE IF EXISTS `erp_master`;
CREATE TABLE IF NOT EXISTS `erp_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `itemCode` varchar(64) NOT NULL,
  `itemRefCode` varchar(64) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `referencePartNo` varchar(64) DEFAULT NULL,
  `invCategory` varchar(64) NOT NULL,
  `uom` varchar(16) NOT NULL,
  `isActive` varchar(16) NOT NULL,
  `price` double NOT NULL DEFAULT '0',
  `sourcePrice` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.event_errors
DROP TABLE IF EXISTS `event_errors`;
CREATE TABLE IF NOT EXISTS `event_errors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(64) NOT NULL,
  `event_type` varchar(64) NOT NULL,
  `event_data` text NOT NULL,
  `reason` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.event_log
DROP TABLE IF EXISTS `event_log`;
CREATE TABLE IF NOT EXISTS `event_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(64) NOT NULL,
  `event_type` varchar(64) NOT NULL,
  `event_data` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.finish_master
DROP TABLE IF EXISTS `finish_master`;
CREATE TABLE IF NOT EXISTS `finish_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `costCode` char(5) NOT NULL DEFAULT 'NA',
  `finishType` varchar(64) NOT NULL DEFAULT 'NA',
  `finishMaterial` varchar(64) NOT NULL,
  `design` char(1) NOT NULL DEFAULT 'N',
  `shutterMaterial` char(16) NOT NULL DEFAULT 'N',
  `finishCode` char(5) NOT NULL DEFAULT 'NA',
  `colorGroupCode` text NOT NULL,
  `cuttingOffset` int(11) NOT NULL DEFAULT '0',
  `title` varchar(64) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `edgeBinding` varchar(32) DEFAULT NULL,
  `doubleExposedCostCode` varchar(50) DEFAULT 'NA',
  `fromDate` datetime NOT NULL,
  `toDate` datetime NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  `setCode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `code_key` (`finishCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Finish Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.floor_plan_master
DROP TABLE IF EXISTS `floor_plan_master`;
CREATE TABLE IF NOT EXISTS `floor_plan_master` (
  `projectId` int(11) NOT NULL,
  `subProjectId` int(11) NOT NULL,
  `floor_plan_setId` int(11) NOT NULL,
  `floor_plan_set_name` text NOT NULL,
  `no_of_level` int(11) NOT NULL,
  `floor_plan_img` text NOT NULL,
  `flag` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`floor_plan_setId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.game_module_component
DROP TABLE IF EXISTS `game_module_component`;
CREATE TABLE IF NOT EXISTS `game_module_component` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `quoteNo` varchar(50) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `crmId` varchar(64) NOT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(64) DEFAULT NULL,
  `cphone1` varchar(64) DEFAULT NULL,
  `cphone2` varchar(64) DEFAULT NULL,
  `projectName` varchar(128) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `version` varchar(8) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `region` varchar(128) NOT NULL,
  `status` varchar(128) NOT NULL,
  `discountAmount` double NOT NULL,
  `discountPercentage` double NOT NULL,
  `spaceType` varchar(128) NOT NULL,
  `room` varchar(128) NOT NULL,
  `prId` int(11) NOT NULL,
  `prTitle` varchar(255) NOT NULL,
  `prPrice` double DEFAULT NULL,
  `prPriceAfterDiscount` double DEFAULT NULL,
  `prArea` double DEFAULT NULL,
  `prNetArea` varchar(128) DEFAULT NULL,
  `prCategory` varchar(255) NOT NULL,
  `moduleType` varchar(255) NOT NULL,
  `moduleSeq` int(11) NOT NULL,
  `moduleCode` varchar(64) NOT NULL,
  `moduleCategory` varchar(64) NOT NULL,
  `erpCode` varchar(16) DEFAULT NULL,
  `articleId` varchar(50) DEFAULT NULL,
  `accPackCode` varchar(64) DEFAULT NULL,
  `carcass` varchar(64) DEFAULT NULL,
  `finish` varchar(64) DEFAULT NULL,
  `finishMaterial` varchar(64) DEFAULT NULL,
  `color` varchar(128) DEFAULT NULL,
  `height` varchar(64) DEFAULT NULL,
  `width` varchar(64) DEFAULT NULL,
  `depth` varchar(64) DEFAULT NULL,
  `panelArea` double NOT NULL,
  `compType` char(8) NOT NULL,
  `compCode` varchar(32) NOT NULL,
  `compQty` double NOT NULL,
  `compTitle` varchar(255) NOT NULL,
  `compPrice` double NOT NULL,
  `compPriceAfterDiscount` double NOT NULL,
  `compWoTax` double NOT NULL,
  `compCost` double NOT NULL,
  `compProfit` double NOT NULL,
  `compMargin` double NOT NULL,
  `compUom` varchar(32) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `proposalId_version` (`proposalId`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module Component Price Info\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.game_product_module
DROP TABLE IF EXISTS `game_product_module`;
CREATE TABLE IF NOT EXISTS `game_product_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` double NOT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `region` varchar(128) NOT NULL,
  `crmId` varchar(128) NOT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(64) DEFAULT NULL,
  `cphone1` varchar(64) DEFAULT NULL,
  `cphone2` varchar(64) DEFAULT NULL,
  `projectName` varchar(128) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `quoteNo` varchar(128) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `spaceType` varchar(128) NOT NULL,
  `room` varchar(128) NOT NULL,
  `prId` int(11) NOT NULL,
  `prTitle` varchar(255) NOT NULL,
  `prNetArea` varchar(128) DEFAULT NULL,
  `prCategoryCode` varchar(16) DEFAULT NULL,
  `moduleSeq` int(11) NOT NULL,
  `moduleCode` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `width` double NOT NULL,
  `depth` double NOT NULL,
  `height` double NOT NULL,
  `moduleCategory` varchar(128) NOT NULL,
  `handleSize` double NOT NULL,
  `handleQty` double NOT NULL,
  `carcass` varchar(128) NOT NULL,
  `finish` varchar(128) NOT NULL,
  `finishMaterial` varchar(128) NOT NULL,
  `color` varchar(128) NOT NULL,
  `exposedLeft` char(8) NOT NULL,
  `exposedRight` char(8) NOT NULL,
  `exposedBottom` char(8) NOT NULL,
  `exposedTop` char(8) NOT NULL,
  `exposedBack` char(8) NOT NULL,
  `exposedOpen` char(8) NOT NULL,
  `noOfAccPacks` int(11) NOT NULL,
  `moduleArea` double NOT NULL,
  `ccPrice` double NOT NULL,
  `ccWoTax` double NOT NULL,
  `ccCost` double NOT NULL,
  `ccProfit` double NOT NULL,
  `ccMargin` double NOT NULL,
  `shPrice` double NOT NULL,
  `shWoTax` double NOT NULL,
  `shCost` double NOT NULL,
  `shProfit` double NOT NULL,
  `shMargin` double NOT NULL,
  `hwPrice` double NOT NULL,
  `hwWoTax` double NOT NULL,
  `hwCost` double NOT NULL,
  `hwProfit` double NOT NULL,
  `hwMargin` double NOT NULL,
  `accPrice` double NOT NULL,
  `accWoTax` double NOT NULL,
  `accCost` double NOT NULL,
  `accProfit` double NOT NULL,
  `accMargin` double NOT NULL,
  `handlePrice` double NOT NULL,
  `handleWoTax` double NOT NULL,
  `handleCost` double NOT NULL,
  `handleProfit` double NOT NULL,
  `handleMargin` double NOT NULL,
  `hingePrice` double NOT NULL,
  `hingeWoTax` double NOT NULL,
  `hingeCost` double NOT NULL,
  `hingeProfit` double NOT NULL,
  `hingeMargin` double NOT NULL,
  `lrPrice` double NOT NULL,
  `lrWoTax` double NOT NULL,
  `lrCost` double NOT NULL,
  `lrProfit` double NOT NULL,
  `lrMargin` double NOT NULL,
  `modulePrice` double NOT NULL,
  `moduleWoTax` double NOT NULL,
  `moduleCost` double NOT NULL,
  `moduleProfit` double NOT NULL,
  `moduleMargin` double NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `knobType` varchar(255) DEFAULT NULL,
  `knobFinish` varchar(255) DEFAULT NULL,
  `handleType` varchar(255) DEFAULT NULL,
  `handleFinish` varchar(255) DEFAULT NULL,
  `knobQty` double DEFAULT NULL,
  `handleTypeSelection` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `proposalId_version` (`proposalId`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module Price information\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.game_proposal
DROP TABLE IF EXISTS `game_proposal`;
CREATE TABLE IF NOT EXISTS `game_proposal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) DEFAULT NULL,
  `version` double DEFAULT NULL,
  `proposalTitle` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `crmId` varchar(255) DEFAULT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(64) DEFAULT NULL,
  `cphone1` varchar(64) DEFAULT NULL,
  `cphone2` varchar(64) DEFAULT NULL,
  `quoteNo` varchar(255) DEFAULT NULL,
  `projectName` varchar(255) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `designPartnerName` varchar(128) DEFAULT NULL,
  `proposalUpdatedBy` varchar(128) DEFAULT NULL,
  `proposalCreateDate` datetime DEFAULT NULL,
  `proposalPriceDate` datetime DEFAULT NULL,
  `businessDate` datetime DEFAULT NULL,
  `versionCreatedBy` varchar(128) DEFAULT NULL,
  `versionCreatedOn` datetime DEFAULT NULL,
  `versionUpdatedBy` varchar(128) DEFAULT NULL,
  `versionUpdatedOn` datetime DEFAULT NULL,
  `stdModuleCount` int(11) DEFAULT NULL,
  `nStdModuleCount` int(11) DEFAULT NULL,
  `hikeModuleCount` int(11) DEFAULT NULL,
  `vrPrice` double DEFAULT NULL,
  `vrPriceAfterDiscount` double DEFAULT NULL,
  `vrPriceAfterTax` double DEFAULT NULL,
  `vrCost` double DEFAULT NULL,
  `vrProfit` double DEFAULT NULL,
  `vrMargin` double DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `prPrice` double DEFAULT NULL,
  `prPriceAfterDiscount` double DEFAULT NULL,
  `prPriceAfterTax` double DEFAULT NULL,
  `prCost` double DEFAULT NULL,
  `prProfit` double DEFAULT NULL,
  `prMargin` double DEFAULT NULL,
  `wwPrice` double DEFAULT NULL,
  `wwPriceAfterDiscount` double DEFAULT NULL,
  `wwPriceAfterTax` double DEFAULT NULL,
  `wwCost` double DEFAULT NULL,
  `wwProfit` double DEFAULT NULL,
  `wwMargin` double DEFAULT NULL,
  `bpPrice` double DEFAULT NULL,
  `bpPriceAfterDiscount` double DEFAULT NULL,
  `bpPriceAfterTax` double DEFAULT NULL,
  `bpCost` double DEFAULT NULL,
  `bpProfit` double DEFAULT NULL,
  `bpMargin` double DEFAULT NULL,
  `svPrice` double DEFAULT NULL,
  `svPriceAfterDiscount` double DEFAULT NULL,
  `svPriceAfterTax` double DEFAULT NULL,
  `svCost` double DEFAULT NULL,
  `svProfit` double DEFAULT NULL,
  `svMargin` double DEFAULT NULL,
  `kitchenCount` int(11) DEFAULT NULL,
  `wardrobeCount` int(11) DEFAULT NULL,
  `NSproductCount` int(11) DEFAULT NULL,
  `bpCount` int(11) DEFAULT NULL,
  `svCount` int(11) DEFAULT NULL,
  `stdModulePrice` double DEFAULT NULL,
  `nStdModulePrice` double DEFAULT NULL,
  `hikeModulePrice` double DEFAULT NULL,
  `hikeModuleCost` double DEFAULT NULL,
  `hwPrice` double DEFAULT NULL,
  `hwPriceAfterTax` double DEFAULT NULL,
  `hwPriceAfterDiscount` double DEFAULT NULL,
  `hwCost` double DEFAULT NULL,
  `hwProfit` double DEFAULT NULL,
  `hwMargin` double DEFAULT NULL,
  `accPrice` double DEFAULT NULL,
  `accPriceAfterTax` double DEFAULT NULL,
  `accPriceAfterDiscount` double DEFAULT NULL,
  `accCost` double DEFAULT NULL,
  `accProfit` double DEFAULT NULL,
  `accMargin` double DEFAULT NULL,
  `hkPrice` double DEFAULT NULL,
  `hkPriceAfterTax` double DEFAULT NULL,
  `hkPriceAfterDiscount` double DEFAULT NULL,
  `hkCost` double DEFAULT NULL,
  `hkProfit` double DEFAULT NULL,
  `hkMargin` double DEFAULT NULL,
  `hingePrice` double DEFAULT NULL,
  `hingePriceAfterTax` double DEFAULT NULL,
  `hingePriceAfterDiscount` double DEFAULT NULL,
  `hingeCost` double DEFAULT NULL,
  `hingeProfit` double DEFAULT NULL,
  `hingeMargin` double DEFAULT NULL,
  `lcPrice` double DEFAULT NULL,
  `lcPriceAfterTax` double DEFAULT NULL,
  `lcPriceAfterDiscount` double DEFAULT NULL,
  `lcCost` double DEFAULT NULL,
  `lcProfit` double DEFAULT NULL,
  `lcMargin` double DEFAULT NULL,
  `laPrice` double DEFAULT NULL,
  `laPriceAfterTax` double DEFAULT NULL,
  `laPriceAfterDiscount` double DEFAULT NULL,
  `laCost` double DEFAULT NULL,
  `laProfit` double DEFAULT NULL,
  `laMargin` double DEFAULT NULL,
  `deepClearingQty` double DEFAULT NULL,
  `floorProtectionSqft` double DEFAULT NULL,
  `projectHandlingQty` double DEFAULT NULL,
  `deepClearingAmount` double DEFAULT NULL,
  `floorProtectionAmount` double DEFAULT NULL,
  `projectHandlingAmount` double DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `proposalId_version` (`proposalId`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.game_proposal_addon
DROP TABLE IF EXISTS `game_proposal_addon`;
CREATE TABLE IF NOT EXISTS `game_proposal_addon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` double NOT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `crmId` varchar(255) NOT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(64) DEFAULT NULL,
  `cphone1` varchar(64) DEFAULT NULL,
  `cphone2` varchar(64) DEFAULT NULL,
  `projectName` varchar(128) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `quoteNo` varchar(255) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `region` varchar(64) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `type` varchar(16) DEFAULT NULL,
  `category` varchar(64) NOT NULL,
  `subCategory` varchar(64) NOT NULL,
  `addonId` int(11) NOT NULL,
  `spaceType` varchar(64) NOT NULL,
  `roomcode` varchar(64) NOT NULL,
  `code` varchar(16) NOT NULL,
  `productTypeCode` char(32) NOT NULL DEFAULT 'All',
  `productSubtypeCode` char(64) DEFAULT NULL,
  `product` varchar(256) DEFAULT NULL,
  `brandCode` varchar(32) DEFAULT NULL,
  `catalogueCode` varchar(32) NOT NULL,
  `quantity` decimal(10,2) NOT NULL DEFAULT '0.00',
  `uom` char(10) NOT NULL DEFAULT 'N',
  `updatedBy` varchar(64) NOT NULL,
  `unitPrice` double NOT NULL,
  `unitSourceCost` double NOT NULL,
  `price` double NOT NULL,
  `priceWoTax` double NOT NULL,
  `sourceCost` double NOT NULL,
  `addonProfit` double NOT NULL,
  `addonMargin` double NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `proposalId_version` (`proposalId`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Addon reporting\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.game_proposal_product
DROP TABLE IF EXISTS `game_proposal_product`;
CREATE TABLE IF NOT EXISTS `game_proposal_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` varchar(8) DEFAULT NULL,
  `proposalTitle` varchar(255) NOT NULL,
  `priceDate` datetime NOT NULL,
  `businessDate` datetime NOT NULL,
  `region` varchar(128) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `discountAmount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `crmId` varchar(255) NOT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(64) DEFAULT NULL,
  `cphone1` varchar(64) DEFAULT NULL,
  `cphone2` varchar(64) DEFAULT NULL,
  `projectName` varchar(128) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `quoteNo` varchar(255) NOT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT NULL,
  `beforeProductionSpecification` varchar(4) DEFAULT NULL,
  `fromProposal` int(11) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `packageFlag` varchar(4) DEFAULT NULL,
  `category` varchar(128) NOT NULL,
  `subCategory` varchar(128) NOT NULL,
  `prId` int(11) NOT NULL,
  `prTitle` varchar(255) NOT NULL,
  `spaceType` varchar(255) NOT NULL,
  `room` varchar(255) NOT NULL,
  `baseCarcass` varchar(255) NOT NULL,
  `wallCarcass` varchar(255) NOT NULL,
  `finishMaterial` varchar(255) NOT NULL,
  `finish` varchar(255) NOT NULL,
  `shutterDesign` varchar(255) NOT NULL,
  `hinge` varchar(128) NOT NULL,
  `glass` varchar(128) NOT NULL,
  `handleSelection` varchar(128) NOT NULL,
  `noOfLengths` int(11) NOT NULL,
  `handleType` varchar(128) NOT NULL,
  `handleFinish` varchar(128) NOT NULL,
  `handleSize` varchar(128) NOT NULL,
  `knobType` varchar(128) NOT NULL,
  `knobFinish` varchar(128) NOT NULL,
  `prArea` varchar(128) NOT NULL,
  `prNetArea` varchar(128) DEFAULT NULL,
  `prCreatedBy` varchar(128) NOT NULL,
  `prCreatedOn` datetime NOT NULL,
  `prUpdatedBy` varchar(50) NOT NULL,
  `prUpdatedOn` datetime NOT NULL,
  `prPrice` double NOT NULL,
  `prPriceAfterDiscount` double NOT NULL,
  `prPriceAfterTax` double NOT NULL,
  `prCost` double NOT NULL,
  `prProfit` double NOT NULL,
  `prMargin` double NOT NULL,
  `wwPrice` double NOT NULL,
  `wwPriceAfterTax` double NOT NULL,
  `wwCost` double NOT NULL,
  `wwProfit` double NOT NULL,
  `wwMargin` double NOT NULL,
  `wwPriceAfterDiscount` double DEFAULT NULL,
  `hwPrice` double NOT NULL,
  `hwPriceAfterTax` double NOT NULL,
  `hwCost` double NOT NULL,
  `hwProfit` double NOT NULL,
  `hwMargin` double NOT NULL,
  `hwPriceAfterDiscount` double DEFAULT NULL,
  `accPrice` double NOT NULL,
  `accPriceAfterTax` double NOT NULL,
  `accCost` double NOT NULL,
  `accProfit` double NOT NULL,
  `accMargin` double NOT NULL,
  `accPriceAfterDiscount` double DEFAULT NULL,
  `laPrice` double NOT NULL,
  `laPriceAfterTax` double NOT NULL,
  `laCost` double NOT NULL,
  `laProfit` double NOT NULL,
  `laMargin` double NOT NULL,
  `laPriceAfterDiscount` double DEFAULT NULL,
  `hkPrice` double NOT NULL,
  `hkPriceAfterTax` double NOT NULL,
  `hkCost` double NOT NULL,
  `hkProfit` double NOT NULL,
  `hkMargin` double NOT NULL,
  `hkPriceAfterDiscount` double DEFAULT NULL,
  `hingePrice` double NOT NULL,
  `hingePriceAfterTax` double NOT NULL,
  `hingeCost` double NOT NULL,
  `hingeProfit` double NOT NULL,
  `hingeMargin` double NOT NULL,
  `hingePriceAfterDiscount` double DEFAULT NULL,
  `lcPrice` double NOT NULL,
  `lcPriceAfterTax` double NOT NULL,
  `lcCost` double NOT NULL,
  `lcProfit` double NOT NULL,
  `lcMargin` double NOT NULL,
  `lcPriceAfterDiscount` double DEFAULT NULL,
  `installationCost` double NOT NULL,
  `transportationCost` double NOT NULL,
  `stdModuleCount` int(11) NOT NULL,
  `stdMPrice` double NOT NULL,
  `nStdModuleCount` int(11) NOT NULL,
  `nStdMPrice` double NOT NULL,
  `hikeModuleCount` int(11) NOT NULL,
  `hikeMPrice` double NOT NULL,
  `hikeMCost` double DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `color` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `proposalId_version` (`proposalId`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Product price Info\r\n';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.game_user
DROP TABLE IF EXISTS `game_user`;
CREATE TABLE IF NOT EXISTS `game_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL DEFAULT 'A',
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `isSendEmail` varchar(8) DEFAULT NULL,
  `roleForEmail` varchar(256) DEFAULT NULL,
  `region` varchar(64) DEFAULT NULL,
  `phone` varchar(25) DEFAULT NULL,
  `salt` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.grouping_table
DROP TABLE IF EXISTS `grouping_table`;
CREATE TABLE IF NOT EXISTS `grouping_table` (
  `projectId` int(11) NOT NULL,
  `grouping_name` varchar(255) NOT NULL,
  `subProjectId` int(11) NOT NULL,
  `groupId` int(11) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `project_name` varchar(255) NOT NULL,
  `sub_project_name` varchar(255) NOT NULL,
  PRIMARY KEY (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.handle_master
DROP TABLE IF EXISTS `handle_master`;
CREATE TABLE IF NOT EXISTS `handle_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL DEFAULT '0',
  `seq` varchar(16) NOT NULL,
  `erpCode` varchar(16) NOT NULL,
  `boqDisplayOrder` int(11) NOT NULL,
  `procurementType` varchar(255) NOT NULL,
  `type` varchar(16) NOT NULL DEFAULT '0',
  `uom` varchar(8) DEFAULT NULL,
  `articleNo` varchar(8) DEFAULT NULL,
  `title` varchar(50) NOT NULL DEFAULT '0',
  `finish` varchar(50) DEFAULT NULL,
  `mgCode` varchar(50) DEFAULT NULL,
  `productCategory` varchar(16) DEFAULT 'all',
  `imagePath` varchar(128) DEFAULT NULL,
  `thickness` varchar(50) DEFAULT NULL,
  `sourcePrice` double NOT NULL DEFAULT '0',
  `msp` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.idsequence
DROP TABLE IF EXISTS `idsequence`;
CREATE TABLE IF NOT EXISTS `idsequence` (
  `sequencename` varchar(64) NOT NULL,
  `sequenceno` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`sequencename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Sequence Generator';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.material_component_mapping
DROP TABLE IF EXISTS `material_component_mapping`;
CREATE TABLE IF NOT EXISTS `material_component_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `materialName` varchar(32) NOT NULL,
  `componentName` varchar(64) NOT NULL,
  `element` varchar(32) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='material_component_mapping';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.material_finish_mapping
DROP TABLE IF EXISTS `material_finish_mapping`;
CREATE TABLE IF NOT EXISTS `material_finish_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `materialName` varchar(50) NOT NULL,
  `finishName` varchar(50) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`materialName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='material_finish_mapping';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.module_acc_pack
DROP TABLE IF EXISTS `module_acc_pack`;
CREATE TABLE IF NOT EXISTS `module_acc_pack` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apcode` varchar(16) NOT NULL,
  `mgcode` varchar(32) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`mgcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='module accessory pack';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.module_category
DROP TABLE IF EXISTS `module_category`;
CREATE TABLE IF NOT EXISTS `module_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `moduleType` varchar(4) NOT NULL,
  `moduleCategory` varchar(64) NOT NULL,
  `moduleCode` varchar(32) NOT NULL,
  `productCategory` varchar(16) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`moduleCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='module Category';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.module_components
DROP TABLE IF EXISTS `module_components`;
CREATE TABLE IF NOT EXISTS `module_components` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modulecode` varchar(32) DEFAULT NULL,
  `comptype` char(1) NOT NULL,
  `compcode` varchar(32) NOT NULL,
  `quantity` decimal(10,2) NOT NULL DEFAULT '0.00',
  `quantityFlag` char(1) DEFAULT NULL,
  `quantityFormula` char(5) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fromDate` datetime NOT NULL,
  `toDate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `mc_key` (`modulecode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Module to Component mapping';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.module_hinge_map
DROP TABLE IF EXISTS `module_hinge_map`;
CREATE TABLE IF NOT EXISTS `module_hinge_map` (
  `modulecode` varchar(50) DEFAULT NULL,
  `hingecode` varchar(50) DEFAULT NULL,
  `qty` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `qtyFlag` varchar(50) DEFAULT NULL,
  `qtyFormula` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.module_master
DROP TABLE IF EXISTS `module_master`;
CREATE TABLE IF NOT EXISTS `module_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `imagePath` varchar(255) NOT NULL,
  `width` int(11) NOT NULL DEFAULT '0',
  `depth` int(11) NOT NULL DEFAULT '0',
  `height` int(11) NOT NULL DEFAULT '0',
  `dimension` varchar(32) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `moduleType` varchar(4) NOT NULL COMMENT 'standard - S and non standard/customized - C',
  `moduleCategory` varchar(64) NOT NULL,
  `productCategory` varchar(24) DEFAULT 'NULL',
  `material` char(8) DEFAULT NULL,
  `unitType` char(16) DEFAULT NULL,
  `accessoryPackDefault` char(4) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT 'Add Remarks',
  `handleMandatory` varchar(4) DEFAULT 'No',
  `knobMandatory` varchar(4) DEFAULT 'No',
  `sqftCalculation` varchar(4) DEFAULT 'Yes',
  `hingeMandatory` varchar(4) DEFAULT 'No',
  `glassMandatory` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.new_price_master
DROP TABLE IF EXISTS `new_price_master`;
CREATE TABLE IF NOT EXISTS `new_price_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` varchar(16) NOT NULL,
  `newTxnAmount` double NOT NULL,
  `discountPercentage` double NOT NULL,
  `discountAmount` double NOT NULL,
  `oldTxnAmount` double NOT NULL,
  `differenceAmount` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.null
DROP TABLE IF EXISTS `null`;
CREATE TABLE IF NOT EXISTS `null` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.OfferMaster
DROP TABLE IF EXISTS `OfferMaster`;
CREATE TABLE IF NOT EXISTS `OfferMaster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `offerName` varchar(50) NOT NULL,
  `fromDate` datetime NOT NULL,
  `toDate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.oldnew_finishmap
DROP TABLE IF EXISTS `oldnew_finishmap`;
CREATE TABLE IF NOT EXISTS `oldnew_finishmap` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oldCode` varchar(50) NOT NULL,
  `newCode` varchar(50) NOT NULL,
  `flag` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `fromDate` datetime DEFAULT NULL,
  `toDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.package_design_gallery
DROP TABLE IF EXISTS `package_design_gallery`;
CREATE TABLE IF NOT EXISTS `package_design_gallery` (
  `design_packageId` int(11) NOT NULL,
  `space_name` varchar(255) NOT NULL,
  `design_img` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `packageId` int(11) DEFAULT NULL,
  PRIMARY KEY (`design_packageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.package_master
DROP TABLE IF EXISTS `package_master`;
CREATE TABLE IF NOT EXISTS `package_master` (
  `packageId` int(11) NOT NULL,
  `package_name` varchar(255) NOT NULL,
  `floor_plan_setId` int(11) NOT NULL,
  `package_price` varchar(25) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `package_logo` text NOT NULL,
  PRIMARY KEY (`packageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.panel_master
DROP TABLE IF EXISTS `panel_master`;
CREATE TABLE IF NOT EXISTS `panel_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) NOT NULL,
  `type` char(1) NOT NULL,
  `side` char(1) NOT NULL,
  `title` varchar(255) NOT NULL,
  `subtitle` varchar(64) NOT NULL DEFAULT 'NA',
  `plength` int(11) NOT NULL DEFAULT '0',
  `breadth` int(11) NOT NULL DEFAULT '0',
  `thickness` int(11) NOT NULL DEFAULT '0',
  `edgebinding` varchar(128) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `dimensionFormula` char(4) DEFAULT NULL,
  `exposed` char(1) DEFAULT NULL COMMENT 'D or S',
  PRIMARY KEY (`id`),
  KEY `code_key` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Panel master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.price_master
DROP TABLE IF EXISTS `price_master`;
CREATE TABLE IF NOT EXISTS `price_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rateType` varchar(16) NOT NULL DEFAULT 'HI',
  `rateId` varchar(64) DEFAULT NULL,
  `city` varchar(16) NOT NULL DEFAULT 'all',
  `price` double NOT NULL DEFAULT '0',
  `sourcePrice` double NOT NULL DEFAULT '0',
  `fromDate` datetime NOT NULL DEFAULT '2017-06-05 00:00:00',
  `toDate` datetime NOT NULL DEFAULT '2030-01-02 00:00:00',
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`rateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Price Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.product
DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` varchar(64) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(2048) NOT NULL,
  `category` varchar(64) DEFAULT NULL,
  `categoryId` varchar(16) DEFAULT NULL,
  `subcategory` varchar(64) DEFAULT NULL,
  `subcategoryId` varchar(16) DEFAULT NULL,
  `styleId` varchar(24) DEFAULT NULL,
  `productShortJson` text,
  `productJson` text,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `popularity` int(11) DEFAULT NULL,
  `relevance` int(11) DEFAULT NULL,
  `createDt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `styleSortSeq` int(11) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `seoId` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_productid` (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.product_category_map
DROP TABLE IF EXISTS `product_category_map`;
CREATE TABLE IF NOT EXISTS `product_category_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prodCategory` varchar(128) NOT NULL,
  `type` varchar(128) NOT NULL,
  `fromDate` datetime NOT NULL,
  `toDate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Product Category Map';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.product_library
DROP TABLE IF EXISTS `product_library`;
CREATE TABLE IF NOT EXISTS `product_library` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL DEFAULT 'A',
  `title` varchar(255) DEFAULT 'TITLE',
  `seq` int(11) NOT NULL DEFAULT '0',
  `type` char(16) NOT NULL DEFAULT 'CUSTOMIZED',
  `spaceType` varchar(64) DEFAULT 'CUSTOMIZED',
  `roomCode` varchar(255) DEFAULT NULL,
  `productCategoryCode` varchar(16) NOT NULL DEFAULT 'Kitchen',
  `catalogueId` varchar(32) DEFAULT NULL,
  `subCategory` varchar(32) DEFAULT NULL,
  `catalogueName` varchar(128) DEFAULT NULL,
  `makeTypeCode` char(1) NOT NULL,
  `baseCarcassCode` varchar(32) NOT NULL DEFAULT 'PLY',
  `wallCarcassCode` varchar(8) NOT NULL DEFAULT 'PLY',
  `finishTypeCode` varchar(16) NOT NULL,
  `finishCode` varchar(32) NOT NULL,
  `shutterDesignCode` varchar(32) DEFAULT NULL,
  `dimension` varchar(64) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `amount` double NOT NULL DEFAULT '0',
  `quoteFilePath` varchar(255) DEFAULT NULL,
  `modules` longtext,
  `addons` text,
  `productDescription` varchar(255) DEFAULT NULL,
  `imageurl` varchar(255) NOT NULL DEFAULT 'https://res.cloudinary.com/mygubbi/image/upload/v1490611606/addons/img-not-abl.jpg',
  `collection` varchar(50) DEFAULT NULL,
  `variantDescription` varchar(50) DEFAULT NULL,
  `productTitle` varchar(50) DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdBy` varchar(64) DEFAULT NULL,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(64) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `costWoAccessories` double DEFAULT '0',
  `profit` double DEFAULT '0',
  `margin` double DEFAULT '0',
  `amountWoTax` double DEFAULT '0',
  `manufactureAmount` double DEFAULT '0',
  `manualSeq` int(11) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `hinge` varchar(32) DEFAULT NULL,
  `glass` varchar(32) DEFAULT NULL,
  `handleType` varchar(32) DEFAULT NULL,
  `handleFinish` varchar(32) DEFAULT NULL,
  `handleImage` varchar(128) DEFAULT NULL,
  `knobType` varchar(32) DEFAULT NULL,
  `knobFinish` varchar(32) DEFAULT NULL,
  `knobImage` varchar(128) DEFAULT NULL,
  `size` varchar(50) DEFAULT NULL,
  `productLocation` varchar(255) DEFAULT NULL,
  `handleTypeSelection` varchar(255) DEFAULT NULL,
  `shutterDesign` varchar(32) DEFAULT NULL,
  `closebuttonFlag` varchar(32) DEFAULT 'No',
  `shutterImageUrl` varchar(128) DEFAULT 'No',
  `handleThickness` varchar(50) DEFAULT NULL,
  `handleCode` varchar(50) DEFAULT NULL,
  `knobCode` varchar(50) DEFAULT NULL,
  `lConnectorPrice` varchar(50) DEFAULT NULL,
  `noOfLengths` varchar(50) DEFAULT NULL,
  `designer` varchar(50) DEFAULT NULL,
  `productCategoryLocked` varchar(8) DEFAULT NULL,
  `finishSetId` varchar(64) DEFAULT NULL,
  `colorGroupCode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Products';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.product_library_master
DROP TABLE IF EXISTS `product_library_master`;
CREATE TABLE IF NOT EXISTS `product_library_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL,
  `subcategory` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.product_master
DROP TABLE IF EXISTS `product_master`;
CREATE TABLE IF NOT EXISTS `product_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(32) NOT NULL,
  `webDisplayName` varchar(32) DEFAULT NULL,
  `dFileLocation` varchar(128) DEFAULT NULL,
  `primaryImageLocation` varchar(128) NOT NULL,
  `secondaryImageLocationJson` text NOT NULL,
  `category` varchar(32) NOT NULL,
  `subcategory` varchar(32) NOT NULL,
  `usp` char(16) DEFAULT NULL,
  `designAttribute` char(32) DEFAULT NULL,
  `spaceJSon` varchar(50) NOT NULL,
  `productStyle` char(128) NOT NULL,
  `otherDesignStyle` char(128) NOT NULL,
  `productColor` varchar(128) NOT NULL,
  `productCompatibilityColor` varchar(128) NOT NULL,
  `furnitureComponentMaterialJson` text NOT NULL,
  `designerDetailsJson` text NOT NULL,
  `sourcingScale` varchar(4) NOT NULL,
  `designerstory` char(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`productName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='product_master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.project_table
DROP TABLE IF EXISTS `project_table`;
CREATE TABLE IF NOT EXISTS `project_table` (
  `projectId` int(11) NOT NULL AUTO_INCREMENT,
  `developerId` int(11) NOT NULL,
  `developer_name` varchar(255) NOT NULL,
  `project_name` varchar(255) NOT NULL,
  `project_address` text NOT NULL,
  `project_url` text,
  `project_logo` text NOT NULL,
  `project_type` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`projectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal
DROP TABLE IF EXISTS `proposal`;
CREATE TABLE IF NOT EXISTS `proposal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` char(10) NOT NULL DEFAULT 'Active',
  `title` varchar(255) NOT NULL,
  `version` char(10) DEFAULT NULL,
  `crmId` varchar(64) DEFAULT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(128) DEFAULT NULL,
  `cphone1` varchar(16) DEFAULT NULL,
  `cphone2` varchar(16) DEFAULT NULL,
  `projectName` varchar(128) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `amount` int(11) NOT NULL DEFAULT '0',
  `folderPath` varchar(255) DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdBy` varchar(64) DEFAULT NULL,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(64) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `quoteNo` varchar(50) DEFAULT NULL,
  `designPartnerName` varchar(128) DEFAULT NULL,
  `designPartnerEmail` varchar(128) DEFAULT NULL,
  `designPartnerPhone` varchar(16) DEFAULT NULL,
  `priceDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `maxDiscountPercentage` double NOT NULL DEFAULT '10',
  `beforeProductionSpecification` varchar(4) DEFAULT 'yes',
  `packageFlag` varchar(4) DEFAULT NULL,
  `sowremarksv1` varchar(255) DEFAULT NULL,
  `sowremarksv2` varchar(255) DEFAULT NULL,
  `adminPackageFlag` varchar(16) DEFAULT NULL,
  `offerType` varchar(16) DEFAULT NULL,
  `noOfDaysForWorkCompletion` int(11) DEFAULT '60',
  `fromProposal` int(11) DEFAULT NULL,
  `quoteNoNew` varchar(50) DEFAULT NULL,
  `fromVersion` varchar(50) DEFAULT NULL,
  `boqDriveLink` varchar(255) DEFAULT NULL,
  `boqStatus` varchar(255) DEFAULT NULL,
  `projectHandlingChargesApplied` varchar(8) DEFAULT 'false',
  `deepClearingChargesApplied` varchar(8) DEFAULT 'false',
  `floorProtectionChargesApplied` varchar(8) DEFAULT 'false',
  `customAddonCheck` varchar(4) DEFAULT 'Yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposaltemp
DROP TABLE IF EXISTS `proposaltemp`;
CREATE TABLE IF NOT EXISTS `proposaltemp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` char(10) NOT NULL DEFAULT 'Active',
  `title` varchar(255) NOT NULL,
  `version` char(10) DEFAULT NULL,
  `crmId` varchar(64) DEFAULT NULL,
  `quoteNo` varchar(64) DEFAULT NULL,
  `customerId` varchar(64) DEFAULT NULL,
  `cname` varchar(64) DEFAULT NULL,
  `caddress1` varchar(128) DEFAULT NULL,
  `caddress2` varchar(128) DEFAULT NULL,
  `caddress3` varchar(128) DEFAULT NULL,
  `ccity` varchar(64) DEFAULT NULL,
  `cemail` varchar(128) DEFAULT NULL,
  `cphone1` varchar(16) DEFAULT NULL,
  `cphone2` varchar(16) DEFAULT NULL,
  `projectName` varchar(128) DEFAULT NULL,
  `paddress1` varchar(128) DEFAULT NULL,
  `paddress2` varchar(128) DEFAULT NULL,
  `pcity` varchar(64) DEFAULT NULL,
  `salesName` varchar(128) DEFAULT NULL,
  `salesEmail` varchar(128) DEFAULT NULL,
  `salesPhone` varchar(16) DEFAULT NULL,
  `designerName` varchar(128) DEFAULT NULL,
  `designerEmail` varchar(128) DEFAULT NULL,
  `designerPhone` varchar(16) DEFAULT NULL,
  `amount` int(11) NOT NULL DEFAULT '0',
  `folderPath` varchar(255) DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdBy` varchar(64) DEFAULT NULL,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(64) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fullJson` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal_addon
DROP TABLE IF EXISTS `proposal_addon`;
CREATE TABLE IF NOT EXISTS `proposal_addon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seq` int(4) NOT NULL DEFAULT '0',
  `proposalId` int(11) NOT NULL,
  `fromVersion` varchar(50) NOT NULL,
  `spaceType` varchar(64) DEFAULT NULL,
  `roomcode` varchar(64) DEFAULT NULL,
  `code` varchar(16) NOT NULL,
  `categoryCode` varchar(32) NOT NULL DEFAULT 'NA',
  `customAddonCategory` varchar(128) DEFAULT NULL,
  `productTypeCode` char(32) NOT NULL DEFAULT 'All',
  `productSubtypeCode` char(64) DEFAULT NULL,
  `product` varchar(256) DEFAULT NULL,
  `brandCode` varchar(32) DEFAULT NULL,
  `catalogueCode` varchar(32) NOT NULL,
  `title` varchar(255) NOT NULL,
  `rate` decimal(10,2) NOT NULL DEFAULT '0.00',
  `quantity` decimal(10,2) NOT NULL DEFAULT '0.00',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `uom` char(10) NOT NULL DEFAULT 'N',
  `updatedBy` varchar(64) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `profit` double DEFAULT NULL,
  `margin` double DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `sourcePrice` decimal(10,2) DEFAULT NULL,
  `amountWOTax` decimal(10,2) DEFAULT NULL,
  `scopeDisplayFlag` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `proposalid_key` (`proposalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Addons';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal_boq
DROP TABLE IF EXISTS `proposal_boq`;
CREATE TABLE IF NOT EXISTS `proposal_boq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL DEFAULT '0',
  `spaceType` varchar(64) NOT NULL,
  `roomcode` varchar(64) NOT NULL,
  `category` varchar(64) NOT NULL,
  `productService` varchar(64) NOT NULL,
  `productId` int(11) DEFAULT NULL,
  `mgCode` varchar(64) NOT NULL,
  `moduleSeq` int(11) NOT NULL,
  `customCheckFlag` varchar(8) NOT NULL,
  `customRemarks` varchar(255) NOT NULL,
  `itemCategory` varchar(64) NOT NULL,
  `DSOErpItemCode` varchar(64) NOT NULL,
  `DSOItemSeq` int(11) DEFAULT NULL,
  `DSOReferencePartNo` varchar(64) NOT NULL,
  `DSODescription` varchar(255) DEFAULT NULL,
  `DSOUom` varchar(64) NOT NULL,
  `DSORate` double NOT NULL,
  `DSOQty` double NOT NULL,
  `DSOPrice` double NOT NULL,
  `plannerErpItemCode` varchar(64) NOT NULL,
  `plannerReferencePartNo` varchar(64) NOT NULL,
  `plannerDescription` varchar(255) DEFAULT NULL,
  `plannerUom` varchar(64) NOT NULL,
  `plannerRate` double NOT NULL,
  `plannerQty` double NOT NULL,
  `plannerPrice` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Billl of Quantities';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal_design_space
DROP TABLE IF EXISTS `proposal_design_space`;
CREATE TABLE IF NOT EXISTS `proposal_design_space` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdBy` varchar(255) DEFAULT NULL,
  `updatedOn` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal_documents
DROP TABLE IF EXISTS `proposal_documents`;
CREATE TABLE IF NOT EXISTS `proposal_documents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `productId` int(11) NOT NULL DEFAULT '0',
  `title` varchar(255) NOT NULL,
  `fileName` varchar(255) NOT NULL,
  `attachmentType` varchar(32) NOT NULL,
  `uploadedBy` varchar(64) DEFAULT NULL,
  `uploadedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `proposalid_key` (`proposalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal documents';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal_product
DROP TABLE IF EXISTS `proposal_product`;
CREATE TABLE IF NOT EXISTS `proposal_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL DEFAULT 'A',
  `proposalId` int(11) NOT NULL,
  `title` varchar(255) NOT NULL DEFAULT 'TITLE',
  `seq` int(11) NOT NULL DEFAULT '0',
  `type` char(16) NOT NULL DEFAULT 'CUSTOMIZED',
  `spaceType` varchar(64) NOT NULL DEFAULT 'CUSTOMIZED',
  `roomCode` varchar(255) DEFAULT NULL,
  `productCategoryCode` varchar(16) NOT NULL DEFAULT 'Kitchen',
  `catalogueId` varchar(32) DEFAULT NULL,
  `catalogueName` varchar(128) DEFAULT NULL,
  `makeTypeCode` char(1) NOT NULL,
  `baseCarcassCode` varchar(32) NOT NULL DEFAULT 'PLY',
  `wallCarcassCode` varchar(8) NOT NULL DEFAULT 'PLY',
  `finishTypeCode` varchar(255) DEFAULT NULL,
  `finishCode` varchar(32) NOT NULL,
  `shutterDesignCode` varchar(32) NOT NULL,
  `dimension` varchar(64) DEFAULT NULL,
  `quantity` int(11) NOT NULL DEFAULT '1',
  `amount` double NOT NULL DEFAULT '0',
  `quoteFilePath` varchar(255) DEFAULT NULL,
  `modules` longtext,
  `addons` text,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdBy` varchar(64) DEFAULT NULL,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedBy` varchar(64) DEFAULT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `costWoAccessories` double DEFAULT '0',
  `fromVersion` varchar(4) DEFAULT NULL,
  `profit` double DEFAULT '0',
  `margin` double DEFAULT '0',
  `amountWoTax` double DEFAULT '0',
  `manufactureAmount` double DEFAULT '0',
  `manualSeq` int(11) DEFAULT NULL,
  `source` varchar(11) DEFAULT NULL,
  `hinge` varchar(32) DEFAULT NULL,
  `glass` varchar(32) DEFAULT NULL,
  `handleType` varchar(32) DEFAULT NULL,
  `handleFinish` varchar(32) DEFAULT NULL,
  `handleImage` varchar(128) DEFAULT NULL,
  `knobType` varchar(32) DEFAULT NULL,
  `knobFinish` varchar(32) DEFAULT NULL,
  `knobImage` varchar(128) DEFAULT NULL,
  `handleTypeSelection` varchar(50) DEFAULT NULL,
  `shutterImage` varchar(128) DEFAULT NULL,
  `shutterCode` varchar(50) DEFAULT NULL,
  `closeButtonFlag` varchar(50) DEFAULT NULL,
  `noOfLengths` varchar(50) DEFAULT NULL,
  `handleThickness` varchar(50) DEFAULT NULL,
  `handleCode` varchar(50) DEFAULT NULL,
  `knobCode` varchar(50) DEFAULT NULL,
  `lConnectorPrice` varchar(50) DEFAULT NULL,
  `productCategoryLocked` char(8) DEFAULT NULL,
  `fromProduct` int(11) DEFAULT '0',
  `colorGroupCode` varchar(64) DEFAULT NULL,
  `finishSetId` varchar(64) DEFAULT NULL,
  `customColorCode` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `proposal_key` (`proposalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Proposal Products';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.proposal_sow
DROP TABLE IF EXISTS `proposal_sow`;
CREATE TABLE IF NOT EXISTS `proposal_sow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL DEFAULT '0',
  `version` char(8) NOT NULL DEFAULT '0',
  `spaceType` varchar(64) NOT NULL DEFAULT '0',
  `roomcode` varchar(255) DEFAULT NULL,
  `addonCode` varchar(64) NOT NULL DEFAULT '0',
  `L1S01Code` varchar(64) NOT NULL DEFAULT '0',
  `L1S01` varchar(64) NOT NULL DEFAULT '',
  `L2S01` varchar(64) NOT NULL DEFAULT '',
  `L2S02` varchar(64) NOT NULL DEFAULT '',
  `L2S03` varchar(64) NOT NULL DEFAULT '',
  `L2S04` varchar(64) NOT NULL DEFAULT '',
  `L2S05` varchar(64) NOT NULL DEFAULT '',
  `L2S06` varchar(64) NOT NULL DEFAULT '',
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.ratecard_master
DROP TABLE IF EXISTS `ratecard_master`;
CREATE TABLE IF NOT EXISTS `ratecard_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'primary_key',
  `rateCardId` varchar(50) NOT NULL DEFAULT '0',
  `type` varchar(50) NOT NULL DEFAULT '0',
  `code` varchar(50) NOT NULL DEFAULT '0',
  `thickness` int(11) NOT NULL DEFAULT '0',
  `productCategory` varchar(16) NOT NULL DEFAULT 'all',
  `setId` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.Recent_Projects
DROP TABLE IF EXISTS `Recent_Projects`;
CREATE TABLE IF NOT EXISTS `Recent_Projects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` int(11) NOT NULL DEFAULT '2',
  `Project_Id` varchar(100) DEFAULT NULL,
  `Project_Name` varchar(100) DEFAULT NULL,
  `Project_Images` text,
  `Project_Address` varchar(100) DEFAULT NULL,
  `Status` varchar(100) DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `main_image` varchar(1000) DEFAULT NULL,
  `Main_Image_Title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.reports_schedular
DROP TABLE IF EXISTS `reports_schedular`;
CREATE TABLE IF NOT EXISTS `reports_schedular` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reportsRunDate` timestamp NULL DEFAULT NULL,
  `status` varchar(64) NOT NULL,
  `lastRun` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comments` text,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.representative_floor_plan
DROP TABLE IF EXISTS `representative_floor_plan`;
CREATE TABLE IF NOT EXISTS `representative_floor_plan` (
  `subProjectId` int(11) NOT NULL,
  `projectId` int(11) NOT NULL,
  `representative_floor_plan_setId` int(11) NOT NULL,
  `developerId` int(11) NOT NULL,
  `representative_floor_planId` int(11) NOT NULL AUTO_INCREMENT,
  `representative_floor_plan_name` varchar(255) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`representative_floor_planId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.seo
DROP TABLE IF EXISTS `seo`;
CREATE TABLE IF NOT EXISTS `seo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seoId` varchar(255) NOT NULL,
  `title` text NOT NULL,
  `description` text,
  `metaKeywords` text,
  `seoJson` text,
  `location` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `subCategory` varchar(255) NOT NULL,
  `content` text,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `url` text,
  `other_catg_url` text,
  `other_subcatg_url` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_seoId` (`seoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.seo_landing_page
DROP TABLE IF EXISTS `seo_landing_page`;
CREATE TABLE IF NOT EXISTS `seo_landing_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` int(11) NOT NULL DEFAULT '2',
  `Banner_Image_Desktop` varchar(999) DEFAULT NULL,
  `Banner_Image_mobile` varchar(999) DEFAULT NULL,
  `Section2_Title` varchar(999) DEFAULT NULL,
  `Section2_Content` text,
  `Section5_Heading` varchar(999) DEFAULT NULL,
  `Section5_Content1` text,
  `Section5_Content2` text,
  `section5_image1_desktop` varchar(999) DEFAULT NULL,
  `section5_image2_desktop` varchar(999) DEFAULT NULL,
  `section5_image1_mobile` varchar(999) DEFAULT NULL,
  `section5_image2_mobile` varchar(999) DEFAULT NULL,
  `Categories` text,
  `Related_Categories` text,
  `Meta_Title` varchar(999) DEFAULT NULL,
  `Meta_Description` varchar(999) DEFAULT NULL,
  `Meta_Keywords` varchar(999) DEFAULT NULL,
  `Section7_Heading` varchar(999) DEFAULT NULL,
  `page_url` varchar(999) DEFAULT NULL,
  `banner_text_desktop` varchar(1000) DEFAULT NULL,
  `banner_text_mobile` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.shortlisted
DROP TABLE IF EXISTS `shortlisted`;
CREATE TABLE IF NOT EXISTS `shortlisted` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(64) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `productId` varchar(64) NOT NULL,
  `product` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `email` (`email`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.shutter_finish_mapping
DROP TABLE IF EXISTS `shutter_finish_mapping`;
CREATE TABLE IF NOT EXISTS `shutter_finish_mapping` (
  `finishcode` varchar(4) NOT NULL,
  `shutterGroupCode` varchar(16) NOT NULL,
  `shutterCode` varchar(16) NOT NULL,
  `touchtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.shutter_master
DROP TABLE IF EXISTS `shutter_master`;
CREATE TABLE IF NOT EXISTS `shutter_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shutterCode` varchar(16) DEFAULT NULL,
  `title` varchar(64) DEFAULT NULL,
  `imagePath` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.sow_master
DROP TABLE IF EXISTS `sow_master`;
CREATE TABLE IF NOT EXISTS `sow_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `spaceType` varchar(64) NOT NULL,
  `L1S01Code` varchar(64) NOT NULL,
  `L1S01` varchar(64) NOT NULL,
  `L2S01` varchar(128) NOT NULL,
  `L2S02` varchar(128) NOT NULL,
  `L2S03` varchar(128) NOT NULL,
  `L2S04` varchar(128) NOT NULL,
  `L2S05` varchar(128) NOT NULL,
  `L2S06` varchar(128) NOT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.sow_service_map
DROP TABLE IF EXISTS `sow_service_map`;
CREATE TABLE IF NOT EXISTS `sow_service_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `L1S01Code` varchar(64) NOT NULL,
  `addOnCode` varchar(64) NOT NULL,
  `spaceType` varchar(64) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`L1S01Code`,`addOnCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SOW and Service Mapping';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.subproject_table
DROP TABLE IF EXISTS `subproject_table`;
CREATE TABLE IF NOT EXISTS `subproject_table` (
  `subProjectId` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(11) NOT NULL,
  `developerId` int(11) NOT NULL,
  `developer_name` varchar(255) NOT NULL,
  `sub_project_name` varchar(255) NOT NULL,
  `project_address` varchar(255) NOT NULL,
  `project_url` varchar(25) DEFAULT NULL,
  `project_logo` varchar(255) NOT NULL,
  `project_type` varchar(255) NOT NULL,
  `grouping_name` text NOT NULL,
  `no_of_group` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `project_name` varchar(255) NOT NULL,
  PRIMARY KEY (`subProjectId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.tagproduct_master
DROP TABLE IF EXISTS `tagproduct_master`;
CREATE TABLE IF NOT EXISTS `tagproduct_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(32) NOT NULL,
  `imagePath` varchar(255) NOT NULL,
  `spaceName` varchar(32) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `code_key` (`productName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='tagproduct_master';

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.tmp_proposal_checks
DROP TABLE IF EXISTS `tmp_proposal_checks`;
CREATE TABLE IF NOT EXISTS `tmp_proposal_checks` (
  `proposalId` int(11) NOT NULL,
  `crmId` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `cname` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `quoteNoNew` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `quoteNo` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `version` varchar(50) NOT NULL,
  `version_status` char(64) NOT NULL,
  `amount` double NOT NULL,
  `discountPercentage` double NOT NULL,
  `discountAmount` double NOT NULL,
  `finalAmount` double NOT NULL,
  `proposal_status` char(10) CHARACTER SET utf8 NOT NULL DEFAULT 'Active',
  `product_total` int(1) NOT NULL DEFAULT '0',
  `addon_total` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.tmp_proposal_checks_1
DROP TABLE IF EXISTS `tmp_proposal_checks_1`;
CREATE TABLE IF NOT EXISTS `tmp_proposal_checks_1` (
  `proposalId` int(11) NOT NULL,
  `crmId` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `cname` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `quoteNoNew` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `quoteNo` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `version` varchar(50) NOT NULL,
  `version_status` char(64) NOT NULL,
  `amount` double NOT NULL,
  `discountPercentage` double NOT NULL,
  `discountAmount` double NOT NULL,
  `finalAmount` double NOT NULL,
  `proposal_status` char(10) CHARACTER SET utf8 NOT NULL DEFAULT 'Active',
  `product_total` int(1) NOT NULL DEFAULT '0',
  `addon_total` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.tmp_report_confirmed
DROP TABLE IF EXISTS `tmp_report_confirmed`;
CREATE TABLE IF NOT EXISTS `tmp_report_confirmed` (
  `proposalId` int(11) DEFAULT NULL,
  `version` varchar(50) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 NOT NULL,
  `cname` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `crmId` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `quoteNo` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `quoteNoNew` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `discountPercentage` double DEFAULT NULL,
  `finalAmount` double DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.transaction
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bankcode` varchar(64) NOT NULL,
  `txnid` varchar(64) NOT NULL,
  `net_amount_debit` float NOT NULL,
  `amount` float NOT NULL,
  `phone` varchar(64) NOT NULL,
  `productinfo` varchar(64) NOT NULL,
  `status` varchar(64) NOT NULL,
  `firstname` varchar(64) NOT NULL,
  `addedon` varchar(64) NOT NULL,
  `bank_ref_num` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  `mihpayid` varchar(64) NOT NULL,
  `udf1` varchar(64) NOT NULL,
  `card_type` varchar(64) NOT NULL,
  `name_on_card` varchar(64) NOT NULL,
  `cardnum` varchar(64) NOT NULL,
  `fulldetails` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_txnid` (`txnid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.unit_master
DROP TABLE IF EXISTS `unit_master`;
CREATE TABLE IF NOT EXISTS `unit_master` (
  `projectId` int(11) NOT NULL,
  `subProjectId` int(11) NOT NULL,
  `group_code` varchar(255) NOT NULL,
  `unit_number` int(11) NOT NULL,
  `unit_type` varchar(255) NOT NULL,
  `unitId` int(11) NOT NULL,
  `floor_plan_setId` int(11) NOT NULL,
  `customerId` int(11) NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sub_project_name` varchar(255) NOT NULL,
  PRIMARY KEY (`unitId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.user_profile
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE IF NOT EXISTS `user_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(64) NOT NULL,
  `active` char(1) NOT NULL DEFAULT 'A',
  `email` varchar(255) NOT NULL,
  `profile` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `crmId` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.user_profile_temp
DROP TABLE IF EXISTS `user_profile_temp`;
CREATE TABLE IF NOT EXISTS `user_profile_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` varchar(50) NOT NULL DEFAULT '0',
  `active` varchar(4) NOT NULL DEFAULT '0',
  `email` varchar(50) NOT NULL DEFAULT '0',
  `profile` text NOT NULL,
  `touchtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `crmId` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.


-- Dumping structure for table prod_copy.version_master
DROP TABLE IF EXISTS `version_master`;
CREATE TABLE IF NOT EXISTS `version_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proposalId` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `fromVersion` varchar(4) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `finalAmount` double NOT NULL,
  `status` char(64) NOT NULL,
  `internalStatus` char(64) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `remarks` varchar(255) DEFAULT NULL,
  `remarksIgnore` varchar(64) DEFAULT NULL,
  `ignoreAndPublishFlag` varchar(64) DEFAULT NULL,
  `discountPercentage` double NOT NULL,
  `discountAmount` double NOT NULL,
  `amount` double NOT NULL,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `profit` double DEFAULT NULL,
  `margin` double DEFAULT NULL,
  `amountWotax` double DEFAULT NULL,
  `manufactureAmount` double DEFAULT NULL,
  `businessDate` datetime DEFAULT NULL,
  `isDataLoadedToReport` varchar(8) DEFAULT NULL,
  `dataLoadedOn` datetime DEFAULT NULL,
  `projectHandlingAmount` double DEFAULT NULL,
  `projectHandlingQty` double DEFAULT '0',
  `deepClearingQty` double DEFAULT '0',
  `deepClearingAmount` double DEFAULT '0',
  `floorProtectionSqft` double DEFAULT '0',
  `floorProtectionAmount` double DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
