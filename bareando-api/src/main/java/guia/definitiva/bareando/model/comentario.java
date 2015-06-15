package guia.definitiva.bareando.model;

public class comentario {
	private int id;
	private String nick;
	private int id_bar;
	private String mensaje;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getId_bar() {
		return id_bar;
	}
	public void setId_bar(int id_bar) {
		this.id_bar = id_bar;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}