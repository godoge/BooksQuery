package com.example.administrator.books;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class AddBtnAnim {


    public static void expand(Context context, FloatingActionButton btn1, FloatingActionButton btn2, FloatingActionButton btn3) {

        btn1.setVisibility(View.VISIBLE);
        btn1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right_qrcode));
        btn2.setVisibility(View.VISIBLE);
        btn2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right_net));
        btn3.setVisibility(View.VISIBLE);
        btn3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right_manual));

    }

    public static void shrink(Context context, final FloatingActionButton btn1, final FloatingActionButton btn2, final FloatingActionButton btn3) {

        btn1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right_qrcode));
        btn1.setVisibility(View.GONE);
        btn2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right_net));
        btn2.setVisibility(View.GONE);
        btn3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right_manual));
        btn3.setVisibility(View.GONE);

    }


}
