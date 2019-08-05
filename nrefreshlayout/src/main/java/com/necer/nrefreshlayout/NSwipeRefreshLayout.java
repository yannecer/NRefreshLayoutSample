package com.necer.nrefreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class NSwipeRefreshLayout extends SwipeRefreshLayout implements ValueAnimator.AnimatorUpdateListener {


    private View mTarget;
    private boolean mLoadMoreEnable = true;
    private View mFooter;
    private float mFooterHeight;

    protected ValueAnimator mValueAnimator;


    public NSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mFooterHeight = dp2px(context, 60);

        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(300);
        mValueAnimator.addUpdateListener(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2) {
            throw new RuntimeException("NSwipeRefreshLayout有且只能有一个直接子View");
        }

        mTarget = getChildAt(1);

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

        gestureMove(dy);
    }

    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        float footerY = mFooter.getY();
        if (footerY != mTarget.getMeasuredHeight()) {
            auto();
        }

    }

    boolean isssss = true;

    private void gestureMove(int dy) {

        float targetPaddingBottom = mTarget.getPaddingBottom();
        float footerY = mFooter.getY();

        if (dy > 0 && !mTarget.canScrollVertically(1)) {
            //向上不能花滑动
            float maxF = mFooterHeight - targetPaddingBottom;
            mFooter.setY(footerY - Math.min(maxF, dy));
            mTarget.setPadding(0, 0, 0, (int) (targetPaddingBottom + Math.min(maxF, dy)));

            if (isssss && targetPaddingBottom == mFooterHeight) {
                isssss = false;
                onLoadMoreListener.onLoadMore();
            }


        } else if (dy < 0 && targetPaddingBottom != 0) {
            float maxF = targetPaddingBottom;
            mTarget.setPadding(0, 0, 0, (int) (targetPaddingBottom - Math.min(maxF, -dy)));
            mFooter.setY(footerY + Math.min(maxF, -dy));
        }

    }

    public float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public void loadMoreFinish() {
        mTarget.setPadding(0, 0, 0, 0);
        mFooter.setY(mTarget.getMeasuredHeight());
    }

    private void auto() {
        float start = mTarget.getPaddingBottom();
        float end = 0;
        mValueAnimator.setFloatValues(start, end);
        mValueAnimator.start();
    }


    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        mTarget.setPadding(0, 0, 0, (int) animatedValue);

        mFooter.setY(mTarget.getMeasuredHeight() - animatedValue);

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
