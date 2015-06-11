-- MySQL dump 10.13  Distrib 5.6.22, for Win64 (x86_64)
--
-- Host: localhost    Database: bareando
-- ------------------------------------------------------
-- Server version	5.6.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bares`
--

DROP TABLE IF EXISTS `bares`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bares` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(250) NOT NULL,
  `nota` int(11) NOT NULL,
  `genero` varchar(50) NOT NULL,
  `lat` varchar(15) DEFAULT NULL,
  `lon` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bares`
--

LOCK TABLES `bares` WRITE;
/*!40000 ALTER TABLE `bares` DISABLE KEYS */;
INSERT INTO `bares` VALUES (8,'Ginger','Abre cada noche su barra esférica de vendedora de cerillas y sonríe maliciosa dentro de un vestido negro. Tiene un rótulo antiguo y americano con la tipografía que imagino tenían los carteles del Gran Gatsby.',6,'Cocktails','41.382806','2.178643'),(9,'Vila Vinateca','En pleno Born encontraréis esta tienda repleta de propuestas nacionales e internacionales y una de las distribuidoras más importantes de Europa. También disponen de un espacio gastronómico.',5,'Vinos','41.382536','2.181528'),(16,'Jonny Aldana','En el Jonny Aldana son unos superdotados. Además de conseguir servir las tapas vegetarianas más sabrosas del planeta, también tiran bien la caña. No les importa invertir un minuto, saben que sólo así se pueden conseguir los dos dedos de espuma.',6,'Cervezas','41.375819','2.166122'),(20,'Els Sortidors del Parlament','Los amantes del buen beber acompañado de buena comida serán felices. Aquí se ofrece una increíble selección de vinos de todo el mundo y una importante sección de vinos catalanes entre los que destacan los del norte de Cataluña.',4,'Vinos','41.377453','2.163769'),(21,'Dry Martini','La legendaria barra de esta coctelería reconocida internacionalmente como una de las mejores del planeta. Son muy listos. Aquí se venera al cóctel. Y es que, señores, eso no es una coctelería, es una iglesia.',9,'Cocktails','41.392705','2.153979'),(22,'Viduca','Una vez nos adentramos en el comedor, descubriremos incluso una pequeña barra donde preparan gin-tonics. La lista de vinos no se queda atrás. Si se toman tantas molestias en hacerlo todo bien, imaginaos qué pueden hacer para servir una simple caña.',7,'Cervezas','41.378022','2.143334'),(29,'Quimet i Quimet','Lo dicen todas las guías del mundo y siento repetirme pero Quimet y Quimet es, bueno, para que te salten las lágrimas. Tienen cerveza propia, sirven el mejor vermut de grifo del planeta y ofrecen una variedad de vinos que llega hasta el techo.',8,'Tapas','41.373949','2.165603'),(31,'El Xampanyet','El cava y el vermut riegan las tapas sencillas pero efectivas de este sitio que acoge tanto a turistas como a nativos.',5,'Tapas','41.384515','2.181582'),(32,'El Nus','A parte de un par de tiradores con cerveza excelente, podréis entreteneros mirando las paredes de este bar cargadas de historia mientras os tiran la caña, rubia, tostada o negra. De las otras ‘tiradas de caña’ no nos ocupamos.',7,'Cervezas','41.384194','2.180694'),(33,'Cata 1.81','Un lugar ideal para todo tipo de consumidores, tanto para los neófitos como para los más sibaritas. Sus platos, de cocina de mercado, acompañan los vinos sin eclipsarlos.',4,'Vinos','41.388917','2.158103'),(34,'Semon 9 Night','El Semon 9 continúa la fiesta hasta las dos de la madrugada donde podréis tomar un cóctel o comer algo ligero, ya sea porque acabáis de salir de la oficina o porque si. Ambiente clásico y público que busca un ambiente moderadamente bandarra.',5,'Cocktails','41.394436','2.137219'),(35,'Las Delicias',' La terraza del Delicias se sujeta de milagro. Nace torcida y termina de la misma manera porque, como todo en el Carmel, está en una cuesta ¿Y qué importa? Las desgracias se olvidan con un boquerón en la mano y hacen unos calamarcitos buenísimos',7,'Tapas','41.418294','2.157549'),(36,'El Ben Plantat','Para tomarse una caña en este nuevo local de la Barceloneta no es necesario llevar el carnet con la estelada. El Ben Plantat es un local pequeño pero muy bien aprovechado donde probaremos unas buenas cañas con tapas sorprendentes.',8,'Cervezas','41.379489','2.190583'),(37,'Cocteleria Tahití','Camareros de cabello repeinado y corbata estrecha sirven cócteles, la mayoría basados en el recetario tradicional de la cultura tiki. ',6,'Cocktails','41.382637','2.165696'),(38,'Can Cisa','Hay unas 300 referencias de vinos, todos ecológicos o biodinámicos de todo el mundo, sin ninguna química ni ningún tipo de aditivo, y a precios medios, en una franja muy interesante. Una taberna de estilo antiguo que vende vermut a granel. ',4,'Vinos','41.384839','2.179921'),(39,'El Vaso de Oro','Vale la pena por la calidad y variedad de sus cervezas. Y de sus tapas: bravas, anchoas, albóndigas con sepia son los mejores amigos de la cerveza.',3,'Tapas','41.381901','2.187127'),(40,'Sa Cava','Rock duro y cervezas que demuestran la misma consistencia, esto es lo que ofrece Sa Cava. En este bar de Sants no se conforman con servir sólo un tipo de birra. La cerveza sale de unos cuantos tiradores.',6,'Cervezas','41.376181','2.138351'),(41,'La Parra','Este restaurante de Hostafrancs se dedica desde sus inicios, hace ya más de 250 años, a la cocina catalana y los productos de temporada. En su carta encontramos ternera de Girona, los pollos picantones, las costillas de cordero o la escalibada.',5,'Vinos','41.375062','2.140928'),(42,'Twist','Sarrià de noche puede parecer un escenario postnuclear estilo Mad Max. La coctelería Twist es la gran excepción, un desconocido en las cumbres pijas de la ciudad que merece ser descubierto. Es muy sencillo, hacen cócteles al nivel de los grandes.',9,'Cocktails','41.395618','2.127676'),(43,'El Jabalí','Este bar-charcutería, con aires de Paral·lel clásico, es un lugar inmejorable para comer unas tapas superlativas -maravillosas bravas y ensalada de pollo- y probar el buen vino y embutido. Así como para apoyarse en la barra y dedicarse a observar.',8,'Tapas','41.375761','2.166598'),(44,'Cosme Vins','Una carismática bodega con todos los clásicos y también un apartado gastronómico, con embutidos, conservas, caviar y chocolates.',8,'Vinos','41.398526','2.146655'),(45,'Marídame','Al inicio de la calle Santaló, encontrarás Marídame, una especie de agencia matrimonial entre los vinos y la buena comida. A partir de etiquetas de diversos colores que indican si un vino es fresco o afrutado, podéis jugar a las parejas.',9,'Vinos','41.394670','2.146809'),(46,'Bar Rosso','En esta coctelería muy abrigada, encontraréis una oferta de cócteles de calidad, creativos, y un trato exquisito con el cliente.',5,'Cocktails','41.379090','2.160465'),(47,'El Paraigua','Este local subterráneo, detrás de la Plaça de Sant Jaume, es todo un clásico. Pero no es un clásico quieto: Jose, el propietario, ha encontrado una manera diferente de navegar por la coctelería clásica: ¡gimlet con tequila, whisky sour con absenta!',6,'Cocktails','41.381851','2.176699'),(48,'La Taverna del Clínic','Bar de tapas, creativas y no, con buenos quesos y cerveza gallega. Sus bravas son una obra de arte gastronómica.',4,'Tapas','41.388842','2.151253'),(49,'Gata Mala','Sirven unas de las mejores tapas de Barcelona y, además, cuando pides cualquier consumición, te regalan una tapa. Vaya, como para plantar una tienda de campaña delante.',7,'Tapas','41.407891','2.157665'),(50,'Bar Ramón','¿A Chuck Berry le gustaba la cerveza? No lo sé, pero le dedicó ‘Beer drinkin’ woman’ a una señorita a la que le gustaba la bebida. En el Bar Ramón son fans de las dos cosas, tanto del blues como de la buena birra. ',6,'Cervezas','41.378673','2.160631'),(51,'Granja Elena','En la Granja Elena, con una croqueta de cocido entre vuestros dedos y una caña de la casa, declararéis la república y vuestro amor al primero que pase.',7,'Cervezas','41.362701','2.137387'),(52,'La Sede','No son los únicos que empezaron con un blog y han acabado montando un bar. Este local de Sarrià nace a partir de www.baresautenticos.com, una web que ha perseguido los rincones de la Península para probar la mejor anchoa y la caña para acompañar.',5,'Cervezas','41.395344','2.143351'),(53,'Aigua del Carmen','Aigua del Carmen, remedio universal. Nuestros abuelos tenían suficiente con un traguito para combatir flemones, migrañas o cualquier achaque de estar por casa. Quien les iba a decir que, esta poción ancestral daría nombre a una coctelería premium.',3,'Cocktails','41.400163','2.164398');
/*!40000 ALTER TABLE `bares` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comentarios`
--

DROP TABLE IF EXISTS `comentarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comentarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nick` varchar(25) NOT NULL,
  `id_bar` int(11) DEFAULT NULL,
  `mensaje` varchar(250) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_bar` (`id_bar`),
  KEY `nick` (`nick`),
  CONSTRAINT `comentarios_ibfk_1` FOREIGN KEY (`id_bar`) REFERENCES `bares` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comentarios_ibfk_2` FOREIGN KEY (`nick`) REFERENCES `usuarios` (`nick`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios`
--

LOCK TABLES `comentarios` WRITE;
/*!40000 ALTER TABLE `comentarios` DISABLE KEYS */;
INSERT INTO `comentarios` VALUES (9,'adricouci',9,'poooor queeeeeeeee????'),(10,'makitos666',9,'WTF?!?!?'),(11,'adricouci',9,'like a siir'),(12,'makitos666',9,'hfiuadekhgjei irjae i iewjlfk flwl oooooo'),(13,'melendioo',9,'muchos monos, genial!'),(14,'adricouci',9,'poooor queeeeeeeee????'),(15,'makitos666',9,'WTF?!?!?'),(16,'adricouci',9,'like a siir'),(17,'makitos666',9,'hfiuadekhgjei irjae i iewjlfk flwl oooooo'),(18,'melendioo',9,'muchos monos, genial!'),(19,'adricouci',9,'poooor queeeeeeeee????'),(20,'makitos666',9,'WTF?!?!?'),(21,'adricouci',9,'like a siir'),(22,'makitos666',9,'hfiuadekhgjei irjae i iewjlfk flwl oooooo'),(23,'melendioo',9,'muchos monos, genial!'),(24,'adricouci',9,'poooor queeeeeeeee????'),(25,'makitos666',9,'WTF?!?!?'),(26,'makitos666',9,'hfiuadekhgjei irjae i iewjlfk flwl oooooo');
/*!40000 ALTER TABLE `comentarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `nick` varchar(25) NOT NULL,
  `rolename` varchar(20) NOT NULL,
  PRIMARY KEY (`nick`,`rolename`),
  CONSTRAINT `user_roles_ibfk_1` FOREIGN KEY (`nick`) REFERENCES `usuarios` (`nick`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES ('adricouci','admin'),('juasjuas','registrado'),('makitos666','admin'),('melendioo','registrado'),('nick','registrado');
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `nick` varchar(25) NOT NULL,
  `nombre` varchar(25) NOT NULL,
  `pass` varchar(32) NOT NULL,
  `mail` varchar(30) NOT NULL,
  PRIMARY KEY (`nick`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('adricouci','adri','f2ffad9a275afdbf109cc5920266bcb8','acouceiro@gmail.com'),('juasjuas','pedrolo','bf9f4637d8929473926bb14bd58266d1','pedrolo@juas.com'),('makitos666','marc','a6da7ad00ceaaa5c289b7428131d37be','makitos666@gmail.com'),('melendioo','melendi','c94a2cad6a860bc6d5e56141cde60f26','marihuana33@yahoo.com'),('nick','name','1a1dc91c907325c69271ddf0c944bc72','mnail@mailer.com');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-06-11 19:40:07
