Use mg;

ALTER TABLE `product` ADD `popularity` varchar(16) DEFAULT NULL;

ALTER TABLE `product` ADD `relevance` varchar(16) DEFAULT NULL;

ALTER TABLE `product` ADD `createDt` date DEFAULT NULL;