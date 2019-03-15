package com.mid.component.base.core.ui;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 关于加载List数据行为操作（下拉刷新，上拉加载更多）
 *               控制下拉刷新上拉加载更多的操作，并标记当前的加载状态，还有对显示/隐藏加载状态有作额外处理
 *  *            加入showLoading()、hideLoading()方法是因为刷新样式需要兼容
 *     version : 0.1.0
 * </pre>
 */

public interface IListBehavior {

    void showLoading();

    void hideLoading();

    void setLoadType(LoadType loadType);

    LoadType getLoadType();

    void loadMoreComplete();

    void loadMoreEnd();

    void loadMoreFail();
}
