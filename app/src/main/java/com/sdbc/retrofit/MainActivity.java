package com.sdbc.retrofit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import model.ImageData;
import model.UserData;
import retrofit.HttpService;
import retrofit.RetrofitClient;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CID";
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRequest();
    }

    private void initRequest() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("mobile", "17686616852");
        loginMap.put("pwd", "123456");
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient
                .getInstance()
                .create()
                .login(loginMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(UserData userData) {
                        progressBar.setVisibility(View.INVISIBLE);
                        APP.setMkid(userData.kid);
                        APP.setToken(userData.token);
                        APP.setCommKid(userData.committeekid);
                        APP.setVillageKid(userData.villagekid);
                        getBanner();
                    }
                });
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("typekid", "k20160629164339B0ZME123");
        map.put("committeekid", APP.getCommKid());
        map.put("villagekid", APP.getVillageKid());
        RetrofitClient
                .getInstance()
                .create(HttpService.class)
                .getImage(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ImageData>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<ImageData> CallBack) {
                        ImageData imageData = CallBack.get(0);
                        tvContent.setText(imageData.title);
                        Glide.with(MainActivity.this)
                                .load(APIConstant.API_LOAD_IMAGE + imageData.picpath)
                                .into(ivImage);
                    }
                });
    }

}
