begin;

insert into code_master (lookupType, code, title) values ('category', 'K', 'Kitchen');
insert into code_master (lookupType, code, title) values ('category', 'W', 'Wardrobe');

insert into code_master (lookupType, code, title) values ('room', 'K', 'Kitchen');
insert into code_master (lookupType, code, title) values ('room', 'MBR', 'Master Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'KBR', 'Kids Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'GBR', 'Guest Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'LIV', 'Living Room');

insert into code_master (lookupType, code, title) values ('maketype', 'S', 'Standard');
insert into code_master (lookupType, code, title) values ('maketype', 'E', 'Economy');
insert into code_master (lookupType, code, title) values ('maketype', 'P', 'Premium');

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

begin;

insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, rateReadOnly, title, rate, mrp, uom, imagePath) values('ADD01','Appliances','Kitchen','Chimney','Faber','RAY60LTW, Silver',1,'Chimney - Faber - RAY60LTW, Silver',20999,20999,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD02','Appliances','Kitchen','Chimney','Faber','CLEO PB SS LTW 60','Chimney - Faber - CLEO PB SS LTW 60',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD03','Appliances','Kitchen','Chimney','Faber','Feel Plus SS TC LTW 90','Chimney - Faber - Feel Plus SS TC LTW 90',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD04','Appliances','Kitchen','Hob','Faber','MDR700MDX','Hob - Faber - MDR700MDX',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD05','Appliances','Kitchen','Hob','Faber','HGG 754 CRS BR CI','Hob - Faber - HGG 754 CRS BR CI',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD06','Appliances','Kitchen','Hob','Faber','HGG 653 CRR BR EI','Hob - Faber - HGG 653 CRR BR EI',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD07','Appliances','Kitchen','Sink','Franke','ACX 610-A','Sink - Franke - ACX 610-A',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD08','Appliances','Kitchen','Sink','Franke','GRX 610','Sink - Franke - GRX 610',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD09','Appliances','Kitchen','Sink','Nirali','Maestro','Sink - Nirali - Maestro',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD10','Appliances','Kitchen','Sink','Nirali','Elegance','Sink - Nirali - Elegance',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD11','Appliances','Kitchen','Sink','Carysil','Single bowl drainer','Sink - Carysil - Single bowl drainer',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD12','Appliances','Kitchen','Tap','Franke','Nyon','Tap - Franke - Nyon',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD13','Appliances','Kitchen','Tap','Franke','Cristallo silk steel','Tap - Franke - Cristallo silk steel',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD14','Appliances','Kitchen','Tap','Jaguar','SOL-CHR-6167B','Tap - Jaguar - SOL-CHR-6167B',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD15','Appliances','Kitchen','Microwave','Faber','FBI MWO 25L CGS','Microwave - Faber - FBI MWO 25L CGS',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD16','Appliances','Kitchen','Dishwasher','Faber','FDW BI 6PR 12S','Dishwasher - Faber - FDW BI 6PR 12S',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD17','Accessories','Kitchen/Bedrooms','LED spot lights','Jaguar','CHRZ01R118FX','LED spot lights - Jaguar - CHRZ01R118FX',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD18','Accessories','Kitchen/Bedrooms','LED spot lights','syska','CAT01','LED spot lights - syska - CAT01',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD19','Accessories','Kitchen/Bedrooms','LED Strip lights','syska','CAT02','LED Strip lights - syska - CAT02',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD20','Accessories','Kitchen/Bedrooms','LED Strip lights','wipro','CAT03','LED Strip lights - wipro - CAT03',134,134,'UNIT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD21','Countertop','Kitchen','Granite','Telephone black','CAT04','Granite - Telephone black - CAT04',1000,1000,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD22','Countertop','Kitchen','Quartz','AGL','CAT05','Quartz - AGL - CAT05',1000,1000,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD23','Countertop','Kitchen','Quartz','Klinga','CAT06','Quartz - Klinga - CAT06',1000,1000,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD24','Countertop','Kitchen','Corian','Dupont','CAT07','Corian - Dupont - CAT07',1000,1000,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD25','Countertop','Kitchen','Corian','LG','CAT08','Corian - LG - CAT08',1000,1000,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD26','Services','Kitchen','Dado','Local','CAT09','Dado - Local - CAT09',300,300,'MTR','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD27','Services','Kitchen','Flooring','Local','CAT10','Flooring - Local - CAT10',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD28','Services','All rooms','Electical','Local','CAT11','Electical - Local - CAT11',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD29','Services','Kitchen','Plumbing','Local','CAT12','Plumbing - Local - CAT12',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD30','Services','All rooms','painting','Local','CAT13','painting - Local - CAT13',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD31','Services','All rooms','False Ceiling','Local','CAT14','False Ceiling - Local - CAT14',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD32','Services','All rooms','Lighting','Syska','CAT15','Lighting - Syska - CAT15',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD33','Services','All rooms','Lighting','Syska','CAT16','Lighting - Syska - CAT16',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD34','Services','All rooms','Lighting','Syska','CAT17','Lighting - Syska - CAT17',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD35','Services','All rooms','Lighting','Philips','CAT18','Lighting - Philips - CAT18',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD36','Services','All rooms','Lighting','Philips','CAT19','Lighting - Philips - CAT19',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD37','Services','Living/ Bedroom','AC','Siemens','CAT20','AC - Siemens - CAT20',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD38','Services','Bathroom','Bathroom Dado','Local','CAT21','Bathroom Dado - Local - CAT21',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD39','Services','Bathroom','Bathroom Plumbing','Local','CAT22','Bathroom Plumbing - Local - CAT22',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD40','Services','Bathroom','Bathroom partitions','Local','CAT23','Bathroom partitions - Local - CAT23',200,200,'SQFT','');
insert addon_master (code, categoryCode, roomCode, productTypeCode, brandCode, catalogueCode, title, rate, mrp, uom, imagePath) values('ADD41','Services','All rooms','Window Dressing','Local','CAT24','Window Dressing - Local - CAT24',200,200,'SQFT','');

commit;
