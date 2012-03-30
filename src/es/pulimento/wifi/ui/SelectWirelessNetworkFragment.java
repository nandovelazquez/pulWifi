package es.pulimento.wifi.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.WirelessNetwork;

public class SelectWirelessNetworkFragment extends ListFragment {

	private List<WirelessNetwork> mWirelessNetList;
	private WifiManager mWifiManager;
	private Vibrator mVibrator;
	private LinearLayout mRefreshSection;
	private Timer mTimer;
	private SharedPreferences mSharedPreferences;
	private FragmentActivity mActivity;
	private Context mContext;
	private IntentFilter mIntentFilter;
	private BroadcastReceiver mBroadcastReceiver;
	private ListAdapter mListAdapter;
	private final String TAG = "pw.NetworkListFragment";
	protected static final boolean D = true; // Debug

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		WirelessNetwork w = (WirelessNetwork) mListAdapter.getItem(position);
		if (w.isCrackeable()) {
			w.crack();
			Intent i = new Intent(mContext, ShowPassActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(ShowPassActivity.EXTRA_NETWORK, w);
			startActivity(i);
		} else {
			Toast.makeText(mContext, getString(R.string.selectwireless_unsupported), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mWirelessNetList = new ArrayList<WirelessNetwork>();
		// Set custom list adapter
		mListAdapter = new NetworkListAdapter(mWirelessNetList, getActivity());
		setListAdapter(mListAdapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_selectwirelessnetworkfragment, container, false);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Unregister receivers...
		mActivity.unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register receivers...
		mActivity.registerReceiver(mBroadcastReceiver, mIntentFilter);

		if (mSharedPreferences.getBoolean(Preferences.PREFERENCES_AUTOUPDATE_KEY,
				Preferences.PREFERENCES_AUTOUPDATE_DEFAULT)) {
			mRefreshSection.setVisibility(View.GONE);
		} else {
			mRefreshSection.setVisibility(View.VISIBLE);
		}

		// Scan for the first time...
		mWifiManager.startScan();
	}

	@Override
	public void onStart() {
		super.onStart();
		mActivity = getActivity();
		mContext = mActivity.getApplicationContext();

		mWifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
		mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		mRefreshSection = (LinearLayout) mActivity.findViewById(R.id.layout_selectwireless_refresh_section);
		mTimer = new Timer();
		((Button) mActivity.findViewById(R.id.layout_selectwireless_refresh_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(D) Log.i(TAG,"Refresh button pressed!");
						mWifiManager.startScan();
					}
				});
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mBroadcastReceiver = new BroadcastReceiver() {
			private TimerTask mStartScanTask;

			@Override
			public void onReceive(Context c, Intent i) {
				// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event
				// occurs.

				if (D) Log.i(TAG, "Refreshing automatically the list");

				mWirelessNetList.clear();

				for (ScanResult wifi : mWifiManager.getScanResults())
					mWirelessNetList.add(new WirelessNetwork(wifi));

				// For testing networks...
				//mWirelessNetList.add(new WirelessNetwork("Andared", "AA:AA:AA:AA:AA:AA", 0, "[WPA]"));
				//mWirelessNetList.add(new WirelessNetwork("Discus--DA1CC5", "00:1C:A2:DA:1C:C5", 0, "[WPA]"));
				//mWirelessNetList.add(new WirelessNetwork("WLAN_1234", "64:68:0c:AA:AA:AA", 0, "[WPA]"));
				//mWirelessNetList.add(new WirelessNetwork("DLink-AAAAAA", "64:68:0c:64:68:0c", 0, "[WPA]"));
				
				// mWirelessNetList.add(new WirelessNetwork("WLAN4DC866", "00:22:2D:04:DC:E8", -80, "[WPA]"));
				// mWirelessNetList.add(new WirelessNetwork("ThomsonF8A3D0", "AA:AA:AA:AA:AA:AA", -100, "[WEP??"));
				// mWirelessNetList.add(new WirelessNetwork("JAZZTEL_E919", "64:68:0C:DE:39:48", -100, "[WPA]??"));
				// mWirelessNetList.add(new WirelessNetwork("HAWEI1", "00:18:82:32:81:20", -100, "[WPA]??"));
				// mWirelessNetList.add(new WirelessNetwork("WLAN_E919", "64:68:0C:96:e9:1c", -100, "[WPA]??"));//dbcd970f0d705754206d
				// mWirelessNetList.add(new WirelessNetwork("HAWEI2", "00:22:A1:32:81:20", -100, "[WPA]??"));
				// mWirelessNetList.add(new WirelessNetwork("YACOMXXXXXX", "00:22:A1:32:81:20", -100, "[WPA]??"));
				// mWirelessNetList.add(new WirelessNetwork("bazinga", "FF:FF:FF:FF:FF:FF", -100, "[WPA]??"));

				/* Trick to refresh the ListView */
				getListView().invalidateViews();

				if (mSharedPreferences.getBoolean(Preferences.PREFERENCES_VIBRATEUPDATE_KEY,
						Preferences.PREFERENCES_VIBRATEUPDATE_DEFAULT)) mVibrator.vibrate(150);

				if (mSharedPreferences.getBoolean(Preferences.PREFERENCES_AUTOUPDATE_KEY,
						Preferences.PREFERENCES_AUTOUPDATE_DEFAULT)) {
					mStartScanTask = new TimerTask() {
						@Override
						public void run() {
							mWifiManager.startScan();
						}
					};
					mTimer.schedule(mStartScanTask,
							Integer.parseInt(mSharedPreferences.getString(Preferences.PREFERENCES_UPDATEINTERVAL_KEY,
									Preferences.PREFERENCES_UPDATEINTERVAL_DEFAULT)));
				}
			}
		};
	}
}

class NetworkListAdapter implements ListAdapter {

	/* Variables. */
	private List<WirelessNetwork> mItems;
	private Drawable mDrawLocked, mDrawUnlocked;
	private Drawable mSignalLevel1, mSignalLevel2, mSignalLevel3, mSignalLevel4;
	private LayoutInflater mLayoutInflater;
	private final String TAG = "pw.ListAdapter";

	public NetworkListAdapter(List<WirelessNetwork> items, Activity act) {
		Resources res = act.getApplicationContext().getResources();

		/* Initialize all variables. */
		// Testing if this force listview to update
		mItems = new ArrayList<WirelessNetwork>();
		mItems = items;
		mItems.add(null);
		if (SelectWirelessNetworkFragment.D) Log.e(TAG, "NetworkListAdapter<init>");
		mLayoutInflater = (LayoutInflater) act.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDrawLocked = res.getDrawable(R.drawable.ic_locked);
		mDrawUnlocked = res.getDrawable(R.drawable.ic_unlocked);
		mSignalLevel1 = res.getDrawable(R.drawable.ic_signal_1);
		mSignalLevel2 = res.getDrawable(R.drawable.ic_signal_2);
		mSignalLevel3 = res.getDrawable(R.drawable.ic_signal_3);
		mSignalLevel4 = res.getDrawable(R.drawable.ic_signal_4);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/* Sort items before showing them. */
		Collections.sort(mItems);

		if (convertView == null) convertView = mLayoutInflater.inflate(R.layout.layout_selectwireless_listitem, null);

		WirelessNetwork item = mItems.get(position);
		if (item != null) {
			TextView crackeable = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_crackeable);
			if (crackeable != null)
				crackeable.setBackgroundDrawable((item.isCrackeable()) ? mDrawUnlocked : mDrawLocked);

			TextView essid = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_essid);
			if (essid != null) essid.setText(item.getEssid());

			TextView bssid = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_bssid);
			if (bssid != null) bssid.setText(item.getBssid());

			ImageView signal = (ImageView) convertView.findViewById(R.id.layout_selecwireless_listitem_strength);
			if (signal != null) {
				int signalLevel = WifiManager.calculateSignalLevel(item.getSignal(), 4);
				signal.setImageDrawable((signalLevel == 0) ? mSignalLevel1 : (signalLevel == 1) ? mSignalLevel2
						: (signalLevel == 2) ? mSignalLevel3 : mSignalLevel4);
			}

			TextView capabilities = (TextView) convertView.findViewById(R.id.layout_selecwireless_listitem_security);
			if (capabilities != null) capabilities.setText(item.getCapabilities().toStringId());
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return mItems.isEmpty();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
}