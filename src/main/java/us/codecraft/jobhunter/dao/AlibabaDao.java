package us.codecraft.jobhunter.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import us.codecraft.jobhunter.model.AlibabaBean;
import us.codecraft.jobhunter.model.WsyBean;

/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:27
 */
public interface AlibabaDao {

    @Insert("insert into alibaba_company (registration,website,company_name) values (#{registration},#{website},#{companyName})")
    public int add(AlibabaBean bean);

    @Update("update alibaba_company set registration=#{registration} ,company_name=#{companyName} where website= #{website}" )
    public int update(AlibabaBean bean);

    @Select(" select registration,website,company_name as companyName from  alibaba_company where website= #{website}" )
    public AlibabaBean getByWebsite(String  website);

    @Delete("DELETE FROM alibaba_company where website = #{website}")
    public void deleteByUrl(String website);
}
