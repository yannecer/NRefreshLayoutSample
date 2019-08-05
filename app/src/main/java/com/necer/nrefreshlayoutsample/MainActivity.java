package com.necer.nrefreshlayoutsample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.necer.nrefreshlayout.MyLog;
import com.necer.nrefreshlayout.NSwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
//
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                boolean b = recyclerView.canScrollVertically(-1);
                boolean b1 = recyclerView.canScrollVertically(1);

                MyLog.d("b b b b 1111:::" + b);
                MyLog.d("b b b b 2222:::" + b1);

            }
        });


        final NSwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.nSwipeRefreshLayout);
        swipeRefreshLayout.setOnLoadMoreListener(new NSwipeRefreshLayout.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.add();
                        swipeRefreshLayout.loadMoreFinish();
                    }
                }, 1000);


            }
        });

//
//        final TextView tv_ = findViewById(R.id.tv_);
//
//
//        tv_.post(new Runnable() {
//            @Override
//            public void run() {
//                boolean b = tv_.canScrollVertically(-1);
//                boolean b1 = tv_.canScrollVertically(1);
//
//                MyLog.d("b b b b 1111:::" + b);
//                MyLog.d("b b b b 2222:::" + b1);
//
//            }
//        });

    }
}
