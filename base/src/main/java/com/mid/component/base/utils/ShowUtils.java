package com.mid.component.base.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.dyhdyh.widget.loading.dialog.LoadingDialog;
import com.mid.component.base.core.ui.dialog.CommonDialogFactory;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/02/17
 *     desc    : 关于显示的工具类
 *     version : 0.1.0
 * </pre>
 */

public class ShowUtils {

    private ShowUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }


    /**
     * 显示加载对话框
     * @param context
     */
    public static void showLoadingDialog(Context context) {
        Dialog dialog = LoadingDialog.make(context, new CommonDialogFactory())
                .setCancelable(true)
                .create();
        dialog.show();
    }

    /**
     * 取消显示加载对话框
     */
    public static void dismissLoadingDialog() {
        LoadingDialog.cancel();
    }


    /**
     * 显示Toast信息
     * @param context
     * @param message
     */
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
