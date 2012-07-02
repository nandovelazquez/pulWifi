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

package es.pulimento.wifi.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import es.pulimento.wifi.R;
import es.pulimento.wifi.ui.dialogs.AboutDialog;
import es.pulimento.wifi.ui.utils.UpdateChecker;

public class Preferences extends PreferenceActivity {

	/* Constants. */
	public final static String PREFERENCES_NAME = "preferences";

	/* Preference keys. */
	public final static String PREFERENCES_AUTOUPDATE_KEY = "prefs_key_autoupdate";
	public final static String PREFERENCES_VIBRATEUPDATE_KEY = "prefs_key_vibrateupdate";
	public final static String PREFERENCES_UPDATEINTERVAL_KEY = "prefs_key_updateinterval";

	/* Preference default values. */
	public final static boolean PREFERENCES_AUTOUPDATE_DEFAULT = true;
	public final static boolean PREFERENCES_VIBRATEUPDATE_DEFAULT = false;
	public final static String PREFERENCES_UPDATEINTERVAL_DEFAULT = "2000";

	/* Preference */
	private Preference mApkVersion = null;

	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		PreferenceScreen prefScreen = getPreferenceScreen();
		mApkVersion = prefScreen.findPreference(getString(R.string.preferences_about_key));
	}

	@Override
	protected void onResume() {
		super.onResume();
		new UpdateVersions().execute();
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		String pref = preference.getKey();
		if (pref.equals(getString(R.string.preferences_updater_key))) {
			new UpdateChecker(this, null).work();
			return true;
		}		
		if (pref.equals(getString(R.string.preferences_about_key))) {
			(new AboutDialog(this)).show();
			return true;
		}
		return false;
	}

	private class UpdateVersions extends AsyncTask<Void, Integer, Integer> {
		private String apkVersion;
		private int apkVersionCode;

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
				apkVersion = pInfo.versionName;
				apkVersionCode = pInfo.versionCode;
			} catch (NameNotFoundException e) {
				Log.e("pulWifi", "Superuser is not installed?", e);
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			mApkVersion.setTitle(getString(R.string.preferences_about_title, apkVersion, apkVersionCode));
		}
	}
}