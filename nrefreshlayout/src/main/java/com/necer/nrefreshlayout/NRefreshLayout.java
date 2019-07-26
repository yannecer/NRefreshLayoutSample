package com.necer.nrefreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class NRefreshLayout extends FrameLayout implements NestedScrollingParent, ValueAnimator.AnimatorUpdateListener {


    private View childView;
    private float maxPullDistance;
    protected ValueAnimator childViewValueAnimator;


    public NRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        maxPullDistance = dp2px(context, 100);

        childViewValueAnimator = new ValueAnimator();
        childViewValueAnimator.setDuration(300);
        childViewValueAnimator.addUpdateListener(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        childView = getChildAt(0);

    }


    private float lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (childView instanceof NestedScrollingChild) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float dy = lastY - y;
                gestureMove(dy, null);
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                autoScroll();
                break;
        }
        return true;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        gestureMove(dy, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onStopNestedScroll(View target) {
        if (childView.getY() != 0) {
            autoScroll();
        }
    }


    private void gestureMove(float dy, int[] consumed) {

        boolean running = childViewValueAnimator.isRunning();
        if (running) {
            return;
        }

        float childViewY = childView.getY();

        // dy = dy * (1 - childViewY / maxPullDistance);//越滑越慢

        if (dy < 0 && childViewY >= 0 && !childView.canScrollVertically(-1)) {
//            float maxDy = maxPullDistance - childViewY;
//            float realDy = -dy;
//            childView.setY(childViewY + Math.min(maxDy, realDy));
            childView.setY(childViewY - dy);
            if (consumed != null) consumed[1] = (int) dy;
        } else if (dy > 0 && childViewY > 0) {
            float maxDy = childViewY;
            float realDy = dy;
            childView.setY(childViewY - Math.min(maxDy, realDy));
            if (consumed != null) consumed[1] = (int) dy;
        } else if (dy > 0 && childViewY <= 0 && !childView.canScrollVertically(1)) {
            childView.setY(childViewY - dy);
            if (consumed != null) consumed[1] = (int) dy;
        } else if (dy < 0 && childViewY < 0) {
            float maxDy = -childViewY;
            float realDy = -dy;
            childView.setY(childViewY + Math.min(maxDy, realDy));
            if (consumed != null) consumed[1] = (int) dy;
        }
    }

    private void autoScroll() {
        float childViewStart = childView.getY();
        float childViewEnd = 0;
        childViewValueAnimator.setFloatValues(childViewStart, childViewEnd);
        childViewValueAnimator.start();
    }


    public float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        childView.setY(animatedValue);
    }
}
