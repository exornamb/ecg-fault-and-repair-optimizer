-- =============================================================================
-- ECG Fault & Repair Optimizer — Migration: Add missing columns to service_requests
-- Run this on any PostgreSQL instance that has the OLD schema (no fault_id / area / crew).
-- Safe to run multiple times (uses IF NOT EXISTS / DO $$ blocks).
-- =============================================================================

-- 1. Add surrogate integer id (SERIAL)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'service_requests' AND column_name = 'id'
    ) THEN
        ALTER TABLE service_requests ADD COLUMN id SERIAL;
    END IF;
END $$;

-- 2. Add fault_id column (alias for request_id, used as display key)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'service_requests' AND column_name = 'fault_id'
    ) THEN
        ALTER TABLE service_requests ADD COLUMN fault_id VARCHAR(20);
        -- Backfill from request_id
        UPDATE service_requests SET fault_id = request_id WHERE fault_id IS NULL;
    END IF;
END $$;

-- 3. Add human-readable area column
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'service_requests' AND column_name = 'area'
    ) THEN
        ALTER TABLE service_requests ADD COLUMN area VARCHAR(150);
        -- Backfill area from source_location_id via locations table (if available)
        UPDATE service_requests sr
        SET area = COALESCE(
            (SELECT name FROM locations l WHERE l.location_id = sr.source_location_id),
            sr.source_location_id
        )
        WHERE sr.area IS NULL;
    END IF;
END $$;

-- 4. Add crew column
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'service_requests' AND column_name = 'crew'
    ) THEN
        ALTER TABLE service_requests ADD COLUMN crew VARCHAR(100) DEFAULT 'Unassigned';
    END IF;
END $$;

-- 5. Widen request_id to VARCHAR(20) if it was VARCHAR(10)
ALTER TABLE service_requests ALTER COLUMN request_id TYPE VARCHAR(20);

-- 6. Relax the status CHECK constraint to allow OPEN and RESOLVED
ALTER TABLE service_requests DROP CONSTRAINT IF EXISTS chk_service_requests_status;
ALTER TABLE service_requests ADD CONSTRAINT chk_service_requests_status
    CHECK (status IN ('NEW', 'OPEN', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'RESOLVED', 'CANCELLED'));

-- 7. Make source/destination_location_id nullable (some faults entered via app won't have IDs)
ALTER TABLE service_requests ALTER COLUMN source_location_id DROP NOT NULL;
ALTER TABLE service_requests ALTER COLUMN destination_location_id DROP NOT NULL;

-- 8. Make deadline nullable (optional field in app)
ALTER TABLE service_requests ALTER COLUMN deadline DROP NOT NULL;

SELECT 'Migration complete. service_requests table updated.' AS result;
