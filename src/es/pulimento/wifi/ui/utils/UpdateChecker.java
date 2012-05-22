package es.pulimento.wifi.ui.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.dialogs.UpdateDialog;

public class UpdateChecker implements Runnable {

	/*
	 * Public constants.
	 */
	final public static int MSG_UPDATE_DONE = 2;

	/*
	 * Private constants.
	 */
	private final String VERSION_URL = "https://raw.github.com/pulWifi/pulWifi/master/version_latest";
	private final String APK_URL = "https://github.com/downloads/pulWifi/pulWifi/pulWifi_%s_signed.apk";

	/*
	 * Global variables.
	 */
	private Handler mHandler;
	private Activity mActivity;
	private UpdateDialog mUpdateDialog;
	private ProgressDialog mProgressDialog;

	public UpdateChecker(Activity activity, Handler handler) {
		/*
		 * Initialize variables.
		 */
		mHandler = handler;
		mActivity = activity;
		mUpdateDialog = null;
		mProgressDialog = null;

		if(mHandler == null) {
			mProgressDialog = new ProgressDialog(mActivity);
			mProgressDialog.setTitle("");
			mProgressDialog.setMessage(mActivity.getString(R.string.dialog_updater_checking));
			mProgressDialog.setCancelable(true);
		}
	}

	/*
	 * Check if newer versions are availiable.
	 */
	@Override
	public void run() {
		try {
			byte[] versionData = new byte[6];
			(new DataInputStream((new URL(VERSION_URL)).openConnection().getInputStream())).read(versionData);
			String latestVersion = new String(versionData).trim();
			if(!latestVersion.equals(mActivity.getString(R.string.app_version))) {
				mUpdateDialog = new UpdateDialog(mActivity, String.format(APK_URL, latestVersion));
				mUpdateDialog.show();
			}
		} catch (UnknownHostException e) {
			// Network error.
		} catch (IOException e) {
			// Network error.
		}
		if(mHandler != null)
			mHandler.sendEmptyMessage(MSG_UPDATE_DONE);
		else
			mActivity.runOnUiThread(new Runnable(){@Override public void run() {mProgressDialog.dismiss();}});
	}

	public void work() {
		/*
		 * Do check.
		 */
		new Thread(this).start();
		if(mProgressDialog != null)
			mProgressDialog.show();
	}

	public void clean() {
		/*
		 * Clean.
		 */
		if(mUpdateDialog != null)
			mUpdateDialog.dismiss();
		if(mProgressDialog != null)
			mProgressDialog.dismiss();
	}
}
