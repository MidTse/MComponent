package com.mid.component.base.core.ui;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.InflateException;

import androidx.fragment.app.FragmentManager;

import com.dyhdyh.widget.loading.dialog.LoadingDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.jess.arms.base.delegate.IActivity;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.CacheType;
import com.jess.arms.integration.lifecycle.ActivityLifecycleable;
import com.jess.arms.mvp.IPresenter;
import com.jess.arms.mvp.IView;
import com.jess.arms.utils.ArmsUtils;
import com.mid.component.base.core.ui.dialog.CommonDialogFactory;
import com.mid.component.base.utils.ShowUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2019/03/15
 *     desc    : 1.提供沉浸式状态栏功能 2.增加通用的加载对话框
 *     version : 0.1.0
 * </pre>
 */
public abstract class BaseActionActivity<P extends IPresenter> extends AppCompatActivity implements IView, ICommit<ActivityEvent>, ActivityLifecycleable, IActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;

    private Dialog submitDialog;

    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null

    protected ImmersionBar mImmersionBar;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            if (e instanceof InflateException) throw e;
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
        submitDialog = null;
    }


    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 {@code true} (默认为使用 {@code true}), Arms 会自动注册 EventBus
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link com.jess.arms.base.BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }



    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (enableImmersionBar()) {
            mImmersionBar = ImmersionBar.with(this);
            configImmersionBar();
        }

        //创建提交对话框
        submitDialog = LoadingDialog.make(this, new CommonDialogFactory())
                .setCancelable(true)
                .create();

    }


    @Override
    public void showLoading() {
        Timber.tag(TAG).i("showLoading");
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).i("hideLoading");
    }

    @Override
    public void showMessage(@NonNull String message) {
        ShowUtils.showMessage(this, message);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void showCommiting() {
        if (!submitDialog.isShowing()) {
            submitDialog.show();
        }
    }

    @Override
    public void hideCommiting() {
        if (submitDialog.isShowing()) {
            submitDialog.dismiss();
        }
    }


    /**
     * 初始化系统状态栏
     */
    protected void configImmersionBar() {
        mImmersionBar.statusBarColor(android.R.color.white)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true)
                .init();
    }


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean enableImmersionBar() {
        return true;
    }
}
