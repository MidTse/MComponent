package com.mid.component.demo;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.di.component.AppComponent;
import com.mid.component.base.core.ui.BaseLoadActivity;

public class MainActivity extends BaseLoadActivity {



    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }


    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showError("test");
            }
        }, 5000);
    }
}
