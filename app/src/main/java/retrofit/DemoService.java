package retrofit;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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
    Observable<JuheCallModel<List<NewsData>>> rxGetNewsData(@FieldMap Map<String, String> Parameters);
}
