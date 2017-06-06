package us.codecraft.jobhunter.pipeline;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * 中国智能制造网站
 */
public class GkzhanPipeline implements PageProcessor {
    public static final String URL = "http://www.gkzhan.com";

    public static final String URL_INDEX = "http://www.gkzhan.com/news";

    public static final String URL_LIST = "/news/t\\d+/list*.html";

    public static final String URL_POST = "/news/detail/\\d+.html";

    private Site site = Site
            .me()
            .setDomain("http://www.gkzhan.com/")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        Selectable pageUrl = page.getUrl();
        List<String> url;
        Function<String,String> trans =  new Function<String, String>() {
            @Override
            public String apply(String s) {
                return  URL+s;
            }
        };
        if (pageUrl.regex(URL_INDEX).match()) {//资讯主页
            url = page.getHtml().links().regex(URL_LIST).all();
            page.addTargetRequests(Lists.transform(url,trans));
        }
        if (pageUrl.regex(URL_LIST).match()) {//列表页
            url = page.getHtml().xpath("//div[@class=\"index-pagelist\"]").links().regex(URL_POST).all();
            page.addTargetRequests(Lists.transform(url,trans));
            url =page.getHtml().links().regex(URL_LIST).all();
            page.addTargetRequests(Lists.transform(url,trans));
        }

        if (pageUrl.regex(URL_POST).match()) {//详情页面

            page.putField("title", page.getHtml().xpath("//h1/a/text()"));
            page.putField("content", page.getHtml().xpath("//div[@id='newsContent']/html()"));

        }

    }

    @Override
    public Site getSite() {
        return site;
    }





    public static void main(String[] args) {
        Spider.create(new GkzhanPipeline()).addUrl(URL_INDEX)
                .run();
    }
}
