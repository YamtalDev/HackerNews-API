CREATE TABLE IF NOT EXISTS Content
(
    id INTEGER AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    desc text,
    status VARCHAR(20) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    data_created TIMESTAMP NOT NULL,
    data_updated TIMESTAMP,
    url VARCHAR(255),
    primary key (id)
);

INSERT INTO Content(title,desc,status,content_type, data_created)
VALUES ('My spring','A post about','IDEA','ARTICLE',CURRENT_TIMESTAMP)