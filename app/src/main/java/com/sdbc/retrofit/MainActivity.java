package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit.AES;
import retrofit.APIConstant;
import retrofit.BaseCallModel;
import retrofit.BcCallback;
import retrofit.DemoService;
import retrofit.JuheCallModel;
import retrofit.LoginData;
import retrofit.NewsData;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRequest();
        try {
            Log.d("解密：", AES.decrypt2Str("tiEPa7VkwLox/Uh2ybZg2Yx26A08ylTpstCt3mj43/izzU3CYZCqWDWw9vDZch8oA4hWLbordpPO" +
                    "1SPo5sk+mtRz4tZCsgRl7JdYiOoPTZBlptMK6hnQIX7qI/EzH9dk", APIConstant.COMMENT_DECODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRequest() {
        DemoService go = RetrofitClient
                .getInstance()
                .create(DemoService.class);
        Map<String, String> map3 = new HashMap<>();
        map3.put("userName", "13205852585");
        map3.put("userPwd", "45454");
        map3.put("isCompany", "0");
        map3.put("mobileInfo", "android");
        map3.put("appVersion", "1");
        map3.put("platform", "1");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "user/login");
            jsonObject.put("params", map3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();


        //RxJava
        Observable<String> observable = go.test1(str);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d("CID", "执行成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvContent.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(String body) {
                        tvContent.setText(body);
                    }
                });
    }


}
