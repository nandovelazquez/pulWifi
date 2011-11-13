package es.pulimento.wifi;

import es.pulimento.wifi.MainTabHost;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;

/* Main activity. */
public class MainActivity extends Activity {

	/* Application context. */
	private Context mContext;
	/* Wifi manager for viewing and modifying wifi status. */
	private WifiManager mWifiManager;
	/* Used for terminating the application. */
	private Activity mActivity;
	/* Receiver for retrieving wifi state changes. */
	private BroadcastReceiver mBroadcastReceiver;
	/* Intent filter used for registering the receiver. */
	private IntentFilter mIntentFilter;
	/* Dialogs for asking the user to enable wifi and notifying errors. */
	private Dialog mFailedDialog, mAskDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		/* Define elements. */
		mContext = getApplicationContext();
		mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		mActivity = this;
		mIntentFilter = new IntentFilter();

		Builder fDialog = new AlertDialog.Builder(mActivity);
		fDialog.setTitle(R.string.splash_failed_dialog_error);
		fDialog.setMessage(R.string.splash_failed_dialog_msg);
		fDialog.setNeutralButton(R.string.splash_failed_dialog_ok_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Exit...
				mActivity.finish();
			}
		});
		mFailedDialog = fDialog.create();

		Builder aDialog = new AlertDialog.Builder(mActivity);
		aDialog.setTitle(R.string.splash_ask_dialog_title);
		aDialog.setMessage(R.string.splash_ask_dialog_msg);
		aDialog.setPositiveButton(R.string.splash_ask_dialog_yes_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(!mWifiManager.setWifiEnabled(true))
					mFailedDialog.show();
			}
		});
		aDialog.setNegativeButton(R.string.splash_ask_dialog_no_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mActivity.finish();
			}
		});
		mAskDialog = aDialog.create();

		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				// Code to execute when WIFI_STATE_CHANGED_ACTION event occurs.
				switch(i.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)) {
				case WifiManager.WIFI_STATE_UNKNOWN:
					mFailedDialog.show();
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					Intent intent = new Intent(mContext, MainTabHost.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					mActivity.finish();
					break;
				case WifiManager.WIFI_STATE_DISABLED:
					mAskDialog.show();
					break;
				}
			}
		};

		/* Set properties. */
		mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
	}

	@Override
	public void onResume() {
		super.onResume();

		/* Check wifi state. */
		if(mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED)
			mAskDialog.show();

		/* Register receivers. */
		registerReceiver(mBroadcastReceiver, mIntentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();

		/* Unregister receivers. */
		unregisterReceiver(mBroadcastReceiver);

		/* Dismiss dialogs. */
		mAskDialog.dismiss();
		mFailedDialog.dismiss();
	}
}
