package es.pulimento.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
			sobre();
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

		// OJO!!
		boolean crackeable = (new CrackNetwork(ESSID,BSSID,"WPA2")).isCrackeable();
		if(crackeable) {
			WirelessNetwork w = new WirelessNetwork(ESSID, BSSID, true, 1, "wawawa");
			String clave = (new CrackNetwork(w.getEssid(),w.getBssid(),"WPA2")).crackNetwork();
			w.setClave(clave);
			ShowPass.current = w;
			Intent intent = new Intent(mContext, ShowPass.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return clave;
		} else {
			Toast.makeText(mContext, R.string.manual_inputerror, Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public void sobre() {
		ScrollView svMessage = new ScrollView(this);
		TextView tvMessage = new TextView(this);
		SpannableString spanText = new SpannableString(mContext.getText(R.string.acerca_de));
		Linkify.addLinks(spanText, Linkify.ALL);
		tvMessage.setText(spanText);
		tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
		svMessage.setPadding(10, 0, 6, 0);
		svMessage.addView(tvMessage);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
			.setView(svMessage)
			.setTitle(getString(R.string.about_title))
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int id) {dialog.cancel();}})
			.setNegativeButton(getString(R.string.about_gpl), new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {WebViewActivity.readme = false;abrirNavegador();}})
			.setNeutralButton(getString(R.string.about_readme), new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {WebViewActivity.readme = true;abrirNavegador();}});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void abrirNavegador() {
		Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Context c = getApplicationContext();
		c.startActivity(i);
	}
}
