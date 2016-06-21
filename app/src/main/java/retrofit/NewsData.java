
package retrofit;

public class NewsData {

    public String title;
    public String content;
    public String imgWidth;
    public String fullTitle;
    public String pdate;
    public String src;
    public String imgLength;
    public String img;
    public String url;
    public String pdateSrc;

    @Override
    public String toString() {
        return "标题:" + title.replace(",", "") + "\n" + "内容:" + content + "\n" + "来源:" + src
                + "\n" + "\n";
    }
}



