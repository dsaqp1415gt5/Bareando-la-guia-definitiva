package guia.definitiva.bareando.model;

import java.util.ArrayList;
import java.util.List;

public class barCollection {
	private List<bar> bares;

	public barCollection(){
		super();
		bares = new ArrayList<>();
	}

	public List<bar> getBares() {
		return bares;
	}

	public void setBares(List<bar> bares) {
		this.bares = bares;
	}
	
	public void addBar(bar BAR){
		bares.add(BAR);
	}
}