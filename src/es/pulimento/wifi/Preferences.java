package es.pulimento.wifi;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {
	
//	private SharedPreferences s;
//	private CheckBoxPreference au;// auto-update list in SCAN Mode
//	private CheckBoxPreference vF;// vibrate when found a vulnerable network
//	private CheckBoxPreference vU;// vibrate on each autoupdate of the list (@requires au==true)
//	private ListPreference uI;//Update interval (2,5,10 seconds)
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}


//		s = getSharedPreferences("preferences", MODE_PRIVATE);
//		au = (CheckBoxPreference) findPreference("autoupdate");
//		au.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {			
//			public boolean onPreferenceChange(Preference arg0, Object arg1) {
//				//Log.v("pulWifi","estamos en Preferences.onPreferenceChange() autoupdate");
//				SharedPreferences.Editor e = s.edit();
//				if(au.isChecked()){
//					//Log.v("pulWifi","autoupdate isChecked() == true");
//					au.setChecked(false);
//					e.putBoolean("autoupdate",false);
//				}else{
//					//Log.v("pulWifi","autoupdate isChecked() == false");
//					au.setChecked(true);
//					e.putBoolean("autoupdate",true);
//				}
//				//Log.v("pulWifi","hacemos commit() y nos las piramos del Preferences.onPreferenceChange() autoupdate");
//				e.commit();
//				return false;
//			}
//		});
//	
//		vF = (CheckBoxPreference) findPreference("vibrateFOUND");
//		vF.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//		
//			public boolean onPreferenceChange(Preference arg0, Object arg1) {
//				//Log.v("pulWifi","estamos en Preferences.onPreferenceChange() vibrateFOUND");
//				SharedPreferences.Editor e = s.edit();
//				if(vF.isChecked()){
//					//Log.v("pulWifi","vibrateFOUND isChecked() == true");
//					vF.setChecked(false);
//					e.putBoolean("vibrateFOUND",false);
//				}else{
//					//Log.v("pulWifi","vibrateFOUND isChecked() == false");
//					vF.setChecked(true);
//					e.putBoolean("vibrateFOUND",true);
//				}
//				//Log.v("pulWifi","hacemos commit() y nos las piramos del Preferences.onPreferenceChange() vibrateFOUND");
//				e.commit();
//				return false;
//			}
//		});
//		
//		vU = (CheckBoxPreference) findPreference("vibrateUPDATE");
//		vU.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//		
//			public boolean onPreferenceChange(Preference arg0, Object arg1) {
//				//Log.v("pulWifi","estamos en Preferences.onPreferenceChange() vibrateUPDATE");
//				SharedPreferences.Editor e = s.edit();
//				if(vU.isChecked()){
//					//Log.v("pulWifi","vibrateUPDATE isChecked() == true");
//					vU.setChecked(false);
//					e.putBoolean("vibrateUPDATE",false);
//				}else{
//					//Log.v("pulWifi","vibrateUPDATE isChecked() == false");
//					vU.setChecked(true);
//					e.putBoolean("vibrateUPDATE",true);
//				}
//				//Log.v("pulWifi","hacemos commit() y nos las piramos del Preferences.onPreferenceChange() vibateUPDATE");
//				e.commit();
//				return false;
//			}
//		});
//		
//		uI = (ListPreference) findPreference("updateInterval");
//		uI.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){			
//			public boolean onPreferenceChange(Preference arg0, Object arg1) {
//				Log.e("pulWifi","estamos en onPreferenceChange() updateInterval");
//				SharedPreferences.Editor e= s.edit();
//				String update = uI.getValue();
//				Log.e("pulWifi",update);
//				e.putString("updateInterval",update);
//				e.commit();
//				return false;
//			}
//		});
//	}
//}