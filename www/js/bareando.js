var apiUrl = new uriObject();
apiUrl.updateUrl();

$(document).ready(function(){    
    apiUrl.random = "R";
    apiUrl.updateUrl();
    printBarPrincipal(apiUrl.makeGetRequest());
    apiUrl.restartUrlParameters();

    $('#loginModal').on('shown.bs.modal', function() {
        var initModalHeight = $('#modal-dialog').outerHeight(); 
        var userScreenHeight = $(document).outerHeight();
        if (initModalHeight > userScreenHeight) {
            $('#modal-dialog').css('overflow', 'auto'); 
        } else {
            $('#modal-dialog').css('margin-top', (userScreenHeight / 2) - (initModalHeight)); 
        }
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
    $('.inicio').click(function(){
        $(".se-pre-con").fadeIn("fast", function(){
            $('#cuerpo').html("<div style='width:100%; height: 100%; background-color: white;'></div>");
            $("#cuerpo").load('bares.html #home', function(){
                apiUrl.restartUrlParameters();
                apiUrl.random = "R";
                apiUrl.updateUrl();
                printBarPrincipal(apiUrl.makeGetRequest());
            });
        });
        $(".se-pre-con").delay(700).fadeOut("slow");
    });

});

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

function printBarPrincipal(objeto){
    this.bar = objeto;
    var instance = this;

    $.each(this.bar, function(i, v) {
        if(i == "bares"){
            var BAR = v;
            var nombre = BAR[0].nombre;
            var descr = BAR[0].descripcion;
            var nota = BAR[0].nota;
            console.log($(".titulo").html());
            $(".titulo").html(nombre);
            $(".descripcion").html(descr);
            $(".nota").html(nota);
        }
    });
}