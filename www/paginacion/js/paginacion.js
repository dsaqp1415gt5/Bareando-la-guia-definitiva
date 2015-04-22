var API_BASE_URL = "https://api.github.com";
var USERNAME = "makitos666";
var PASSWORD = "morolandia666";

$.ajaxSetup({
    headers: { 'Authorization': "Basic "+ btoa(USERNAME+':'+PASSWORD) }
});

$(document).ready(function(){
	var url = API_BASE_URL + '/users/' + USERNAME + '/gists?per_page=2';
	getRepos(url);

    $("#botoncreate").click(function(e) {
    console.log("click");
	e.preventDefault();
    var newGist ;
    if($('#descripcion_create').val() == "" || $('#contenido_create').val()== ""){
		$('<div class="alert alert-info">Debes rellenar los campos descripcion y contenido</div>').appendTo($("#create_result"));
    }else{
        var filename = $('#nombre_create').val();
		newGist = {
			"description" : $('#descripcion_create').val(),
			"public" : true,
			"files":{
				 "archivo1.txt" :  {
					"content" : $('#contenido_create').val()
				}
			}
		}
        console.log(newGist);
		createGist(newGist);
	}
});

    $("#botoneditar").click(function(e){
	e.preventDefault();
	if($('#id_edit').val() == ""){
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Debes proporcionar una ID </div>').appendTo($("#update_result"));
	}else{
		getGistToEdit($('#id_edit').val());
	}
});

$("#botonguardar").click(function(e){
	e.preventDefault();
	var gistEditado;
	if($('#descripcion_edit').val() == "" || $('#contenido_edit').val()==""){
		$('<div class="alert alert-info">Debes rellenar los campos descripcion y contenido </div>').appendTo($("#update_result"));
	}else{
		gistEditado = {
			"description" : $("#descripcion_edit").val(),
			"files" :{
				"archivo1" : {
					"content" : $("#contenido_edit").val()
				}
			}
		}
		editGist(gistEditado);
	}
});
    $("#botoneliminar").click(function(e) {
	e.preventDefault();
    if($('#id_delete').val() ==""){
		$('<div class="alert alert-info">Debes proporcionar una ID</div>').appendTo($("#delete_result"));
	}else{
		deleteGist($("#id_delete").val());
	}
});
});






function RepoCollection(repoCollection){
	this.repos = repoCollection;
	var instance = this;

	this.buildLinks = function(header){
		if (header != null ) {
			this.links = weblinking.parseHeader(header);
		} else {
			this.links = weblinking.parseHeader('');
		}
	}

	this.getLink = function(rel){
                return this.links.getLinkValuesByRel(rel);
	}

    this.toHTML = function(){
		var html = '';
		$.each(this.repos, function(i, v) {
			var repo = v;
            html = html.concat("<div class='row'><div class='col-md-7'><a href='#'><img class='img-responsive' src='" + repo.owner.avatar_url + "' alt=''></a></div><div class='col-md-5'><h3>"+ repo.description +"</h3> Gist id <p>" + repo.id + "</p></div></div><hr>");

		});

		html = html.concat(' <br> ');

        var prev = this.getLink('prev');

        if (prev.length == 1) {
			$("#previous").html(' <a onClick="getRepos(\'' + prev[0].href + '\');" style="cursor: pointer; cursor: hand;">Previous</a> ');
		}

        var next = this.getLink('next');

		if (next.length == 1) {
			$("#next").html(' <a onClick="getRepos(\'' + next[0].href + '\');" style="cursor: pointer; cursor: hand;">Next</a> ');
		}

 		return html;
	}

}

function getRepos(url) {
	$("#pepe").text('');

	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
        var response = data;
        var repoCollection = new RepoCollection(response);
        var linkHeader = jqxhr.getResponseHeader('Link');
        repoCollection.buildLinks(linkHeader);
		var html = repoCollection.toHTML();
		$("#pepe").html(html);
	}).fail(function(jqXHR, textStatus) {
		console.log(textStatus);
	});

}

function createGist(gist) {
	var url = API_BASE_URL + '/gists';
	var data = JSON.stringify(gist);
    console.log(data);
	$("#create_result").text('');

	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		dataType : 'json',
		data : data,
	}).done(function(data, status, jqxhr) {
		$('<div class="alert alert-success"> <strong>Ok!</strong> Gist creado</div>').appendTo($("#create_result"));
		$("#descripcion_create").val("");
		$("#contenido_create").val("");
        var url = API_BASE_URL + '/users/' + USERNAME + '/gists?per_page=2';
	getRepos(url);
  	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#create_result"));
	});

}

function getGistToEdit(gist_id){
	var url = API_BASE_URL + '/gists/' + gist_id;
	$("#update_result").text('');

	$.ajax({
		url: url,
		type : 'GET',
		crossDomain : true,
		dataType : 'json',
	}).done(function(data,status,jqxhr){
				var gist = data;
				var contenido = "";
				for (var n in gist.files){
					for(var fn in gist.files[n]){
						if (fn == "content") contenido = gist.files[n][fn];
					}
				}
				$('#descripcion_edit').val(gist.description);
				$('#contenido_edit').val(contenido);
				console.log(gist);
			}).fail(function(){
				$('<div class="alert alert-danger"> <strong>Oh!</strong> No se encuentra un Gist con esa ID </div>').appendTo($("#update_result"));
    });

}

function editGist(gistEditado){

	var url = API_BASE_URL + '/gists/' + $("#id_edit").val() ;
	var gist = JSON.stringify(gistEditado);

	$("#update_result").text('');

	$.ajax({
		url: url,
		type : 'PATCH',
		crossDomain :true,
		dataType : 'json',
		data : gist,
		statusCode: {
    		404: function() {$('<div class="alert alert-danger"> <strong>Oh!</strong> Page not found </div>').appendTo($("#update_result"));}
    	}
	}).done(function(data, status, jqxhr) {
		$('<div class="alert alert-success"> <strong>Ok!</strong> Gist Editado</div>').appendTo($("#update_result"));
		$("#descripcion_edit").val("");
		$("#contenido_edit").val("");
        var url = API_BASE_URL + '/users/' + USERNAME + '/gists?per_page=2';
	getRepos(url);
		console.log(data);
  	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#update_result"));
	});

}

function deleteGist(id_gist) {
    $("#delete_result").text('');
	var url = API_BASE_URL + '/gists/' + id_gist;
	$.ajax({
		url: url,
		type : 'DELETE',
		crossDomain :true,
		dataType : 'json',
	}).done(function(data, status, jqxhr) {
		$('<div class="alert alert-success"> <strong>Ok!</strong> Gist Eliminado</div>').appendTo($("#delete_result"));
		console.log(data);
        var url = API_BASE_URL + '/users/' + USERNAME + '/gists?per_page=2';
	    getRepos(url);
  	}).fail(function() {
		$('<div class="alert alert-danger"> <strong>Oh!</strong> Error </div>').appendTo($("#delete_result"));
	});

}

