package com.baiiu.tsnakebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.baiiu.tsnackbar.LUtils;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //跟布局为CoordinatorLayout,并且添加了fitsSystemWindows属性,需要调用该方法
        //建议将该属性添加在Toolbar上或者AppBarLayout上
        //TSnackBar.setCoordinatorLayoutFitsSystemWindows(true);

        //若将fitsSystemWindows添加在AppBarLayout或者Toolbar上,则不用调用此方法
        //if (LUtils.hasKitKat()) {
        //    LUtils.instance(this)
        //            .setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        //}
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.success:
                TSnackBar.make(v, "success_info", Prompt.SUCCESS)
                        .show();
                break;
            case R.id.error:
                TSnackBar.make(v, "error_info", Prompt.ERROR)
                        .show();
                break;
            case R.id.warning:
                TSnackBar.make(v, "warning_info", Prompt.WARNING)
                        .show();
                break;
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        LUtils.clear();
    }
}
