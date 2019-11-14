package com.mid.component.base.core.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
public abstract class BaseListFragment<P extends IPresenter, A extends BaseQuickAdapter> extends BaseLoadFragment<P> implements ListOwner, OnRefreshLoadMoreListener {

    protected RecyclerView mRecyclerView;

    //使用Dagger2预先注入所需对象，可在编译时检测该对象是否注入成功，不会在运行时报 NullPointerException
    @Inject protected A mAdapter;
    @Inject protected RecyclerView.LayoutManager mLayoutManager;

    private IListBehavior mListBehavior;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.public_recyclerView);
        if (mRecyclerView == null) {
            throw new NullPointerException("Unable to find viewID is public_recyclerView");
        }

        //配置recyclerAdapter 和 recyclerView
        configAdapter(mAdapter);
        configRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void setupData() {
        //初始化PageLister
        mListBehavior = provideListBehavior();
        if (mListBehavior == null) {
            throw new NullPointerException("PageLister can't null");
        }

        //初始化一些操作，并获取列表数据
        onSetupListDataBefore();
        setupListData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView = null;
        mAdapter = null;
        mLayoutManager = null;
        mListBehavior = null;
    }

    @Override
    public void showLoading() {
        if (mListBehavior != null) {
            mListBehavior.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mListBehavior != null) {
            mListBehavior.hideLoading();
        }
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
    public void onRefresh() {
        setupListData();
    }

    @Override
    public void onLoadMore() {
        setupListData();
    }


    @Override
    protected void onRetry() {
        super.onRetry();
        setupListData();
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
                        Glide.with(BaseListFragment.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //暂停加载
                        Glide.with(BaseListFragment.this).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //加载
                        Glide.with(BaseListFragment.this).resumeRequests();
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
     * 提供实现IListBehavior的对象
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
