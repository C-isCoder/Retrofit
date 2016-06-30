package retrofit;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sdbc.retrofit.APP;
import com.sdbc.retrofit.AppToolUtil;
import com.sdbc.retrofit.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
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

    private RetrofitClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //网络请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String enCode = "";
                String deCode = "";
                Request originalRequest = chain.request();
                RequestBody str = originalRequest.body();
                try {
                    enCode = AES.encrypt2Str(str.toString(), APIConstant.COMMENT_ENCRYP);
                    if (TextUtils.isEmpty(enCode)) {
                        throw new HttpException("参数错误");
                    }
                    Log.i("Http请求：", "加密后的参数：" + replaceBlank(enCode));
                } catch (Exception e) {
                    throw new HttpException("参数错误");
                }
                RequestBody newBody = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), replaceBlank(enCode));
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("User-Agent", "android")
                        .header("Content-Type", "application/json")
                        .method(originalRequest.method(), newBody);
                Request request = requestBuilder.build();
                return chain.proceed(request);
                //Response response = chain.proceed(request);
//                String body = response.body().string();
//                Log.i("Http响应:", body);
//                MediaType type = response.body().contentType();
//                try {
//                    deCode = AES.decrypt2Str(body, APIConstant.COMMENT_DECODE);
//                    if (TextUtils.isEmpty(enCode)) {
//                        throw new HttpException("请求服务器异常");
//                    }
//                    Log.i("Http响应：", "解密后的参数：" + deCode);
//                } catch (Exception e) {
//                    throw new HttpException("请求服务器异常");
//                }
//                try {
//                    JSONObject jb = new JSONObject(deCode);
//                    // 服务器状态
//                    int service_state = jb.getInt("state");
//                    if (service_state == 1) {
//                        // 接口状态
//                        int ret_state = jb.getJSONObject("res").getInt("code");
//                        if (ret_state == 40000) {
//                            //data 为null  直接返回
//                            if (jb.getJSONObject("res").isNull("data")) {
//                                throw new HttpException("请求服务器异常");
//                            } else {
//                                String parames = jb.getJSONObject("res").toString();
//                                Log.i("Http响应：", "返回的Data实体：" + parames);
//                                ResponseBody responseBody = ResponseBody.create(type, parames);
//                                Response.Builder responseBuilder = response.newBuilder();
//                                responseBuilder.body(responseBody);
//                                Response newResponse = responseBuilder.build();
//                                return newResponse;
//                            }
//                        } else if (ret_state == 30000) {
//                            throw new HttpException(jb.getJSONObject("res").getString("msg"));
//                        } else {
//                            // 接口异常
//                            throw new HttpException(jb.getJSONObject("res").getString("msg"));
//                        }
//                    } else {
//                        // 服务器异常
//                        throw new HttpException(jb.getString("msg"));
//                    }
//                } catch (Exception e) {
//                    throw new HttpException(e.getMessage());
//                }
            }
        };
        //设置头
        builder.addNetworkInterceptor(headerInterceptor);

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
        //公共参数
        Interceptor addQueryParmeterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                String method = originalRequest.method();
                Headers headers = originalRequest.headers();
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("platform", "android")
                        //.addQueryParameter("version", "1.0.0")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        //设置公共参数
        //builder.addInterceptor(addQueryParmeterInterceptor);


        //设置cookie
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(APIConstant.BASE_URL)
                .addConverterFactory(BaseConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
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

    /**
     * 去掉 /n 等字符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
