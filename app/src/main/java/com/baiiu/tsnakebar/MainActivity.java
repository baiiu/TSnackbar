package com.baiiu.tsnakebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baiiu.tsnackbar.LUtils;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LUtils lUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (LUtils.hasKitKat()) {
            LUtils.instance(this).setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.success:
                TSnackbar.make(v, "success_info", Prompt.SUCCESS).show();
                break;
            case R.id.error:
                TSnackbar.make(v, "error_info", Prompt.ERROR).show();
                break;
            case R.id.warning:
                TSnackbar.make(v, "warning_info", Prompt.WARNING).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LUtils.instance(this).clear();
    }
}
