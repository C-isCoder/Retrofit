package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.AES;
import retrofit.APIConstant;
import retrofit.HttpService;
import retrofit.HttpRequestParamsUtils;
import retrofit.RetrofitClient;
import retrofit.UserData;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
            Log.d("传参解密：", AES.decrypt2Str("c6zYVpfx35iYh+wiw/xhZofGaM3jwEr01ybcCcLythDaglNtatav3UUl/TcmP27cr2yjSi3IJBtdOldzH+CmyGzO0NtSWbTTLvCCtZTI6IQ=", APIConstant.COMMENT_ENCRYP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRequest() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("mobile", "17686616852");
        loginMap.put("pwd", "123456");
        RetrofitClient
                .getInstance()
                .create(HttpService.class)
                .login(HttpRequestParamsUtils.paramsConvert(loginMap, "member/memberLogin"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.d("CID", "success");
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
