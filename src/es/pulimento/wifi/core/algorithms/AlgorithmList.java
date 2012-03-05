package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;

public class AlgorithmList extends ArrayList<CrackAlgorithm> {
	// TODO: I think this can be optimized...

	private static final long serialVersionUID = 9078810203314526774L;

	public boolean isCrackeable() {

		for(int i = 0; i < this.size(); i++)
			if(this.get(i).isCrackeable())
				return true;

		return false;
	}

	public String crack() {
		for(int i = 0; i < this.size(); i++)
			if(this.get(i).isCrackeable())
				return this.get(i).crack();

		return null;
	}
}
