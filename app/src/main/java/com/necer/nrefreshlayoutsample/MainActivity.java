package com.necer.nrefreshlayoutsample;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.necer.nrefreshlayout.NSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<String> strings;

    private NSwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        strings = new ArrayList<>();
//        strings.add("23r234");
//        strings.add("23r234");
//        strings.add("23r234");
//        strings.add("23r234");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this, strings);
        recyclerView.setAdapter(recyclerViewAdapter);

//
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                strings.add("ssss");
                // strings.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                // recyclerViewAdapter.add();

                //  swipeRefreshLayout.noMoreDate();
            }
        }, 1000);
//
//

        swipeRefreshLayout = findViewById(R.id.nSwipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new NSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        swipeRefreshLayout.setOnLoadMoreListener(new NSwipeRefreshLayout.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {

//
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        strings.add("ssss");
                        strings.add("ssss");
                        strings.add("ssss");
                        strings.add("ssss");
                        recyclerViewAdapter.notifyDataSetChanged();
//                        swipeRefreshLayout.loadMoreDisable();
                          swipeRefreshLayout.loadMoreComplete();
                    }
                }, 3000);


            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void aaa(View view) {

        strings.add(2, "rrrrr");
        recyclerViewAdapter.notifyItemInserted(2);


        //   recyclerViewAdapter.remove();
        //recyclerViewAdapter.add();


//        strings.add("ssss");
//        strings.add("ssss");
//        strings.add("ssss");
//        strings.add("ssss");
//        strings.add("ssss");
//        strings.add("ssss");
//        strings.add("ssss");
//        strings.add("ssss");
//
//        recyclerViewAdapter.notifyDataSetChanged();
    }
}
