package us.codecraft.jobhunter.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import us.codecraft.jobhunter.model.LieTouJobInfo;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:27
 */

public interface JobInfoDAO {

    @Insert("insert into news (title,content,news_date,view_count,category,create_time) values (#{title},#{content},#{newsDate}," +
            "#{viewCount}," +
            "#{category},now())")
    public int add(LieTouJobInfo jobInfo);
    @Insert("delete from news")
    public int delete();

}
