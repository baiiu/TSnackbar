/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiiu.tsnackbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 之所以做成对象,是要使用属性动画
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LUtils {

    static final String ATTR_STATUSBAR_COLOR = "statusBarColor";

    private Activity mActivity;

    private LUtils(Activity activity) {
        mActivity = activity;
    }

    public static LUtils instance(Activity activity) {
        return new LUtils(activity);
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public int getStatusBarColor() {
        return getStatusBarColor(mActivity);
    }

    public void setStatusBarColor(int color) {
        setStatusBarColor(mActivity, color);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public static void clear() {
        resetColorPrimaryDark();
    }

    public static int getStatusBarColor(Activity activity) {
        if (Version.belowKitKat()) {
            // On pre-kitKat devices, you can have any status bar color so long as it's black.
            return Color.BLACK;
        }

        if (Version.hasL()) {
            return activity.getWindow()
                    .getStatusBarColor();
        }

        if (Version.hasKitKat()) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            View statusBarView = contentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getMeasuredHeight() == ScreenUtil.getStatusHeight(activity)) {
                Drawable drawable = statusBarView.getBackground();
                if (drawable != null) {
                    return ((ColorDrawable) drawable).getColor();
                }
            }
        }

        return -1;
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Version.belowKitKat() || activity == null) {
            return;
        }

        if (Version.hasL()) {
            activity.getWindow()
                    .setStatusBarColor(color);
            return;
        }

        if (Version.hasKitKat()) {
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);

            View statusBarView = contentView.getChildAt(0);
            //改变颜色时避免重复添加statusBarView
            if (statusBarView != null && statusBarView.getMeasuredHeight() == ScreenUtil.getStatusHeight(activity)) {
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(activity);
            statusBarView.setId(R.id.statusBarView);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                   ScreenUtil.getStatusHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, 0, lp);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    private static final int[] THEME_ATTRS = {
            android.R.attr.colorPrimaryDark, android.R.attr.windowTranslucentStatus
    };

    static int getDefaultStatusBarBackground(Context context) {

        final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
        try {
            return a.getColor(0, Color.TRANSPARENT);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            a.recycle();
        }

        return Color.TRANSPARENT;
    }

    public static void resetColorPrimaryDark() {
        TSnackBar.setAnimationEndColor(-1);
    }


    //=====================================TranslucentStatusBar====================================================

    /**
     * 将布局扩展到状态栏
     */
    public static void translucentToStatusBar(Activity activity) {
        if (activity == null || Version.belowKitKat()) {
            return;
        }

        if (Version.hasKitKatAndUnderL()) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                              WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            return;
        }

        if (Version.hasL()) {
            activity.getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow()
                    .setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    public static boolean isTranslucentStatus(Context context) {
        if (Version.belowKitKat()) {
            return false;
        }

        if (context instanceof Activity) {
            if (Version.hasL()) {
                return (((Activity) context).getWindow()
                        .getDecorView()
                        .getSystemUiVisibility() & (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)) != 0;
            } else {
                return hasTranslucentStatusFlag((Activity) context);
            }
        }

        final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);

        try {
            return a.getBoolean(1, false);
        } finally {
            a.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT) private static boolean hasTranslucentStatusFlag(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow()
                    .getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    public static void paddingContainer(Context context, View container) {
        if (context == null || container == null) return;
        if (context instanceof Activity) {
            if (Version.hasKitKat() && isTranslucentStatus(context)) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
                params.height = ScreenUtil.getStatusHeight(context) + params.height;
                container.setLayoutParams(params);
                container.setPadding(0, ScreenUtil.getStatusHeight(context), 0, 0);
            }
        } else {
            if (Version.hasKitKatAndUnderL() && isTranslucentStatus(context)) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
                params.height = ScreenUtil.getStatusHeight(context) + params.height;
                container.setLayoutParams(params);
                container.setPadding(0, ScreenUtil.getStatusHeight(context), 0, 0);
            }
        }

    }

}
