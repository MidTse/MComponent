package com.mid.component.base.core.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import com.dyhdyh.widget.loading.factory.DialogFactory;
import com.mid.component.base.R;


/**
 * <pre>
 *     @author : Mid
 *     e-mail  : boyce903301689@gmail.com
 *     time    : 2018/09/17
 *     desc    : Dialog构建工厂
 *     version : 0.1.0
 * </pre>
 */
public class CommonDialogFactory implements DialogFactory {
    @Override
    public Dialog onCreateDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.public_loadingDialog);
        dialog.setContentView(R.layout.public_dialog_loading);
        return dialog;
    }

    @Override
    public void setMessage(Dialog dialog, CharSequence message) {

    }

    @Override
    public int getAnimateStyleId() {
        return R.style.public_Dialog_Alpha_Animation;
    }
}
