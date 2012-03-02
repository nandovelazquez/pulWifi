package es.pulimento.wifi.core.algorithms;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import es.pulimento.wifi.core.WirelessEncryption;

public class Wlan6XAlgorithm extends CrackAlgorithm {

	/*
	 * Wlan 6X algorithm.
	 * 
	 * TODO: Description and supported routers.
	 */

	public Wlan6XAlgorithm(String essid, String bssid) {
		super(essid, bssid);
	}

	// TODO: Add encryption and clean all up...
	public static WirelessEncryption[] encryption = {  };

	@Override
	protected void setPatterns() {
		
		// ESSID: WLANXXXXXX
		// BSSID: Any
		addPattern("WLAN([0-9a-fA-F]{6})", "([0-9A-Fa-f:]{17})");

		// ESSID: WIFIXXXXXX
		// BSSID: Any
		addPattern("WIFI([0-9a-fA-F]{6})", "([0-9A-Fa-f:]{17})");

		// ESSID: YACOMXXXXXX
		// BSSID: Any
		addPattern("YACOM([0-9a-fA-F]{6})", "([0-9A-Fa-f:]{17})");
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		bssid_data = bssid_data.toUpperCase();

		return crack2(essid_data, bssid_data);
	}

	public static String crack2(String ESSID, String BSSID){
		String ssidStr = ESSID.substring(ESSID.length()-6);
		String macStr = BSSID;
		Log.e("pulWifi","Using new WLAN6X algorythm, "+ssidStr+" "+macStr);
		char [] ssidSubPart = {'1', '2','3', '4', '5','6' };/*These values are not revelant.*/
		char [] bssidLastByte = { '6', '6' };
		ssidSubPart[0] = ssidStr.charAt(0);
		ssidSubPart[1] = ssidStr.charAt(1);
		ssidSubPart[2] = ssidStr.charAt(2);
		ssidSubPart[3] = ssidStr.charAt(3);
		ssidSubPart[4] = ssidStr.charAt(4);
		ssidSubPart[5] = ssidStr.charAt(5);
		bssidLastByte[0] = macStr.charAt(15);
		bssidLastByte[1] = macStr.charAt(16);
		for ( int  k = 0; k < 6 ; ++k ) 
		    if( ssidSubPart[k] >= 'A')
		        ssidSubPart[k] = (char)(ssidSubPart[k] - 55);

	    if(bssidLastByte[0] >= 'A' )
	        bssidLastByte[0] = (char)(bssidLastByte[0] - 55);
	    if(bssidLastByte[1] >= 'A' )
	        bssidLastByte[1] = (char)(bssidLastByte[1] - 55);
	    
	    List<String> passList = new ArrayList<String>();
		for ( int i = 0; i < 10 ; ++i )
		{
			/*Do not change the order of this instructions*/
			int aux = i + ( ssidSubPart[3] & 0xf ) +  ( bssidLastByte[0] & 0xf ) + ( bssidLastByte[1] & 0xf );
			int aux1 = ( ssidSubPart[1] & 0xf ) + ( ssidSubPart[2] & 0xf ) + ( ssidSubPart[4] & 0xf ) + ( ssidSubPart[5] & 0xf );
			int second = aux ^ ( ssidSubPart[5] & 0xf );
			int sixth = aux ^ ( ssidSubPart[4] & 0xf );
			int tenth = aux ^ ( ssidSubPart[3] & 0xf );
			int third = aux1 ^ ( ssidSubPart[2] & 0xf );
			int seventh = aux1 ^  ( bssidLastByte[0] & 0xf );
			int eleventh = aux1 ^ ( bssidLastByte[1] & 0xf );
			int fourth =  ( bssidLastByte[0] & 0xf ) ^ ( ssidSubPart[5] & 0xf );
			int eighth = ( bssidLastByte[1] & 0xf ) ^ ( ssidSubPart[4] & 0xf );
			int twelfth = aux ^ aux1;
			int fifth = second ^ eighth;
			int ninth = seventh ^ eleventh;
			int thirteenth = third ^ tenth;
			int first = twelfth ^ sixth;
			String key = Integer.toHexString(first & 0xf) + Integer.toHexString(second & 0xf)+
						Integer.toHexString(third & 0xf) + Integer.toHexString(fourth & 0xf) +
						Integer.toHexString(fifth & 0xf) + Integer.toHexString(sixth & 0xf) +
						Integer.toHexString(seventh & 0xf) + Integer.toHexString(eighth & 0xf) +
						Integer.toHexString(ninth & 0xf) + Integer.toHexString(tenth & 0xf) + 
						Integer.toHexString(eleventh & 0xf) + Integer.toHexString(twelfth & 0xf) +
						Integer.toHexString(thirteenth & 0xf);
			
			passList.add(key.toUpperCase());
		}
		StringBuilder s = new StringBuilder();
		for (String sp : passList) 
			s.append(sp+"\n");
  		Log.d("pulWifi", "CLAVE -> "+s.toString());
		return s.toString();		
	}

}
