package com.mid.component.base.core.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.mvp.IView;
import com.mid.component.base.utils.ShowUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 1.提供是否懒加载功能 2.继承该类需要将原先initData()方法替换成setupData()
 *     version : 0.1.0
 * </pre>
 */

public abstract class BaseActionFragment<P extends IPresenter> extends BaseFragment<P> implements IView, ICommit<FragmentEvent> {

    //判断控件是否加载完毕
    private boolean isCreateView = false;
    //判断是否已加载过数据
    protected boolean isLoadData = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //标记当前视图已经创建
        isCreateView = true;

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //视图初始化操作
        setupView();

        //懒加载处理
        if (enableLazyLoad()) {
            if (getUserVisibleHint() && isCreateView && !isLoadData()) {
                setupData();
                setLoadData(true);
            }
        } else {
            setupData();
            setLoadData(true);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (enableLazyLoad()) {
            if (isVisibleToUser && isCreateView && !isLoadData()) {
                setupData();
                setLoadData(true);
            }
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
        ShowUtils.showMessage(getContext(), message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        startActivity(intent);
    }

    @Override
    public void killMyself() {
        getActivity().finish();
    }

    @Override
    public void setData(@Nullable Object data) {
        Timber.tag(TAG).i("setData");
    }


    @Override
    public void showCommiting() {
        ShowUtils.showLoadingDialog(getContext());
    }

    @Override
    public void hideCommiting() {
        ShowUtils.dismissLoadingDialog();
    }


    /**
     * 视图初始化操作
     */
    protected void setupView() {
        Timber.i("setupView");
    }

    /**
     * 懒加载操作，一般存放业务处理逻辑、比如网络请求
     */
    public abstract void setupData();

    /**
     * 是否开启懒加载功能
     *
     * @return
     */
    public boolean enableLazyLoad() {
        return false;
    }

    /**
     * 数据是否已经加载过
     *
     * @param loadData
     */
    public void setLoadData(boolean loadData) {
        this.isLoadData = loadData;
    }

    public boolean isLoadData() {
        return isLoadData;
    }

    /**
     * View是否已经创建
     *
     * @return
     */
    public boolean isCreateView() {
        return isCreateView;
    }
}
