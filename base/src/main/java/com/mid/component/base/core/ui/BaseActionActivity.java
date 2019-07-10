package com.mid.component.base.core.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.mvp.IView;
import com.mid.component.base.utils.ShowUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 1.提供沉浸式状态栏功能 2.增加通用的加载对话框
 *     version : 0.1.0
 * </pre>
 */
public abstract class BaseActionActivity<P extends IPresenter> extends BaseActivity<P> implements IView, ICommit<ActivityEvent> {

    protected ImmersionBar mImmersionBar;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (enableImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this);
            configImmersionBar();
        }

    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).i("showLoading");
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).i("hideLoading");
    }

    @Override
    public void showMessage(@NonNull String message) {
        ShowUtils.showMessage(this, message);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void showCommiting() {
        ShowUtils.showLoadingDialog(this);
    }

    @Override
    public void hideCommiting() {
        ShowUtils.dismissLoadingDialog();
    }


    /**
     * 初始化系统状态栏
     */
    protected void configImmersionBar() {
        mImmersionBar.statusBarColor(android.R.color.white)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
    }


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean enableImmersionBar() {
        return true;
    }
}
