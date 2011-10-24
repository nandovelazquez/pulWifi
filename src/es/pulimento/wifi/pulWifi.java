package es.pulimento.wifi;

import es.pulimento.wifi.ActividadPestanias;
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
public class pulWifi extends Activity {

	private Context context;
	private WifiManager wifi;
	private Activity activity;
	BroadcastReceiver broadcastReceiver;
	IntentFilter intentFilter;
	Dialog failedDialog,askDialog;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Mostramos el splashscreen
        setContentView(R.layout.splashscreen);

        //Damos valor a los atributos...
        context = getApplicationContext();
        wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        activity = this;

        //Failed dialog...
        Builder fDialog = new AlertDialog.Builder(activity);
        fDialog.setTitle(R.string.splash_failed_dialog_error);
        fDialog.setMessage(R.string.splash_failed_dialog_msg);
		fDialog.setNeutralButton(R.string.splash_failed_dialog_ok_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Exit...
				activity.finish();
			}
		});
		failedDialog = fDialog.create();

		//Ask dialog...
		Builder aDialog = new AlertDialog.Builder(activity);
		aDialog.setTitle(R.string.splash_ask_dialog_title);
		aDialog.setMessage(R.string.splash_ask_dialog_msg);
		aDialog.setPositiveButton(R.string.splash_ask_dialog_yes_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(!wifi.setWifiEnabled(true))
					failedDialog.show();
			}
		});
		aDialog.setNegativeButton(R.string.splash_ask_dialog_no_button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();			
			}
		});
		askDialog = aDialog.create();

		//Intent filters...
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
				// Code to execute when WIFI_STATE_CHANGED_ACTION event occurs.
				switch(i.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1)) {
				case WifiManager.WIFI_STATE_UNKNOWN:
					failedDialog.show();
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					Intent intent = new Intent(context, ActividadPestanias.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					activity.finish();
					break;
				case WifiManager.WIFI_STATE_DISABLED:
					askDialog.show();					
					break;
				}
			}
		};
	}

	@Override
	public void onResume(){
		super.onResume();

		//Check wifi state...
		if(wifi.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
			askDialog.show();
		}

		//Register receivers...
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onPause(){
		super.onPause();

		//Unregister receivers...
		unregisterReceiver(broadcastReceiver);

		//Dismiss dialogs...
		askDialog.dismiss();
		failedDialog.dismiss();
	}
}