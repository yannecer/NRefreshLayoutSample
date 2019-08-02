package com.necer.nrefreshlayoutsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.necer.nrefreshlayout.MyLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
//
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);
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
