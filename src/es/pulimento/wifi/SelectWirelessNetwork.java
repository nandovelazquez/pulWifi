package es.pulimento.wifi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class SelectWirelessNetwork extends Activity implements OnItemClickListener{
	
	//Atributos
	ListView wirelessNetListView;
	ArrayList<WirelessNetwork> wirelessNetList;
	ListViewAdapter listViewAdapter;
	Context context;
	WifiManager wifiManager;
	BroadcastReceiver broadcastReceiver;
	IntentFilter intentFilter;
	AlertDialog crackDialog;
	Button refreshNetworks;
	SharedPreferences s;
	//Activity activity;
	HashMap<String,Boolean> db;
	boolean autoupdate;
	boolean vibrateFOUND;
	boolean vibrateUPDATE;
	String updateInterval;
	Long updateIntervalL;
	//TimerTask timerTask;
	
	final boolean SHOW_ADS = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_wireless_network);
		//Log.d("pulWifi","entramos en el onCreate() de SelectWirelessNetwork");		
		//Define elements...
		context = getApplicationContext();
		//activity = this;		
		wirelessNetListView = (ListView) findViewById(R.id.list);
		wirelessNetListView.setEmptyView(findViewById(R.id.list_empty));
		wirelessNetList = new ArrayList<WirelessNetwork>();
		listViewAdapter = new ListViewAdapter(this, R.layout.listview_item, wirelessNetList);
		wirelessNetListView.setAdapter(listViewAdapter);
		wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		db = new HashMap<String, Boolean>();

		s = getSharedPreferences("preferences", MODE_PRIVATE);
		autoupdate = s.getBoolean("autoupdate", true);
		vibrateFOUND = s.getBoolean("vibrateFOUND", false);
		vibrateUPDATE = s.getBoolean("vibrateUPDATE", false);
		updateInterval = s.getString("ui","2000");
		updateIntervalL = new Long(updateInterval);
		//Log.e("pulWifi","Update Interval (from long) -> "+updateIntervalL.toString());
		
		refreshNetworks = (Button) findViewById(R.id.button_refresh_network); ////////////////////
		refreshNetworks.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				updateListOnce();				
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(120);
				//Log.d("pulWifi","Actualiza lista de redes!!");
			}
		});		
		if(autoupdate)refreshNetworks.setVisibility(Button.GONE);		
		//Log.d("pulWifi","SelectWirelessNetwork 1");
		//Set onClickListeners...
		wirelessNetListView.setClickable(true);
		wirelessNetListView.setOnItemClickListener(this);
		
		if(SHOW_ADS){
		
		/*
         * AdMob
         */         
         
        AdView adView = new AdView(this, AdSize.BANNER, "a14de2ce2207597");
        LinearLayout layout = (LinearLayout)findViewById(R.id.ads);
        layout.addView(adView);
        AdRequest ar = new AdRequest();
        //ar.setTesting(true);/////////////////////////////////////////////////////////////////////////////////
        //ar.addTestDevice(AdRequest.TEST_EMULATOR);
        //ar.addTestDevice("04FED1DF0A6D24A7EF092E57617BED52"); //HTC Hero
        adView.loadAd(ar);
		}
		
		if(autoupdate){
		//Intent filters...
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				//Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs
				wirelessNetList.clear();
				Long a = System.currentTimeMillis();
				for(ScanResult wifi : wifiManager.getScanResults()){
					boolean crackeable;
					//Log.e("pulWifi",wifi.BSSID);
					if(wifi.BSSID!=null && db.containsKey(wifi.BSSID)){
						crackeable = db.get(wifi.BSSID);
						//Log.e("pulWifi",wifi.BSSID+" estaba en el HashMap");
					}else{
						crackeable = (new CrackNetwork(wifi.SSID,  wifi.BSSID, wifi.capabilities)).isCrackeable();
						db.put(wifi.BSSID, crackeable);
						//Log.w("pulWifi",wifi.BSSID+" es introducido en el hashMap");
					}	
					if(wifi.SSID!=null && wifi.BSSID!=null)wirelessNetList.add(new WirelessNetwork(wifi.SSID, wifi.BSSID,crackeable,wifi.level,wifi.capabilities));
					if(vibrateFOUND && crackeable){
						Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
						v.vibrate(150);
						//Log.d("pulWifi","Encontrada red vulnerable!!");
					}				
				}
				//wirelessNetList.add(new WirelessNetwork("WLAN4DC866","00:22:2D:04:DC:E8",true,-80,"[WPA]"));
				//wirelessNetList.add(new WirelessNetwork("WLAN_1234","64:68:0c:AA:AA:AA",true,-100,"[WPA]"));
				//wirelessNetList.add(new NetworkItem("ThomsonF8A3D0", "AA:AA:AA:AA:AA:AA", (new CrackerFramework(context, "ThomsonF8A3D0",  "AA:AA:AA:AA:AA:AA")).isCrackeable()));
				//wirelessNetList.add(new WirelessNetwork("WLAN_E919", "64:68:0C:96:e9:1c",true));//dbcd970f0d705754206d
				Collections.sort(wirelessNetList);
				listViewAdapter.notifyDataSetChanged();
				Long b = System.currentTimeMillis() - a;
		//		Log.e("pulWifi",b.toString());
				if(b<1600){//Si tarda menos de 1.6 segundos en procesar el onReceive()
					TimerTask timerTask = new TimerTask(){						
						@Override public void run() {
		//					Log.e("pulWifi","Running timer task!!!");
							wifiManager.startScan();}};
					Timer ti = new Timer();
//					SharedPreferences suuu = PreferenceManager.getDefaultSharedPreferences(context);
//					SharedPreferences saa =getSharedPreferences("preferences", MODE_WORLD_READABLE + MODE_WORLD_WRITEABLE); 
//					Long wawawa = new Long(saa.getString("ui", "8"));
					ti.schedule(timerTask, updateIntervalL);
		//			Log.e("pulWifi","Tarea encargada para dentro de "+updateIntervalL.toString()+"ms");
				}else{
					Log.e("pulWifi","Escaneando normalmente, ya que ha tardado más de un segundo");
					wifiManager.startScan();
				}				
				
				if(vibrateUPDATE){
					Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(50);
				}
				Log.v("pulWifi","new WifiScanResult, APs on range : "+wifiManager.getScanResults().size());
				//Log.i("pulWifi","size -> "+wirelessNetList.size());
			}};

	}else{
		updateListOnce();	
	}
}
	
	@Override
	public void onResume(){
		super.onResume();
		//Log.d("pulWifi","SelectWirelessNetwork.onResume()");
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		autoupdate = sp.getBoolean("autoupdate", true);
		vibrateFOUND = sp.getBoolean("vibrateFOUND", false);
		vibrateUPDATE = sp.getBoolean("vibrateUPDATE", false);
		updateInterval = sp.getString("ui","2000");
		updateIntervalL = new Long(updateInterval);
//		Log.e("pulWifi",updateIntervalL+"estamos en el onResume, se debería de haber hecho ya el getString de las preferencias");
		//Log.d("pulWifi","El autoupdate estaba a "+autoupdate);
		//Register receivers...
		
		//Scan for wireless networks...
		if(autoupdate){
			refreshNetworks.setVisibility(Button.GONE);
			registerReceiver(broadcastReceiver, intentFilter);
			wifiManager.startScan();
		}else{
			refreshNetworks.setEnabled(true);
			refreshNetworks.setVisibility(Button.VISIBLE);
			updateListOnce();
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();		
		//Unregister receivers...
		if(autoupdate){
		unregisterReceiver(broadcastReceiver);
		}
	}
	
	/*@Override
	public void onStop(){
		super.onStop();
		
		//Unregister receivers...
		if(autoupdate){
		unregisterReceiver(broadcastReceiver);
		}
	}
	*/

	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		//Log.d("pulWifi","acabamos de entrar en el onItemClick!!");
		final WirelessNetwork w = (WirelessNetwork) adapter.getItemAtPosition(position);
		if(w.getCrackeable()){
	    	String clave = 	(new CrackNetwork(w.getEssid(),w.getBssid(),w.getCapabilities())).crackNetwork();
	    	w.setClave(clave);
	    	ShowPass.current = w;
	    	Intent intent = new Intent(context, ShowPass.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intent);
		}else{
			Toast t = Toast.makeText(context,getString(R.string.select_wireless_network_dialog_not_valid), Toast.LENGTH_SHORT);
    		t.show();
		}
	}
	
	public void updateListOnce(){
		wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
		wirelessNetList.clear();
		for(ScanResult wifi : wifiManager.getScanResults()){
	    	boolean crackeable = (new CrackNetwork(wifi.SSID,wifi.BSSID,wifi.capabilities)).isCrackeable();
			wirelessNetList.add(new WirelessNetwork(wifi.SSID, wifi.BSSID,crackeable,wifi.level,wifi.capabilities));
			if(crackeable){
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(40);
				//Log.i("pulWifi","Found vulnerable network (SCAN_ONDEMAND)");
			}				
		}
		
//		wirelessNetList.add(new WirelessNetwork("JAZZTEL_E919", "64:68:0C:DE:39:48",true));
//		wirelessNetList.add(new WirelessNetwork("HAWEI1", "00:18:82:32:81:20",false));
//		wirelessNetList.add(new WirelessNetwork("WLAN_E919", "64:68:0C:96:e9:1c"));//dbcd970f0d705754206d
//		wirelessNetList.add(new WirelessNetwork("HAWEI2", "00:22:A1:32:81:20"));
//		wirelessNetList.add(new WirelessNetwork("YACOMXXXXXX", "00:22:A1:32:81:20"));
//		wirelessNetList.add(new WirelessNetwork("bazinga", "FF:FF:FF:FF:FF:FF"));


		listViewAdapter.notifyDataSetChanged();	
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		//Log.d("pulWifi","SelectWirelessNetwork.onOptionsMenuClosed() 1");
		super.onOptionsMenuClosed(menu);
		//Log.d("pulWifi","SelectWirelessNetwork.onOptionsMenuClosed() 2");
		//Scan for wireless networks...
		if(autoupdate){
			refreshNetworks.setVisibility(8);
			//Log.d("pulWifi","SelectWirelessNetwork.onOptionsMenuClose() @pre registerReceiver");
			registerReceiver(broadcastReceiver, intentFilter);
			//Log.d("pulWifi","SelectWirelessNetwork.onOptionsMenuClose() @pos registerReceiver");
			wifiManager.startScan();
		}else{
			refreshNetworks.setEnabled(true);
			refreshNetworks.setVisibility(0);
			updateListOnce();
		}
	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/////////////////////////////////////////////////////////////////////////
    //Menú
	/////////////////////////////////////////////////////////////////////////    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_scanmode, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ACERCA_DE:
	    	sobre();
	        return true;
	    case R.id.SALIR:
	    	//System.runFinalizersOnExit(true);
	    	//System.exit(0);
	    	//onPause();
	    	//onStop();
	    	//onDestroy();
	    	finish();
	    	return true;
	    case R.id.NETWORKS:
	    	Builder aDialog = new AlertDialog.Builder(this);
			aDialog.setTitle(R.string.supported_networks_title);
			aDialog.setMessage(R.string.supported_networks);
			aDialog.setPositiveButton(R.string.splash_ask_dialog_ok_button, new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.cancel();
			   }
			});
			AlertDialog a = aDialog.create();
			a.show();
	        return true;    
	    case R.id.AJUSTES:
	    	 Intent settingsActivity = new Intent(getBaseContext(),Preferences.class);
	    	 startActivity(settingsActivity);
	    	 //onStop();
	    	 return true;
	    }
	    return false;
	}	
    
	/////////////////////////////////////////////////////////////////////////
    //Varios
	/////////////////////////////////////////////////////////////////////////    
    
	public void sobre(){
		
	    ScrollView svMessage = new ScrollView(this); 
	    TextView tvMessage = new TextView(this);
	    SpannableString spanText = new SpannableString(context.getText(R.string.acerca_de));
	    Linkify.addLinks(spanText, Linkify.ALL);
	    tvMessage.setText(spanText);
	    tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
	    tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
	    svMessage.setPadding(10, 0, 6, 0);
	    svMessage.addView(tvMessage);

				
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(svMessage)
			.setTitle(getString(R.string.about_title))
		    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       })
		       .setNegativeButton(getString(R.string.about_gpl), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					WebViewActivity.readme = false;
					abrirNavegador();
				}
			})
		       .setNeutralButton(getString(R.string.about_readme), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					WebViewActivity.readme = true;
					abrirNavegador();
				}
			});
		AlertDialog alert = builder.create();
		alert.show();    	
    }
    
   public void abrirNavegador(){
	   Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
	   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
	   //Log.i("pulWifi", "Se va a abrir un navegador");
	   Context c = getApplicationContext();
       c.startActivity(i);
       //Log.i("pulWifi", "Navegador abierto satisfactoriamente ;)");
   }
}