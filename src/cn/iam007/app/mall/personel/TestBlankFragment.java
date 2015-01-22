package cn.iam007.app.mall.personel;

import java.util.Random;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import cn.iam007.app.mall.base.BaseFragment;

public class TestBlankFragment extends BaseFragment {

    private int randomColor() {
        Random random = new Random(System.currentTimeMillis() + this.hashCode());

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return Color.argb(255, r, g, b);
    }

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container) {
        FrameLayout blankLayout = new FrameLayout(getActivity());
        blankLayout.setBackgroundColor(randomColor());
        blankLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        setInitViewFinish();
        return blankLayout;
    }
}
