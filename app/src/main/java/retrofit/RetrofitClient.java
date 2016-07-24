package retrofit;


import android.text.TextUtils;
import android.util.Log;

import com.sdbc.retrofit.AES;
import com.sdbc.retrofit.APIConstant;
import com.sdbc.retrofit.APP;
import com.sdbc.retrofit.AppToolUtil;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by iscod.
 * Time:2016/6/21-9:50.
 */
public class RetrofitClient {
    private static RetrofitClient instance;
    private Retrofit retrofit;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private RetrofitClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //网络请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                String strBody = originalRequest.body().toString();
                Log.i("CID", "RequestBody：" + strBody);
                RequestBody newBody = RequestBody.create(MEDIA_TYPE,
                        strBody.getBytes(UTF_8));
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .method(originalRequest.method(), newBody);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        //设置头
        //builder.addInterceptor(headerInterceptor);

        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        //网络请求缓存
        File cacheFile = new File(APP.getInstance().getBaseContext().getExternalCacheDir(),
                APP.getInstance().getPackageName());
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!AppToolUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (AppToolUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    //有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public,max-age=" + maxAge)
                            .build();
                } else {
                    // 无网络时 设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached,max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };
        //设置缓存
        //builder.cache(cache).addInterceptor(cacheInterceptor);

        //设置超时
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(APIConstant.BASE_URL)
                .addConverterFactory(BaseConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                instance = new RetrofitClient();
            }
        }
        return instance;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

}
