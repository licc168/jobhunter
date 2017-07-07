package us.codecraft.jobhunter.pageprocessor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import us.codecraft.jobhunter.JobCrawler;
import us.codecraft.jobhunter.dao.JobInfoDAO;
import us.codecraft.jobhunter.dao.WSYDao;
import us.codecraft.jobhunter.model.LieTouJobInfo;
import us.codecraft.jobhunter.model.WsyBean;
import us.codecraft.jobhunter.pipeline.UrlConst;
import us.codecraft.jobhunter.pipeline.WsyPopeLine;
import us.codecraft.jobhunter.util.OkHttpUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Function:(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2017/7/6 19:28
 * @see
 */
@Component
public class WsyPageProcess implements PageProcessor {
  @Qualifier("wsyPopeLine")
      @Resource
  WsyPopeLine wsyPopeLine;
  private Site site = Site
      .me()
      .setDomain("http://www.wsy.com/54634")
      .setSleepTime(3000)

      .setUserAgent(
          "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

  @Override
  public void process(Page page) {
    Selectable pageUrl = page.getUrl();
    List<String> urls;
    try {
      if (pageUrl.regex(UrlConst.URL_INDEX).match()) {
        urls =    page.getHtml().xpath("//ul[@class='nav-ul']/li").links().all();
        page.addTargetRequests(urls,2);
        System.out.println(urls);
      }
      if(pageUrl.regex(UrlConst.MARKET_URL).match()){
        urls =    page.getHtml().xpath("//div[@class='dk']/a").links().all();
        page.addTargetRequests(urls,1);
        System.out.println(urls);

      }
      if(pageUrl.regex(UrlConst.SHOP_URL).match()){
        urls =    page.getHtml().xpath("//div").links().regex(UrlConst.DETAIL_URL).all();
        page.addTargetRequests(urls,0);
        System.out.println(urls);
      }
      if(pageUrl.regex(UrlConst.DETAIL_URL).match()){
        String  id =   pageUrl.get().replaceAll("\\D","");
        String privewNum =   OkHttpUtils
            .getStringFromServer("https://www.wsy.com/ajax/getpv.php?stats=getpv&id="+id);
        String name =  page.getHtml().xpath("//div[@class=item-mb]/a/text()").get();
        String upTimeString = page.getHtml().xpath("//div[@class=item-msg-time]/span/text()").get();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date upTime = formatter.parse(upTimeString);

        WsyBean bean  = new WsyBean();
        bean.setName(name);
        bean.setPrivewNum(Integer.parseInt(privewNum));
        bean.setUpTime(upTime);
        bean.setUrl(id);
        page.putField("wsyBean",bean);
      }


    } catch (IOException e){
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

  }

  @Override
  public Site getSite() {
    return site;
  }

  public void craw() {
    OOSpider.create(new WsyPageProcess()).addPipeline(wsyPopeLine).addUrl(UrlConst.URL_INDEX).thread(10)

        .run();

  }



  public static void main(String[] args) {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext*.xml");
    final WsyPageProcess wsyPageProcess = applicationContext.getBean(WsyPageProcess.class);
    wsyPageProcess.craw();

  }
}
