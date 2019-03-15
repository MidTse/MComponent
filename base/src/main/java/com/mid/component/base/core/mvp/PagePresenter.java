package com.mid.component.base.core.mvp;

import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import com.mid.component.base.core.entity.IPageResult;
import com.mid.component.base.core.ui.ListOwner;
import com.mid.component.base.utils.RxUtils;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/16
 *     desc    : 基于分页功能的Presenter，主要对page相关索引和大小进行处理，
 *     version : 0.1.0
 * </pre>
 */

public abstract class PagePresenter<M extends IModel, V extends ListOwner, T extends IPageResult> extends BasePresenter<M,V> {

    protected RxErrorHandler mErrorHandler;

    //当前页
    protected int pageNo = 1;
    //分页大小
    private int pageSize;


    public PagePresenter(M model, V rootView, RxErrorHandler rxErrorHandler) {
        super(model, rootView);
        pageSize = providePageSize();
        mErrorHandler = rxErrorHandler;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
    }

    /**
     * 获取页面数据
     * @param params
     */
    public void getPageData(HashMap<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        switch (mRootView.getLoadType()) {
            case LOADMORE:
                pageNo++;
                break;

            default:
                pageNo = 1;
                break;
        }
        Observable<T> observable = providePageObservable(configPageMap(params));
        setupObservable(observable);
    }
    public void getPageData() {
        getPageData(null);
    }


    /**
     * 配置Page中map参数中key-value，兼容其它接口配置的key-value（非本项目）
     * @param params
     * @return
     */
    protected abstract HashMap<String, String> configPageMap(HashMap<String, String> params);

    /**
     * 获取PageObservable
     * @param params
     * @return
     */
    protected abstract Observable<T> providePageObservable(HashMap<String, String> params);


    /**
     * Observable对象处理过程，兼容其它接口的处理过程（非本项目）
     * @param pageObservable
     */
    protected void setupObservable(Observable<T> pageObservable) {
        pageObservable
                .compose(RxUtils.applySchedulers(mRootView))
                .subscribe((Observer<? super T>) new PageHandleSubscriber<T>(mErrorHandler, mRootView));
    }

    /**
     * 获取当前page是第几页
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 获取当前分页大小
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 配置分页数据大小
     * @return 分页数据大小
     */
    protected int providePageSize() {
        return 10;
    }
}
