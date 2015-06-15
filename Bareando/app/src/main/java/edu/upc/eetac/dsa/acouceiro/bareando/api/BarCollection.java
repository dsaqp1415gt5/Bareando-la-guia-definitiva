package edu.upc.eetac.dsa.acouceiro.bareando.api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarCollection {
    private List<Bar> bares;
    private long newestTimestamp;
    private long oldestTimestamp;
    private Map<String, Link> links = new HashMap<String, Link>();

    public BarCollection() {
        super();
        bares = new ArrayList<Bar>();
    }

    public List<Bar> getBares() {
        return bares;
    }

    public void setBares(List<Bar> bares) {
        this.bares = bares;
    }

    public void addBar(Bar bar) {
        bares.add(bar);
    }

    public long getNewestTimestamp() {
        return newestTimestamp;
    }

    public void setNewestTimestamp(long newestTimestamp) {
        this.newestTimestamp = newestTimestamp;
    }

    public long getOldestTimestamp() {
        return oldestTimestamp;
    }

    public void setOldestTimestamp(long oldestTimestamp) {
        this.oldestTimestamp = oldestTimestamp;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

}

