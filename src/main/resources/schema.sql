CREATE TABLE dokumente (
  id bigserial NOT NULL,
  name varchar(2000) NOT NULL,
  fullpath varchar(255) NOT NULL,
  extension varchar(255) NOT NULL,
  size bigint NOT NULL,

  CONSTRAINT pk_dokumente PRIMARY KEY (id)
)
