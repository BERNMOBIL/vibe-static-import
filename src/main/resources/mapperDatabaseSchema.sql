CREATE TABLE area_mapper (
  gtfs_id TEXT, --PRIMARY KEY,
  id INTEGER,
  update TIMESTAMP
);
CREATE TABLE calendar_date_mapper (
  gtfs_id TEXT, -- PRIMARY KEY,
  id INTEGER,
  update TIMESTAMP
);
CREATE TABLE journey_mapper (
  gtfs_trip_id TEXT, -- TEXT PRIMARY KEY,
  gtfs_service_id TEXT,
  id INTEGER,
  update TIMESTAMP
);
CREATE TABLE route_mapper (
  gtfs_id TEXT, -- PRIMARY KEY,
  id INTEGER,
  update TIMESTAMP
);
CREATE TABLE stop_mapper (
  gtfs_id TEXT, -- PRIMARY KEY,
  id INTEGER,
  update TIMESTAMP
);

CREATE TABLE update_history (
  id SERIAL PRIMARY KEY,
  time TIMESTAMP
);

CREATE INDEX area_mapper_id_index ON area_mapper(id);
CREATE INDEX calendar_date_mapper_id_index ON calendar_date_mapper(id);
CREATE INDEX journey_mapper_id_index ON journey_mapper(id);
CREATE INDEX journey_mapper_service_id_index ON journey_mapper(gtfs_service_id);
CREATE INDEX route_mapper_id_index ON route_mapper(id);
CREATE INDEX stop_mapper_id_index ON stop_mapper(id);
CREATE INDEX update_history_id_index ON update_history(id);