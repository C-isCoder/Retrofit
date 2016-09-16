package model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by iscod on 2016/9/16.
 */
public class ImageData implements Serializable {
    @Expose
    public String picpath;
    @Expose
    public String title;
    @Expose
    public String kid;
    @Expose
    public String remark;

    @Override
    public String toString() {
        return "      url:" + picpath + "\n" + "      title:" + title;
    }
}
