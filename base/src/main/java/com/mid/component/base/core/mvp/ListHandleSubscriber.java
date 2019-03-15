package com.mid.component.base.core.mvp;



import com.mid.component.base.core.entity.IListResult;
import com.mid.component.base.core.ui.ListOwner;
import com.mid.component.base.utils.Utils;

import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/15
 *     desc    : 当数据返回类型是List时，该观察者作好了公共处理
 *     version : 0.1.0
 * </pre>
 */

public class ListHandleSubscriber<T> extends ErrorHandleSubscriber<IListResult<T>> {

    private ListOwner mListOwner;

    public ListHandleSubscriber(RxErrorHandler rxErrorHandler, ListOwner listOwner) {
        super(rxErrorHandler);
        this.mListOwner = listOwner;
    }


    @Override
    public void onNext(IListResult<T> listResult) {
        if (!listResult.isSuccess()) {
            mListOwner.showError(listResult.getMsg());
            return;
        }
        List<T> data = listResult.getData();
        if (data.size() == 0) {
            mListOwner.showEmpty();
        } else {
            mListOwner.showSuccess();
            mListOwner.setNewData(data);
        }
    }

    @Override
    public void onError(Throwable t) {
        mListOwner.showError(Utils.converException(t));
    }
}
