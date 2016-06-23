package retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;
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
        return new BaseResponseBodyConverter<>(adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new BaseRequestBodyConverter<>(adapter);
    }


    public class BaseRequestBodyConverter<T> implements Converter<T, RequestBody> {

        private TypeAdapter<T> adapter;
        private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        public BaseRequestBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            String request = gson.toJson(value);
            Log.d("CID", "加密前：" + request);
            try {
                //加密
                request = AES.encrypt2Str(request, APIConstant.COMMENT_ENCRYP);
                Log.d("CID", "加密后：" + request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), request);
        }
    }


    public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final TypeAdapter<T> adapter;

        public BaseResponseBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody response) throws IOException {
            String result = response.string();
            //解密
            try {
                result = AES.decrypt2Str(result, APIConstant.COMMENT_DECODE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }
            return adapter.fromJson(result);
        }
    }

}
