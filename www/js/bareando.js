var API_BASE_URL = "http://tgrupo5.dsa:8080/bareando-api";

$(document).ready(function(){    
    var x = Math.floor((Math.random() * 3) + 1);
	var url = API_BASE_URL + '/bares/' + x + '-0-0-0';
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
    
});

function PrinterBarPrincipal(objeto){
	this.bar = objeto;
	var instance = this;

	/*this.buildLinks = function(header){
		if (header != null ) {
			this.links = weblinking.parseHeader(header);
		} else {
			this.links = weblinking.parseHeader('');
		}
	}

	this.getLink = function(rel){
                return this.links.getLinkValuesByRel(rel);
	}
    */
    
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