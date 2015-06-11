var apiUrl = new uriObject();
var oneBar = new uriObject();
oneBar.updateUrl();
apiUrl.updateUrl();
var BASE_URL = "http://localhost:8080/bareando-api";
var nick = $.cookie('nick');
var pass = $.cookie('pass');

$(document).ready(function(){
    if(nick != undefined){
        console.log("hola " + nick);
        $("#loged").html("");
        $("#logout").html("<a href='#'>Logout</a>");
    }else{
        console.log("login");
    }
    apiUrl.random = "R";
    apiUrl.updateUrl();
    printBarPrincipal(apiUrl.makeGetRequest());
    apiUrl.restartUrlParameters();

    $('#logout').click(function(){
        if(nick != undefined){
            $.removeCookie('nick');
            $.removeCookie('pass');
            location.reload();
        }
    });

    $('#loginModal').on('shown.bs.modal', function() {
        var initModalHeight = $('#modal-dialog').outerHeight(); 
        var userScreenHeight = $(document).outerHeight();
        if (initModalHeight > userScreenHeight) {
            $('#modal-dialog').css('overflow', 'auto'); 
        } else {
            $('#modal-dialog').css('margin-top', (userScreenHeight / 2) - (initModalHeight)); 
        }
    });

    $("form#data").submit(function(event){
        event.preventDefault();
        var formData = new FormData($(this)[0]);
        $.ajax({
            url: 'http://localhost:8080/bareando-api/foto/upload-makitos',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (returndata) {
                alert(returndata);
            }
        });
        return false;
    });

    $('#tapas, #cervezas, #vinos, #cocktails').click(function(event){
        var genero = event.target.id;
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #lista', function(){
                apiUrl.restartUrlParameters();
                apiUrl.genero = genero;
                apiUrl.perpage = 3;
                apiUrl.updateUrl();
                PrinterBares(apiUrl.makeGetRequest());
                printarPaginasEnPaginacion(apiUrl.paginas);
                startSearchOptions();

                var check = '#' + genero.slice(0, -1);
                $(check).prop( "checked", true );
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
    
    $('#entrar').click(function(){
        var login ={
            "nick":$("#nick").val(),
            "pass":$("#pass").val()
        };
        var data = JSON.stringify(login);
        var urllogin = BASE_URL + '/usuarios/login';
        $.ajax({
            url: urllogin,
            type: 'POST',
            data: data,
            async: false,  
            crossDomain : true,
            dataType : 'json',
            contentType: 'application/vnd.bareando.api.user+json',
        }).done(function(data, status, jqxhr) {
            if(data.loginSuccessful == true){
                $.cookie('nick', $("#nick").val());
                $.cookie('pass', $("#pass").val());
                location.reload();
            }else{
                console.log("error login");
            }
        }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            respuesta = textStatus;
        });
    });

    $('#buscar').click(function(){
        var nombre = $('#busqueda').val();
        if (nombre == "")
            nombre = "0";
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #lista', function(){
                apiUrl.restartUrlParameters();
                apiUrl.nombre = nombre;
                apiUrl.perpage = 3;
                apiUrl.updateUrl();
                PrinterBares(apiUrl.makeGetRequest());
                printarPaginasEnPaginacion(apiUrl.paginas);
                startSearchOptions();
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });

    $('a').click(function(event){
        var idbar = event.target.id;
        console.log(idbar);
        /*$(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #descrip', function(){
                oneBar.restartUrlParameters();
                oneBar.id = idBar;
                oneBar.updateUrl();
                printarBarDescripcion(oneBar.makeGetRequest());
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");*/
    });
    $('.inicio').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #home', function(){
                apiUrl.restartUrlParameters();
                apiUrl.random = "R";
                apiUrl.updateUrl();
                printBarPrincipal(apiUrl.makeGetRequest());
                $('.descr').click(function(event){
                    var idBar = event.target.id;
                    console.log(idBar);
                    $(".se-pre-con").fadeIn("fast", function(){
                        $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
                        $("#cuerpo").load('bares.html #tresdos', function(response, status, xhr){
                            console.log(response);
                            console.log(status);
                            console.log(xhr);
                            oneBar.restartUrlParameters();
                            oneBar.id = idBar;
                            oneBar.updateUrl();
                            printarBarDescripcion(oneBar.makeGetRequest());
                            initialize();
                        });
                    });
                    $(".se-pre-con").delay(700).fadeOut("slow");
                });
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
    $('.descr').click(function(event){
        var idBar = event.target.id;
        console.log(idBar);
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #tresdos', function(response, status, xhr){
                console.log(response);
                console.log(status);
                console.log(xhr);
                oneBar.restartUrlParameters();
                oneBar.id = idBar;
                oneBar.updateUrl();
                printarBarDescripcion(oneBar.makeGetRequest());
                initialize();
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });

});
function printarBarDescripcion(bar){
    console.log(bar.bares[0].ID);
    $("#fotoBar").attr("src", "http://147.83.7.200/tgrupo5.dsa/public_html/img/bares/" + bar.bares[0].ID +".jpg");
    $(".titulo").html(bar.bares[0].nombre);
    $("#cosas").html(bar.bares[0].descripcion + "<br /><div><h3 style='display:inline'>Nota:    </h3><h2 style='display:inline' class='nota'>"+bar.bares[0].nota+"</h2></div>");
    printComentarios(bar.bares[0].ID);
}
function initialize() {
    var mapCanvas = document.getElementById('mapa');
    var mapOptions = {
        center: new google.maps.LatLng(44.5403, -78.5463),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions);
}

function printComentarios(barId){
    /*<div class='media'><a class='pull-left' href='#'><img class='media-object' src='img/user.jpg' alt='Media Object'></a><div class='media-body'><h4 class='media-heading'>username</h4>comentario</div></div>*/
    var response = getComentarios(barId);
    var htmlcmt = ""; 
    $.each(response.comentarios, function(i, v) {
        //console.log(v);
        var comentario = v;
        htmlcmt+="<div class='media'><a class='pull-left' href='#'><img class='media-object' src='img/user.jpg' alt='Media Object'></a><div class='media-body'><h4 class='media-heading'>";
        htmlcmt+=v.nick;
        htmlcmt+="</h4>";
        htmlcmt+=v.mensaje;
        htmlcmt+="</div></div>";
    });
    if(nick != undefined){
        htmlcmt+="<h3>Añadir comentario</h3><textarea rows='6' cols='60' class='rta' id='ta_sample' name='ta_sample' placeholder='Escribe tu comentario aqui...'></textarea><button id='enviarComentario'> Enviar</button>";
    }
    $("#comentarios").html(htmlcmt);
    $.rta();
    
    $('#enviarComentario').click(function(){
        console.log("pepepepepe");
    });
}
function startSearchOptions(){
    $("#range").ionRangeSlider({
        hide_min_max: true,
        keyboard: true,
        min: 0,
        max: 10 ,
        from: 0,
        to: 10,
        type: 'int',
        step: 1,
        prefix: "Nota:",
        grid: true,
        grid_num: 2.5,
        onFinish: function (data) {
            apiUrl.minNota = $("#range").data("ionRangeSlider").old_from; 
            apiUrl.maxNota = $("#range").data("ionRangeSlider").old_to; 
            apiUrl.updateUrl();
            PrinterBares(apiUrl.makeGetRequest());
        },
    });
    $('#tapa, #cerveza, #vino, #cocktail').click(function(event){
        //event.target.id;
    });
}

function printarPaginasEnPaginacion(paginas){
    //tipo 1 --> genero
    //tipo 2 --> nombre
    var pagina;
    $(".pagination").html("");
    for (i = 0; i < paginas; i++) { 
        pagina = i+1;
        $(".pagination").append("<li><a onClick=\"page('" + i + "')\">" + pagina + "</a></li>");
    }
}
function page(i){
    apiUrl.page = i;
    apiUrl.updateUrl();
    PrinterBares(apiUrl.makeGetRequest());
}

function PrinterBares(objeto){
    this.bar = objeto;
    var instance = this;
    apiUrl.paginas = this.bar.paginas;
    $("#pepe").html("");
    var stringHtml = "";
    $.each(this.bar, function(i, v) {
        this.bares = v;
        if(v.length == 0)
            $("#pepe").html("La busqueda no ha dado resultados");
        $.each(this.bares, function(i, v) {
            var appender;
            var BAR = v;
            var id = BAR.ID;
            var nombre = BAR.nombre;
            if(nombre != undefined){
                var descr = BAR.descripcion;
                var nota = BAR.nota;
                appender = stringHtml.concat("<div class='row'><div class='col-md-7'><img class='img-responsive' src='http://147.83.7.200/tgrupo5.dsa/public_html/img/bares/" + id + ".jpg' alt=''></div><div class='col-md-5'>      <h3>", nombre, "</h3><h4>Nota: ", nota, "</h4><p>", descr, "</p><a id='"+id+"' class='btn btn-primary descr' href='#'>Mas detalles...<span class='glyphicon glyphicon-chevron-right'></span></a></div></div><hr>");
                $("#pepe").append(appender);
            }
        });      
    });
    $('.descr').click(function(event){
        var idBar = event.target.id;
        console.log(idBar);
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #tresdos', function(response, status, xhr){
                console.log(response);
                console.log(status);
                console.log(xhr);
                oneBar.restartUrlParameters();
                oneBar.id = idBar;
                oneBar.updateUrl();
                printarBarDescripcion(oneBar.makeGetRequest());
                initialize();
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
}


function PrinterBarPrincipal(objeto){
    this.bar = objeto;
    var instance = this;

    this.printBar = function(){
        $.each(this.bar, function(i, v) {
            var BAR = v;
            var nombre = BAR[0].nombre;
            var descr = BAR[0].descripcion;
            var nota = BAR[0].nota;
            var id = BAR[0].ID;
            $(".titulo").html(nombre);
            $(".descripcion").html(descr + "<br><br><a id='"+id+"' class='btn btn-primary descr' href='#'>Mas detalles...<span class='glyphicon glyphicon-chevron-right'></span></a>");
            $(".nota").html(nota);
            document.getElementById("photo").src = "http://147.83.7.200/tgrupo5.dsa/public_html/img/bares/" + BAR[0].ID + ".jpg";
        });
    }

}

function printBarPrincipal(objeto){
    this.bar = objeto;
    var instance = this;

    $.each(this.bar, function(i, v) {
        if(i == "bares"){
            var BAR = v;
            var nombre = BAR[0].nombre;
            var descr = BAR[0].descripcion;
            var nota = BAR[0].nota;
            var id = BAR[0].ID;
            console.log($(".titulo").html());
            $(".titulo").html(nombre);
            $(".descripcion").html(descr + "<br><br><a id='"+id+"' class='btn btn-primary descr' href='#'>Mas detalles...<span class='glyphicon glyphicon-chevron-right'></span></a>");
            $(".nota").html(nota);
            document.getElementById("photo").src = "http://147.83.7.200/tgrupo5.dsa/public_html/img/bares/" + BAR[0].ID + ".jpg";
        }
    });
}