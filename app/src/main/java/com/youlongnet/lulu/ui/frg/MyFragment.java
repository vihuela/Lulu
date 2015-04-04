package com.youlongnet.lulu.ui.frg;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.kyleduo.switchbutton.switchbutton.SwitchButton;
import com.software.shell.fab.ActionButton;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseFragment;
import com.youlongnet.lulu.ui.widget.ProgressDrawble;

import butterknife.InjectView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

public class MyFragment extends BaseFragment {

    @InjectView(R.id.switchbutton)
    SwitchButton mSwitchBtn;
    @InjectView(R.id.dialog)
    Button mBtnDialog;
    @InjectView(R.id.fabbtn)
    ActionButton fab;

    @Override
    public int setLayout() {
        return R.layout.frg_my;
    }

    @Override
    public void initData(View view) {
        /*toogle开关*/
        mSwitchBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String res = isChecked ? "选中" : "未选中";
                Toast.makeText(mContext, res, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initWidget() {

    }

    @OnClick({R.id.dialog, R.id.loadingProgess, R.id.fab})
    public void dialog(View v) {
        switch (v.getId()) {
            case R.id.dialog:
                showDialog();
                break;
            case R.id.loadingProgess:
                showLoadProgress();
                break;
            case R.id.fab:
                showFab();
                break;

        }
    }

    /*悬浮按钮*/
    private void showFab() {
        /*按钮动画
        fab.setShowAnimation(ActionButton.Animations.FADE_IN);
		fab.setHideAnimation(ActionButton.Animations.FADE_OUT);
		// 左右移动动画
		fab.setShowAnimation(ActionButton.Animations.JUMP_FROM_RIGHT);
		fab.setHideAnimation(ActionButton.Animations.JUMP_TO_RIGHT);*/
        // 上下移动动画
        fab.setShowAnimation(ActionButton.Animations.JUMP_FROM_DOWN);
        fab.setHideAnimation(ActionButton.Animations.JUMP_TO_DOWN);
        fab.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.hide();
            }
        }, 2000);

    }

    /*菊花进度圈圈*/
    private void showLoadProgress() {
        final ProgressDrawble myinstance = ProgressDrawble.getInstance(mContext);
        myinstance.setTitle("加载中");
        myinstance.setOnDialogDismiss(new ProgressDrawble.OnDialogDismiss() {
            @Override
            public void onDismiss() {
                Toast.makeText(mContext, "dialog已经关闭", Toast.LENGTH_SHORT).show();
            }
        });
        myinstance.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myinstance.dismiss();
            }
        }, 5000);

    }

    /*对话框*/
    private void showDialog() {
        final MaterialDialog materialDialog = new MaterialDialog(mContext);
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.setMessage("这里是用来展示的文字信息的东西").setTitle("标题").setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        }).setNegativeButton("取消", null);
        materialDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Toast.makeText(mContext, "运行到关闭了", Toast.LENGTH_SHORT).show();
            }
        });
        materialDialog.show();
    }
}
