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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.utils.UpdateChecker;
import es.pulimento.wifi.ui.utils.WifiEnabler;
import es.pulimento.wifi.ui.views.ActionBarActivity;

/**
 * Simple splash screen that is used to check some pre-requisites before running.
 */
public class MainActivity extends ActionBarActivity {

	private Handler mHandler;
	private Activity mActivity;
	private Boolean mWifiDone;
	private Boolean mUpdatesDone;
	private WifiEnabler mWifiEnabler;
	private UpdateChecker mUpdateChecker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_mainactivity);

		mActivity = this;
		mWifiDone = false;
		mUpdatesDone = false;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what) {
				case WifiEnabler.MSG_WIFI_ENABLED:
					mWifiDone = true;
					break;
				case UpdateChecker.MSG_UPDATE_DONE:
					mUpdatesDone = true;
				}

				if(mWifiDone && mUpdatesDone) {
					mActivity.startActivity(new Intent(mActivity, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					mActivity.finish();
				}
			}
		};
		mWifiEnabler = new WifiEnabler(mActivity, mHandler);
		mUpdateChecker = new UpdateChecker(mActivity, mHandler);
	}


	@Override
	public void onResume() {
		super.onResume();

		/*
		 * Do checks.
		 */
		mWifiEnabler.work();
		mUpdateChecker.work();
	}

	@Override
	public void onPause() {
		super.onPause();

		/*
		 * Clean it all.
		 */
		mWifiEnabler.clean();
		mUpdateChecker.clean();
	}
}
