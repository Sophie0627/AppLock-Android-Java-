package applock.protect.bit.applock.Security;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.ajalt.reprint.core.Reprint;
import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.applock.AppLockActivity;

public class FingerprintActivity extends BaseActivity {

    TextView tv_fingerprint;
    SwitchCompat sc_fingerprintToggle;
    ImageView iv_fingerprint;
    FloatingActionButton fab_addFingerprint;
    //RelativeLayout rl_fingerprintIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        SecurityLocksCommon.IsAppDeactive = true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fingerprint Lock");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SecurityLocksCommon.isFingerprintEnabled = SecurityLocksSharedPreferences.GetObject(this).GetFingerPrintActive();
        tv_fingerprint = (TextView) findViewById(R.id.tv_fingerprint);
        sc_fingerprintToggle = (SwitchCompat) findViewById(R.id.sc_fingerprintToggle);
        iv_fingerprint = (ImageView) findViewById(R.id.iv_fingerprint);
        fab_addFingerprint = (FloatingActionButton) findViewById(R.id.fab_addFingerprint);
       // rl_fingerprintIcon  = (RelativeLayout) findViewById(R.id.rl_fingerprintIcon);

        sc_fingerprintToggle.setChecked(SecurityLocksCommon.isFingerprintEnabled);






        fab_addFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SecurityLocksCommon.IsAppDeactive = true;
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }
        });

        sc_fingerprintToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {

                SecurityLocksSharedPreferences.GetObject(FingerprintActivity.this).SetFingerPrintActive(isChecked);
                SecurityLocksCommon.isFingerprintEnabled = isChecked;
                if (isChecked)
                {
                    if (Reprint.hasFingerprintRegistered())
                    {
                        tv_fingerprint.setVisibility(View.VISIBLE);
                        iv_fingerprint.setVisibility(View.VISIBLE);
                        fab_addFingerprint.setVisibility(View.GONE);
                    //    rl_fingerprintIcon.setVisibility(View.VISIBLE);
                        tv_fingerprint.setText("Fingerprint enabled");
                        iv_fingerprint.setColorFilter(getResources().getColor(R.color.sharp_green_color));
                        //change icon to green
                    }
                    else
                    {
                        tv_fingerprint.setVisibility(View.VISIBLE);
                        iv_fingerprint.setVisibility(View.VISIBLE);
                        fab_addFingerprint.setVisibility(View.VISIBLE);
                    //    rl_fingerprintIcon.setVisibility(View.VISIBLE);
                        tv_fingerprint.setText("Add fingerprint");
                        iv_fingerprint.setColorFilter(getResources().getColor(android.R.color.white));
                        //change icon to green

                    }



                }
                else
                {
                    //change icon to grey
                    //tv_fingerprint.setVisibility(View.GONE);
                    tv_fingerprint.setText("Fingerprint disabled");
                    //iv_fingerprint.setVisibility(View.GONE);
                    fab_addFingerprint.setVisibility(View.GONE);
                   // rl_fingerprintIcon.clearAnimation();
                  //  rl_fingerprintIcon.setVisibility(View.GONE);
                    iv_fingerprint.setColorFilter(getResources().getColor(R.color.list_bg_Gray));
                }

            }
        });

        iv_fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SecurityLocksSharedPreferences.GetObject(FingerprintActivity.this).GetFingerPrintActive() && !Reprint.hasFingerprintRegistered())
                {
                    SecurityLocksCommon.IsAppDeactive = true;
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Reprint.isHardwarePresent())
        {
            sc_fingerprintToggle.setEnabled(true);
            if (SecurityLocksCommon.isFingerprintEnabled )
            {
                if (Reprint.hasFingerprintRegistered())
                {


                    fab_addFingerprint.setVisibility(View.GONE);
                    // rl_fingerprintIcon.setVisibility(View.VISIBLE);
                    tv_fingerprint.setText("Fingerprint enabled");
                    iv_fingerprint.setColorFilter(getResources().getColor(R.color.sharp_green_color));

                    //change icon to green
                }
                else
                {
                    tv_fingerprint.setText("Add fingerprint");
                    tv_fingerprint.setVisibility(View.VISIBLE);
                    fab_addFingerprint.setVisibility(View.VISIBLE);
                    //   rl_fingerprintIcon.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                //tv_fingerprint.setVisibility(View.GONE);
                tv_fingerprint.setText("Fingerprint disabled");
                //iv_fingerprint.setVisibility(View.GONE);
                fab_addFingerprint.setVisibility(View.GONE);
                iv_fingerprint.setColorFilter(getResources().getColor(R.color.list_bg_Gray));
            }



        }
        else
        {
            sc_fingerprintToggle.setEnabled(false);
            tv_fingerprint.setText("Fingerprint disabled");
            //tv_fingerprint.setVisibility(View.GONE);
            //iv_fingerprint.setVisibility(View.GONE);
            fab_addFingerprint.setVisibility(View.GONE);
            iv_fingerprint.setColorFilter(getResources().getColor(R.color.list_bg_Gray));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
	       case android.R.id.home:
               if(SecurityLocksCommon.isFingerprintSetFirstTime)
               {
                   SecurityLocksCommon.IsAppDeactive = false;
                   finish();
               }
               else
               {
                   SecurityLocksCommon.IsAppDeactive = false;
                   Intent intent = new Intent(this, AppLockActivity.class);
                   startActivity(intent);
                   finish();
               }


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {

        super.onPause();

        if(!SecurityLocksCommon.isFingerprintSetFirstTime && SecurityLocksCommon.IsAppDeactive){
            SecurityLocksCommon. IsFirstLogin = true ;
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed()
    {

        if(SecurityLocksCommon.isFingerprintSetFirstTime)
        {
            SecurityLocksCommon.IsFirstLogin = true;
            SecurityLocksCommon.IsAppDeactive = false;
            finish();
        }
        else
        {
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(this, AppLockActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
