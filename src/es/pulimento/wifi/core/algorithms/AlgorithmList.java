package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;

/**
 * An extension of the class ArrayList for supporting some more functions.
 */
public class AlgorithmList extends ArrayList<CrackAlgorithm> {

	private static final long serialVersionUID = 9078810203314526774L;

	/**
	 * Interacts with its childs in order to know if a crack algorithm can break the security of a network.
	 * @return True if vulnerable or false if not.
	 */
	public boolean isCrackeable() {
		for(int i = 0; i < this.size(); i++)
			if(this.get(i).isCrackeable())
				return true;

		return false;
	}

	/**
	 * Breaks the network and returns the password.
	 * @return The possible passwords of a net splited by the new line character ('\n').
	 */
	public String crack() {
		for(int i = 0; i < this.size(); i++)
			if(this.get(i).isCrackeable())
				return this.get(i).crack();

		return null;
	}
}
