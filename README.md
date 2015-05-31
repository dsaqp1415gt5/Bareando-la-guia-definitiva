# Comentarios
- url: *:8080/bareando-api/comentarios

###POST:
- Content-type --> application/vnd.bareando.api.comentario.collection+json
- raw: {"id":"0","nick":"makitos666","id_bar":"8","mensaje":"la mona chita mola"}
- el id es indiferente, no se tiene en cuenta
- nick debe existir como user regitrado, evidentemente
- id_bar, tambien debe ser el id de un bar existente, no comentaras un bar que no existe
mensaje, max 250 caracteres xD
- retorna el mismo comentario que has añadido, cualquier otra cosa que no sea eso es que algo has hecho mal.

###GET
- url = url + /id_bar --> id_bar un numero
- Content-type --> application/vnd.bareando.api.comentario.collection+json
- id_bar hace referencia a la id de un bar, :|
- te devuelve una coleccion de comentarios referente al bar del que has puesto la id, tambien paginable, aunque aun no lo hace...
