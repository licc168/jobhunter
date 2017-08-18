package us.codecraft.jobhunter.model;

import java.util.Date;

/**
 * Function:(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2017/7/6 16:52
 * @see
 */
public class WsyBean {
   private  String  name;
   private Date createTime;
   private  Integer privewNum;
   private  Date upTime;
   private  String Url;
  public Date getUpTime() {
    return upTime;
  }

  public void setUpTime(Date upTime) {
    this.upTime = upTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Integer getPrivewNum() {
    return privewNum;
  }

  public void setPrivewNum(Integer privewNum) {
    this.privewNum = privewNum;
  }

  public String getUrl() {
    return Url;
  }

  public void setUrl(String url) {
    Url = url;
  }
}
