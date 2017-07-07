package us.codecraft.jobhunter.pipeline;

import java.time.LocalDate;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import us.codecraft.jobhunter.dao.JobInfoDAO;
import us.codecraft.jobhunter.dao.WSYDao;
import us.codecraft.jobhunter.model.WsyBean;
import us.codecraft.jobhunter.util.DateUtil;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/7/6 19:31
 * @see
 */
@Component("wsyPopeLine")
public class WsyPopeLine implements Pipeline {
  @Resource
  private WSYDao wsyDao;

  @Override
  public void process(ResultItems resultItems, Task task) {
    WsyBean  wsyBean=  resultItems.get("wsyBean");
    if(wsyBean!=null) {
      wsyBean.getUpTime().toString();
      LocalDate upDate = DateUtil.dateToLocalDate(wsyBean.getUpTime());
      if(upDate.getYear()==2017) {
        wsyDao.deleteByUrl(wsyBean.getUrl());
        wsyDao.add(wsyBean);
      }
    }
  }
}
