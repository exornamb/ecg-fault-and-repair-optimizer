-- =============================================================================
-- ECG Dumsor Response Optimizer — Database Seed Script
-- Group 15: Codebility v2.0
-- Database: PostgreSQL (Neon.tech)
-- =============================================================================

-- 1. Insert Locations (Sample from data/locations.csv)
INSERT INTO locations (location_id, name, area, location_type, x_coord, y_coord) VALUES
('L001', 'UG Legon Main Substation', 'Legon', 'Substation', 5.6508, -0.1869),
('L002', 'East Legon Switching Hub', 'East Legon', 'Substation', 5.6350, -0.1580),
('L003', 'Madina Feeder Station', 'Madina', 'Transformer Station', 5.6698, -0.1667),
('L004', 'Adenta Barrier Customer Centre', 'Adenta', 'Customer Service', 5.7100, -0.1600),
('L005', 'Haatso Industrial Hub', 'Haatso', 'Transformer Station', 5.6720, -0.2010),
('L006', 'Achimota Primary Bulk Supply', 'Achimota', 'Substation', 5.6120, -0.2250),
('L007', 'Noguchi Memorial Substation', 'Legon', 'Transformer Station', 5.6420, -0.1820),
('L008', 'Atomic Junction Transformer', 'Atomic', 'Transformer Station', 5.6600, -0.1870),
('L009', 'West Legon Residential Hub', 'West Legon', 'Transformer Station', 5.6480, -0.2050),
('L010', 'Legon Botanical Gardens Grid', 'Legon', 'Customer Service', 5.6550, -0.1800)
ON CONFLICT (location_id) DO NOTHING;

-- 2. Insert Feeder Roads (Sample from data/roads.csv)
INSERT INTO roads (road_id, from_location_id, to_location_id, distance_km, travel_time_min, road_type, bidirectional) VALUES
('R001', 'L006', 'L001', 4.2, 12, 'Primary Feeder', true),
('R002', 'L001', 'L002', 2.8, 8, 'Overhead Trunk', true),
('R003', 'L001', 'L003', 3.5, 10, 'Overhead Trunk', true),
('R004', 'L002', 'L003', 2.1, 6, 'Distribution Line', true),
('R005', 'L003', 'L004', 4.5, 15, 'Primary Feeder', true),
('R006', 'L001', 'L007', 1.2, 4, 'Underground Cable', true),
('R007', 'L001', 'L008', 1.8, 5, 'Overhead Trunk', true),
('R008', 'L008', 'L005', 2.6, 7, 'Distribution Line', true),
('R009', 'L006', 'L009', 3.1, 9, 'Primary Feeder', true),
('R010', 'L001', 'L010', 1.5, 4, 'Distribution Line', true)
ON CONFLICT (road_id) DO NOTHING;

-- 3. Insert Resources / Repair Crews (Sample from data/resources.csv)
INSERT INTO resources (crew_id, name, type, availability, capacity, base_location_id) VALUES
('CRW-01', 'Alpha Fast Response', 'Substation', 'Available', 4, 'L001'),
('CRW-02', 'Bravo Cable Tech', 'Underground', 'Available', 3, 'L002'),
('CRW-03', 'Charlie High Voltage Line', 'Overhead', 'Available', 5, 'L003'),
('CRW-04', 'Delta Emergency Mobile', 'Emergency', 'Available', 2, 'L004'),
('CRW-05', 'Echo Substation Unit', 'Substation', 'Available', 4, 'L005'),
('CRW-06', 'Foxtrot Underground Line', 'Underground', 'Available', 3, 'L006'),
('CRW-07', 'Golf Transformer Squad', 'Overhead', 'Available', 4, 'L007'),
('CRW-08', 'Hotel Outage Rescue', 'Emergency', 'Available', 3, 'L008')
ON CONFLICT (crew_id) DO NOTHING;

-- 4. Insert Initial Service Requests (Sample from data/service_requests.csv)
INSERT INTO service_requests (fault_id, area, category, urgency, crew, status) VALUES
('SR-001', 'Legon Main Campus', 'Substation', 5, 'Alpha Fast Response', 'Pending'),
('SR-002', 'East Legon High St', 'Overhead', 4, 'Charlie High Voltage Line', 'Dispatched'),
('SR-003', 'Madina Market Line', 'Underground', 3, 'Bravo Cable Tech', 'In Progress'),
('SR-004', 'Adenta Barrier Gate', 'Transformer', 4, 'Delta Emergency Mobile', 'Pending'),
('SR-005', 'Haatso Industrial Hub', 'Substation', 5, 'Echo Substation Unit', 'Pending'),
('SR-006', 'Noguchi Medical Lab', 'Emergency', 5, 'Alpha Fast Response', 'Dispatched'),
('SR-007', 'Atomic Junction Feeder', 'Overhead', 3, 'Golf Transformer Squad', 'Completed')
ON CONFLICT DO NOTHING;

-- 5. Insert Sample Algorithm Runs
INSERT INTO algorithm_runs (algorithm_name, input_size, time_ns, memory_kb, run_number) VALUES
('LinearSearch', 1000, 45200, 1024, 1),
('BinarySearch', 1000, 1200, 512, 1),
('SelectionSort', 1000, 3450000, 2048, 1),
('InsertionSort', 1000, 1850000, 1536, 1),
('MergeSort', 1000, 210000, 1024, 1),
('QuickSort', 1000, 165000, 1024, 1);
