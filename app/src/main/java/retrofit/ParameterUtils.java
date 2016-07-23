package retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by iscod on 2016/7/24.
 */
public class ParameterUtils {
    private static Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    public static String MD5(String original) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(original.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toLowerCase();
    }


    public static String JsonConvert(Map map) {
        return gson.toJson(map);
    }

}
