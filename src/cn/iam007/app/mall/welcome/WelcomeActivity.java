package cn.iam007.app.mall.welcome;

import java.io.File;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.iam007.app.common.cache.CacheConfiguration;
import cn.iam007.app.common.config.AppConstants;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.common.utils.CommonHttpUtils.DownloadCallback;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.TestMainActivity;
import cn.iam007.app.mall.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView container = (ImageView) findViewById(R.id.container);

        mCheckUpdateStart = System.currentTimeMillis();
        //        PlatformUtils.checkUpdate(this, new RequestCallBack<String>() {
        //
        //            @Override
        //            public void onSuccess(ResponseInfo<String> arg0) {
        //                parseCheckUpdate(arg0.result);
        //            }
        //
        //            @Override
        //            public void onFailure(HttpException arg0, String arg1) {
        //                parseCheckUpdate(arg1);
        //            }
        //        });

        final String startImagePath = CacheConfiguration.getStartImagePath();

        if (!TextUtils.isEmpty(startImagePath)) {
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    String url = AppConstants.HOST
                            + "iam007/static/start.image";
                    CommonHttpUtils.download(url,
                            startImagePath,
                            new DownloadCallback() {

                                @Override
                                public void onFinish(boolean state, File file) {
                                    LogUtil.d("down start image:" + state + " "
                                            + file);
                                }
                            });
                }
            }, 1500);

            try {
                File startImageFile = new File(startImagePath);
                if (startImageFile.isFile()) {
                    //                    container.setImageBitmap(BitmapFactory.decodeFile(startImagePath));
                    ImageUtils.showImageByUrl("file:///" + startImagePath,
                            container);
                }
            } catch (Exception e) {
            }

        }

        mHandler.postDelayed(mFinishRunnable, 5000);
    }

    private Handler mHandler = new Handler();
    private Runnable mFinishRunnable = new Runnable() {

        @Override
        public void run() {
            loadHomeUI();
        }
    };

    private long mCheckUpdateStart = 0;
    private String mVersion = null; // 更新的版本号
    private boolean mForceUpdate = false; // 是否强制更新
    private String mUpdateShortLog = null;
    private String mUpdateDetailLog = null;
    private String mDownUrl = null;

    private void parseCheckUpdate(String result) {
        boolean showUpdateDialog = false;
        try {
            JSONObject object = new JSONObject(result);
            if (object.optInt("state", -1) == 1) {
                mVersion = object.optString("version");
                mForceUpdate = object.optBoolean("forceUpdate");
                mUpdateShortLog = object.optString("updateShortLog");
                mUpdateDetailLog = object.optString("updateDetailLog");
                mDownUrl = object.optString("downUrl");

                showUpdateDialog = true;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "Check update error:" + e.toString());
        }
        long interval = System.currentTimeMillis() - mCheckUpdateStart;
        if (interval < 1000) {
            try {
                Thread.sleep(1000 - interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (showUpdateDialog) {
            showUpdateDialog();
        } else {
            loadHomeUI();
        }
    }

    private void showUpdateDialog() {
        // TODO: 需要完善
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(R.string.check_update_dialog_title);
        dialog.setMessage(mUpdateShortLog);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.update_now),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadHomeUI();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.update_later),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadHomeUI();
                    }
                });
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * login
     */
    public void loadHomeUI() {
        Intent intent = new Intent(this, TestMainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);

        mHandler.removeCallbacks(mFinishRunnable);

        finish();
    }

}
