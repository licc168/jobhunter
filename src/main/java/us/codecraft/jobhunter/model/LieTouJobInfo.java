package us.codecraft.jobhunter.model;

import org.apache.commons.codec.digest.DigestUtils;
import us.codecraft.jobhunter.format.DateTemplateFormatter;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.*;

import java.util.Date;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:28
 */
@TargetUrl("http://www.gkzhan.com/news/detail/\\d+.html")
@HelpUrl("http://www.gkzhan.com/news/t\\d+/list(|_p\\d+).html")
public class LieTouJobInfo implements AfterExtractor {
    @ExtractBy("//h1/a/text()")
    private String title = "";
    @ExtractBy("//div[@id='newsContent']/html()")
    private String content = "";


    @Formatter(value = "yyyy-MM-dd",formatter= DateTemplateFormatter.class)
    @ExtractBy("//div[@class='newsTime']/dl/dt/regex(\\d+年\\d+月\\d+日)")
    private Date newsDate;

    @ExtractBy("//div[@class='newsTime']/dl/dt/regex((?<=人气：)\\d+)")
    private Integer viewCount;

    @ExtractBy("//div[@class='mainTop']/p/span/a/text()")
    private  String category="";
    @Override
    public String toString() {
        return "JobInfo{" +
                "title='" + title + '\'' +
                "category='" + category + '\'' +
                "newsDate='"+newsDate.toString()+ '\'' +
                "viewCount='" + viewCount + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void afterProcess(Page page) {

    }
}
