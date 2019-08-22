package com.necer.nrefreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

public class NSwipeRefreshLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {


    private View mTarget;

    protected ValueAnimator mValueAnimator;
    private boolean isLoading;
    private LoadMoreAdapter loadMoreAdapter;

    private boolean hasMoreDate = true;//是否还有更多的数据

    public NSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(300);
        mValueAnimator.addUpdateListener(this);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 1) {
            throw new RuntimeException("NSwipeRefreshLayout有且只能有一个直接子View");
        }

        mTarget = getChildAt(0);

        if (mTarget instanceof RecyclerView) {
            dealRecyclerView();
        }

//
//        mEmptyView = new TextView(getContext());
//
//        mEmptyView.setGravity(Gravity.CENTER);
//        mEmptyView.setText("暂无数据");
//        mEmptyView.setBackgroundColor(Color.parseColor("#ffffff"));
//         mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//        addView(mEmptyView);
//        mEmptyView.setVisibility(GONE);
    }


    private void dealRecyclerView() {
        post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) mTarget;
                final RecyclerView.Adapter adapter = recyclerView.getAdapter();
                loadMoreAdapter = new LoadMoreAdapter(adapter, getContext(), new LoadMoreAdapter.OnDateCountlistener() {
                    @Override
                    public void onRealCount(int dataCount) {

                    }
                });
                recyclerView.setAdapter(loadMoreAdapter);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (dy > 0 && !loadMoreAdapter.isLoadMoreEnable() && onLoadMoreListener != null && hasMoreDate) {
                            loadMoreAdapter.setLoadMoreAvailable();
                        }

                        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int itemCount = layoutManager.getItemCount();
                            int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                            if (lastPosition > itemCount - 2 && !isLoading && loadMoreAdapter.isLoadMoreEnable() && hasMoreDate) {
                                MyLog.d("loadMoreloadMoreloadMore:::");
                                loadMoreStart();
                            }
                        }
                    }
                });
            }
        });
    }
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        mEmptyView.layout(left,top,right,bottom);
//    }
//


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        gestureMove(dy, consumed);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }


    @Override
    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        float tagetY = mTarget.getY();
        if (tagetY != 0) {
            auto();
        }

    }


    private void gestureMove(int dy, int[] consumed) {

        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }

        float targetY = mTarget.getY();
        if ((dy > 0 && !mTarget.canScrollVertically(1)) || dy < 0 && !mTarget.canScrollVertically(-1)) {
            mTarget.setY(targetY - dy);
            if (consumed != null) consumed[1] = (int) dy;
        } else if (dy < 0 && targetY < 0) {
            mTarget.setY(targetY - Math.max(dy, targetY));
            if (consumed != null) consumed[1] = (int) dy;
        } else if (dy > 0 && targetY > 0) {
            mTarget.setY(targetY - Math.min(dy, targetY));
            if (consumed != null) consumed[1] = (int) dy;
        }
    }

    public float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    private void loadMoreStart() {
        setEnabled(false);//loadmore的时候不能下拉
        isLoading = true;
        onLoadMoreListener.onLoadMore();
        loadMoreAdapter.onStart();
    }

    public void loadMoreComplete() {
        setEnabled(true);
        isLoading = false;
        loadMoreAdapter.onComplete();
    }

    public void loadMoreException() {
        setEnabled(true);
        isLoading = false;
        loadMoreAdapter.onException();
    }


    public void loadMoreDisable() {
        hasMoreDate = false;
        setEnabled(true);
        isLoading = false;
        loadMoreAdapter.setLoadMoreDisable();
    }

    private void auto() {
        float start = mTarget.getY();
        float end = 0;
        mValueAnimator.setFloatValues(start, end);
        mValueAnimator.start();
    }


    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        //  mTarget.setPadding(0, 0, 0, (int) animatedValue);
        mTarget.setY(animatedValue);
        // mFooter.setY(mTarget.getMeasuredHeight() - animatedValue);

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
