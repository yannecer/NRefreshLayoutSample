package com.necer.nrefreshlayout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter mAdapter;
    private Context mContext;
    private static final int LOADMORE_VIEWTYPE = -11111;
    private boolean mIsLoadMoreEnable = false;
    private BallPulseView mBallPulseView;

    private FrameLayout mLoadMoreView;
    private TextView mTextViewComplete;
    private TextView mTextViewException;
    private TextView mTextViewNoMore;

    public LoadMoreAdapter(final RecyclerView.Adapter adapter, Context context, final OnDateCountlistener onDateCountlistener) {
        this.mAdapter = adapter;
        this.mContext = context;

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                onDateCountlistener.onRealCount(adapter.getItemCount());
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                onDateCountlistener.onRealCount(adapter.getItemCount());
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                onDateCountlistener.onRealCount(adapter.getItemCount());
                notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                onDateCountlistener.onRealCount(adapter.getItemCount());
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                onDateCountlistener.onRealCount(adapter.getItemCount());
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                onDateCountlistener.onRealCount(adapter.getItemCount());
                notifyItemMoved(fromPosition, toPosition);
            }
        });

        mLoadMoreView = new FrameLayout(context);
        mLoadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dp2px(mContext, 60)));

        mBallPulseView = new BallPulseView(context);
        mBallPulseView.setAnimatingColor(Color.parseColor("#3F51B5"));
        mBallPulseView.setIndicatorColor(Color.parseColor("#3F51B5"));
        mBallPulseView.setNormalColor(Color.parseColor("#3F51B5"));
        mLoadMoreView.addView(mBallPulseView);

        mTextViewComplete = getTextView(context, "加载完成");
        mLoadMoreView.addView(mTextViewComplete);

        mTextViewException = getTextView(context, "加载异常");
        mLoadMoreView.addView(mTextViewException);

        mTextViewNoMore = getTextView(context, "没有更多数据了");
        mLoadMoreView.addView(mTextViewNoMore);

        mBallPulseView.setVisibility(View.GONE);
        mTextViewComplete.setVisibility(View.GONE);
        mTextViewException.setVisibility(View.GONE);
        mTextViewNoMore.setVisibility(View.GONE);
    }

    private TextView getTextView(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        return textView;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == LOADMORE_VIEWTYPE) {
            return new LoadMoreViewHolder(mLoadMoreView);
        } else {
            return mAdapter.onCreateViewHolder(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (!(viewHolder instanceof LoadMoreViewHolder)) {
            mAdapter.onBindViewHolder(viewHolder, i);
        }
    }


    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + (mIsLoadMoreEnable ? 1 : 0);
    }


    @Override
    public int getItemViewType(int position) {
        if (mIsLoadMoreEnable && position == getItemCount() - 1) {
            return LOADMORE_VIEWTYPE;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }


    public void onStart() {
        mBallPulseView.setVisibility(View.VISIBLE);
        mTextViewComplete.setVisibility(View.GONE);
        mTextViewException.setVisibility(View.GONE);
        mTextViewNoMore.setVisibility(View.GONE);
    }

    public void onComplete() {
        mBallPulseView.setVisibility(View.GONE);
        mTextViewComplete.setVisibility(View.VISIBLE);
        mTextViewException.setVisibility(View.GONE);
        mTextViewNoMore.setVisibility(View.GONE);
    }

    public void onException() {
        mBallPulseView.setVisibility(View.GONE);
        mTextViewComplete.setVisibility(View.GONE);
        mTextViewNoMore.setVisibility(View.GONE);
        mTextViewException.setVisibility(View.VISIBLE);
    }

    private void onNoMore() {
        mBallPulseView.setVisibility(View.GONE);
        mTextViewComplete.setVisibility(View.GONE);
        mTextViewNoMore.setVisibility(View.VISIBLE);
        mTextViewException.setVisibility(View.GONE);
    }


    public void setLoadMoreAvailable() {
        this.mIsLoadMoreEnable = true;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setLoadMoreDisable() {
        onNoMore();
    }

    public boolean isLoadMoreEnable() {
        return mIsLoadMoreEnable;
    }

    public static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        public LoadMoreViewHolder(@NonNull final View itemView) {
            super(itemView);
        }
    }


    public static float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public interface OnDateCountlistener {
        void onRealCount(int dataCount);
    }

}
