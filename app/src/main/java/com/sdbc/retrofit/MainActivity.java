package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.AES;
import retrofit.APIConstant;
import retrofit.BaseCallModel;
import retrofit.DemoService;
import retrofit.LoginData;
import retrofit.NewsData;
import retrofit.RetrofitClient;
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
        DemoService go = RetrofitClient
                .getInstance()
                .create(DemoService.class);
//        Map<String, String> map = new HashMap<>();
//        map.put("q", "英雄联盟");
//        map.put("key", "d0efcc052db3181db11f0e35db1f56b4");
//        map.put("dtype", "json");

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("userName", "17686616852");
        map1.put("pwd", "123456");
        map1.put("Integer", "android");
        map1.put("isCompany", "0");
        map1.put("appVersion", "1.0");
        map1.put("action", "user/login");
        Call<BaseCallModel<LoginData>> call = go.toPostService(map1);
        call.enqueue(new Callback<BaseCallModel<LoginData>>() {
            @Override
            public void onResponse(Call<BaseCallModel<LoginData>> call,
                                   Response<BaseCallModel<LoginData>> response) {
                tvContent.setText(response.body().state);
            }

            @Override
            public void onFailure(Call<BaseCallModel<LoginData>> call, Throwable t) {
                tvContent.setText(t.getMessage());
            }
        });
    }
}
