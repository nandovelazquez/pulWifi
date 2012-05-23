/*
 *  pulWifi , Copyright (C) 2011-2012 Javi Pulido / Antonio V‡zquez
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

package es.pulimento.wifi.ui.utils;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import es.pulimento.wifi.BuildConfig;
import es.pulimento.wifi.ui.dialogs.EnableWifiDialog;
import es.pulimento.wifi.ui.dialogs.FailedDialog;

public class WifiEnabler extends BroadcastReceiver {

	/*
	 * Public constants.
	 */
	final public static int MSG_WIFI_ENABLED = 1;

	/*
	 * Global variables.
	 */
	private Handler mHandler;
	private Activity mActivity;
	private WifiManager mWifiManager;
	private IntentFilter mIntentFilter;
	private FailedDialog mFailedDialog;
	private EnableWifiDialog mEnableWifiDialog;

	/*
	 * Constructor.
	 */
	public WifiEnabler(Activity activity, Handler handler) {
		/*
		 * Initialize variables.
		 */
		mHandler = handler;
		mActivity = activity;
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mWifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
		mFailedDialog = new FailedDialog(mActivity, new WeakReference<Activity>(mActivity));
		mEnableWifiDialog = new EnableWifiDialog(mActivity, new WeakReference<Activity>(mActivity));		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)) {
		case WifiManager.WIFI_STATE_UNKNOWN:
			if(BuildConfig.DEBUG)
				mHandler.sendEmptyMessage(MSG_WIFI_ENABLED);
			else
				mFailedDialog.show();
			break;
		case WifiManager.WIFI_STATE_ENABLED:
			mHandler.sendEmptyMessage(MSG_WIFI_ENABLED);
			break;
		case WifiManager.WIFI_STATE_DISABLED:
			mEnableWifiDialog.show();
			break;
		}
	}

	public void work() {
		/*
		 * Register receiver.
		 */
		mActivity.registerReceiver(this, mIntentFilter);

		/*
		 * Is wifi enabled? If not ask for it...
		 */
		if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
			mEnableWifiDialog.show();
	}

	public void clean() {
		/*
		 * Unregister everything and dismiss dialogs.
		 */
		mFailedDialog.dismiss();
		mEnableWifiDialog.dismiss();
		mActivity.unregisterReceiver(this);
	}
}