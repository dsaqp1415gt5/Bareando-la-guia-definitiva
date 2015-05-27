var API_BASE_URL = "http://147.83.7.200:8080/bareando-api";

$(document).ready(function(){    
    var url = API_BASE_URL + '/bares/0-0-0-0-R-0-0';
    getBar(url);

    $('#loginModal').on('shown.bs.modal', function() {
        var initModalHeight = $('#modal-dialog').outerHeight(); //give an id to .mobile-dialog
        var userScreenHeight = $(document).outerHeight();
        if (initModalHeight > userScreenHeight) {
            $('#modal-dialog').css('overflow', 'auto'); //set to overflow if no fit
        } else {
            $('#modal-dialog').css('margin-top', 
                                   (userScreenHeight / 2) - (initModalHeight)); //center it if it does fit
            console.log(initModalHeight);
        }
    });

    $('#tapas').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.php?link=tapas');
            getBarByGenero("tapas");
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
    $('#cervezas').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.php?link=tapas');
            getBarByGenero("cervezas");
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
    $('#vinos').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.php?link=tapas');
            getBarByGenero("vinos");
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
    $('#cocktails').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.php?link=tapas');
            getBarByGenero("cocktails");
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });
    $('.inicio').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.php?link=home');
            getBar(url);
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });

});

function getBarByGenero(genero){
    var url = API_BASE_URL + '/bares/genero:' + genero;
    $.ajax({
        url : url,
        type : 'GET',
        crossDomain : true,
        dataType : 'json',
    }).done(function(data, status, jqxhr) {
        var response = data;
        console.log(response);
        var imprimirBares = new PrinterBares(response);
        imprimirBares.printBares();
    }).fail(function(jqXHR, textStatus) {
        console.log(textStatus);
    });
}

/*
<div class="row"><div class="col-md-7"><img class="img-responsive" src="http://placehold.it/700x300" alt=""></div><div class="col-md-5">      <h3>tiulo bar</h3><h4>Nota: nota</h4><p>Descripcion</p><a class="btn btn-primary" href="#">Mas detalles...<span class="glyphicon glyphicon-chevron-right"></span></a></div></div><hr>
*/
function PrinterBares(objeto){
    this.bar = objeto;
    var instance = this;

    this.printBares = function(){
        var stringHtml = "";
        $.each(this.bar, function(i, v) {
            this.bares = v;
            $.each(this.bares, function(i, v) {
                var appender;
                var BAR = v;
                var nombre = BAR.nombre;
                if(nombre != undefined){
                    var descr = BAR.descripcion;
                    var nota = BAR.nota;
                    appender = stringHtml.concat("<div class='row'><div class='col-md-7'><img class='img-responsive' src='http://placehold.it/700x300' alt=''></div><div class='col-md-5'>      <h3>", nombre, "</h3><h4>Nota: ", nota, "</h4><p>", descr, "</p><a class='btn btn-primary' href='#'>Mas detalles...<span class='glyphicon glyphicon-chevron-right'></span></a></div></div><hr>");
                    $("#pepe").append(appender);
                }
            });      
        });
    }
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
            $(".titulo").html(nombre);
            $(".descripcion").html(descr);
            $(".nota").html(nota);
        });
    }

}

function getBar(url) {
    $.ajax({
        url : url,
        type : 'GET',
        crossDomain : true,
        dataType : 'json',
    }).done(function(data, status, jqxhr) {
        var response = data;
        console.log(response);
        var imprimirBar = new PrinterBarPrincipal(response);
        imprimirBar.printBar();
    }).fail(function(jqXHR, textStatus) {
        console.log(textStatus);
    });
}