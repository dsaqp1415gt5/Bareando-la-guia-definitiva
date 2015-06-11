package guia.definitiva.bareando.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.glassfish.jersey.server.internal.process.MappableException;

@Path("/chat")
public class chat {

	static Map<String, String> map = new HashMap<String, String>();
	static Map<String, Integer> maps = new HashMap<String, Integer>();

	// static int ola;

	@GET
	@Path("/{user}")
	public String getMensajes(@PathParam("user") String usuario) {
		String mensaje = map.get(usuario);
		int ola = 0;
		try {
			ola = maps.get(usuario);
		} catch (NullPointerException e) {
			System.out.println(e);
		}
		if (mensaje == null)
			ola = 0;
		else
			ola = 1;
		while (ola == 0) {
			System.out.println(ola);
		}
		mensaje = map.get(usuario);
		maps.put(usuario, 0);
		map.remove(usuario);
		return mensaje;
	}

	@GET
	@Path("/{msj}-{usr}")
	public String postMensajes(@PathParam("msj") String mensaje,
			@PathParam("usr") String usuario) {
		map.put(usuario, mensaje);
		maps.put(usuario, 1);
		// ola = 1;
		return "ok";
	}
}
