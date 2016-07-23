package retrofit;


import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by iscod.
 * Time:2016/6/21-9:46.
 */
public interface HttpService {

    @FormUrlEncoded
    @POST("onebox/news/query")
    Observable<String> rxGetNewsData(@FieldMap Map<String, String> Parameters);

    @POST("member/memberLogin")
    @FormUrlEncoded
    Observable<UserData> login(@Query("sign") String sign,
                               @Field("mobile") String mobile, @Field("pwd") String pwd);
}
