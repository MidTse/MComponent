package com.mid.component.base.core.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengchen.uistatus.controller.IUiStatusController;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.utils.ArmsUtils;
import com.mid.component.base.R;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 基础的RecyclerView列表控件加载处理
 *     version : 0.1.0
 * </pre>
 */

public abstract class BaseListActivity<P extends IPresenter, A extends BaseQuickAdapter> extends BaseLoadActivity<P> implements ListOwner, OnRefreshLoadMoreListener {

    protected RecyclerView mRecyclerView;

    //使用Dagger2预先注入所需对象，可在编译时检测该对象是否注入成功，不会在运行时报 NullPointerException
    @Inject
    protected A mAdapter;
    @Inject protected RecyclerView.LayoutManager mLayoutManager;

    //列表操作数据的行为控制（下拉刷新，上拉加载更多）
    private IListBehavior mListBehavior;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mRecyclerView = findViewById(R.id.public_recyclerView);
        if (mRecyclerView == null) {
            throw new NullPointerException("Unable to find viewID is public_recyclerView");
        }
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        //配置recyclerAdapter 和 recyclerView
        configAdapter(mAdapter);
        configRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        //获取IListBehavior
        mListBehavior = provideListBehavior();
        if (mListBehavior == null) {
            throw new NullPointerException("ListBehavior can't null");
        }

        //初始化一些操作，并获取列表数据
        onSetupListDataBefore();
        setupListData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView = null;
        mAdapter = null;
        mLayoutManager = null;
        mListBehavior = null;
    }

    @Override
    public void showLoading() {
        mListBehavior.showLoading();
    }

    @Override
    public void hideLoading() {
        mListBehavior.hideLoading();
    }

    @Override
    public LoadType getLoadType() {
        return mListBehavior.getLoadType();
    }

    @Override
    public void loadMoreComplete() {
        mListBehavior.loadMoreComplete();
    }

    @Override
    public void loadMoreFail() {
        mListBehavior.loadMoreFail();
    }

    @Override
    public void loadMoreEnd() {
        mListBehavior.loadMoreEnd();
    }

    @Override
    public void setNewData(List data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void addData(List data) {
        mAdapter.addData(data);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onUiStatusRetry(Object o, IUiStatusController iUiStatusController, View view) {
        super.onUiStatusRetry(o, iUiStatusController, view);
        setupListData();
    }

    @Override
    public void onRefresh() {
        setupListData();
    }

    @Override
    public void onLoadMore() {
        setupListData();
    }


    /**
     * 是否开启下拉刷新，默认开启
     * @return
     */
    protected boolean enableRefresh() {
        return true;
    }

    /**
     * 是否开启加载更多，默认开启
     * @return
     */
    protected boolean enableLoadMore() {
        return true;
    }


    /**
     * 配置RecyclerView属性
     * @param recyclerView
     */
    protected void configRecyclerView(RecyclerView recyclerView) {
        ArmsUtils.configRecyclerView(recyclerView, mLayoutManager);
        //recyclerView滑动优化
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //加载
                        Glide.with(BaseListActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //暂停加载
                        Glide.with(BaseListActivity.this).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //加载
                        Glide.with(BaseListActivity.this).resumeRequests();
                        break;
                    default:
                        break;

                }
            }
        });
        Timber.tag(TAG).i("configRecyclerView");
    }

    /**
     * 配置RecyclerView.Adapter属性
     * @param quickAdapter
     */
    protected void configAdapter(A quickAdapter) {
        Timber.tag(TAG).i("configAdapter");
    }


    /**
     * 提供List数据操作的行为操作者
     * @return
     */
    protected abstract IListBehavior provideListBehavior();


    /**
     * 在setupListData执行之前
     */
    protected void onSetupListDataBefore() {
        Timber.tag(TAG).i("onSetupListDataBefore");
    }


    /**
     * 获取列表数据
     */
    protected abstract void setupListData();
}
