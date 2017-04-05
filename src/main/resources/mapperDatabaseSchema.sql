CREATE TABLE area_mapper (
  gtfs_id STRING PRIMARY KEY,
  id INTEGER
);
CREATE TABLE calendar_date_mapper (
  gtfs_id STRING PRIMARY KEY,
  id INTEGER
);
CREATE TABLE journey_mapper (
  gtfs_trip_id STRING PRIMARY KEY,
  gtfs_service_id STRING,
  id INTEGER
);
CREATE TABLE route_mapper (
  gtfs_id STRING PRIMARY KEY,
  id INTEGER
);
CREATE TABLE stop_mapper (
  gtfs_id STRING PRIMARY KEY,
  id INTEGER
);
CREATE INDEX area_mapper_id_index ON area_mapper(id);
CREATE INDEX calendar_date_mapper_id_index ON calendar_date_mapper(id);
CREATE INDEX journey_mapper_id_index ON journey_mapper(id);
CREATE INDEX journey_mapper_service_id_index ON journey_mapper(gtfs_service_id);
CREATE INDEX route_mapper_id_index ON route_mapper(id);
CREATE INDEX stop_mapper_id_index ON stop_mapper(id);