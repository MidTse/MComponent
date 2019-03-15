package com.mid.component.demo;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.fengchen.uistatus.UiStatusManager;
import com.fengchen.uistatus.annotation.UiStatus;
import com.jess.arms.base.delegate.AppLifecycles;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    :
 *     version : 1.0
 * </pre>
 */

public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //在android5.0以下的系统就执行安装指令，在5.0以上ART采用oat技术，预先将多个dex加载好
            MultiDex.install(base);
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {

        UiStatusManager.getInstance()
                .addUiStatusConfig(UiStatus.LOADING, R.layout.public_loadsir_progress)
                .addUiStatusConfig(UiStatus.LOAD_ERROR, R.layout.public_loadsir_error)
                .addUiStatusConfig(UiStatus.EMPTY, R.layout.public_loadsir_empty);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
