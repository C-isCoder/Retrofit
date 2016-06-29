package retrofit;

import android.widget.Toast;

import com.sdbc.retrofit.APP;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iscod.
 * Time:2016/6/21-14:56.
 */
public abstract class BcCallback<T extends BaseCallModel> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.raw().code() == 200) {
            try {
                int service_state = Integer.parseInt(response.body().state);
                if (service_state == 1) {
                    int res_state = response.body().res.code;
                    if (res_state == 4000) {
                        onSuccess(call, response);
                    } else if (res_state == 3000) {
                        //onAutoLogin();
                    } else {
                        onError(response.body().res.msg);
                        Toast.makeText(APP.getInstance(),
                                response.body().res.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                onError("未知错误！！");
            }

        } else {
            onError("请求服务器异常");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof SocketTimeoutException) {

        } else if (t instanceof ConnectException) {

        } else if (t instanceof RuntimeException) {

        }
        onError(t.getMessage());
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);

    public abstract void onError(String message);

    public abstract void onAutoLogin();
}
