package cn.iam007.app.mall.base;

import org.apache.log4j.Logger;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import cn.iam007.app.common.config.AppConstants;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.widget.WaitingProgressDialog;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.baidu.mobstat.StatService;

public abstract class BaseActivity extends SherlockFragmentActivity {
    protected final static String TAG = "activity";

    protected Dialog mWaitingDialog = null;

    private GestureDetector mGestureDetector;

    /**
     * 显示等待的dialog
     */
    protected void showWaitingDialog() {
        final Context context = this;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mWaitingDialog = new WaitingProgressDialog(context);
                mWaitingDialog.setCanceledOnTouchOutside(false);
                mWaitingDialog.show();
            }
        });
    }

    /**
     * 销毁等待的dialog
     */
    protected void dismissWaitingDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mWaitingDialog != null) {
                        mWaitingDialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    /**
     * 不显示搜索view
     */
    protected final static int FLAG_SEARCH_VIEW = 0x01;
    protected final static int FLAG_DISABLE_HOME_AS_UP = 0x02;

    protected int getFlag() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if ((getFlag() & FLAG_DISABLE_HOME_AS_UP) != FLAG_DISABLE_HOME_AS_UP) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        //GestureDetector 
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                            float velocityX, float velocityY) {
                        if (mGestureDetectorState) {
                            float absVelocityX = Math.abs(velocityX);
                            float absVelocityY = Math.abs(velocityY);
                            if (absVelocityX >= absVelocityY) {
                                // 横向滑动
                                if (absVelocityX > 150) {
                                    if (velocityX > 0) {
                                        onFlingRight();
                                    } else {
                                        onFlingLeft();
                                    }
                                }
                            } else {
                                // 竖形滑动
                                if (absVelocityY > 150) {
                                    if (velocityY > 0) {
                                        onFlingBottom();
                                    } else {
                                        onFlingTop();
                                    }
                                }
                            }
                        }

                        return false;
                    }
                });
    }

    /**
     * 手势向上滑动
     */
    protected void onFlingTop() {
    }

    /**
     * 手势向下滑动
     */
    protected void onFlingBottom() {
    }

    /**
     * 手势向右滑动
     */
    protected void onFlingLeft() {
    }

    /**
     * 手势向右滑动
     */
    protected void onFlingRight() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        StatService.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if ((getFlag() & FLAG_SEARCH_VIEW) == FLAG_SEARCH_VIEW) {
            MenuItem menuItem = menu.add("delete");
            menuItem.setIcon(R.drawable.activity_action_bar_search_btn_bg)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                            | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //                    Intent intent = new Intent();
                    //                    intent.setClass(BaseActivity.this,
                    //                            SearchActivity.class);
                    //                    startActivity(intent);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;

        default:
            break;
        }
        return true;
    }

    private Logger mLogger = LogUtil.getLogger();

    public final void debug(String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            mLogger.debug(msg);
        }
    }

    public final void info(String msg) {
        if (AppConstants.LOGGER_ENABLE) {
            mLogger.info(msg);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        //        boolean consume = super.dispatchTouchEvent(ev);
        //        if (consume) {
        //            LogUtil.d(TAG, "dispatchTouchEvent:" + ev.getAction() + " "
        //                    + consume);
        //        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean mGestureDetectorState = true;

    /**
     * 将手势失效
     */
    public void disableGestureDetector() {
        mGestureDetectorState = false;
    }

    /**
     * 开启手势识别
     */
    public void enableGestureDetector() {
        mGestureDetectorState = true;
    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        mGestureDetector.onTouchEvent(event);
    //        return super.onTouchEvent(event);
    //    }

    //    public void next(View view) {
    //        showNext();
    //    }
    //
    //    public void pre(View view) {
    //        showPre();
    //    }
    //
    //    public void showNext() {
    //
    //    }
    //
    //    public void showPre() {
    //
    //    }
}
