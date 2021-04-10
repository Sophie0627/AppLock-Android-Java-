package applock.protect.bit.applock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;


public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_preference);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsPreferenceFragment()).commit();
//        } else {
//            SettingsPreferenceFragment frag = (SettingsPreferenceFragment) getFragmentManager().findFragmentById(R.id.content_frame);
//            if (frag != null) frag.createSettings();
//        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
          case android.R.id.home:

              SecurityLocksCommon.IsAppDeactive = false;
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(SecurityLocksCommon.IsAppDeactive ){
/*            Common.CurrentActivity = this;
            if(!Common.IsStealthModeOn){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }*/
            finish();
            System.exit(0);

        }

    }


    @Override
    public void onBackPressed() {

        SecurityLocksCommon.IsAppDeactive = false;
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

