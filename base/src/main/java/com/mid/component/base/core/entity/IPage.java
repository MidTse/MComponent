package com.mid.component.base.core.entity;

import java.util.List;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/14
 *     desc    : 分页操作数据所需的功能
 *     version : 0.1.0
 * </pre>
 */
public interface IPage<T> {

    int getPageCount();

    int getPageNo();

    boolean hasNextPage();

    List<T> getPageData();

}
