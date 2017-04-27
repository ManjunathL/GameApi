begin;

insert into code_master (lookupType, code, title) values ('category', 'K', 'Kitchen');
insert into code_master (lookupType, code, title) values ('category', 'W', 'Wardrobe');

insert into code_master (lookupType, code, title) values ('room', 'K', 'Kitchen');
insert into code_master (lookupType, code, title) values ('room', 'MBR', 'Master Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'KBR', 'Kids Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'GBR', 'Guest Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'LIV', 'Living Room');

insert into code_master (lookupType, code, title) values ('maketype', 'S', 'Standard');
insert into code_master (lookupType, code, title) values ('maketype', 'P', 'Premium');
insert into code_master (lookupType, code, title) values ('maketype', 'L', 'Luxury');

insert into code_master (lookupType, code, title) values ('carcassmaterial', 'PLY', 'PLY');
insert into code_master (lookupType, code, title) values ('carcassmaterial', 'MDF', 'MDF');
insert into code_master (lookupType, code, title) values ('carcassmaterial', 'PLPB', 'PLPB');

insert into code_master (lookupType, code, title) values ('finishtype', 'LAMINATE', 'Laminate');
insert into code_master (lookupType, code, title) values ('finishtype', 'FOIL', 'Foil');
insert into code_master (lookupType, code, title) values ('finishtype', 'PAINT', 'Paint');
insert into code_master (lookupType, code, title) values ('finishtype', 'ACRYLIC', 'Acrylic');
insert into code_master (lookupType, code, title) values ('finishtype', 'VENEER', 'Veneer');
insert into code_master (lookupType, code, title) values ('finishtype', 'SOLIDWOOD', 'Solid Wood');
insert into code_master (lookupType, code, title) values ('finishtype', 'PRELAM', 'Prelam');

insert into code_master (lookupType, code, title) values ('addontype', 'ACCESSORY', 'Accessory');
insert into code_master (lookupType, code, title) values ('addontype', 'APPLIANCE', 'Appliance');
insert into code_master (lookupType, code, title) values ('addontype', 'COUNTERTOP', 'Counter Top');
insert into code_master (lookupType, code, title) values ('addontype', 'SERVICE', 'Service');

insert into code_master (lookupType, code, title) values ('colorgroup', 'MTS', 'Matt Solid');
insert into code_master (lookupType, code, title) values ('colorgroup', 'MWG', 'Matt Wood grain');
insert into code_master (lookupType, code, title) values ('colorgroup', 'HGS', 'Highgloss Solid');
insert into code_master (lookupType, code, title) values ('colorgroup', 'HGW', 'Highgloss Wood grain');
insert into code_master (lookupType, code, title) values ('colorgroup', 'TXR', 'Texture');
insert into code_master (lookupType, code, title) values ('colorgroup', 'PUP', 'PU Paint');
insert into code_master (lookupType, code, title) values ('colorgroup', 'PFL', 'PVC foil');
insert into code_master (lookupType, code, title) values ('colorgroup', 'MTL', 'Metal Laminate');
insert into code_master (lookupType, code, title) values ('colorgroup', 'GLS', 'Glass Finish');

insert into code_master (lookupType, code, title) values ('pcategory', 'livingndining', 'Living & Dining');
insert into code_master (lookupType, code, title) values ('pcategory', 'kitchen', 'Kitchen');
insert into code_master (lookupType, code, title) values ('pcategory', 'bedroom', 'Bedroom');

insert into code_master (lookupType, code, title) values ('psubcategory', 'entunit', 'Entertainment Unit');
insert into code_master (lookupType, code, title) values ('psubcategory', 'shoerack', 'Shoe Rack');
insert into code_master (lookupType, code, title) values ('psubcategory', 'crockunit', 'Crockery Unit');
insert into code_master (lookupType, code, title) values ('psubcategory', 'foyerunit', 'Foyer Unit');
insert into code_master (lookupType, code, title) values ('psubcategory', 'sideboard', 'Sideboard');

insert into code_master (lookupType, code, title) values ('psubcategory', 'lshapedk', 'L Shaped Kitchen');
insert into code_master (lookupType, code, title) values ('psubcategory', 'ushapedk', 'U Shaped Kitchen');
insert into code_master (lookupType, code, title) values ('psubcategory', 'straightk', 'Straight Kitchen');
insert into code_master (lookupType, code, title) values ('psubcategory', 'parallelk', 'Parallel Kitchen');

insert into code_master (lookupType, code, title) values ('psubcategory', 'wardrobe', 'Wardrobe');
insert into code_master (lookupType, code, title) values ('psubcategory', 'studytable', 'Study Table');
insert into code_master (lookupType, code, title) values ('psubcategory', 'sidetable', 'Side Table');
insert into code_master (lookupType, code, title) values ('psubcategory', 'bookrack', 'Book Rack');

insert into code_master (lookupType, code, title) values ('spacecategory', 'kitchen', 'Kitchen');
insert into code_master (lookupType, code, title) values ('spacecategory', 'living', 'Living');

commit;

update product p, code_master c set p.categoryId = c.code where p.category = c.title and c.lookupType = 'pcategory';
update product p, code_master c set p.subcategoryId = c.code where p.subcategory = c.title and c.lookupType = 'psubcategory';

-- Changes done for new pricing module

-- create panel_master

insert panel_master(code, type, side, title, subtitle, plength, breadth, thickness, edgebinding)
select code, 'C', type, title, 'NA', plength, breadth, thickness, edgebinding from carcass_master;

insert panel_master(code, type, side, title, subtitle, plength, breadth, thickness, edgebinding)
select code, 'S', 'O', title, type, plength, breadth, thickness, edgebinding from shutter_master;

alter table module_master add column material char(8) NULL;
alter table module_master add column unitType char(16) NOT NULL DEFAULT 'NA';
