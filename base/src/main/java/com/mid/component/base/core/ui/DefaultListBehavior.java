package com.mid.component.base.core.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.annotation.UiStatus;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 默认的列表加载器
 *     version : 0.1.0
 * </pre>
 */

public class DefaultListBehavior implements IListBehavior {

    private SwipeRefreshLayout mRefreshLayout;
    private BaseQuickAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private UiStatusController mUiStatusController;
    private Context mContext;
    private boolean enableRefresh;
    private boolean enableLoadMore;
    private LoadType mLoadType = LoadType.NORMAL;


    private DefaultListBehavior(Builder builder) {
        mRefreshLayout = builder.mRefreshLayout;
        mAdapter = builder.mAdapter;
        mRecyclerView = builder.mRecyclerView;
        mUiStatusController = builder.mUiStatusController;
        mContext = builder.mContext;
        enableRefresh = builder.enableRefresh;
        enableLoadMore = builder.enableLoadMore;

        OnRefreshLoadMoreListener refreshLoadMoreListener = builder.mOnRefreshLoadMoreListener;
        //设置下拉刷新和上拉加载, 上拉加载更多使用BaseQuickAdapter中的功能
        mRefreshLayout.setEnabled(enableRefresh);
        if (enableRefresh) {
            mRefreshLayout.setOnRefreshListener(() -> {
                if (refreshLoadMoreListener != null) {
                    setLoadType(LoadType.REFRESH);
                    refreshLoadMoreListener.onRefresh();
                }
            });
        }
        if (enableLoadMore) {
            mAdapter.setOnLoadMoreListener(() -> {
                if (refreshLoadMoreListener != null) {
                    setLoadType(LoadType.LOADMORE);
                    refreshLoadMoreListener.onLoadMore();
                }
            }, mRecyclerView);
        }

    }


    public static Builder newBuilder() {
        return new Builder();
    }


    @Override
    public void showLoading() {
        switch (getLoadType()) {
            case NORMAL:
                mUiStatusController.changeUiStatus(UiStatus.LOADING);
                break;

            default:
                break;
        }
    }

    @Override
    public void hideLoading() {
        switch (getLoadType()) {
            case REFRESH:
                if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                break;

            default:
                break;
        }
        setLoadType(LoadType.NORMAL);
    }

    @Override
    public void setLoadType(LoadType loadType) {
        mLoadType = loadType;
    }

    @Override
    public LoadType getLoadType() {
        return mLoadType;
    }

    @Override
    public void loadMoreComplete() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void loadMoreEnd() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void loadMoreFail() {
        mAdapter.loadMoreFail();
    }


    public static final class Builder {
        private SwipeRefreshLayout mRefreshLayout;
        private BaseQuickAdapter mAdapter;
        private RecyclerView mRecyclerView;
        private UiStatusController mUiStatusController;
        private Context mContext;
        private boolean enableRefresh;
        private boolean enableLoadMore;
        private OnRefreshLoadMoreListener mOnRefreshLoadMoreListener;

        private Builder() {
        }

        public Builder withRefreshLayout(SwipeRefreshLayout val) {
            mRefreshLayout = val;
            return this;
        }

        public Builder withAdapter(BaseQuickAdapter val) {
            mAdapter = val;
            return this;
        }

        public Builder withRecyclerView(RecyclerView val) {
            mRecyclerView = val;
            return this;
        }

        public Builder withUiStatusController(UiStatusController val) {
            mUiStatusController = val;
            return this;
        }

        public Builder withContext(Context val) {
            mContext = val;
            return this;
        }

        public Builder withOnRefreshLoadMoreListener(OnRefreshLoadMoreListener val) {
            mOnRefreshLoadMoreListener = val;
            return this;
        }

        public Builder enableRefresh(boolean val) {
            enableRefresh = val;
            return this;
        }

        public Builder enableLoadMore(boolean val) {
            enableLoadMore = val;
            return this;
        }

        public DefaultListBehavior build() {
            return new DefaultListBehavior(this);
        }
    }

}
