package es.pulimento.wifi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class Preferences extends PreferenceActivity implements OnPreferenceChangeListener {

	/* Constants. */
	private final String PREFERENCES_NAME = "preferences";

	/* Needed variables. */
	private Context mContext;
	private SharedPreferences mPreferences;

	/* Preference keys. */
	public final String PREFERENCES_AUTOUPDATE_KEY;
	public final String PREFERENCES_VIBRATEUPDATE_KEY;
	public final String PREFERENCES_VIBRATEFOUND_KEY;
	public final String PREFERENCES_UPDATEINTERVAL_KEY;

	/* Preference default values. */
	public final boolean PREFERENCES_AUTOUPDATE_DEFAULT;
	public final boolean PREFERENCES_VIBRATEUPDATE_DEFAULT;
	public final boolean PREFERENCES_VIBRATEFOUND_DEFAULT;
	public final int PREFERENCES_UPDATEINTERVAL_DEFAULT;

	/* Current preferences. */
	public boolean PREFERENCES_AUTOUPDATE_CURRENT;
	public boolean PREFERENCES_VIBRATEUPDATE_CURRENT;
	public boolean PREFERENCES_VIBRATEFOUND_CURRENT;
	public int PREFERENCES_UPDATEINTERVAL_CURRENT;

	/* Class constructor for getting preferences. */
	Preferences(Context ctx) {
		mContext = ctx;
		mPreferences = mContext.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		PREFERENCES_AUTOUPDATE_KEY = mContext.getString(R.string.preferences_autoupdate_key);
		PREFERENCES_AUTOUPDATE_DEFAULT = Boolean.parseBoolean(mContext.getString(R.string.preferences_autoupdate_default));
		PREFERENCES_VIBRATEUPDATE_KEY = mContext.getString(R.string.preferences_vibrateupdate_key);
		PREFERENCES_VIBRATEUPDATE_DEFAULT = Boolean.parseBoolean(mContext.getString(R.string.preferences_vibrateupdate_default));
		PREFERENCES_VIBRATEFOUND_KEY = mContext.getString(R.string.preferences_vibratefound_key);
		PREFERENCES_VIBRATEFOUND_DEFAULT = Boolean.parseBoolean(mContext.getString(R.string.preferences_vibratefound_default));
		PREFERENCES_UPDATEINTERVAL_KEY = mContext.getString(R.string.preferences_updateinterval_key);
		PREFERENCES_UPDATEINTERVAL_DEFAULT = Integer.parseInt(mContext.getString(R.string.preferences_updateinterval_default));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		/* Set on preference listeners for updating variables. */
		PreferenceScreen prefScreen = this.getPreferenceScreen();
		prefScreen.findPreference(PREFERENCES_AUTOUPDATE_KEY).setOnPreferenceChangeListener(this);
		prefScreen.findPreference(PREFERENCES_UPDATEINTERVAL_KEY).setOnPreferenceChangeListener(this);
		prefScreen.findPreference(PREFERENCES_VIBRATEFOUND_KEY).setOnPreferenceChangeListener(this);
		prefScreen.findPreference(PREFERENCES_VIBRATEUPDATE_KEY).setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		/* Update current preference variables any time a preference is changed. */
		PREFERENCES_AUTOUPDATE_CURRENT = mPreferences.getBoolean(PREFERENCES_AUTOUPDATE_KEY, PREFERENCES_AUTOUPDATE_DEFAULT);
		PREFERENCES_VIBRATEUPDATE_CURRENT = mPreferences.getBoolean(PREFERENCES_VIBRATEUPDATE_KEY, PREFERENCES_VIBRATEUPDATE_DEFAULT);
		PREFERENCES_VIBRATEFOUND_CURRENT = mPreferences.getBoolean(PREFERENCES_VIBRATEFOUND_KEY, PREFERENCES_VIBRATEFOUND_DEFAULT);
		PREFERENCES_UPDATEINTERVAL_CURRENT = Integer.parseInt(mPreferences.getString(PREFERENCES_UPDATEINTERVAL_KEY, String.valueOf(PREFERENCES_UPDATEINTERVAL_DEFAULT)));
		return true;
	}
}