package edu.upc.eetac.dsa.acouceiro.bareando.api;

import java.util.HashMap;
import java.util.Map;

public class BareandoRootAPI {

    private Map<String, Link> links;

    public BareandoRootAPI() {
        links = new HashMap<String, Link>();
    }

    public Map<String, Link> getLinks() {
        return links;
    }
}
