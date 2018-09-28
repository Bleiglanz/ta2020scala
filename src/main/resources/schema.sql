CREATE TABLE document (
  id bigserial NOT NULL,
  name varchar(255) NOT NULL,
  doctype varchar(255) NOT NULL,
  fullpath varchar(255) NOT NULL,
  extension varchar(255) NOT NULL,
  size bigint NOT NULL,

  CONSTRAINT pk_document PRIMARY KEY (id)
)
CREATE TABLE mergetable (
  id bigserial NOT NULL,
  s1 varchar(255) NOT NULL,
  s2 varchar(255) NOT NULL,
  s3 varchar(255) NOT NULL,
  s4 varchar(255) NOT NULL,
  s5 varchar(255) NOT NULL,
  s6 varchar(255) NOT NULL,
  s7 varchar(255) NOT NULL,
  s8 varchar(255) NOT NULL,
  s9 varchar(255) NOT NULL,
  s10 varchar(255) NOT NULL,
  s11 varchar(255) NOT NULL,
  s12 varchar(255) NOT NULL,
  s13 varchar(255) NOT NULL,
  s14 varchar(255) NOT NULL,
  s15 varchar(255) NOT NULL,
  s16 varchar(255) NOT NULL,
  s17 varchar(255) NOT NULL,
  s18 varchar(255) NOT NULL,
  s19 varchar(255) NOT NULL,
  s20 varchar(255) NOT NULL,

  CONSTRAINT pk_mergetable PRIMARY KEY (id)
)
