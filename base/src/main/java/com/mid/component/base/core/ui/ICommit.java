package com.mid.component.base.core.ui;

import com.jess.arms.integration.lifecycle.Lifecycleable;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/17
 *     desc    : 用于提交数据时的等待操作,一般在页面既有数据加载和数据提交的场景下使用
 *     version : 0.1.0
 * </pre>
 */

public interface ICommit<E> extends Lifecycleable<E> {

    void showCommiting();

    void hideCommiting();
}
