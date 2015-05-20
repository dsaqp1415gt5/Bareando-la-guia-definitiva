/****************************************
DROP    DATABASE IF EXISTS  bareando;
CREATE  DATABASE            bareando;

USE                         bareando;
*****************************************/

CREATE TABLE bares(
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    nota        INTEGER NOT NULL,
    genero		VARCHAR(50) NOT NULL
);

CREATE TABLE usuarios(
    nick        VARCHAR(25) NOT NULL PRIMARY KEY,
    nombre		VARCHAR(25) NOT NULL,
    pass        VARCHAR(32) NOT NULL,
    mail        VARCHAR(30) NOT NULL
);

CREATE TABLE comentarios(
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nick        VARCHAR(25) NOT NULL,
    id_bar      INTEGER,
    mensaje     VARCHAR(250) NOT NULL,
    FOREIGN KEY (id_bar)  REFERENCES bares(id),
    FOREIGN KEY (nick) REFERENCES usuarios(nick)
);

CREATE TABLE user_roles (
    nick VARCHAR(25) NOT NULL,
    rolename VARCHAR(20) NOT NULL,
    FOREIGN KEY (nick)
        REFERENCES usuarios (nick)
        ON DELETE CASCADE,
    PRIMARY KEY (nick, rolename)
); 	