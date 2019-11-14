package com.mid.component.base.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jess.arms.mvp.IPresenter;
import com.mid.component.base.R;

import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 基础的加载页面，主要通过UiStatus提供支持 具体参考 https://github.com/FengChenSunshine/UiStatus
 *               如果后期没有更换页面状态加载框架，仅仅只是更换加载页面的话，通过UiStatusManager全局配置
 *               如果后期需要更换页面状态加载框架，可以更换BaseLoadActivity
 *     version : 0.1.0
 * </pre>
 */
public abstract class BaseLoadActivity<P extends IPresenter> extends BaseActionActivity<P> implements LoadOwner {

    private View contentView;

    //多视图状态管理器
    protected StatusViewManager statusViewManager;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.public_include_view_root;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewGroup rootView = findViewById(R.id.public_rootView);
        if (rootView != null) {
            //加载标题栏和获取标题以下的内容视图
            LayoutInflater layoutInflater = getLayoutInflater();
            if (initTitleView() != 0) {
                layoutInflater.inflate(initTitleView(), rootView, true);
            }
            //由于LoadSir加载需要保持标题栏，需要通过以下方式配置
            contentView = View.inflate(this, initContentView(), null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rootView.addView(wrapperStatusView(contentView), layoutParams);
        } else {
            throw new RuntimeException("Unable to find viewID is rootView");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusViewManager = null;
        contentView = null;
    }

    @Override
    public void showLoading() {
        super.showLoading();
        if (statusViewManager != null) {
            statusViewManager.showLoading();
        }
    }

    @Override
    public void showSuccess() {
        if (statusViewManager != null) {
            statusViewManager.showSuccess();
        }
    }

    @Override
    public void showEmpty() {
        if (statusViewManager != null) {
            statusViewManager.showEmpty("");
        }
    }

    @Override
    public void showError(String hint) {
        if (statusViewManager != null) {
            statusViewManager.showError(hint);
        }
    }


    protected void onRetry() {
        Timber.tag(TAG).i("onRetry");
    }

    /**
     * 返回顶部标题视图的布局ID
     *
     * @return
     */
    protected int initTitleView() {
        return R.layout.public_include_title;
    }

    /**
     * 返回内容视图ID，内容视图指的是顶部标题以下的部分
     *
     * @return
     */
    protected abstract int initContentView();


    /**
     * 包装状态View
     * @param view
     * @return
     */
    private View wrapperStatusView(View view) {
        statusViewManager = new StatusViewManager(view, () -> onRetry());
        return statusViewManager.wrapperView();
    }
}
