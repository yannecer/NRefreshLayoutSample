package com.necer.nrefreshlayoutsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by necer on 2017/6/7.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private int count = 50;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textView = holder.textView;
        textView.setText("RecyclerView ----- " + position);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void add() {
        count = count + 50;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_);
        }
    }

}




