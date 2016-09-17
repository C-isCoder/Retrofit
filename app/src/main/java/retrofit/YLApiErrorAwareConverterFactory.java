package retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by iCong on 2016/9/17.
 */
public class YLApiErrorAwareConverterFactory extends Converter.Factory {

    private final EmptyJsonLenientConverterFactory mEmptyJsonLenientConverterFactory;

    public YLApiErrorAwareConverterFactory(
            EmptyJsonLenientConverterFactory emptyJsonLenientConverterFactory) {
        mEmptyJsonLenientConverterFactory = emptyJsonLenientConverterFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return mEmptyJsonLenientConverterFactory.requestBodyConverter(type, parameterAnnotations,
                methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        final Converter<ResponseBody, ?> apiErrorConverter =
                mEmptyJsonLenientConverterFactory.responseBodyConverter(HttpException.class,
                        annotations, retrofit);
        final Converter<ResponseBody, ?> delegateConverter =
                mEmptyJsonLenientConverterFactory.responseBodyConverter(type, annotations,
                        retrofit);
        return value -> {
            ResponseBody clone = RealResponseBody.create(value.contentType(), value.contentLength(),
                    value.source().buffer().clone());
            Object apiError = apiErrorConverter.convert(clone);
            if (apiError instanceof HttpException) {
                throw (HttpException) apiError;
            }
            return delegateConverter.convert(value);
        };
    }
}
