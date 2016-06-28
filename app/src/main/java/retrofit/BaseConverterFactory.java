package retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by iscod on 2016/6/22.
 */
public class BaseConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static BaseConverterFactory create() {
        return create(new Gson());
    }

    public static BaseConverterFactory create(Gson gson) {
        return new BaseConverterFactory(gson);
    }

    private BaseConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new BaseResponseBodyConverter<>(gson, adapter);//响应
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new BaseRequestBodyConverter<>();//请求
    }


    public class BaseRequestBodyConverter<T> implements Converter<T, RequestBody> {

        private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        public BaseRequestBodyConverter() {
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            String strValue = value.toString();
            Log.i("CID", "request中传递的json数据：" + strValue);
            try {
                //加密
                strValue = AES.encrypt2Str(strValue, APIConstant.COMMENT_ENCRYP);
                Log.i("CID", "加密后：" + strValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String request = gson.toJson(strValue);
            return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), request);
        }
    }


    public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> adapter;
        private Gson gson;

        public BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.adapter = adapter;
            this.gson = gson;
        }

        @Override
        public T convert(ResponseBody response) throws IOException {
//            InputStream in = response.byteStream();
            String strResponse = response.string();
            //String strResult = strResponse.substring(1, strResponse.length() - 1);
            String result = "";
            try {
                result = AES.decrypt2Str(strResponse, APIConstant.COMMENT_DECODE);
                Log.i("CID", "解密的服务器数据：" + result);
            } catch (Exception e) {

            }
            return (T) adapter.toJson((T) result);
        }
    }

//    public static <T> T fromJsonString(Class<T> cls, String jsonString) throws Exception {
//        try {
//            //data 直接 为String的时候 直接返回
//            if (!TextUtils.isEmpty(jsonString) && !jsonString.startsWith("{") && !jsonString.startsWith("[")) {
//                return (T) jsonString;
//            }
//
//            T entity = null;
//            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//            entity = gson.fromJson(jsonString, cls);
//            return entity;
//        } catch (Exception e) {
//            Log.d("ERROR", e == null ? "" : e.getMessage() + "");
////            throw new MLParserException(String.format("%s(%s:%s)", e.getMessage(),
////                    "解析的字符串为"
////                    , jsonString));
//        }
//    }
}
