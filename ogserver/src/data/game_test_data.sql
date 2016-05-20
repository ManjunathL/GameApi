insert into kdmax_def_map (kdmcode, kdmdefcode) values ('MG-BU600-0237', 'MG-BU600-1760');
insert into kdmax_def_map (kdmcode, kdmdefcode) values ('MG-BU900-0255', 'MG-BU900-1778');


insert into kdmax_mg_map (kdmcode, mgcode) values ('MG-BU600-1760', 'K3DU6072');
insert into kdmax_mg_map (kdmcode, mgcode) values ('MG-BU600-1760', 'K3DU8072');
insert into kdmax_mg_map (kdmcode, mgcode) values ('MG-BU900-1778', 'KLUG9072');

insert into module_master(code, title, imageurl, width, depth, height, dimension) values ('K3DU6072', 'Kitchen 3 Drawer unit', 'a.jpg', 600, 575, 720, '600x575x720');
insert into module_master(code, title, imageurl, width, depth, height, dimension) values ('K3DU8072', 'Kitchen 2 Drawer unit', 'a.jpg', 800, 475, 620, '600x575x720');
insert into module_master(code, title, imageurl, width, depth, height, dimension) values ('KLUG9072', 'Kitchen Tall unit', 'a.jpg', 750, 575, 720, '600x575x720');


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



insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'A1', '1027389', 'Cutlery tray', 'E', 'Hettich', 'a.jpg', 'N', 100.00, 98.00);

insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'A1', '10273891', 'Cutlery tray', 'S', 'Hafele', 'a.jpg', 'N', 150.00, 148.00);

insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'A1', '10273892', 'Cutlery tray', 'P', 'Blum', 'a.jpg', 'N', 300.00, 298.00);

insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'A2', '1027389', 'Innotech Drawer with Railing+quardo', 'E', 'Hettich', 'b.jpg', 'N', 100.00, 98.00);

insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'A2', '10273891', 'Innotech Drawer with Railing+quardo', 'S','Hafele', 'b.jpg', 'N', 150.00, 148.00);

insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'A2', '10273892', 'Innotech Drawer with Railing+quardo', 'P', 'Blum', 'b.jpg', 'N', 300.00, 298.00);

insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'H1', '79724-R', 'Suspension bracker', 'E', 'Hettich', 'h1.jpg', 'N', 20.00, 18.00);
insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'H1', '79724-R', 'Suspension bracker', 'S', 'Hafele', 'h1.jpg', 'N', 20.00, 18.00);
insert into acc_hw_master (type, code, catalogcode, title, maketype, make, imageurl, uom, mrp, price)
    values ('A', 'H1', '79724-R', 'Suspension bracker', 'P','Blum',  'h1.jpg', 'N', 20.00, 18.00);


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

