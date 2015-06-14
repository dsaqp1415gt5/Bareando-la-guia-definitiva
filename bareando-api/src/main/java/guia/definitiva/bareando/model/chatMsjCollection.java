package guia.definitiva.bareando.model;

import java.util.ArrayList;
import java.util.List;

public class chatMsjCollection {
	private List<chatMsj> mensajes;

	public chatMsjCollection() {
		super();
		mensajes = new ArrayList<>();
	}

	public void addMensaje(chatMsj msj) {
		mensajes.add(msj);
	}

	public List<chatMsj> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<chatMsj> mensajes) {
		this.mensajes = mensajes;
	}
}
