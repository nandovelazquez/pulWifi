package es.pulimento.wifi.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import es.pulimento.wifi.R;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}