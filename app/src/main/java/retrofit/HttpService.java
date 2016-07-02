package retrofit;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by iscod.
 * Time:2016/6/21-9:46.
 */
public interface HttpService {

    @FormUrlEncoded
    @POST("user/login")
    Call<BaseCallModel<LoginData>> loginService(@FieldMap Map<String, Object> Parameters);

    @FormUrlEncoded
    @POST("onebox/news/query")
    Observable<String> rxGetNewsData(@FieldMap Map<String, String> Parameters);

    @POST("pointhelpapi/mobile/")
    Call<BaseCallModel<String>> test(@Body String string);

    @POST("lollipop/mobile/")
    Observable<String> test1(@Body String string);

    @POST("pointhelpapi/mobile/")
    Observable<UserData> login(@Body String string);
}
