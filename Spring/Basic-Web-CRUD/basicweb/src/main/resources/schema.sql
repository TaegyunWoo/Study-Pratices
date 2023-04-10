CREATE TABLE user (
    ID BIGINT(5) NOT NULL AUTO_INCREMENT,
    EMAIL VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE article (
    ID BIGINT(5) NOT NULL AUTO_INCREMENT,
    TITLE VARCHAR(255) NOT NULL,
    CONTENT TEXT NOT NULL,
    WRITER BIGINT(5) NOT NULL,
    DATETIME TIMESTAMP NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY(WRITER) REFERENCES user(ID)
);