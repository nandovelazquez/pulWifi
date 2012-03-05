package es.pulimento.wifi.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;
import es.pulimento.wifi.dialogs.AboutDialog;
import es.pulimento.wifi.dialogs.SupportedNetworksDialog;
import es.pulimento.wifi.dialogs.UpdateDialog;

public class SelectWirelessNetwork extends Activity implements OnItemClickListener, OnClickListener {

	private Context mContext;
	private ListView mWirelessNetListView;
	private ArrayList<WirelessNetwork> mWirelessNetList;
	private ListViewAdapter mListViewAdapter;
	private WifiManager mWifiManager;
	private TimerTask mStartScanTask;
	private Timer mTimer;
	private Vibrator mVibrator;
	private BroadcastReceiver mBroadcastReceiver;
	private IntentFilter mIntentFilter;
	private SharedPreferences mSharedPreferences;
	private LinearLayout mRefreshSection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_selectwireless);

		/* Define objects. */
		mContext = this;
		mWirelessNetListView = (ListView) findViewById(R.id.layout_selectwireless_list_id);
		mWirelessNetList = new ArrayList<WirelessNetwork>();
		mListViewAdapter = new ListViewAdapter(this, R.layout.layout_selectwireless_listitem, mWirelessNetList);
		mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		mRefreshSection = (LinearLayout) findViewById(R.id.layout_selectwireless_refresh_section);
		mTimer = new Timer();
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

		/* Set object properties. */
		mWirelessNetListView.setEmptyView(findViewById(R.id.layout_selectwireless_list_empty));
		mWirelessNetListView.setAdapter(mListViewAdapter);
		mWirelessNetListView.setClickable(true);
		mWirelessNetListView.setOnItemClickListener(this);
		((Button) findViewById(R.id.layout_selectwireless_refresh_button)).setOnClickListener(this);

		// Intent filters...
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs.

				mWirelessNetList.clear();

				for(ScanResult wifi : mWifiManager.getScanResults())
					mWirelessNetList.add(new WirelessNetwork(wifi));

				mWirelessNetList.add(new WirelessNetwork("Andared", "FF:FF:FF:FF:FF:FF", -50, "[WPA]"));
				//mWirelessNetList.add(new WirelessNetwork("WLAN4DC866", "00:22:2D:04:DC:E8", -80, "[WPA]"));
				//mWirelessNetList.add(new WirelessNetwork("WLAN_1234", "64:68:0c:AA:AA:AA", -100, "[WPA]"));
				//mWirelessNetList.add(new WirelessNetwork("ThomsonF8A3D0", "AA:AA:AA:AA:AA:AA", -100, "[WEP??"));
				//mWirelessNetList.add(new WirelessNetwork("JAZZTEL_E919", "64:68:0C:DE:39:48", -100, "[WPA]??"));
				//mWirelessNetList.add(new WirelessNetwork("HAWEI1", "00:18:82:32:81:20", -100, "[WPA]??"));
				//mWirelessNetList.add(new WirelessNetwork("WLAN_E919", "64:68:0C:96:e9:1c", -100, "[WPA]??"));//dbcd970f0d705754206d
				//mWirelessNetList.add(new WirelessNetwork("HAWEI2", "00:22:A1:32:81:20", -100, "[WPA]??"));
				//mWirelessNetList.add(new WirelessNetwork("YACOMXXXXXX", "00:22:A1:32:81:20", -100, "[WPA]??"));
				//mWirelessNetList.add(new WirelessNetwork("bazinga", "FF:FF:FF:FF:FF:FF", -100, "[WPA]??"));

				mListViewAdapter.notifyDataSetChanged();

				if(mSharedPreferences.getBoolean(Preferences.PREFERENCES_VIBRATEUPDATE_KEY, Preferences.PREFERENCES_VIBRATEUPDATE_DEFAULT))
					mVibrator.vibrate(150);

				if(mSharedPreferences.getBoolean(Preferences.PREFERENCES_AUTOUPDATE_KEY, Preferences.PREFERENCES_AUTOUPDATE_DEFAULT)) {
					mStartScanTask = new TimerTask() {
						@Override
						public void run() {
							mWifiManager.startScan();
						}
					};
					mTimer.schedule(mStartScanTask, Integer.parseInt(mSharedPreferences.getString(Preferences.PREFERENCES_UPDATEINTERVAL_KEY, Preferences.PREFERENCES_UPDATEINTERVAL_DEFAULT)));
				}
			}
		};
	}
	
	@Override
	public void onResume() {
		super.onResume();

		// Register receivers...
		registerReceiver(mBroadcastReceiver, mIntentFilter);

		if(mSharedPreferences.getBoolean(Preferences.PREFERENCES_AUTOUPDATE_KEY, Preferences.PREFERENCES_AUTOUPDATE_DEFAULT)) {
			mRefreshSection.setVisibility(View.GONE);
		} else {
			mRefreshSection.setVisibility(View.VISIBLE);
		}

		// Scan for the first time...
		mWifiManager.startScan();
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister receivers...
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		WirelessNetwork w = (WirelessNetwork) adapter.getItemAtPosition(position);
		if(w.getCrackeable()){
	    	w.crack();
	    	Intent i = new Intent(mContext, ShowPass.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(ShowPass.EXTRA_NETWORK, w);
			startActivity(i);
		}else{
			Toast.makeText(mContext, getString(R.string.selectwireless_unsupported), Toast.LENGTH_SHORT).show();
		}
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_scanmode, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_about:
	    	(new AboutDialog(mContext)).show();
	        return true;
	    case R.id.menu_quit:
	    	this.finish();
	    	return true;
	    case R.id.menu_networks:
	    	(new SupportedNetworksDialog(mContext)).show();
	        return true;    
	    case R.id.menu_settings:
	    	 startActivity(new Intent(mContext, Preferences.class));
	    	 return true;
	    case R.id.menu_updater:
			(new UpdateDialog(mContext)).show();
			return true;
	    }
	    return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.layout_selectwireless_refresh_button:
			mWifiManager.startScan();
			break;
		}
	}
}