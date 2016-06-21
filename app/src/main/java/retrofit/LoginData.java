package retrofit;

/**
 * Created by lingyun on 2016/5/9.
 */
public class LoginData {
//    "data":{"template":[],"appVersion":"","hxPwd":null,
//            "cityId":6,"cityName":"济南2222","name":"修理厂","userId":2,"hxUser":null,"isEnforce":"","isCompany":0}

    public String appVersion;
    public String hxPwd;
    public String cityId;
    public String cityName;
    public String name;
    public String userId;
    public String hxUser;
    public String hxKid;
    public String isEnforce;
    public String isCompany;
    public String hxLoginId;
    public String phone;
    public String pwd;

    @Override
    public String toString() {
        return "version:" + appVersion + "\n" +
                "name:" + name + "\n" +
                "phone" + phone + "\n";
    }
}
