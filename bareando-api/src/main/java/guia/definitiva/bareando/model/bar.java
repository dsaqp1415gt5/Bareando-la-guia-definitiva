package guia.definitiva.bareando.model;

import guia.definitiva.bareando.api.MediaType;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

public class bar {
	@InjectLinks({ @InjectLink(value = "bares/{id}-0-0-0-0-0-0-0-0", style = Style.ABSOLUTE, rel = "get Bar", title = "Bar", type = MediaType.BAREANDO_BAR, method = "getBar", bindings = @Binding(name = "id", value = "${instance.ID}")) })
	private List<Link> links;
	private int ID;
	private String nombre;
	private String descripcion;
	private int nota;
	private String genero;

	public String getGenero() {
		return genero;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}
}
