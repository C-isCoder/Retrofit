package retrofit;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by iscod on 2016/6/22.
 */
public class BaseConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new BaseResponseBodyConverter(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return new BaseRequestBodyConverter<>();
    }


    public class BaseRequestBodyConverter<T> implements Converter<T, RequestBody> {

        private Gson gson = new Gson();

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

        private Type type;
        Gson gson = new Gson();

        public BaseResponseBodyConverter(Type type) {
            this.type = type;
        }

        @Override
        public T convert(ResponseBody response) throws IOException {
            String result = response.toString();
            try {
                //解密
                result = AES.decrypt2Str(result, APIConstant.COMMENT_DECODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            T data = gson.fromJson(result, type);
            return data;
        }
    }

}
