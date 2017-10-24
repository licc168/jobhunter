package us.codecraft.jobhunter.pageprocessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import us.codecraft.jobhunter.model.AlibabaBean;
import us.codecraft.jobhunter.pipeline.AlibabaPopeline;
import us.codecraft.jobhunter.util.OkHttpUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 阿里巴巴 国际站数据
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2017/10/23 9:26
 * @see
 */
@Component
public class AlbabaProcess implements PageProcessor {
    @Qualifier("alibabaPopeline")
    @Resource
    AlibabaPopeline            alibabaPopeline;

    public static final String URL                     = "http://www.alibaba.com";
    // 深圳阿里巴巴公司查询
    public static final String URL_INDEX               = "http://www.alibaba.com/trade/search?fsb=y&IndexArea=company_en&CatId=&SearchText=shenzhen";

    // 分页数据
    public static final String URL_PAGE                = "//www.alibaba.com/trade/search\\?.+";

    // 详情页数据
    public static final String URL_COMPANY_DETAIL      = ".*\\.alibaba.com/company_profile\\.html";

    // 详情页公司基本信息

    public static final String URL_COMPANY_TRUSTPASS   = "/company_profile/trustpass_profile.html";

    // 公司联系人信息
    public static final String URL_COMPANY_CONTACTINFO = "contactinfo.html";

    public void craw() {
      AlbabaProcess albabaProcess =    new AlbabaProcess();
      albabaProcess.login();
        OOSpider.create(albabaProcess).addPipeline(alibabaPopeline).addUrl(URL_INDEX).thread(1)

                .run();

    }

    private Site site = Site.me().setSleepTime(100).setUserAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        Selectable pageUrl = page.getUrl();
        List<String> url;

        if (URL_INDEX.equals(pageUrl.get()) || pageUrl.regex(URL_PAGE).match()) {
            url = page.getHtml().xpath("//div[@class='ui2-pagination-pages']/a[@class=next]").links().all();
            // url = page.getHtml().links().regex(URL_PAGE).all();
            page.addTargetRequests(url);
        }
        if (pageUrl.regex(URL_PAGE).match()) {
            url = page.getHtml().links().regex(URL_COMPANY_DETAIL).all();
            page.addTargetRequests(tarnsList(url));
        }

        if (pageUrl.regex(URL_COMPANY_TRUSTPASS).match()) {
            String webSite = pageUrl.get().replace(URL_COMPANY_TRUSTPASS, "") + "/";
            String registration = page.getHtml().xpath("//table[@class=table]//tbody//tr[2]//td//dl//dd/text()").get();
            AlibabaBean alibabaBean = new AlibabaBean();
            alibabaBean.setWebsite(webSite);
            alibabaBean.setRegistration(registration);
            page.putField("alibabaBean", alibabaBean);
        }
        if (pageUrl.regex(URL_COMPANY_CONTACTINFO).match()) {

            String webSite = pageUrl.get().replace(URL_COMPANY_CONTACTINFO, "");
            String id = page.getHtml().xpath("//div[@class=contact-detail-mask]").$("a", "data-account-id").get();
            String company = page.getHtml().xpath("//article[@class=article]//table[@class=company-info-data]//tbody/tr/td[2]/text()")
                    .get();
            AlibabaBean alibabaBean = new AlibabaBean();
            alibabaBean.setCompanyName(company);
            alibabaBean.setWebsite(webSite);
            page.putField("alibabaBean", alibabaBean);
            try {
                String privewNum = OkHttpUtils
                        .getStringFromServer(webSite + "/event/app/contactPerson/showContactInfo.htm?encryptAccountId=" + id);
              System.out.println(privewNum);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(id);

        }

    }

    public List<String> tarnsList(List<String> list) {
        if (CollectionUtils.isEmpty(list))
            return Collections.EMPTY_LIST;
        List<String> urlLists = new ArrayList<>();
        for (String url : list) {
            url = url.replace("company_profile.html", "");
            String url1 = url + "company_profile/trustpass_profile.html";
            urlLists.add(url1);
            String url2 = url + "contactinfo.html";
            urlLists.add(url2);
        }
        return urlLists;
    }

    @Override
    public Site getSite() {
        // 将获取到的cookie信息添加到webmagic中
        if (cookies != null) {
            for (Cookie cookie : cookies) {
              site.addCookie(cookie.getName().toString(),cookie.getValue().toString());
            }
        }
        return site.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext*.xml");
        final AlbabaProcess albabaProcess = applicationContext.getBean(AlbabaProcess.class);
        albabaProcess.craw();
    }

    // 用来存储cookie信息
    private Set<Cookie> cookies;

    // 模拟登陆
    public void login() {
        System.setProperty("webdriver.chrome.driver", "D:\\soft\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(
                "https://passport.alibaba.com/mini_login.htm?lang=zh_cn&appName=icbu&appEntrance=default&styleType=auto&bizParams=&notLoadSsoView=false&notKeepLogin=true&isMobile=false&loginId=liccwork%40126.com&ut=&showKeepLogin=false&showSnsLogin=true&cssLink=https%3A%2F%2Fu.alicdn.com%2Fcss%2F6v%2Frun%2Fcommon%2Fxman%2Fhavana-thirdpart-login.css&regUrl=https%3A%2F%2Faccounts.alibaba.com%2Fregister%2Fregister.htm%3Freturn_url%3Dhttp%253A%252F%252Fwww.alibaba.com&rnd=0.8266436392382828");

        driver.findElement(By.id("fm-login-id")).clear();

        // 在******中填你的用户名
        driver.findElement(By.id("fm-login-id")).sendKeys("111111");

        driver.findElement(By.id("fm-login-password")).clear();
        // 在*******填你密码
        driver.findElement(By.id("fm-login-password")).sendKeys("11111~");

        // 模拟点击登录按钮
        driver.findElement(By.id("fm-login-submit")).click();

        // 获取cookie信息
        cookies = driver.manage().getCookies();
       //driver.close();

    }

}
