package es.pulimento.wifi.dialogs;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;

public class UpdateDialog extends Dialog implements OnClickListener {

	private Context mContext;
	private TextView mLatestVersion;
	private Button updateButton;

	private final String VERSION_URL = "https://raw.github.com/pulWifi/pulWifi/master/version_latest";
	private final String APK_URL = "https://github.com/downloads/pulWifi/pulWifi/pulWifi_%s_signed.apk";

	public UpdateDialog(Context context) {
		super(context);

		mContext = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the content for our dialog...
		this.setContentView(R.layout.dialog_updater);
		this.setTitle(R.string.dialog_udpater_title);

		mLatestVersion = (TextView) findViewById(R.id.layout_updater_latest_version);
		updateButton = (Button) findViewById(R.id.layout_updater_update);

		// Set click listeners...
		((Button) findViewById(R.id.layout_updater_check)).setOnClickListener(this);
		updateButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.layout_updater_check:
			new GetLatestVersion().execute();
			break;
		case R.id.layout_updater_update:
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(APK_URL, mLatestVersion.getText()))));
			break;
		}
	}

	public class GetLatestVersion extends AsyncTask<Void, Void, String> {

		private ProgressDialog dialog;

		@Override
		public void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "", mContext.getString(R.string.dialog_updater_checking), true);
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				byte [] versionData = new byte[6];
				(new DataInputStream((new URL(VERSION_URL)).openConnection().getInputStream())).read(versionData);
				return new String(versionData).trim();
			} catch (MalformedURLException e) {
				// TODO: This should not be fired. Should be reported.
				return "ERR";
			} catch (UnknownHostException e) {
				// Network error.
				return "ERR";
			} catch (IOException e) {
				// TODO: Check what causes this.
				return "ERR";
			}
		}

		@Override
		public void onPostExecute(String res) {
			if(res == "ERR")
			{
				Toast.makeText(mContext, R.string.dialog_updater_error, Toast.LENGTH_LONG).show();
				updateButton.setVisibility(View.GONE);
			}
			else
			{
				mLatestVersion.setText(res);
				if(!res.equals(mContext.getString(R.string.app_version)))
					updateButton.setVisibility(View.VISIBLE);
			}
			dialog.dismiss();
			return;
		}
	}
}