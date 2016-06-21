package retrofit;

/**
 * Created by iscod.
 * Time:2016/6/21-14:46.
 */
public class BaseCallModel<T> {
    public String msg;
    public BaseRes<T> res;
    public String state;

    /*public String reason;
    public T result;
    public int error_code;
*/
    public static class BaseRes<T> {
        public int code;
        public String msg;
        public T data;
    }
}
