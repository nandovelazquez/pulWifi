/*
 *  pulWifi , Copyright (C) 2011-2012 Javi Pulido / Antonio Vázquez
 *  
 *  This file is part of "pulWifi"
 *
 *  "pulWifi" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  "pulWifi" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with "pulWifi".  If not, see <http://www.gnu.org/licenses/>.
 */

package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;

/**
 * An extension of the class ArrayList for supporting some more functions.
 */
public class AlgorithmList extends ArrayList<CrackAlgorithm> {

	private static final long serialVersionUID = 9078810203314526774L;

	/**
	 * Interacts with its child elements in order to know if a crack algorithm can break the security of a network.
	 * @return True if vulnerable or false if not.
	 */
	public boolean isCrackeable() {
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).isCrackeable()) return true;

		return false;
	}

	/**
	 * Breaks the network and returns the password.
	 * @return The possible passwords of a net separated by the new line character ('\n').
	 */
	public String crack() {
		for (int i = 0; i < this.size(); i++)
			if (this.get(i).isCrackeable()) return this.get(i).crack();

		return null;
	}
}
