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
 * Infostrada cracking algorithm.
 * Cracks all networks with "InfostradaWifi-XXXXXX"
 * Needs testing!!
 */
public class InfostradaAlgorithm extends CrackAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public InfostradaAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setPatterns() {

		// ESSID: InfostradaWiFi-XXXXXX
		// BSSID: Any
		addPattern("InfostradaWiFi-([0-9a-zA-Z]{6})", "([0-9A-Fa-f:]{17})");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		// Delete dots from bssid and use caps only...
		bssid_data = bssid_data.replace(":", "").toUpperCase();

		return 2+bssid_data;
	}

	/**
	 * {@inheritDoc}
	 */
	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return true;
	}
}