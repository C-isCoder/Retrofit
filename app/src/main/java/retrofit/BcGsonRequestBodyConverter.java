package retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by iscod on 2016/6/21.
 */
public class BcGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */

    public BcGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }


    @Override
    public RequestBody convert(T value) throws IOException {
        //加密
        String data = "";
        Log.i("CID", "request中传递的json数据：" + value.toString());
        try {
            data = AES.encrypt2Str(value.toString(), APIConstant.COMMENT_ENCRYP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String postBody = gson.toJson(data); //对象转化成json
        Log.i("CID", "转化后的数据：" + postBody);
        return RequestBody.create(MEDIA_TYPE, postBody);
    }

}