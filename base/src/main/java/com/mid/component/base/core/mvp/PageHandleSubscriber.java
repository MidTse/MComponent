package com.mid.component.base.core.mvp;


import com.mid.component.base.core.entity.IPage;
import com.mid.component.base.core.entity.IPageResult;
import com.mid.component.base.core.ui.ListOwner;
import com.mid.component.base.utils.Utils;

import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/15
 *     desc    : 当返回数据是分页类型时，使用此观察者，内部已经处理好公共逻辑
 *     version : 0.1.0
 * </pre>
 */

public class PageHandleSubscriber<T> extends ErrorHandleSubscriber<IPageResult<T>> {

    private ListOwner mListOwner;

    public PageHandleSubscriber(RxErrorHandler rxErrorHandler, ListOwner listOwner) {
        super(rxErrorHandler);
        this.mListOwner = listOwner;
    }

    @Override
    public void onNext(IPageResult<T> pageResult) {
        if (!pageResult.isSuccess()) {
            switch (mListOwner.getLoadType()) {
                case NORMAL:
                case REFRESH:
                    mListOwner.showError(pageResult.getMsg());
                    return;

                case LOADMORE:
                    mListOwner.loadMoreFail();
                    return;
                default:
                    break;
            }
        } else {
            IPage<T> page = pageResult.getData();
            List<T> data = page.getPageData();
            switch (mListOwner.getLoadType()) {
                //兼容客户端被动接受数据时，可以刷新当前页面,所以刷新状态和普通状态一样
                case NORMAL:
                case REFRESH:
                    if (data.size() == 0) {
                        mListOwner.showEmpty();
                    } else {
                        mListOwner.showSuccess();
                        mListOwner.setNewData(data);
                    }
                    break;
                //加载更多
                case LOADMORE:
                    mListOwner.addData(data);
                    mListOwner.loadMoreComplete();
                    break;

                default:
                    break;
            }
            if (!page.hasNextPage()) {
                switch (mListOwner.getLoadType()) {
                    case NORMAL:
                    case REFRESH:
                        mListOwner.showSuccess();
                        break;

                    case LOADMORE:
                        mListOwner.loadMoreEnd();
                        break;
                }
            }

            //加载成功的监听
            onSuccess(data);
        }

    }

    @Override
    public void onError(Throwable t) {
        mListOwner.showError(Utils.converException(t));
    }

    /**
     * 加载成功的监听
     * @param data
     */
    protected void onSuccess(List<T> data) {
        Timber.tag("PageHandleSubscriber").i("onSuccess");
    }
}
