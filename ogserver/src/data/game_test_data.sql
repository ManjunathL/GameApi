insert into kdmax_def_map (kdmcode, kdmdefcode) values ('MG-BU600-0237', 'MG-BU600-1760');
insert into kdmax_def_map (kdmcode, kdmdefcode) values ('MG-BU900-0255', 'MG-BU900-1778');


insert into kdmax_mg_map (kdmcode, mgcode) values ('MG-BU600-1760', 'K3DU6072');
insert into kdmax_mg_map (kdmcode, mgcode) values ('MG-BU600-1760', 'K3DU8072');
insert into kdmax_mg_map (kdmcode, mgcode) values ('MG-BU900-1778', 'KLUG9072');

insert into module_master(code, description, imagePath, width, depth, height, dimension) values ('K3DU6072', 'Kitchen 3 Drawer unit', 'w1530-specs-1.png', 600, 575, 720, '600x575x720');
insert into module_master(code, description, imagePath, width, depth, height, dimension) values ('K3DU8072', 'Kitchen 2 Drawer unit', 'w1530-specs-1.png', 800, 475, 620, '600x575x720');
insert into module_master(code, description, imagePath, width, depth, height, dimension) values ('KLUG9072', 'Kitchen Tall unit', 'w1530-specs-1.png', 750, 575, 720, '600x575x720');


insert into module_components (modulecode, comptype, compcode, quantity) values ('KLUG9072', 'C', 'WS7129L', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('KLUG9072', 'C', 'TP8629', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('KLUG9072', 'C', 'BO8629', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('KLUG9072', 'S', 'KLU9072', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('KLUG9072', 'S', 'WE2971R', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('KLUG9072', 'H', 'H1', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'C', 'BS7155L', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'C', 'BS7155R', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'C', 'TF5698', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'S', 'K3DU600', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'S', '3DU600', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'A', 'A1', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU6072', 'A', 'A2', 2);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'H', 'H1', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'C', 'BS7155L', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'C', 'BS7155R', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'S', 'K3DU600', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'S', '3DU600', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'A', 'A1', 1);
insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'A', 'A2', 1);

insert into module_components (modulecode, comptype, compcode, quantity) values ('K3DU8072', 'H', 'H1', 1);


insert into carcass_master (code, title, plength, breadth, thickness, edgebinding, area)
    values ('WS7129L', 'Side Carcasse Left with Boring & Grooving', 718, 298, 18, '4 edge 0.8mm pvc lipping', 2.303108496);

insert into carcass_master (code, title, plength, breadth, thickness, edgebinding, area)
    values ('TP8629', 'Top panel with Boring & Grooving', 864, 298, 18, '2 long 0.8mm pvc lipping', 2.771428608);

insert into carcass_master (code, title, plength, breadth, thickness, edgebinding, area)
    values ('BO8629', 'Bottom Panel with Boring & Grooving', 864, 298, 18, '2 long 0.8mm pvc lipping', 2.771428608);

insert into carcass_master (code, title, plength, breadth, thickness, edgebinding, area)
    values ('BS7155L', 'Side Carcasse Left with Boring & Grooving', 718, 555, 18, '4 edge 0.8mm pvc lipping', 4.28934636);

insert into carcass_master (code, title, plength, breadth, thickness, edgebinding, area)
    values ('BS7155R', 'Side Carcasse Right with Boring & Grooving', 718, 555, 18, '4 edge 0.8mm pvc lipping', 4.28934636);

insert into carcass_master (code, title, plength, breadth, thickness, edgebinding, area)
    values ('TF5698', 'Top panel with Boring', 564, 98, 18, '2 long 0.8mm pvc lipping', 0.594947808);


insert into shutter_master (code, title, plength, breadth, thickness, edgebinding, quantity)
    values ('KLU9072', 'Kitchen LiftUp Unit-720mm Height', 800, 600, 18, '4 edge 0.8mm pvc lipping', 1);

insert into shutter_master (code, title, plength, breadth, thickness, edgebinding, quantity)
    values ('WE2971R', 'Wall unit Side carcasse-exposed', 298, 718, 18, '4 edge 0.8mm pvc lipping', 1);

insert into shutter_master (code, title, plength, breadth, thickness, edgebinding, quantity)
    values ('K3DU600', 'Drawer Facia', 600, 575, 18, '4 edge 0.8mm pvc lipping', 2);

insert into shutter_master (code, title, plength, breadth, thickness, edgebinding, quantity)
    values ('3DU600', 'Drawer Facia', 600, 575, 18, '4 edge 0.8mm pvc lipping', 1);


insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('A', 'A1', '1027389', 'Cutlery tray', 'E', 'Hettich', 'accessories/cultery-tray-hauss.jpg', 'N', 100.00, 98.00);

insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('A', 'A1', '10273891', 'Cutlery tray', 'S', 'Hafele', 'accessories/cultery-tray-hauss.jpg', 'N', 150.00, 148.00);

insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('A', 'A1', '10273892', 'Cutlery tray', 'P', 'Blum', 'accessories/cultery-tray-hauss.jpg', 'N', 300.00, 298.00);

insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('A', 'A2', '1027389', 'Innotech Drawer with Railing+quardo', 'E', 'Hettich', 'accessories/acsimages.jpg', 'N', 100.00, 98.00);

insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('A', 'A2', '10273891', 'Innotech Drawer with Railing+quardo', 'S','Hafele', 'accessories/acsimages.jpg', 'N', 150.00, 148.00);

insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('A', 'A2', '10273892', 'Innotech Drawer with Railing+quardo', 'P', 'Blum', 'accessories/deutschland.jpg', 'N', 300.00, 298.00);

insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('H', 'H1', '79724-R', 'Suspension bracker', 'E', 'Hettich', 'accessories/drawer-dustbin-mobel.jpg', 'N', 20.00, 18.00);
insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('H', 'H1', '79724-R', 'Suspension bracker', 'S', 'Hafele', 'accessories/drawer-dustbin-mobel.jpg', 'N', 20.00, 18.00);
insert into acc_hw_master (type, code, catalogCode, title, makeType, make, imagePath, uom, mrp, price)
    values ('H', 'H1', '79724-R', 'Suspension bracker', 'P','Blum',  'accessories/pull-out-basket-hauss.jpg', 'N', 20.00, 18.00);


insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'A', 'A001', '79724-R', 'Wicker basket', 'Hettich', 'a001.jpg', 'N', 20.00);
insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'A', 'A002', '79724-R', 'Glasses holder', 'Hettich', 'a002.jpg', 'N', 20.00);
insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'A', 'A003', '79724-R', 'Spice rack', 'Hettich', 'a003.jpg', 'N', 20.00);

insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'C', 'C001', '79724-R', 'Black granite top', 'Local', 'c001.jpg', 'F', 20.00);
insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'C', 'C002', '79724-R', 'Red granite', 'Local', 'c002.jpg', 'F', 20.00);

insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'S', 'S001', '79724-R', 'Dado', 'Local', 's001.jpg', 'N', 20.00);
insert into addon_master (producttype, addontype, code, catalogcode, title, make, imageurl, uom, rate)
    values ('K', 'S', 'S002', '79724-R', 'Wall painting', 'Local', 's002.jpg', 'N', 20.00);


finishtype, finish

-- insert into code_master (lookupType, levelType, additionalType, code, title) values ('category', '', '');

insert into code_master (lookupType, code, title) values ('category', 'K', 'Kitchen');
insert into code_master (lookupType, code, title) values ('category', 'W', 'Wardrobe');

insert into code_master (lookupType, code, title) values ('room', 'K', 'Kitchen');
insert into code_master (lookupType, code, title) values ('room', 'MBR', 'Master Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'KBR', 'Kids Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'GBR', 'Guest Bedroom');
insert into code_master (lookupType, code, title) values ('room', 'LIV', 'Living Room');

insert into code_master (lookupType, code, title) values ('maketype', 'E', 'Economy');
insert into code_master (lookupType, code, title) values ('maketype', 'S', 'Standard');
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

insert into code_master (lookupType, code, title) values ('addontype', 'ACCESSORY', 'Accessory');
insert into code_master (lookupType, code, title) values ('addontype', 'COUNTERTOP', 'Counter Top');
insert into code_master (lookupType, code, title) values ('addontype', 'SERVICE', 'Service');

insert into code_master (lookupType, additionalType, code, title) values ('finish', 'PAINT', 'M01', 'MDF with PU');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'PAINT', 'M02', 'MDF with PU B/W');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'FOIL', 'M03', 'PVC Foil');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'FOIL', 'M04', 'Glossy Foil');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'LAMINATE', 'M10', 'PLY/LAM');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'LAMINATE', 'M11', 'Glossy Laminate/PLY');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'ACRYLIC', 'M08', 'Acrylic/MDF');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'LAMINATE', 'M07', 'Glossy Laminate/MDF');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'LAMINATE', 'M05', 'MDF/ Mat LAM');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'SOLIDWOOD', 'M09', 'Solid Wood');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'LAMINATE', 'M06', 'MDF Pre Lam');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'LAMINATE', 'M12', 'Ply Pre Lam');
insert into code_master (lookupType, additionalType, code, title) values ('finish', 'VENEER', 'M13', 'Ply Veneer');



