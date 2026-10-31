-- SmartCity360 Seed Data
-- Note: app_user rows are NOT seeded here; StartupSeeder initializes them on startup if missing.

-- 1. Zone Hierarchy: 1 City -> 2 Districts -> 4 Streets (2 per district) -> 8 Buildings (2 per street)
INSERT INTO zone (id, name, level, parent_id, powered, base_usage_kwh) VALUES
(1, 'SmartCity Central', 'CITY', NULL, 1, 0.00),
(2, 'North District', 'DISTRICT', 1, 1, 0.00),
(3, 'South District', 'DISTRICT', 1, 1, 0.00),
(4, 'Main Street', 'STREET', 2, 1, 0.00),
(5, 'Oak Avenue', 'STREET', 2, 1, 0.00),
(6, 'Park Road', 'STREET', 3, 1, 0.00),
(7, 'River Lane', 'STREET', 3, 1, 0.00),
(8, 'Building A1', 'BUILDING', 4, 1, 150.00),
(9, 'Building A2', 'BUILDING', 4, 1, 120.00),
(10, 'Building B1', 'BUILDING', 5, 1, 200.00),
(11, 'Building B2', 'BUILDING', 5, 1, 180.00),
(12, 'Building C1', 'BUILDING', 6, 1, 160.00),
(13, 'Building C2', 'BUILDING', 6, 1, 140.00),
(14, 'Building D1', 'BUILDING', 7, 1, 190.00),
(15, 'Building D2', 'BUILDING', 7, 1, 170.00);

-- 2. Energy Meters (one per building, layers: ENCRYPTION,AUTHENTICATION,COMPRESSION)
INSERT INTO energy_meter (id, meter_code, zone_id, layers, installed_at) VALUES
(1, 'EM-A1', 8, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-01-15 08:00:00'),
(2, 'EM-A2', 9, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-01-15 08:30:00'),
(3, 'EM-B1', 10, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-02-01 09:00:00'),
(4, 'EM-B2', 11, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-02-01 09:30:00'),
(5, 'EM-C1', 12, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-03-10 10:00:00'),
(6, 'EM-C2', 13, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-03-10 10:30:00'),
(7, 'EM-D1', 14, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-04-20 11:00:00'),
(8, 'EM-D2', 15, 'ENCRYPTION,AUTHENTICATION,COMPRESSION', '2025-04-20 11:30:00');

-- 3. Staff Members (one per role)
INSERT INTO staff_member (id, full_name, role, email) VALUES
(1, 'John Smith', 'LOCAL_STAFF', 'john.smith@smartcity.lk'),
(2, 'Jane Doe', 'SUPERVISOR', 'jane.doe@smartcity.lk'),
(3, 'Robert Brown', 'CITY_MANAGER', 'robert.brown@smartcity.lk');

-- 4. Residents (3 residents)
INSERT INTO resident (id, full_name, email, address, zone_id) VALUES
(1, 'Alice Perera', 'alice.perera@gmail.com', 'Suite 101, Building A1, Main Street', 8),
(2, 'Kamal Silva', 'kamal.silva@gmail.com', 'Flat 202, Building B1, Oak Avenue', 10),
(3, 'Nimal Fernando', 'nimal.fernando@gmail.com', 'Apt 303, Building C1, Park Road', 12);

-- 5. Streetlight Models (5 models)
INSERT INTO streetlight_model (model_code, lamp_type, wattage, max_lumens, dim_profile, manufacturer) VALUES
(1, 'LED-60W-STD', 'LED', 60, 6000, 'LINEAR', 'Philips Lumec'),
(2, 'LED-90W-HIGH', 'LED', 90, 9500, 'STEPPED', 'Osram Opto'),
(3, 'LED-120W-EXP', 'LED', 120, 13000, 'EXPONENTIAL', 'GE Current'),
(4, 'SOLAR-40W-ECO', 'SOLAR_LED', 40, 4000, 'LINEAR', 'SunPower Eco'),
(5, 'SMART-75W-IOT', 'SMART_LED', 75, 8000, 'ADAPTIVE', 'Signify CityTouch');

-- 6. Streetlights (40 streetlights spread across 4 streets: zones 4, 5, 6, 7)
INSERT INTO streetlight (id, model_code, zone_id, latitude, longitude, state, brightness_percent) VALUES
(1, 1, 4, 6.927079, 79.861244, 'ON', 100),
(2, 1, 4, 6.927180, 79.861345, 'ON', 100),
(3, 2, 4, 6.927281, 79.861446, 'DIMMED', 70),
(4, 2, 4, 6.927382, 79.861547, 'DIMMED', 70),
(5, 3, 4, 6.927483, 79.861648, 'OFF', 0),
(6, 4, 4, 6.927584, 79.861749, 'ON', 100),
(7, 4, 4, 6.927685, 79.861850, 'DIMMED', 50),
(8, 5, 4, 6.927786, 79.861951, 'ON', 100),
(9, 5, 4, 6.927887, 79.862052, 'ON', 100),
(10, 1, 4, 6.927988, 79.862153, 'DIMMED', 80),

(11, 2, 5, 6.928089, 79.862254, 'ON', 100),
(12, 2, 5, 6.928190, 79.862355, 'ON', 100),
(13, 3, 5, 6.928291, 79.862456, 'DIMMED', 60),
(14, 3, 5, 6.928392, 79.862557, 'OFF', 0),
(15, 4, 5, 6.928493, 79.862658, 'ON', 100),
(16, 4, 5, 6.928594, 79.862759, 'ON', 100),
(17, 5, 5, 6.928695, 79.862860, 'DIMMED', 75),
(18, 1, 5, 6.928796, 79.862961, 'ON', 100),
(19, 1, 5, 6.928897, 79.863062, 'DIMMED', 50),
(20, 2, 5, 6.928998, 79.863163, 'ON', 100),

(21, 3, 6, 6.929099, 79.863264, 'ON', 100),
(22, 3, 6, 6.929200, 79.863365, 'DIMMED', 65),
(23, 4, 6, 6.929301, 79.863466, 'ON', 100),
(24, 4, 6, 6.929402, 79.863567, 'OFF', 0),
(25, 5, 6, 6.929503, 79.863668, 'ON', 100),
(26, 5, 6, 6.929604, 79.863769, 'DIMMED', 85),
(27, 1, 6, 6.929705, 79.863870, 'ON', 100),
(28, 1, 6, 6.929806, 79.863971, 'ON', 100),
(29, 2, 6, 6.929907, 79.864072, 'DIMMED', 50),
(30, 2, 6, 6.930008, 79.864173, 'OFF', 0),

(31, 3, 7, 6.930109, 79.864274, 'ON', 100),
(32, 3, 7, 6.930210, 79.864375, 'ON', 100),
(33, 4, 7, 6.930311, 79.864476, 'DIMMED', 70),
(34, 4, 7, 6.930412, 79.864577, 'ON', 100),
(35, 5, 7, 6.930513, 79.864678, 'DIMMED', 40),
(36, 5, 7, 6.930614, 79.864779, 'ON', 100),
(37, 1, 7, 6.930715, 79.864880, 'OFF', 0),
(38, 2, 7, 6.930816, 79.864981, 'ON', 100),
(39, 3, 7, 6.930917, 79.865082, 'DIMMED', 60),
(40, 4, 7, 6.931018, 79.865183, 'ON', 100);

-- 7. Sensors (6 sensors: 2 NOISE, 2 PARKING, 2 TEMPERATURE)
INSERT INTO sensor (id, sensor_code, sensor_type, zone_id, active) VALUES
(1, 'NS-01', 'NOISE', 4, 1),
(2, 'NS-02', 'NOISE', 6, 1),
(3, 'PK-01', 'PARKING', 4, 1),
(4, 'PK-02', 'PARKING', 7, 1),
(5, 'TM-01', 'TEMPERATURE', 5, 1),
(6, 'TM-02', 'TEMPERATURE', 6, 1);

-- Sensor readings (3 readings each, including noise above and below 70)
INSERT INTO sensor_reading (id, sensor_id, value, recorded_at) VALUES
(1, 1, 62.5, '2025-05-01 10:00:00'),
(2, 1, 74.8, '2025-05-01 22:30:00'),
(3, 1, 78.2, '2025-05-01 23:15:00'),
(4, 2, 55.0, '2025-05-01 10:00:00'),
(5, 2, 68.4, '2025-05-01 18:00:00'),
(6, 2, 81.1, '2025-05-01 23:45:00'),
(7, 3, 45.0, '2025-05-01 09:00:00'),
(8, 3, 85.0, '2025-05-01 14:00:00'),
(9, 3, 98.0, '2025-05-01 17:30:00'),
(10, 4, 30.0, '2025-05-01 09:00:00'),
(11, 4, 75.0, '2025-05-01 13:00:00'),
(12, 4, 96.0, '2025-05-01 19:00:00'),
(13, 5, 29.5, '2025-05-01 08:00:00'),
(14, 5, 33.2, '2025-05-01 12:00:00'),
(15, 5, 36.8, '2025-05-01 14:30:00'),
(16, 6, 28.0, '2025-05-01 08:00:00'),
(17, 6, 34.0, '2025-05-01 12:00:00'),
(18, 6, 37.5, '2025-05-01 15:00:00');

-- 8. Waste Bins (6 bins with mixed fill levels)
INSERT INTO waste_bin (id, bin_code, zone_id, location_desc, capacity_litres, fill_percent, last_collected_at) VALUES
(1, 'WB-01', 4, 'Corner of Main St & 1st Cross', 240, 45, '2025-04-28 06:00:00'),
(2, 'WB-02', 4, 'Main St Transit Stop', 500, 85, '2025-04-25 06:30:00'),
(3, 'WB-03', 5, 'Oak Ave Community Center', 240, 20, '2025-04-29 07:00:00'),
(4, 'WB-04', 5, 'Oak Ave Park Entrance', 360, 92, '2025-04-24 07:15:00'),
(5, 'WB-05', 6, 'Park Rd Children Playground', 240, 88, '2025-04-26 06:45:00'),
(6, 'WB-06', 7, 'River Lane Promenade', 500, 60, '2025-04-27 06:15:00');

-- 9. Policies (3 required operational policies)
INSERT INTO policy (id, name, rule_text, action_text, enabled, updated_at) VALUES
(1, 'noise_after_hours', 'noise_level > 70 AND time_between(22:00, 06:00)', 'ALERT noise limit exceeded', 1, '2025-05-01 00:00:00'),
(2, 'parking_full', 'parking_occupancy >= 95', 'ALERT parking zone full', 1, '2025-05-01 00:00:00'),
(3, 'heat_warning', 'temperature > 35 OR (temperature > 32 AND noise_level > 60)', 'ALERT heat advisory', 1, '2025-05-01 00:00:00');
