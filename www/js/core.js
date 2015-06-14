var BASE_URL = "http://147.83.7.200:8080/bareando-api";
//var BASE_URL = "http://localhost:8080/bareando-api";

function uriObject (){
    this.url = BASE_URL;
    this.id = 0;
    this.nombre = 0;
    this.minNota = 0;
    this.maxNota = 0;
    this.random = 0;
    this.genero1 = 0;
    this.genero2 = 0;
    this.genero3 = 0;
    this.genero4 = 0;
    this.orden = 0;
    this.perpage = 0;
    this.page = 0;
    this.paginas = 0;
    this.response = 0;

    this.restartUrlParameters = function(){
        this.url = BASE_URL;
        this.id = 0;
        this.nombre = 0;
        this.minNota = 0;
        this.maxNota = 0;
        this.random = 0;
        this.genero1 = 0;
        this.genero2 = 0;
        this.genero3 = 0;
        this.genero4 = 0;
        this.orden = 0;
        this.perpage = 0;
        this.page = 0;
        this.response = 0;
        this.paginas = 0;
        this.updateUrl();
    }

    this.updateUrl = function(){
        this.url = BASE_URL + '/bares/' + this.id + '-' + this.nombre + '-' + this.minNota + '-' + this.maxNota + '-' + this.random + '-' + this.genero1 +
        ',' + this.genero2 + ',' + this.genero3 + ',' + this.genero4 + '-' + this.orden + '-' + this.perpage + '-' + this.page;
        return this.url;
    };

    this.makeGetRequest = function(){
        this.response = makeRequest('GET', this.url);
        return this.response;
    };
}

function makeRequest(type, url){
    var respuesta;
    $.ajax({
        url : url,
        type : type,
        async: false,  
        crossDomain : true,
        dataType : 'json',
    }).done(function(data, status, jqxhr) {
        respuesta = data;
    }).fail(function(jqXHR, textStatus) {
        console.log(textStatus);
        respuesta = textStatus;
    });
    return respuesta;
}

function getComentarios(id){
    var respuesta;
    var urlcmt = BASE_URL + "/comentarios/" + id;
    $.ajax({
        url : urlcmt,
        async: false,  
        crossDomain : true,
        dataType : 'json',
    }).done(function(data, status, jqxhr) {
        respuesta = data;
    }).fail(function(jqXHR, textStatus) {
        console.log(textStatus);
        respuesta = textStatus;
    });
    return respuesta;
}