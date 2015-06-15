package guia.definitiva.bareando.model;

import java.util.ArrayList;
import java.util.List;

public class userCollection {
	private List<usuario> usuarios;

	public userCollection() {
		super();
		usuarios = new ArrayList<>();
	}
	public void addUser(usuario usr) {
		usuarios.add(usr);
	}
	public List<usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
