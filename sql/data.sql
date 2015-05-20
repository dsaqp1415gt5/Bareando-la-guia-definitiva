INSERT INTO bares       VALUES (null, "chitano", "un gran bar de monos", 6, "cerveza");
INSERT INTO bares       VALUES (null, "tito roberts", "muy aburrido", 2, "copas");
INSERT INTO bares       VALUES (null, "vinooooooos", "wiiiiiiiiiiiiiiiiiii", 9, "vinos");

INSERT INTO usuarios    VALUES ("makitos666", "marc", MD5("666makitos"), "makitos666@gmail.com");
INSERT INTO usuarios    VALUES ("adricouci", "adri", MD5("makitos666"), "acouceiro@gmail.com");
INSERT INTO usuarios    VALUES ("melendioo", "melendi", MD5("perroflauta"), "marihuana33@yahoo.com");

INSERT INTO comentarios VALUES (null, "melendioo", 1, "muchos monos, genial!");
INSERT INTO comentarios VALUES (null, "adricouci", 2, "poooor queeeeeeeee????");
INSERT INTO comentarios VALUES (null, "makitos666", 1, "WTF?!?!?");
INSERT INTO comentarios VALUES (null, "adricouci", 3, "like a siir");
INSERT INTO comentarios VALUES (null, "makitos666", 2, "hfiuadekhgjei irjae i iewjlfk flwl oooooo");

INSERT INTO user_roles VALUES ("adricouci","admin");
INSERT INTO user_roles VALUES ("makitos666","admin");
INSERT INTO user_roles VALUES ("melendioo","registrado");