package guia.definitiva.bareando.model;

public class bareandoError {
	private int status;
	private String message;
 
	public bareandoError() {
		super();
	}
 
	public bareandoError(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
 
	public int getStatus() {
		return status;
	}
 
	public void setStatus(int status) {
		this.status = status;
	}
 
	public String getMessage() {
		return message;
	}
 
	public void setMessage(String message) {
		this.message = message;
	}
}
