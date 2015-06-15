package guia.definitiva.bareando.model;

import guia.definitiva.bareando.api.MediaType;
import guia.definitiva.bareando.api.barResource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

public class barCollection {
	@InjectLinks({ @InjectLink(resource = barResource.class, style = Style.ABSOLUTE, rel = "crear-bar", title = "crear bar", type = MediaType.BAREANDO_BAR) })
	private List<Link> links;
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	private List<bar> bares;
	private int paginas;

	public int getPaginas() {
		return paginas;
	}

	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}

	public barCollection() {
		super();
		bares = new ArrayList<>();
	}

	public List<bar> getBares() {
		return bares;
	}

	public void setBares(List<bar> bares) {
		this.bares = bares;
	}

	public void addBar(bar BAR) {
		bares.add(BAR);
	}
}