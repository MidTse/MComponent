package com.mid.component.base.core.ui;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.billy.android.loading.Gloading;

import java.util.HashMap;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/11/14
 *     desc    : 对全局LoadingView的轻量级处理框架Gloading（https://github.com/luckybilly/Gloading/blob/master/README-zh-CN.md）再封装
 *  *            主要对加载中、加载失败、加载为空、加载成功多视图状态切换的简易实现
 *     version : 1.0
 * </pre>
 */
public class StatusViewManager {

    private View attachView;
    private Runnable retryTask;
    private Gloading.Holder holder;

    private HashMap<String, String> holdMap = new HashMap<>();

    public static final String KEY_HINT_ERROR = "hint_error";
    public static final String KEY_HINT_EMPTY = "hint_empty";

    public StatusViewManager(View attachView, Runnable retryTask) {
        this.attachView = attachView;
        this.retryTask = retryTask;
    }

    public void showLoading() {
        provideHolder().showLoading();
    }

    public void showSuccess() {
        provideHolder().showLoadSuccess();
    }

    public void showError(String hint) {
        holdMap.put(KEY_HINT_ERROR, TextUtils.isEmpty(hint) ? "加载数据出错" : hint);
        provideHolder().showLoadFailed();
    }

    public void showEmpty(String hint) {
        holdMap.put(KEY_HINT_EMPTY, TextUtils.isEmpty(hint) ? "加载信息为空" : hint);
        provideHolder().showEmpty();
    }


    public ViewGroup wrapperView() {
        return provideHolder().getWrapper();
    }

    private Gloading.Holder provideHolder() {
        if (holder == null) {
            if (attachView.getId() == android.R.id.content) {
                Gloading.getDefault()
                        .wrap((Activity) attachView.getContext())
                        .withData(holdMap)
                        .withRetry(() -> retryTask.run());

            } else {
                Gloading.getDefault()
                        .wrap(attachView)
                        .withData(holdMap)
                        .withRetry(() -> retryTask.run());
            }
        }
        return holder;
    }
}
