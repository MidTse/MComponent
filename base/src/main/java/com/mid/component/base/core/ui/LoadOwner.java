package com.mid.component.base.core.ui;

import com.jess.arms.mvp.IView;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/12
 *     desc    : 根据callback类型来显示相应的页面状态
 *     version : 1.0
 * </pre>
 */
public interface LoadOwner extends IView {

    void showSuccess();

    void showEmpty();

    void showError(String hint);
}
