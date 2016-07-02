package retrofit;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by iscod.
 * Time:2016/6/22-9:04.
 */
public class UserData implements Serializable {
    @Expose
    public String kid;

    public String mobile;
    public String password;
}
