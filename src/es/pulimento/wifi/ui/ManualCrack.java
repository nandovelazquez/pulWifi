package es.pulimento.wifi.ui;

import android.app.Activity;
import android.content.Context;
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
import es.pulimento.wifi.core.WirelessNetwork;
import es.pulimento.wifi.dialogs.AboutDialog;
import es.pulimento.wifi.dialogs.SupportedNetworksDialog;

public class ManualCrack extends Activity implements OnClickListener {

	private Context mContext;
	private EditText mEditTextEssid;
	private EditText mEditTextBssid;
	private WirelessNetwork mWirelessNetwork;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manualcrack);

		mContext = this;
		mEditTextEssid = (EditText) findViewById(R.id.layout_manualcrack_essid);
		mEditTextBssid = (EditText) findViewById(R.id.layout_manualcrack_bssid);

		((Button) findViewById(R.id.layout_manualcrack_accept)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO: Let the user choose encryption.

		mWirelessNetwork = new WirelessNetwork(mEditTextEssid.getText().toString(), mEditTextBssid.getText().toString(), 0, "[WEP] [WPA]");
		if(!mWirelessNetwork.getCrackeable())
		{
			Toast.makeText(mContext, R.string.manual_inputerror, Toast.LENGTH_LONG).show();
			return;
		}
		mWirelessNetwork.crack();
		Intent i = new Intent(mContext, ShowPass.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(ShowPass.EXTRA_NETWORK, mWirelessNetwork);
		startActivity(i);
		return;
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
			(new SupportedNetworksDialog(mContext)).show();
			return true;
		case R.id.SALIR:
			this.finish();
			return true;
		}
		return false;
	}
}
