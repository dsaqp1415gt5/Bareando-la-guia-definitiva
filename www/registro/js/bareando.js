var API_BASE_URL = "http://localhost:8080/bareando-api";
//var API_BASE_URL = "http://147.83.7.200:8080/bareando-api
//var BASE_URL = "http://147.83.7.200:8080/bareando-api";
var BASE_URL = "http://localhost:8080/bareando-api";
var incorrectNameInput = true;
var incorrectPassInput = true;

$(document).ready(function(){    
    var url = API_BASE_URL + '/usuarios/';

    $("#fin").click(function(e) {
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

    $("form#regis").submit(function(event){
        event.preventDefault();
        var ok = comprobarCampos();
        if(ok == true){
            var formData = new FormData($("form#regis")[0]);
            var nick = $('#inputNick').val();
            console.log(nick);
            $.ajax({
                url: BASE_URL + '/foto/upload-'+ nick,
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                processData: false,
                contentType: false,
            }).done(function(data, status, jqxhr) {
                console.log(status);
            }).fail(function(jqXHR, textStatus) {
                console.log(textStatus);
                console.log(jqXHR);
            });
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

            var data = JSON.stringify(usuario);

            $.ajax({
                url : url,
                type : 'POST',
                crossDomain : true,
                contentType : "application/vnd.bareando.api.user+json",
                dataType : 'json',
                data : data,
            }).done(function(data, status, jqxhr) {
                $(".wizard-container").html("Registrado correctamente, espere un momento...");
                setTimeout(function(){
                    $(location).attr('href', "../index.html");
                }, 4000);
            }).fail(function() {
                $(".wizard-container").effect("shake", {times:4}, 1000 );
            });

        }else{
            $(".wizard-container").effect("shake", {times:4}, 1000 );
        }
    }else{
        $(".wizard-container").effect( "shake", {times:4}, 1000 );
    }

}