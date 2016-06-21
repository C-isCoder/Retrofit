package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.BaseCallModel;
import retrofit.BcCallback;
import retrofit.DemoService;
import retrofit.LoginData;
import retrofit.NewsData;
import retrofit.RetrofitWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DemoService go = RetrofitWrapper
                .getInstance()
                .create(DemoService.class);
        Map<String, String> map = new HashMap<>();
        map.put("q", "英雄联盟");
        map.put("key", "d0efcc052db3181db11f0e35db1f56b4");
        map.put("dtype", "json");
        Call<BaseCallModel<List<NewsData>>> call = go
                .postService(map);
        call.enqueue(new Callback<BaseCallModel<List<NewsData>>>() {
            @Override
            public void onResponse(Call<BaseCallModel<List<NewsData>>> call,
                                   Response<BaseCallModel<List<NewsData>>> response) {
                tvContent.setText(response
                        .body()
                        .result
                        .toString()
                        .replace("[", "")
                        .replace("]", ""));
            }

            @Override
            public void onFailure(Call<BaseCallModel<List<NewsData>>> call, Throwable t) {
                tvContent.setText(t.getMessage());
            }
        });
    }
}
