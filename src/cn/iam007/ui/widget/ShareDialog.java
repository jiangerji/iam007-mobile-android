package cn.iam007.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import cn.iam007.R;
import cn.iam007.common.utils.PlatformUtils;

public class ShareDialog extends Dialog {

    private View mCancelBtn = null;

    public ShareDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_layout);

        setCanceledOnTouchOutside(false);
        mCancelBtn = findViewById(R.id.share_to_cancle);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();

        //        WindowManager windowManager = getOwnerActivity().getWindowManager();
        //        Display display = windowManager.getDefaultDisplay();
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = PlatformUtils.getScreenWidth(getContext()); //设置宽度
        getWindow().setAttributes(lp);
        getWindow().setGravity(Gravity.BOTTOM);
    }

}
