-- =========================================================
-- Ghana Smart Service Operations Optimizer
-- DCIT 204/308 Joint DSA Project - Database Schema
-- Target: PostgreSQL (Neon.tech)
-- =========================================================
-- Column names/types are matched to the provided CSV templates
-- (locations_template.csv, roads_template.csv,
--  service_requests_template.csv, resources_template.csv)
-- so CSV import requires no column renaming.
-- =========================================================

-- Drop tables in dependency order (useful for re-running during dev)
DROP TABLE IF EXISTS audit_events;
DROP TABLE IF EXISTS algorithm_runs;
DROP TABLE IF EXISTS service_requests;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS roads;
DROP TABLE IF EXISTS locations;

-- =========================================================
-- 1. locations
-- Nodes in the local service network (graph vertices)
-- =========================================================
CREATE TABLE locations (
    location_id     VARCHAR(10)  PRIMARY KEY,          -- e.g. L001
    name            VARCHAR(150) NOT NULL,
    area            VARCHAR(100) NOT NULL,
    location_type   VARCHAR(50)  NOT NULL,             -- e.g. Library, Academic, Health
    x_coord         DOUBLE PRECISION NOT NULL,          -- latitude / local x
    y_coord         DOUBLE PRECISION NOT NULL           -- longitude / local y
);

-- =========================================================
-- 2. roads
-- Weighted edges between locations (graph edges)
-- =========================================================
CREATE TABLE roads (
    road_id            VARCHAR(10) PRIMARY KEY,        -- e.g. R001
    from_location_id   VARCHAR(10) NOT NULL REFERENCES locations(location_id),
    to_location_id     VARCHAR(10) NOT NULL REFERENCES locations(location_id),
    distance_km        NUMERIC(6,2) NOT NULL CHECK (distance_km >= 0),
    travel_time_min    INTEGER      NOT NULL CHECK (travel_time_min >= 0),
    condition_weight   NUMERIC(4,2) NOT NULL CHECK (condition_weight > 0),
    CONSTRAINT chk_roads_not_self_loop CHECK (from_location_id <> to_location_id)
);

-- =========================================================
-- 3. service_requests
-- Jobs to be queued, prioritised, searched and sorted
-- =========================================================
CREATE TABLE service_requests (
    request_id               VARCHAR(10) PRIMARY KEY,   -- e.g. Q001
    source_location_id       VARCHAR(10) NOT NULL REFERENCES locations(location_id),
    destination_location_id  VARCHAR(10) NOT NULL REFERENCES locations(location_id),
    category                 VARCHAR(50) NOT NULL,      -- e.g. Medical, Document
    urgency                  INTEGER     NOT NULL CHECK (urgency BETWEEN 1 AND 5),
    time_submitted           TIMESTAMP   NOT NULL,
    deadline                 TIMESTAMP   NOT NULL,
    status                   VARCHAR(20) NOT NULL DEFAULT 'NEW',
    CONSTRAINT chk_service_requests_status
        CHECK (status IN ('NEW', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT chk_service_requests_deadline
        CHECK (deadline >= time_submitted)
);

-- =========================================================
-- 4. resources
-- Vehicles, officers, staff, riders or assets to be assigned
-- =========================================================
CREATE TABLE resources (
    resource_id           VARCHAR(10) PRIMARY KEY,      -- e.g. V001, R001
    resource_type         VARCHAR(50) NOT NULL,          -- e.g. Van, Rider
    home_location_id      VARCHAR(10) NOT NULL REFERENCES locations(location_id),
    capacity              INTEGER     NOT NULL CHECK (capacity > 0),
    availability_status   VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT chk_resources_availability
        CHECK (availability_status IN ('AVAILABLE', 'BUSY', 'OFFLINE'))
);

-- =========================================================
-- 5. algorithm_runs
-- Empirical runtime measurements and input-size metadata
-- (no CSV template provided - team generates this data at runtime)
-- =========================================================
CREATE TABLE algorithm_runs (
    run_id           SERIAL PRIMARY KEY,
    algorithm_name   VARCHAR(100) NOT NULL,             -- e.g. QuickSort, Dijkstra
    input_size       INTEGER      NOT NULL CHECK (input_size >= 0),
    time_ns          BIGINT       NOT NULL CHECK (time_ns >= 0),
    memory_kb        INTEGER      CHECK (memory_kb >= 0),
    run_number       INTEGER      NOT NULL CHECK (run_number >= 0),
    date_run         TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- =========================================================
-- 6. audit_events
-- Stack-based undo/audit operations and important system events
-- =========================================================
CREATE TABLE audit_events (
    event_id            SERIAL PRIMARY KEY,
    event_type          VARCHAR(50)  NOT NULL,          -- e.g. DISPATCH, UNDO, STATUS_CHANGE
    related_entity_type VARCHAR(50),                    -- e.g. service_requests, resources
    related_entity_id   VARCHAR(20),
    description          TEXT,
    event_timestamp     TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- =========================================================
-- Indexes to support common query patterns
-- =========================================================
CREATE INDEX idx_roads_from ON roads(from_location_id);
CREATE INDEX idx_roads_to ON roads(to_location_id);

CREATE INDEX idx_service_requests_status ON service_requests(status);
CREATE INDEX idx_service_requests_urgency ON service_requests(urgency);
CREATE INDEX idx_service_requests_deadline ON service_requests(deadline);

CREATE INDEX idx_resources_availability ON resources(availability_status);
CREATE INDEX idx_resources_home_location ON resources(home_location_id);

CREATE INDEX idx_algorithm_runs_name ON algorithm_runs(algorithm_name);
CREATE INDEX idx_audit_events_type ON audit_events(event_type);
