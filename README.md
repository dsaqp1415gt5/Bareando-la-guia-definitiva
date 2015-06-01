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
- url = url + /id\_bar-pagina --> id\_bar un numero, pagina 0 por defecto
- Content-type --> application/vnd.bareando.api.comentario.collection+json
- id_bar hace referencia a la id de un bar, :|
- Te devuelve una coleccion de 10 comentarios por pagina referente al bar del que has puesto la id.
- Uno de los parametros que devuelve es el numero de paginas, 0 es que no hay.

###DELETE
- Igual que el get, pero lo borra
- El id en este caso es del comentario en cuestion
- {
    "message": "001",
    "status": 404
} --> esto indica el mensaje de error personalizado, 001 es que no hay comntario con tal ID