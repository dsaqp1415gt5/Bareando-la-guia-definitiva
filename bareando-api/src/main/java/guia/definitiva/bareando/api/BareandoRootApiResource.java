package guia.definitiva.bareando.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
 
 
@Path("/")
public class BareandoRootApiResource {
	@GET
	public BareandoRootApi getRootAPI() {
		BareandoRootApi api = new BareandoRootApi();
		return api;
	}
}