package retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
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
        return new BaseRequestBodyConverter<>(adapter);//请求
    }


    public class BaseRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private TypeAdapter<T> adapter;
        private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        public BaseRequestBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {

//            Buffer buffer = new Buffer();
//            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
//            JsonWriter jsonWriter = gson.newJsonWriter(writer);
//            adapter.write(jsonWriter, value);
//            jsonWriter.close();
//            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
            String strValue = value.toString();
            Log.i("CID", "request中传递的json数据：" + strValue);
            try {
                //加密
                strValue = AES.encrypt2Str(strValue, APIConstant.COMMENT_ENCRYP);
                Log.i("CID", "加密后：" + strValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String request = gson.toJson(replaceBlank(strValue));
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
            String strResponse = response.string();
            String deCode = "";
            Log.i("Http响应:", strResponse);
            try {
                deCode = AES.decrypt2Str(strResponse, APIConstant.COMMENT_DECODE);
                if (TextUtils.isEmpty(deCode)) {
                    throw new HttpException("请求服务器异常");
                }
                Log.i("Http响应：", "解密后的参数：" + deCode);
            } catch (Exception e) {
                throw new HttpException("请求服务器异常");
            }
            try {
                JSONObject jb = new JSONObject(deCode);
                // 服务器状态
                int service_state = jb.getInt("state");
                if (service_state == 1) {
                    // 接口状态
                    int ret_state = jb.getJSONObject("res").getInt("code");
                    if (ret_state == 40000) {
                        //data 为null  直接返回
                        if (jb.getJSONObject("res").isNull("data")) {
                            throw new HttpException("请求服务器异常");
                        } else {
                            String parames = jb.getJSONObject("res").toString();
                            Log.i("Http响应：", "返回的Data实体：" + parames);
                            return (T) adapter.toJson((T) parames);
                        }
                    } else if (ret_state == 30000) {
                        throw new HttpException(jb.getJSONObject("res").getString("msg"));
                    } else {
                        // 接口异常
                        throw new HttpException(jb.getJSONObject("res").getString("msg"));
                    }
                } else {
                    // 服务器异常
                    throw new HttpException(jb.getString("msg"));
                }
            } catch (Exception e) {
                throw new HttpException(e.getMessage());
            }
        }
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
