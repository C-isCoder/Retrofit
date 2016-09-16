package retrofit;


import android.text.TextUtils;
import android.util.Log;

import com.sdbc.retrofit.APIConstant;
import com.sdbc.retrofit.APP;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by iscod.
 * Time:2016/6/21-9:50.
 */
public class RetrofitClient {
    private static RetrofitClient INSTANCE;
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
                RequestBody requestBody = originalRequest.body();
                HttpUrl url = originalRequest.url();
                HttpUrl newUrl;
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                String parameter = buffer.readString(UTF_8);
                String token = APP.getToken();
                String md5 = ParameterUtils.MD5(parameter);
                if (TextUtils.isEmpty(token)) {
                    newUrl = HttpUrl.parse(url.toString() + "?sign=" + md5);
                } else {
                    newUrl = HttpUrl.parse(url.toString() + "?sign=" + md5 + "&token=" + token);
                }
                Log.d("Request", "开始请求:" + "================================================" +
                        "====================================================");
                Log.d("Request", "请求参数:" + "【" + parameter + "】");
                Log.d("Request", "请求地址:" + "【" + newUrl + "】");
                Log.d("Request", "请求方法:" + "【" + originalRequest.method() + "】");
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .method(originalRequest.method(), originalRequest.body()).url(newUrl);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        //设置头
        builder.addInterceptor(headerInterceptor);

        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        builder.addInterceptor(loggingInterceptor);

        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(APIConstant.BASE_URL)
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static RetrofitClient getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                INSTANCE = new RetrofitClient();
            }
        }
        return INSTANCE;
    }

    /**
     * 自定义Service
     *
     * @param service 传入自定义的Service
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * 默认的service
     *
     * @return Service
     */
    public HttpService create() {
        return retrofit.create(HttpService.class);
    }

}
