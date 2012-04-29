/*
 *  pulWifi , Copyright (C) 2011-2012 Javi Pulido / Antonio Vázquez
 *  
 *  This file is part of "pulWifi"
 *
 *  "pulWifi" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  "pulWifi" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with "pulWifi".  If not, see <http://www.gnu.org/licenses/>.
 */

package es.pulimento.wifi.ui;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import es.pulimento.wifi.BuildConfig;
import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.dialogs.EnableWifiDialog;
import es.pulimento.wifi.ui.dialogs.FailedDialog;
import es.pulimento.wifi.ui.utils.ActionBarActivity;

/**
 * Simple splash screen that is used to check some pre-requisites before running.
 */
public class MainActivity extends ActionBarActivity {

	private WifiManager mWifiManager;
	private Activity mActivity;
	private BroadcastReceiver mBroadcastReceiver;
	private IntentFilter mIntentFilter;
	private EnableWifiDialog mEnableWifiDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mainactivity);

		/* Define elements. */
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mActivity = this;
		mIntentFilter = new IntentFilter();
		mEnableWifiDialog = new EnableWifiDialog(mActivity, new WeakReference<Activity>(mActivity));

		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				// Code to execute when wifi state changes.
				switch (i.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)) {
				case WifiManager.WIFI_STATE_UNKNOWN:
					if(BuildConfig.DEBUG) {
						startActivity(new Intent(mActivity, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
						mActivity.finish();
					} else
						new FailedDialog(mActivity, new WeakReference<Activity>(mActivity)).show();
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					startActivity(new Intent(mActivity, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					mActivity.finish();
					break;
				case WifiManager.WIFI_STATE_DISABLED:
					new EnableWifiDialog(mActivity, new WeakReference<Activity>(mActivity));
					break;
				}
			}
		};

		// Set properties.
		mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Check wifi state.
		if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
			mEnableWifiDialog.show();

		// Register receivers.
		registerReceiver(mBroadcastReceiver, mIntentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();

		// Dismiss dialogs.
		mEnableWifiDialog.dismiss();

		// Unregister receivers.
		unregisterReceiver(mBroadcastReceiver);
	}
}
