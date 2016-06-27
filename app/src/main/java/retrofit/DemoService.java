package retrofit;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by iscod.
 * Time:2016/6/21-9:46.
 */
public interface DemoService {
    //    @FormUrlEncoded
//    @POST("onebox/news/query")
//    Call<BaseCallModel<List<NewsData>>> postService(@Field("q") String name,
//                                                    @Field("dtype") String json,
//                                                    @Field("key") String key);
    @FormUrlEncoded
    @POST("onebox/news/query")
    Call<JuheCallModel<List<NewsData>>> postService(@FieldMap Map<String, String> Parameters);

    @FormUrlEncoded
    @POST("user/login")
    Call<BaseCallModel<LoginData>> loginService(@FieldMap Map<String, Object> Parameters);

    @FormUrlEncoded
    @POST("onebox/news/query")
    Observable<String> rxGetNewsData(@FieldMap Map<String, String> Parameters);

    @FormUrlEncoded
    @POST("member/hello")
    Call<String> test(@Field("") String string);

    @FormUrlEncoded
    @POST("member/hello")
    Observable<String> RxTest(@Field("") String string);
}
