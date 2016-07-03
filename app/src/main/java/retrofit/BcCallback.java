package retrofit;

import android.widget.Toast;

import com.sdbc.retrofit.APP;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

/**
 * Created by iscod.
 * Time:2016/6/21-14:56.
 */
public abstract class BcCallback<T extends BaseCallModel> extends Observable<T> {

    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param f {@link OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected BcCallback(OnSubscribe<T> f) {
        super(f);
    }

    @Override
    public <R> R extend(Func1<? super OnSubscribe<T>, ? extends R> conversion) {
        return super.extend(conversion);
    }

    @Override
    public <R> Observable<R> compose(Transformer<? super T, ? extends R> transformer) {
        return super.compose(transformer);
    }

    @Override
    public Completable toCompletable() {
        return super.toCompletable();
    }

    @Override
    public Single<T> toSingle() {
        return super.toSingle();
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onError(String message);

    public abstract void onAutoLogin();
}
