package retrofit;

/**
 * Created by iscod on 2016/6/21.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class BcGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public BcGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {

        String response = responseBody.string();

        //String strResult = response.substring(1, response.length() - 1);
        String result = null;//解密
        try {
            result = AES.decrypt2Str(response, APIConstant.COMMENT_DECODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("CID", "解密的服务器数据：" + result);
        T resultData = mGson.fromJson(result, new TypeToken<T>() {
        }.getType());
        return resultData;
    }

}
