package guia.definitiva.bareando.api;

import java.util.List;

import javax.ws.rs.core.Link;
 
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;
 
import guia.definitiva.bareando.api.BareandoRootApiResource;
import guia.definitiva.bareando.api.MediaType;
import guia.definitiva.bareando.api.barResource;
 
public class BareandoRootApi {
	@InjectLinks({
            @InjectLink(resource = BareandoRootApiResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Baeando Root API"),
            @InjectLink(value = "bares/todos", style = Style.ABSOLUTE, rel = "collection", title = "Todos los Bares", type = MediaType.BAREANDO_BAR_COLLECTION),
            @InjectLink(resource = barResource.class, style = Style.ABSOLUTE, rel = "create-bar", title = "Create nuevo bar", type = MediaType.BAREANDO_BAR)})
    	private List<Link> links;
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
