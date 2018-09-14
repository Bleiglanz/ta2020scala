DROP TABLE users


CREATE TABLE users
(
  id bigserial NOT NULL,
  username VARCHAR NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id)
);

INSERT INTO users(username) values ('anton');