package us.codecraft.jobhunter.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import us.codecraft.jobhunter.model.LieTouJobInfo;
import us.codecraft.jobhunter.model.WsyBean;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:27
 */
public interface WSYDao {

    @Insert("insert into wsy (name,privew_num,up_time,url,create_time) values (#{name},#{privewNum},#{upTime},#{url},now())")
    public int add(WsyBean bean);

    @Delete("DELETE FROM wsy where url = #{url}")
    public void deleteByUrl(String url);
}
