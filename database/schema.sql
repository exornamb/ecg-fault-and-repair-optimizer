CREATE TABLE IF NOT EXISTS locations(
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    area TEXT NOT NULL,
    type TEXT NOT NULL,
    x DOUBLE PRECISION NOT NULL,
    y DOUBLE PRECISION NOT NULL);

CREATE TABLE IF NOT EXISTS roads(
    id INTEGER PRIMARY KEY,
    from_id INTEGER NOT NULL REFERENCES locations(id),
    to_id INTEGER NOT NULL REFERENCES locations(id),
    km DOUBLE PRECISION NOT NULL,
    minutes INTEGER NOT NULL,
    condition INTEGER NOT NULL);

CREATE TABLE IF NOT EXISTS service_requests(
    id INTEGER PRIMARY KEY,
    source TEXT NOT NULL,
    location_id INTEGER NOT NULL REFERENCES locations(id),
    category TEXT NOT NULL,
    urgency INTEGER NOT NULL CHECK(urgency BETWEEN 1 AND 5),
    submitted TEXT NOT NULL,
    deadline TEXT NOT NULL,
    status TEXT NOT NULL,
    assigned_crew TEXT
);
CREATE TABLE IF NOT EXISTS resources(
    id INTEGER PRIMARY KEY,
    crew_name TEXT NOT NULL UNIQUE,
    type TEXT NOT NULL,
    home_location INTEGER NOT NULL REFERENCES locations(id),
    capacity INTEGER NOT NULL,
    availability TEXT NOT NULL
);
CREATE TABLE IF NOT EXISTS algorithm_runs(
    id SERIAL PRIMARY KEY,
    algorithm TEXT NOT NULL,
    input_size INTEGER NOT NULL,
    time_ns BIGINT NOT NULL,
    memory_kb BIGINT NOT NULL,
    date_run TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS audit_events(
    id SERIAL PRIMARY KEY,
    event TEXT NOT NULL,
    occurred TEXT NOT NULL);
