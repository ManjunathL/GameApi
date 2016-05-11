Use mg;

ALTER TABLE `product` ADD `popularity` int(11) DEFAULT NULL;

ALTER TABLE `product` ADD `relevance` int(11)) DEFAULT NULL;

ALTER TABLE `product` ADD `createDt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;