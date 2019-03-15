package com.mid.component.base.core.mvp;



import com.mid.component.base.core.entity.IResult;
import com.mid.component.base.core.ui.LoadOwner;
import com.mid.component.base.utils.Utils;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/14
 *     desc    : 普通的错误处理对象Subscriber，继承ErrorHandleSubscriber
 *               主要将错误信息页面显示出来，不是Toast方式
 *     version : 0.1.0
 * </pre>
 */
public abstract class BaseHandleSubscriber<T extends IResult> extends ErrorHandleSubscriber<T> {

    private LoadOwner mLoadOwner;

    public BaseHandleSubscriber(RxErrorHandler rxErrorHandler, LoadOwner loadOwner) {
        super(rxErrorHandler);
        this.mLoadOwner = loadOwner;
    }

    @Override
    public void onNext(T t) {
        //根据返回的Result是否成功来作相应操作
        if (!t.isSuccess()) {
            mLoadOwner.showError(t.getMsg());
        } else {
            mLoadOwner.showSuccess();
            onSuccess(t);
        }

    }

    /**
     * 加载成功的回调
     * @param t
     */
    protected abstract void onSuccess(T t);


    @Override
    public void onError(Throwable t) {
        mLoadOwner.showError(Utils.converException(t));
    }
}
