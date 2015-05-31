package guia.definitiva.bareando.model;

import java.util.ArrayList;
import java.util.List;

public class comentarioCollection {
	private List<comentario> comentarios;
	private int paginas;
	

	public comentarioCollection() {
		super();
		comentarios = new ArrayList<>();
	}


	public List<comentario> getComentarios() {
		return comentarios;
	}


	public void setComentarios(List<comentario> comentarios) {
		this.comentarios = comentarios;
	}


	public int getPaginas() {
		return paginas;
	}


	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}

	public void addComentario(comentario cmt) {
		comentarios.add(cmt);
	}
}
