package com.iamback.talistpagetwo;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updatePreferences();
        activateTheme(theme, themeOn);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
    //public  static  final String SORT = "SORT";
    //public static final String SORTVALUES = "SORT_VALUES";
    //public static final String A_OR_D = "A_OR_D";
    public static final String THEMES_ON = "THEMES_ON";
    public static final String THEME = "THEME";


    //boolean sortable = true;
    //int sortBy = 2;
    //int order = 1;
    boolean themeOn = false;
    int theme = 1;

    public void updatePreferences(){
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //sortable = defaultSharedPreferences.getBoolean(SORT, false);
        themeOn = defaultSharedPreferences.getBoolean(THEMES_ON, true);
        //sortBy = Integer.parseInt(defaultSharedPreferences.getString(SORTVALUES, "2"));
        //order = Integer.parseInt(defaultSharedPreferences.getString(A_OR_D, "1"));
        theme = Integer.parseInt(defaultSharedPreferences.getString(THEME, "2"));
    }
    public void activateTheme(int theme, boolean themeOn){
        if (themeOn){
            if (theme == 1){setTheme(R.style.AppTheme2);}
            if (theme == 2){setTheme(R.style.AppTheme);}
            if (theme == 3){setTheme(R.style.AppThemeRed);}
            if (theme == 4){setTheme(R.style.AppThemeOrange);}
            if (theme == 5){setTheme(R.style.AppThemeYellow);}
            if (theme == 6){setTheme(R.style.AppThemeGreen);}
            if (theme == 7){setTheme(R.style.AppThemeBlue);}
            if (theme == 8){setTheme(R.style.AppThemeIndigo);}
            if (theme == 9){setTheme(R.style.AppThemeCyan);}
            if (theme == 10){setTheme(R.style.AppThemeBrown);}
            if (theme == 11){setTheme(R.style.AppThemeGrey);}

        }
    }

}