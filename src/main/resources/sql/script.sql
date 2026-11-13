create database spring_security;

CREATE TABLE users (
                       username VARCHAR(50) NOT NULL PRIMARY KEY,
                       password VARCHAR(500) NOT NULL,
                       enabled BOOLEAN NOT NULL
);
CREATE TABLE authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             CONSTRAINT fk_authorities_users FOREIGN KEY (username)
                                 REFERENCES users (username)
);
create unique index ix_auth_username on authorities (username,authority);


CREATE TABLE customer (
                          id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                          email VARCHAR(45) NOT NULL,
                          pwd VARCHAR(500) NOT NULL,
                          role VARCHAR(45) NOT NULL
);
insert into customer(email,pwd,role)
values ("shayan@gmial.com","{noop}Yrm$1212","Boss");

insert into customer(email,pwd,role)
values ("shayan@gmial.com","{bcrypt}$2a$12$Bk/9RS/WlI4vDC9Hqt0eQ.IkIxfeTjJGMnjkDS.bagfUC3sF5z7j2","Admin");