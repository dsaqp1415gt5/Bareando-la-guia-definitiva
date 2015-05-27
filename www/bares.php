<?php if ($_GET['link'] == "tapas") { ?>
<!-- Page Content -->
<div class="container">

    <!-- Page Heading -->
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Paginación
                <small>Prueba con Gists</small>
            </h1>
        </div>
    </div>

    <!-- /.row -->
    <div id="pepe">
    </div>

    <!-- Pagination -->
    <div class="row text-center">
        <div class="col-lg-12">
            <ul class="pagination">
                <li >
                    <a id="previous" href="#">Previous</a>
                </li>
                <li>
                    <a id="next" href="#">Next</a>
                </li>
                <!-- Lo que habia originalmente
<li>
<a href="#">&laquo;</a>
</li>
<li class="active">
<a href="#">1</a>
</li>
<li>
<a href="#">2</a>
</li>
<li>
<a href="#">3</a>
</li>
<li>
<a href="#">4</a>
</li>
<li>
<a href="#">5</a>
</li>
<li>
<a href="#">&raquo;</a>
</li>
-->

            </ul>
        </div>
    </div>
    <!-- /.row -->

    <hr>

    <!-- Footer -->
    <footer>
        <div class="row">
            <div class="col-lg-12">
                <p>Copyright &copy; Your Website 2014</p>
            </div>
        </div>
        <!-- /.row -->
    </footer>

</div>
<?php } elseif ($_GET['link'] == "home"){ ?>
<div class="jumbotron text-center cabecera">
    <div class="container">
        <div class="row">
            <div class="col col-lg-12 col-sm-12">
                <img src="img/logo2.png" width="100%">
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <div class="well trescolumnas">
                <div class="cero"><h3 class="titulo">El bar del tito Roberts</h3></div>
                <div class="uno"><img class="img-responsive" src="img/bar.jpg"></div>
                <div class="dos">
                    <ul class="descripcion">
                        <li>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</li>
                        <li>    Fusce eu metus non elit pulvinar tempus ac sit amet augue.</li>
                        <li>    Nullam et lacus posuere magna viverra maximus sed quis magna.</li>
                        <li>    Suspendisse et lectus non turpis tempus volutpat ac vel justo.</li>
                        <li>    Maecenas tristique quam nec augue dictum tincidunt.</li>
                        <li>    Donec fermentum justo eget justo pellentesque, ut condimentum enim tincidunt.</li>
                        <li>    Sed imperdiet ex eu augue accumsan, at vulputate quam feugiat.</li>
                        <li>    In ultrices ligula sit amet nibh aliquam, et interdum lorem finibus.</li>
                    </ul>
                </div>
                <div class="tres"><h2 class="nota">7.25</h2>/10</div>
                <br>
            </div>
        </div>
    </div>
    <div class="row">
        <a href="paginacion/">
            <div class="col-md-4">
                <div class="well logo_cont">
                    <img class="img-responsive logo" src="img/copa1.png">
                    <p class="logo text">Bares de Copas/Nocturnos</p>
                </div>
            </div>
        </a>
        <a href="paginacion/">
            <div class="col-md-4">
                <div class="well logo_cont">
                    <img class="img-responsive logo" src="img/tapa.png">
                    <p class="logo text">Bares de Tapas</p>
                </div>
            </div>
        </a>
        <a href="paginacion/">
            <div class="col-md-4">
                <div class="well logo_cont">
                    <img class="img-responsive logo" src="img/wine.png">
                    <p class="logo text">Vinotecas</p>
                </div>
            </div>
        </a>
    </div>
</div>
<hr>
<div id="loginModal" class="modal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" id="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button id="cerrar" type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h1 class="text-center">Login</h1>
            </div>
            <div class="modal-body">
                <form class="form col-md-12 center-block">
                    <div class="form-group">
                        <input type="text" class="form-control input-lg" placeholder="Email">
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control input-lg" placeholder="Password">
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block">Sign In</button>
                        <span class="pull-right"><a href="#">Register</a></span><span><a href="#">Need help?</a></span>
                    </div>
                </form>
            </div>
            <div class="modal-footer" style="border:none;">
            </div>
        </div>
    </div>
</div>
<?php } ?>



