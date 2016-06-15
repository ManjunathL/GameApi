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


commit;
