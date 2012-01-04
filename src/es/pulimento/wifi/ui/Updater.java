package es.pulimento.wifi.ui;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.pulimento.wifi.R;

public class Updater extends Activity implements OnClickListener {

	private Context mContext;
	private TextView mLatestVersion;
	private Button updateButton;

	private final String VERSION_URL = "https://raw.github.com/pulWifi/pulWifi/master/version_latest";
	private final String APK_URL = "https://github.com/downloads/pulWifi/pulWifi/pulWifi_%s_signed.apk";
	private final String APK_NAME = "pulWifi.apk";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_updater);

		mContext = this;
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
			break;
		}
	}

	public void Update(String apkurl) throws MalformedURLException, IOException {
		HttpURLConnection c = (HttpURLConnection) new URL(apkurl).openConnection();
		c.setRequestMethod("GET");
		c.setDoOutput(true);
		c.connect();
		File file = new File(Environment.getExternalStorageDirectory()+ "/downloads/");
		file.mkdirs();
		FileOutputStream fos = new FileOutputStream(new File(file, APK_NAME));
		InputStream is = c.getInputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1)
			fos.write(buffer, 0, len);
		fos.close();
		is.close();
		startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+ "/downloads/app.apk")), "application/vnd.android.package-archive"));
	}

	public class GetLatestVersion extends AsyncTask<Void, Void, Integer> {

		private ProgressDialog dialog;

		@Override
		public void onPreExecute() {
			dialog = ProgressDialog.show(mContext, "", "Checking...", true);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				byte [] versionData = new byte[6];
				(new DataInputStream((new URL(VERSION_URL)).openConnection().getInputStream())).read(versionData);
				Log.v("PULW", String.valueOf(versionData));
			} catch (MalformedURLException e) {
				// TODO: This should not be fired. Should be reported.
				return -1;
			} catch (UnknownHostException e) {
				// Network error.
				return -1;
			} catch (IOException e) {
				// TODO: Check what causes this.
				return -1;
			}
			return -1;
		}

		@Override
		public void onPostExecute(Integer res) {
			if(res == -1)
			{
				Toast.makeText(mContext, R.string.updater_error, Toast.LENGTH_LONG).show();
				updateButton.setVisibility(View.GONE);
			}
			else
			{
				mLatestVersion.setText(String.valueOf(res));
				updateButton.setVisibility(View.VISIBLE);
			}
			dialog.dismiss();
			return;
		}
	}
}