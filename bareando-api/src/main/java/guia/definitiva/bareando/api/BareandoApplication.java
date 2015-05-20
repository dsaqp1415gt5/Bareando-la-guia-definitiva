package guia.definitiva.bareando.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
 
public class BareandoApplication extends ResourceConfig {
	public BareandoApplication() {
		super();
		register(DeclarativeLinkingFeature.class);
	}
}