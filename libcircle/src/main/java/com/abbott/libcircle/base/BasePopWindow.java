package com.abbott.libcircle.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import com.abbott.libcircle.R;
import com.abbott.libcircle.utils.AbPreconditions;


/**
 * Created by zhangjianlin on 16-12-7.
 */

public abstract class BasePopWindow extends PopupWindow implements  PopupWindow.OnDismissListener {

    private boolean isAnimationEable = true;//

//    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    private OnDismissListener extOnDismissListener;
    private Context mContext;



    public BasePopWindow(Context context) {
        this(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BasePopWindow(Context context, int width, int height) {
        super(context);
        this.mContext = context;
        setContentView(getConView(context));
        setContentViewBackground();
        setWidth(width);

        //Navigation Bar遮挡PopupWindow的解决方法
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setHeight(height);
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        super.setOnDismissListener(this);
        setAnimationStyle(R.style.FadeInPopWin);

        update();
    }

    protected abstract View getConView(Context context);

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.extOnDismissListener = onDismissListener;
    }

    /**
     * 统一的背景
     */
    protected void setContentViewBackground(){
        getContentView().setBackgroundColor(mContext.getResources().getColor(R.color.shadow_grey));
    }

    /**
     * 是否使用动画
     *
     * @param animationEable
     */
    protected void setAnimationEable(boolean animationEable) {
        isAnimationEable = animationEable;
    }



    public void showPop(Activity activity) {
        if (null != activity) {
            showPop(activity.getWindow().getDecorView());
        }
    }

    public void showPop(View mView) {
        showAtLocation(mView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (isAnimationEable) {
            doAnomationOnlyShow();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        if (isAnimationEable) {
            doAnomationOnlyShow();
        }
    }

    @Override
    public void dismiss() {
        if (isAnimationEable) {
            dismissWithAnimation();
        } else {
            dismissAsOrg();
        }
    }

    /**
     * 最原始的消失
     */
    public void dismissAsOrg() {
        super.dismiss();
    }


    @Override
    public void onDismiss() {
        if (AbPreconditions.checkNotNullRetureBoolean(extOnDismissListener)) {
            extOnDismissListener.onDismiss();
        }
    }

    /**
     * 关闭地址选择器弹层
     */
    public void dismissWithAnimation() {
        AbPreconditions.checkArgument(getContentView() instanceof ViewGroup, "popupwindow contentview must extends ViewGroup");
            ViewGroup contentGroup = (ViewGroup) getContentView();
        AbPreconditions.checkArgument(contentGroup.getChildCount() == 1,
                "popupwindow contentview only have one child, please wrap childrends with LinearLayout or some ViewGrop!");
                TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

                trans.setDuration(100);
                trans.setInterpolator(new AccelerateInterpolator());
                trans.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismissAsOrg();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                contentGroup.getChildAt(0).startAnimation(trans);
    }

    public void doAnomationOnlyShow() {
        AbPreconditions.checkArgument(getContentView() instanceof ViewGroup, "popupwindow contentview must extends ViewGroup");
        ViewGroup contentGroup = (ViewGroup) getContentView();
        AbPreconditions.checkArgument(contentGroup.getChildCount() == 1,
                "popupwindow contentview only have one child, please wrap childrends with LinearLayout!");
        TranslateAnimation trans = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        trans.setDuration(200);
        trans.setInterpolator(new AccelerateDecelerateInterpolator());
        contentGroup.getChildAt(0).startAnimation(trans);
    }

}

