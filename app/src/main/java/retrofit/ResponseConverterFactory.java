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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by iscod on 2016/6/22.
 */
public class ResponseConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static ResponseConverterFactory create() {
        return create(new Gson());
    }

    public static ResponseConverterFactory create(Gson gson) {
        return new ResponseConverterFactory(gson);
    }

    private ResponseConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new BaseResponseBodyConverter<>(gson, adapter, type);//响应
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

            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> adapter;
        private Gson gson;
        private Type type;//泛型，当服务器返回的数据为数组的时候回用到

        public BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
            this.adapter = adapter;
            this.gson = gson;
            this.type = type;
        }

        @Override
        public T convert(ResponseBody response) throws IOException {
            String strResponse = response.string();
            if (TextUtils.isEmpty(strResponse)) {
                throw new HttpException("请求服务器异常");
            }
            Log.d("Request", "服务器响应:" + "············································" +
                    "·························································");
            Log.d("Request", "服务器返回:" + strResponse);
            Log.d("Request", "请求结束:" + "==========================================" +
                    "==========================================================");
            try {
                JSONObject jb = new JSONObject(strResponse);
                // 服务器状态
                int service_state = jb.getInt("state");
                if (service_state != 1) {
                    // 服务器异常
                    throw new HttpException(jb.getString("msg"));
                }
                // 接口状态
                int ret_state = jb.getJSONObject("res").getInt("code");
                if (ret_state == 40000) {
                    if (jb.getJSONObject("res").isNull("data")) {
                        throw new HttpException("请求服务器异常");
                    }
                    String parameters = jb.getJSONObject("res").get("data").toString();
                    if (parameters.startsWith("{")) {
                        return adapter.fromJson(parameters);
                    } else if (parameters.startsWith("[")) {
                        return gson.fromJson(parameters, type);
                    } else {
                        throw new HttpException("请求数据异常");
                    }
                } else if (ret_state == 30000) {
                    throw new HttpException(jb.getJSONObject("res").getString("msg"));
                } else {
                    // 接口异常
                    throw new HttpException(jb.getJSONObject("res").getString("msg"));
                }
            } catch (Exception e) {
                throw new HttpException(e.getMessage());
            } finally {
                response.close();
            }
        }
    }
}


