package us.codecraft.jobhunter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Function:(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @version 1.0.0
 * @date 2017/7/6 20:33
 * @see
 */
public class DateUtil {
  // 02. java.util.Date --> java.time.LocalDate
  public static LocalDate dateToLocalDate(Date date) {
    Instant instant = date.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
    LocalDate localDate = localDateTime.toLocalDate();
    return  localDate;
  }
public static void main(String arge[]) throws ParseException {
  String upTimeString = "2017-06-21";
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  Date upTime = formatter.parse(upTimeString);
  LocalDate localDate = DateUtil.dateToLocalDate(upTime);
  System.out.println(localDate.toString());
}
}
