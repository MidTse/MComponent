package com.mid.component.base.core.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.annotation.UiStatus;
import com.fengchen.uistatus.controller.IUiStatusController;
import com.fengchen.uistatus.listener.OnRetryListener;
import com.jess.arms.mvp.IPresenter;
import com.mid.component.base.R;

import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 基础的加载页面，主要提供了根据callback类型来显示相应的页面状态。通过LoadSir来提供支持，具体参考  https://github.com/KingJA/LoadSir
 *     version : 0.1.0
 * </pre>
 */
public abstract class BaseLoadFragment<P extends IPresenter> extends BaseActionFragment<P> implements LoadOwner, OnRetryListener {

    private View contentView;
    protected UiStatusController mUiStatusController = UiStatusController.get();

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.public_include_view_root, container, false);
        ViewGroup rootView = view.findViewById(R.id.public_rootView);
        if (rootView == null) {
            throw new RuntimeException("Unable to find viewID is rootView");
        }
        //由于LoadSir加载需要保持标题栏，需要通过以下方式配置
        contentView = View.inflate(getContext(), initContentView(), null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (initTitleView() != 0) {
            inflater.inflate(initTitleView(), rootView, true);
            rootView.addView(contentView, layoutParams);
        } else {
            rootView.addView(contentView, layoutParams);
        }
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //配置UiStatusController相关属性
        configUiStatusController(mUiStatusController);
        mUiStatusController.setListener(UiStatus.NETWORK_ERROR, this);
        mUiStatusController.setListener(UiStatus.LOAD_ERROR, this);
        mUiStatusController.setListener(UiStatus.EMPTY, this);
        super.initData(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiStatusController = null;
        this.contentView = null;
    }

    @Override
    public void showLoading() {
        super.showLoading();
        if (mUiStatusController != null) {
            mUiStatusController.changeUiStatus(UiStatus.LOADING);
        }
    }


    @Override
    public void showSuccess() {
        if (mUiStatusController != null) {
            mUiStatusController.changeUiStatusIgnore(UiStatus.CONTENT);
        }
    }

    @Override
    public void showEmpty() {
        if (mUiStatusController != null) {
            mUiStatusController.changeUiStatus(UiStatus.EMPTY);
        }
    }

    @Override
    public void showError(String hint) {
        if (mUiStatusController != null) {
            mUiStatusController.changeUiStatus(UiStatus.LOAD_ERROR);
        }
        showMessage(hint);
    }


    @Override
    public void onUiStatusRetry(Object o, IUiStatusController iUiStatusController, View view) {
        Timber.tag(TAG).i("onUiStatusRetry");
    }

    /**
     * 获取标题的资源ID
     * @return 标题的资源ID
     */
    protected int initTitleView() {
        return 0;
    }

    /**
     * 返回内容视图ID，内容视图指的是顶部标题以下的部分
     * @return
     */
    protected abstract int initContentView();


    /**
     * 配置UiStatusController
     * @param controller
     */
    protected void configUiStatusController(UiStatusController controller) {
        controller.bind(contentView);
    }


}
