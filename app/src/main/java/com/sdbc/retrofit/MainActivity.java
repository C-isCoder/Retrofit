package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit.HttpService;
import retrofit.ParameterUtils;
import retrofit.RetrofitClient;
import retrofit.UserData;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CID";
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRequest();
    }

    private void initRequest() {
        Map<String, String> map = new HashMap<>();
        //map.put("mobile", "17686616852");
        map.put("buildkid", "k20160628111146DODB1e6F");
        String sign = ParameterUtils.MD5("content=" + ParameterUtils.JsonConvert(map));
        Log.i(TAG, "Map=>Json:" + ParameterUtils.JsonConvert(map));
        RetrofitClient
                .getInstance()
                .create(HttpService.class)
                .getDoors(sign, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvContent.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(UserData data) {
                        tvContent.setText(data.kid);
                    }
                });
    }


}
