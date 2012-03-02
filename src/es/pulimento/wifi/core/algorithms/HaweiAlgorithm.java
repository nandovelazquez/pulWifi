package es.pulimento.wifi.core.algorithms;

import es.pulimento.wifi.core.WirelessEncryption;

public class HaweiAlgorithm extends CrackAlgorithm {

	/*
	 * Hawei algorithm.
	 * 
	 * TODO: Description and supported routers.
	 */

	// TODO: Add encryption and clean all up...
	public static WirelessEncryption[] encryption = {  };

	@Override
	protected void setPatterns() {
		/* TODO: Add this patterns.
		// Los que tienen un simple comentario est�n verificados, los que tienen DOBLE comentario
		// NO FUNCIONAN, pero como la gente se queja...pues hala...
		matcher_hawei[0] = Pattern.compile("(00:1E:10:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Huawei HG520c, debe de ser compatible!
		matcher_hawei[1] = Pattern.compile("(00:22:A1:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en routerkeygen
		matcher_hawei[2] = Pattern.compile("(00:25:68:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en HG520 espa�ol
		matcher_hawei[3] = Pattern.compile("(00:25:9E:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en routerkeygen		
		matcher_hawei[4] = Pattern.compile("(04:C0:6F:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[5] = Pattern.compile("(08:19:A6:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[6] = Pattern.compile("(0C:37:DC:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[7] = Pattern.compile("(10:C6:1F:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[8] = Pattern.compile("(1C:1D:67:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[9] = Pattern.compile("(20:2B:C1:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en routerkeygen
		matcher_hawei[10] = Pattern.compile("(20:F3:A3:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[11] = Pattern.compile("(24:DB:AC:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[12] = Pattern.compile("(28:5F:DB:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[13] = Pattern.compile("(28:6E:D4:[0-9A-Fa-f:]{8})").matcher(mBSSID);		
		matcher_hawei[14] = Pattern.compile("(30:87:30:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////HG265
		matcher_hawei[15] = Pattern.compile("(40:4D:8E:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[16] = Pattern.compile("(4C:54:99:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en Telmex, HG530 (pero en vodafone no funciona!!!!!)
		matcher_hawei[17] = Pattern.compile("(4C:1F:CC:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[18] = Pattern.compile("(54:A5:1B:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_hawei[19] = Pattern.compile("(54:89:98:[0-9A-Fa-f:]{8})").matcher(mBSSID);		
		matcher_hawei[20] = Pattern.compile("(64:16:F0:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto a varias VodafoneXXXX
		matcher_hawei[21] = Pattern.compile("(00:E0:FC:[0-9A-Fa-f:]{8})").matcher(mBSSID);//////Huawei wa1003A
		matcher_hawei[22] = Pattern.compile("(00:18:82:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////Por lo visto es el que tiene el Huawei U8100/8150, y otros routers
		//matcher_hawei[23] = Pattern.compile("(30:87:30:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////Visto en algunas Vodafone, pero tb en Orange
		matcher_hawei[23] = Pattern.compile("(24:DB:AC:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////
		matcher_hawei[24] = Pattern.compile("(00:0F:E2:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////
		matcher_hawei[25] = Pattern.compile("(28:6E:D4:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////
		matcher_hawei[26] = Pattern.compile("(00:11:F5:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////
		matcher_hawei[27] = Pattern.compile("(F4:C7:14:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////Es de Orange, no comercializa ese router
		//matcher_hawei[29] = Pattern.compile("(78:1D:BA:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////VodafoneInternetMovil
		matcher_hawei[28] = Pattern.compile("(5C:4C:A9:[0-9A-Fa-f:]{8})").matcher(mBSSID);/////Es de Orange, no comercializa ese router
		*/
		
	}

	@Override
	protected String crackAlgorithm(String essid_data, String bssid_data) {
		bssid_data = bssid_data.replace(":", "");
		return crack3(bssid_data);
	}

	public static String crack3(String bssid) {// HAWEI
		//Log.d("pulWifi","Using HAWEI algorithm");
   		//Log.d("pulWifi", "MAC -> "+bssid);
		char[] mac2 = bssid.toLowerCase().toCharArray();
		int[] a0 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] a1 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		int[] a2 = { 0, 13, 10, 7, 5, 8, 15, 2, 10, 7, 0, 13, 15, 2, 5, 8};
		int[] a3 = { 0, 1, 3, 2, 7, 6, 4, 5, 15, 14, 12, 13, 8, 9, 11, 10};
		int[] a5 = { 0, 4, 8, 12, 0, 4, 8, 12, 0, 4, 8, 12, 0, 4, 8, 12};
		int[] a7 = { 0, 8, 0, 8, 1, 9, 1, 9, 2, 10, 2, 10, 3, 11, 3, 11};
		int[] a8 = { 0, 5, 11, 14, 6, 3, 13, 8, 12, 9, 7, 2, 10, 15, 1, 4};
		int[] a10 = { 0, 14, 13, 3, 11, 5, 6, 8, 6, 8, 11, 5, 13, 3, 0, 14};
		int[] a14 = { 0, 1, 3, 2, 7, 6, 4, 5, 14, 15, 13, 12, 9, 8, 10, 11};
		int[] a15 = { 0, 1, 3, 2, 6, 7, 5, 4, 13, 12, 14, 15, 11, 10, 8, 9};
		int[] n5 = { 0, 5, 1, 4, 6, 3, 7, 2, 12, 9, 13, 8, 10, 15, 11, 14};
		int[] n6 = { 0, 14, 4, 10, 11, 5, 15, 1, 6, 8, 2, 12, 13, 3, 9, 7};
		int[] n7 = { 0, 9, 0, 9, 5, 12, 5, 12, 10, 3, 10, 3, 15, 6, 15, 6};
		int[] n11 = { 0, 14, 13, 3, 9, 7, 4, 10, 6, 8, 11, 5, 15, 1, 2, 12};
		int[] n12 = { 0, 13, 10, 7, 4, 9, 14, 3, 10, 7, 0, 13, 14, 3, 4, 9};
		int[] n13 = { 0, 1, 3, 2, 6, 7, 5, 4, 15, 14, 12, 13, 9, 8, 10, 11};
		int[] n14 = { 0, 1, 3, 2, 4, 5, 7, 6, 12, 13, 15, 14, 8, 9, 11, 10};
		int[] n31 = { 0, 10, 4, 14, 9, 3, 13, 7, 2, 8, 6, 12, 11, 1, 15, 5};
		int[] key = { 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 61, 62, 63, 64, 65, 66};
		int[] mac = new int[12];
		for(int i = 0; i<12; i++)
			mac[i] = hexToDec(mac2[i]);
		StringBuilder a = new StringBuilder();
		a.append(key[((a2[mac[0]])^(n11[mac[1]])^(a7[mac[2]])^(a8[mac[3]])^(a14[mac[4]])^(a5[mac[5]])^(a5[mac[6]])^(a2[mac[7]])^(a0[mac[8]])^(a1[mac[9]])^(a15[mac[10]])^(a0[mac[11]])^13)]);
		a.append(key[((n5[mac[0]])^(n12[mac[1]])^(a5[mac[2]])^(a7[mac[3]])^(a2[mac[4]])^(a14[mac[5]])^(a1[mac[6]])^(a5[mac[7]])^(a0[mac[8]])^(a0[mac[9]])^(n31[mac[10]])^(a15[mac[11]])^4)]);
		a.append(key[((a3[mac[0]])^(a5[mac[1]])^(a2[mac[2]])^(a10[mac[3]])^(a7[mac[4]])^(a8[mac[5]])^(a14[mac[6]])^(a5[mac[7]])^(a5[mac[8]])^(a2[mac[9]])^(a0[mac[10]])^(a1[mac[11]])^7)]);
		a.append(key[((n6[mac[0]])^(n13[mac[1]])^(a8[mac[2]])^(a2[mac[3]])^(a5[mac[4]])^(a7[mac[5]])^(a2[mac[6]])^(a14[mac[7]])^(a1[mac[8]])^(a5[mac[9]])^(a0[mac[10]])^(a0[mac[11]])^14)]);
		a.append(key[((n7[mac[0]])^(n14[mac[1]])^(a3[mac[2]])^(a5[mac[3]])^(a2[mac[4]])^(a10[mac[5]])^(a7[mac[6]])^(a8[mac[7]])^(a14[mac[8]])^(a5[mac[9]])^(a5[mac[10]])^(a2[mac[11]])^7)]);
  		//Log.d("pulWifi", "CLAVE -> "+a.toString());
		return a.toString();
	}
	private static int hexToDec(char s) {//crack3() lo necesita
		return Integer.parseInt(String.valueOf(s), 16);
	}	

}