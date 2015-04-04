package com.youlongnet.lulu.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.youlongnet.lulu.R;


public class ProgressDrawble extends Dialog {

    static ProgressDrawble instance;
    private View view;
    private TextView tvMessage;
    private ImageView ivProgressSpinner;
    private AnimationDrawable adProgressSpinner;
    private Context context;

    private OnDialogDismiss onDialogDismiss;

    public ProgressDrawble(Context context) {
        super(context, R.style.DialogTheme);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);
        this.context = context;

        view = getLayoutInflater().inflate(R.layout.pd_dialog_progress, null);
        tvMessage = (TextView) view.findViewById(R.id.textview_message);
        ivProgressSpinner = (ImageView) view.findViewById(R.id.imageview_progress_spinner);

        ivProgressSpinner.setImageResource(R.anim.pd_round_spinner_fade);
        adProgressSpinner = (AnimationDrawable) ivProgressSpinner.getDrawable();

        this.setContentView(view);
    }

    public static ProgressDrawble getInstance(Context context) {
        if (instance == null) {
            instance = new ProgressDrawble(context);
        }
        return instance;
    }

    public OnDialogDismiss getOnDialogDismiss() {
        return onDialogDismiss;
    }

    public void setOnDialogDismiss(OnDialogDismiss onDialogDismiss) {
        this.onDialogDismiss = onDialogDismiss;
    }

    public void setMessage(String message) {
        tvMessage.setText(message);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onDialogDismiss != null) {
            onDialogDismiss.onDismiss();
        }
    }

    @Override
    public void show() {
        if (!((Activity) context).isFinishing()) {
            super.show();
        } else {
            instance = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ivProgressSpinner.post(new Runnable() {
            @Override
            public void run() {
                adProgressSpinner.start();
            }
        });
    }

    public interface OnDialogDismiss {
        public void onDismiss();
    }

}
