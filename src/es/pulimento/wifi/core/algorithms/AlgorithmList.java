package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;

public class AlgorithmList extends ArrayList<CrackAlgorithm> {

	int working_algo = -1;

	public boolean isCrackeable() {

		for(int i = 0; i < this.size(); i++)
			if(this.get(i).isCrackeable())
			{
				working_algo = i;
				return true;
			}
		return false;
	}

	public String crack() {
		return this.get(working_algo).crack();
	}
}
