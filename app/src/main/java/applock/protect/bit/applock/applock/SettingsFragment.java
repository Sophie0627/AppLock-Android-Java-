//package applock.protect.bit.applock.applock;
//
//import android.os.Bundle;
//import android.preference.ListPreference;
//import android.preference.Preference;
//import android.preference.PreferenceFragment;
//import android.view.View;
//import java.util.ArrayList;
//import applock.protect.bit.applock.R;
//
//public class SettingsFragment extends PreferenceFragment {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.advance_settings_prefs);
//
//    }
//
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        createSettings();
//    }
//
//    public void ClearTempApplOckEnt(){
//        AppLockCommon.TempAppLockEnts.clear();
//        AppLockCommon.TempAppLockEnts = new ArrayList<AppLockEnt>();
//        AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
//    }
//
//
//    public void createSettings()
//    {
//
//        final SwitchPreferenceCompat lock_new_app = (SwitchPreferenceCompat) findPreference("lock_new_app");
//        final SwitchPreferenceCompat delay_lock = (SwitchPreferenceCompat) findPreference("delay_lock");
//        final SwitchPreferenceCompat advanced_lock = (SwitchPreferenceCompat) findPreference("advanced_lock");
//        final ListPreference delay_lock_limit = (ListPreference) findPreference("delay_lock_limit");
//
//        lock_new_app.setChecked(AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).GetLock_The_New_App());
//        delay_lock.setChecked(AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).GetDelay_In_Time_Lock());
//        advanced_lock.setChecked(AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).GetAdvanced_Lock());
//
//        delay_lock_limit.setValue(String.valueOf(AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).GetBrief_Exit_time()));
//        delay_lock_limit.setSummary(delay_lock_limit.getEntry());
//        delay_lock_limit.setEnabled(delay_lock.isChecked());
//
//        lock_new_app.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//                try
//                {
//                    boolean value = (boolean) newValue;
//                    AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).SetIsLock_The_New_App(value);
//                    lock_new_app.setChecked(value);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return false;
//            }
//        });
//
//        delay_lock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//                try
//                {
//                    boolean value = (boolean) newValue;
//                    AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).SetDelay_In_Time_Lock(value);
//                    delay_lock.setChecked(value);
//                    delay_lock_limit.setEnabled(delay_lock.isChecked());
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return false;
//            }
//        });
//
//        advanced_lock.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//                try
//                {
//                    boolean value = (boolean) newValue;
//                    AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).SetIsAdvanced_Lock(value);
//                    advanced_lock.setChecked(value);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return false;
//            }
//        });
//
//        delay_lock_limit.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//                try
//                {
//                    String str = (String) newValue;
//                    int value = Integer.parseInt(str);
//                    AppLockAdvSettingsSharedPreferences.GetObject(getActivity()).SetBrief_Exit_time(value);
//                    delay_lock_limit.setValue(String.valueOf(value));
//                    delay_lock_limit.setSummary(delay_lock_limit.getEntry());
//                    ClearTempApplOckEnt();
//
//
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                return false;
//            }
//        });
//
//    }
//
//}