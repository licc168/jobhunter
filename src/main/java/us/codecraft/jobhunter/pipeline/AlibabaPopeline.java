package us.codecraft.jobhunter.pipeline;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import us.codecraft.jobhunter.dao.AlibabaDao;
import us.codecraft.jobhunter.model.AlibabaBean;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Function:(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2017/10/23 10:26
 * @see
 */
@Component("alibabaPopeline")
public class AlibabaPopeline implements Pipeline {
    @Resource
    private AlibabaDao alibabaDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        AlibabaBean wsyBean = resultItems.get("alibabaBean");
        if (wsyBean != null) {
            String website =  wsyBean.getWebsite();
            if(!StringUtils.isEmpty(website)){
                AlibabaBean    wsyBean2 =  alibabaDao.getByWebsite(website);

                if(wsyBean2 == null){
                    alibabaDao.deleteByUrl(wsyBean.getWebsite());
                    alibabaDao.add(wsyBean);
                }else {
                    if(StringUtils.isEmpty(wsyBean2.getRegistration())){
                        wsyBean2.setRegistration(wsyBean.getRegistration());
                    }
                    if(StringUtils.isEmpty(wsyBean2.getWebsite())){
                        wsyBean2.setWebsite(wsyBean.getWebsite());
                    }

                    if(StringUtils.isEmpty(wsyBean2.getCompanyName())){
                        wsyBean2.setCompanyName(wsyBean.getCompanyName());
                    }
                    alibabaDao.update(wsyBean2);
                }
            }

        }
    }

}
