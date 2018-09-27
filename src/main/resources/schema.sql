CREATE TABLE document (
  id bigserial NOT NULL,
  name varchar(255) NOT NULL,
  doctype varchar(255) NOT NULL,
  fullpath varchar(255) NOT NULL,
  extension varchar(255) NOT NULL,
  size bigint NOT NULL,

  CONSTRAINT pk_document PRIMARY KEY (id)
)
