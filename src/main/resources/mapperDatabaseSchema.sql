CREATE TABLE area_mapper (
  gtfs_id TEXT,
  id UUID,
  update TIMESTAMP,
  UNIQUE (gtfs_id, update)
);
CREATE TABLE calendar_date_mapper (
  gtfs_id TEXT,
  id UUID,
  update TIMESTAMP
  --UNIQUE (gtfs_id, update)
);
CREATE TABLE journey_mapper (
  gtfs_trip_id TEXT,
  gtfs_service_id TEXT,
  id UUID,
  update TIMESTAMP,
  UNIQUE (gtfs_trip_id, gtfs_service_id, update)
);
CREATE TABLE route_mapper (
  gtfs_id TEXT,
  id UUID,
  update TIMESTAMP,
  UNIQUE (gtfs_id, update)
);
CREATE TABLE stop_mapper (
  gtfs_id TEXT,
  id UUID,
  update TIMESTAMP,
  UNIQUE (gtfs_id, update)
);

CREATE INDEX area_mapper_id_index ON area_mapper(id);
CREATE INDEX calendar_date_mapper_id_index ON calendar_date_mapper(id);
CREATE INDEX journey_mapper_id_index ON journey_mapper(id);
CREATE INDEX journey_mapper_service_id_index ON journey_mapper(gtfs_service_id);
CREATE INDEX route_mapper_id_index ON route_mapper(id);
CREATE INDEX stop_mapper_id_index ON stop_mapper(id);