package retrofit;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by lingyun on 2016/5/9.
 */
public class LoginData implements Serializable {
    //    "data":{"template":[],"appVersion":"","hxPwd":null,
//            "cityId":6,"cityName":"济南2222","name":"修理厂","userId":2,"hxUser":null,"isEnforce":"","isCompany":0}
    @Expose
    public String appVersion;
    @Expose
    public String hxPwd;
    @Expose
    public String cityId;
    @Expose
    public String cityName;
    @Expose
    public String name;
    @Expose
    public String userId;
    @Expose
    public String hxUser;
    @Expose
    public String hxKid;
    @Expose
    public String isEnforce;
    @Expose
    public String isCompany;
    @Expose
    public String hxLoginId;
    @Expose
    public String phone;
    @Expose
    public String pwd;

    @Override
    public String toString() {
        return "version:" + appVersion + "\n" +
                "name:" + name + "\n" +
                "phone" + phone + "\n";
    }
}
