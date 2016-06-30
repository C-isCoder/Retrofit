package retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by iscod.
 * Time:2016/6/30-8:56.
 */
public class HttpRequestParamsUtils {
    public static String paramsConvert(Map<String, String> map,String strInterface) {
        if (map == null || map.isEmpty()) return "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", strInterface);
            jsonObject.put("params", map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
