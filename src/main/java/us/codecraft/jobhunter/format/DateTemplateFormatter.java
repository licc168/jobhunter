package us.codecraft.jobhunter.format;

import us.codecraft.webmagic.model.formatter.ObjectFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTemplateFormatter implements ObjectFormatter<Date> {

    private String template;

    @Override
    public Date format(String raw) throws Exception {
        DateFormat format = new SimpleDateFormat(template);
        return format.parse(raw.replaceAll("年|月","-").replace("日",""));

    }

    @Override
    public Class<Date> clazz() {
        return Date.class;
    }

    @Override
    public void initParam(String[] extra) {
        template = extra[0];
    }
}