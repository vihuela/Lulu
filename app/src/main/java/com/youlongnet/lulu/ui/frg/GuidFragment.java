package com.youlongnet.lulu.ui.frg;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.youlongnet.lulu.R;
import com.youlongnet.lulu.ui.base.BaseFragment;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

public class GuidFragment extends BaseFragment {

    @InjectView(R.id.upload)
    Button upload;
    @InjectView(R.id.pb)
    ProgressBar pb;

    @Override
    public int setLayout() {
        return R.layout.frg_guid;
    }

    @Override
    public void initData(View view) {

    }

    @OnClick(R.id.upload)
    public void onClick(View v) {
        Ion.with(mContext)
                .load("http://b.hiphotos.baidu.com/image/w%3D230/sign=f84406c4554e9258a63481edac83d1d1/fd039245d688d43f76f37f527e1ed21b0ef43b3c.jpg")
                .write(new File(Environment.getExternalStorageDirectory(), "temp.jpg"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (file != null) {
                            uploadTest(file);
                        }
                    }
                });

    }

    private void uploadTest(File file) {
        Ion.with(mContext)
                .load("http://115.47.55.60/Upload.aspx")
                .uploadProgressBar(pb)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        upload.setText("" + downloaded + " / " + total);
                    }
                })
                .setMultipartFile("image", file)
                .asString()
                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String arg1) {
                        if (e == null) {
                            upload.setText("上传完成");
                        }
                    }
                });
    }
}
