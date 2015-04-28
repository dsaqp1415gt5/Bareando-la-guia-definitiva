/****************************************
DROP    DATABASE IF EXISTS  bareando;
CREATE  DATABASE            bareando;

USE                         bareando;
*****************************************/

CREATE TABLE bares(
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    nota        INTEGER NOT NULL
);

CREATE TABLE usuarios(
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nick        VARCHAR(25) NOT NULL,
    pass        VARCHAR(25) NOT NULL,
    mail        VARCHAR(30) NOT NULL
);

CREATE TABLE comentarios(
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_bar      INTEGER,
    id_user     INTEGER,
    mensaje     VARCHAR(250) NOT NULL,
    FOREIGN KEY (id_bar)  REFERENCES bares(id),
    FOREIGN KEY (id_user) REFERENCES usuarios(id)
);