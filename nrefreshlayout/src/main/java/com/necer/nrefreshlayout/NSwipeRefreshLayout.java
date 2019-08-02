package com.necer.nrefreshlayout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NSwipeRefreshLayout extends SwipeRefreshLayout {


    private View mTarget;
    private boolean mLoadMoreEnable = true;
    private View mFooter;
    private float mFooterHeight;


    public NSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mFooterHeight = dp2px(context, 60);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("NSwipeRefreshLayout有且只能有一个直接子View");
        }

        mTarget = getChildAt(1);

        MyLog.d("mTarget::" + mTarget);

        mFooter = new View(getContext());
        mFooter.setBackgroundColor(Color.BLACK);
        addView(mFooter);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int measuredHeight = mTarget.getMeasuredHeight();

        mFooter.layout(left, measuredHeight, right, (int) (measuredHeight + mFooterHeight));

    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);


        gestureMove(dy, consumed);
    }


    private void gestureMove(int dy, int[] consumed) {

        float targetY = mTarget.getY();
        float footerY = mFooter.getY();

        MyLog.d("top:111:" + targetY);
        MyLog.d("top:222:" + footerY);

        if (dy > 0 && !mTarget.canScrollVertically(1)) {
            //向上不能花滑动

            float f = mFooterHeight - (-targetY);

            mTarget.setY(targetY - dy);
            mFooter.setY(footerY - dy);
            if (consumed != null) consumed[1] = dy;
        } else if (dy < 0 && footerY < mTarget.getMeasuredHeight()) {
            mTarget.setY(targetY - dy);
            mFooter.setY(footerY - dy);
            if (consumed != null) consumed[1] = dy;
        }

    }

    public float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
