package es.pulimento.wifi.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import es.pulimento.wifi.R;
import es.pulimento.wifi.core.CrackNetwork;
import es.pulimento.wifi.core.WirelessNetwork;
import es.pulimento.wifi.dialogs.AboutDialog;

public class ManualCrack extends Activity implements OnClickListener {

	private Context mContext;
	private EditText mEditTextEssid;
	private EditText mEditTextBssid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manualcrack);

		mContext = this;
		mEditTextEssid = (EditText) findViewById(R.id.inputESSID);
		mEditTextBssid = (EditText) findViewById(R.id.inputMAC);

		((Button) findViewById(R.id.Button01)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		crack();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_manualmode, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ACERCA_DE:
			(new AboutDialog(mContext)).show();
			return true;
		case R.id.NETWORKS:
			Builder aDialog = new AlertDialog.Builder(this);
			aDialog.setTitle(R.string.supported_networks_title);
			aDialog.setMessage(R.string.supported_networks);
			aDialog.setPositiveButton(R.string.splash_ask_dialog_ok_button, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {dialog.cancel();}});
			AlertDialog a = aDialog.create();
			a.show();
			return true;
		case R.id.SALIR:
			this.finish();
			return true;
		}
		return false;
	}

	public String crack() {
		String ESSID = mEditTextEssid.getText().toString();
		String BSSID = mEditTextBssid.getText().toString();
		boolean bssidOK = (BSSID.length() == 12 || BSSID.length() == 17);
		if(ESSID == "" || !bssidOK) {
			Toast.makeText(mContext, R.string.manual_inputerror, Toast.LENGTH_SHORT).show();
			return null;
		}

		boolean crackeable = (new CrackNetwork(ESSID,BSSID,"WPA2")).isCrackeable();
		if(crackeable) {
			WirelessNetwork w = new WirelessNetwork(ESSID, BSSID, 1, "wawawa");
			w.crack();
			Intent i = new Intent(mContext, ShowPass.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(ShowPass.EXTRA_NETWORK, w);
			startActivity(i);
			return w.getPassword();
		} else {
			Toast.makeText(mContext, R.string.manual_inputerror, Toast.LENGTH_LONG).show();
			return null;
		}
	}
}
