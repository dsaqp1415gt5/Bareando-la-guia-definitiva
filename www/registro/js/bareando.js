var API_BASE_URL = "http://tgrupo5.dsa:8080/bareando-api";
var incorrectNameInput = true;
var incorrectPassInput = true;

$(document).ready(function(){    
    var url = API_BASE_URL + '/usuarios/';

    $("#fin").click(function(e) {
        console.log("click");
        registrar(url);
    });

    $('#inputNick').on('input', function() {
        comprobarNick($('#inputNick').val());
    });

    $('#inputPassDos').on('input', function() {
        if($('#inputPassDos').val() == $('#inputPass').val()){
            $("#inputPassDos").css({ background: "#FFFFFF"});
            $("#inputPass").css({ background: "#FFFFFF"});
            incorrectPassInput = false;
        }else{
            $("#inputPassDos").css({ background: "red"});
            $("#inputPass").css({ background: "red"});
            incorrectPassInput = true;
        }
    });
});

function validateEmail($email) {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
    return emailReg.test( $email );
}

function comprobarCampos(){
    var ok = false;
    var str = $("#inputNombre").val();
    var len = str.length;
    if(len >= 5)
        ok = true;
    else
        ok = false;
    str = $("#inputNick").val();
    len = str.length;
    if(len >= 5 && ok != false)
        ok = true;
    else
        ok = false;
    str = $("#inputMail").val();
    len = str.length;
    if(len >= 10 && ok != false)
        ok = true;
    else
        ok = false;
    str = $("#inputPass").val();
    len = str.length;
    if(len >= 5 && ok != false)
        ok = true;
    else
        ok = false;

    if( !validateEmail(str = $("#inputMail").val())) {
        ok = false;
    }
    return ok;
}

function comprobarNick(nick){
    var urlNick = API_BASE_URL + '/usuarios/' + nick;
    $.ajax({
        url : urlNick,
        type : 'GET',
        crossDomain : true,
        dataType : 'json',
    }).done(function(data, status, jqxhr) {
        incorrectNameInput = data;
        if(incorrectNameInput == true){
            $("#inputNick").css({ background: "red"});
        }else{
            $("#inputNick").css({ background: "#FFFFFF"});
        }
    }).fail(function(jqXHR, textStatus) {
        console.log(textStatus);
    });
}
function registrar(url) {
    if(incorrectNameInput == false && incorrectPassInput == false){  
        var ok = comprobarCampos();
        if(ok == true){
            usuario = {
                "nick" : $('#inputNick').val(),
                "nombre" : $('#inputNombre').val(),
                "pass" : $('#inputPass').val(),
                "mail" : $('#inputMail').val()
            }
            console.log(usuario);
        }else{
            $(".wizard-container").effect("shake", {times:4}, 1000 );
        }
    }else{
        $(".wizard-container").effect( "shake", {times:4}, 1000 );
    }
    /*	
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
	});*/

}