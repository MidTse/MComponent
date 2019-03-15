package com.mid.component.base.core.ui;

import android.content.Context;

import java.util.List;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/12
 *     desc    : 对包含有List数据的页面行为处理（普通加载、下拉刷新、上拉加载更多）
 *     version : 1.0
 * </pre>
 */
public interface ListOwner extends LoadOwner {

    LoadType getLoadType();

    Context getContext();

    void setNewData(List data);

    void addData(List data);

    void loadMoreComplete();

    void loadMoreEnd();

    void loadMoreFail();
}
