package com.mid.component.base.core.entity;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/14
 *     desc    : 对后端返回数据结果的统一处理信息，主要是兼容不同数据结构，为数据结构作统一处理
 *     version : 0.1.0
 * </pre>
 */
public interface IResult {

    boolean isSuccess();

    String getMsg();
}
