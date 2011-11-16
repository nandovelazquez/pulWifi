package es.pulimento.wifi;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import es.pulimento.wifi.dialogs.AboutDialog;
import es.pulimento.wifi.dialogs.SupportedNetworksDialog;

public class SelectWirelessNetwork extends Activity implements OnItemClickListener {

	private Context mContext;
	private ListView mWirelessNetListView;
	private ArrayList<WirelessNetwork> mWirelessNetList;
	private ListViewAdapter mListViewAdapter;
	private Preferences mPreferences;
	private WifiManager mWifiManager;
	private TimerTask mStartScanTask;
	private Timer mTimer;
	private Vibrator mVibrator;
	private BroadcastReceiver mBroadcastReceiver;
	private IntentFilter mIntentFilter;
	private Button mRefreshNetworks;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_wireless_network);

		/* Define objects. */
		mContext = this;
		mPreferences = new Preferences(mContext);
		mWirelessNetListView = (ListView) findViewById(R.id.list);
		mWirelessNetList = new ArrayList<WirelessNetwork>();
		mListViewAdapter = new ListViewAdapter(this, R.layout.listview_item, mWirelessNetList);
		mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		mTimer = new Timer();
		mStartScanTask = new TimerTask() {
			@Override
			public void run() {
				mWifiManager.startScan();
			}
		};

		/* Set object properties. */
		mWirelessNetListView.setEmptyView(findViewById(R.id.list_empty));
		mWirelessNetListView.setAdapter(mListViewAdapter);
		mWirelessNetListView.setClickable(true);
		mWirelessNetListView.setOnItemClickListener(this);



		mRefreshNetworks = (Button) findViewById(R.id.button_refresh_network);
		mRefreshNetworks.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mWifiManager.startScan();			
				mVibrator.vibrate(120);
			}
		});

			//Intent filters...
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
			mBroadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context c, Intent i) {
					// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs.
					//wirelessNetList.clear();

					for(ScanResult wifi : mWifiManager.getScanResults()) {

						mWirelessNetList.add(new WirelessNetwork(wifi.SSID, wifi.BSSID ,wifi.level,wifi.capabilities));

						/*if(mPreferences.PREFERENCES_VIBRATEFOUND_CURRENT && crackeable)
							mVibrator.vibrate(150);*/
					}

					//wirelessNetList.add(new WirelessNetwork("WLAN4DC866","00:22:2D:04:DC:E8",true,-80,"[WPA]"));
					//wirelessNetList.add(new WirelessNetwork("WLAN_1234","64:68:0c:AA:AA:AA",true,-100,"[WPA]"));
					//wirelessNetList.add(new NetworkItem("ThomsonF8A3D0", "AA:AA:AA:AA:AA:AA", (new CrackerFramework(context, "ThomsonF8A3D0",  "AA:AA:AA:AA:AA:AA")).isCrackeable()));
					//wirelessNetList.add(new WirelessNetwork("JAZZTEL_E919", "64:68:0C:DE:39:48",true));
					//wirelessNetList.add(new WirelessNetwork("HAWEI1", "00:18:82:32:81:20",false));
					//wirelessNetList.add(new WirelessNetwork("WLAN_E919", "64:68:0C:96:e9:1c"));//dbcd970f0d705754206d
					//wirelessNetList.add(new WirelessNetwork("HAWEI2", "00:22:A1:32:81:20"));
					//wirelessNetList.add(new WirelessNetwork("YACOMXXXXXX", "00:22:A1:32:81:20"));
					//wirelessNetList.add(new WirelessNetwork("bazinga", "FF:FF:FF:FF:FF:FF"));

					mListViewAdapter.notifyDataSetChanged();

					if(mPreferences.PREFERENCES_VIBRATEUPDATE_CURRENT)
						mVibrator.vibrate(50);

					if(mPreferences.PREFERENCES_AUTOUPDATE_CURRENT)
						mTimer.schedule(mStartScanTask, mPreferences.PREFERENCES_UPDATEINTERVAL_CURRENT);
				}
			};
	}
	
	@Override
	public void onResume(){
		super.onResume();

		if(mPreferences.PREFERENCES_AUTOUPDATE_CURRENT) {
			mRefreshNetworks.setVisibility(Button.GONE);
			mWifiManager.startScan();
		} else {
			mRefreshNetworks.setVisibility(Button.VISIBLE);
			mRefreshNetworks.setEnabled(true);
		}

		// Register receivers...
		registerReceiver(mBroadcastReceiver, mIntentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister receivers...
		if(mPreferences.PREFERENCES_AUTOUPDATE_CURRENT) {
			unregisterReceiver(mBroadcastReceiver);
		}
	}

	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		WirelessNetwork w = (WirelessNetwork) adapter.getItemAtPosition(position);
		if(w.getCrackeable()){
	    	w.crack();
	    	ShowPass.current = w;
	    	Intent intent = new Intent(mContext, ShowPass.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			startActivity(intent);
		}else{
			Toast t = Toast.makeText(mContext, getString(R.string.select_wireless_network_dialog_not_valid), Toast.LENGTH_SHORT);
    		t.show();
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		// Scan for wireless networks...
		if(mPreferences.PREFERENCES_AUTOUPDATE_CURRENT){
			mRefreshNetworks.setVisibility(8);
			registerReceiver(mBroadcastReceiver, mIntentFilter);
			
		}else{
			mRefreshNetworks.setEnabled(true);
			mRefreshNetworks.setVisibility(0);

		}
		mWifiManager.startScan();
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_scanmode, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.ACERCA_DE:
	    	(new AboutDialog(mContext)).show();
	        return true;
	    case R.id.SALIR:
	    	this.finish();
	    	return true;
	    case R.id.NETWORKS:
	    	(new SupportedNetworksDialog(mContext)).show();
	        return true;    
	    case R.id.AJUSTES:
	    	 startActivity(new Intent(mContext, Preferences.class));
	    	 return true;
	    }
	    return false;
	}
}