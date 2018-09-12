package com.dmall.web.common.converter;

import com.dmall.common.utils.DateUtil;
import com.dmall.common.utils.StringUtil;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author:yuhang
 * @Date:2018/9/12
 */
public class DateConverter implements Converter<String,Date> {
    @Override
    public Date convert(String source) {
        if (source.length() == 0) {
            return null;
        }
        if (StringUtil.isEmpty(source)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_PUBLIC);
            return df.parse(source.toString());
        } catch (ParseException e) {
            try {
                SimpleDateFormat df = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
                return df.parse(source.toString());
            } catch (ParseException e1) {
                try {
                    SimpleDateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_CHINA);
                    return df.parse(source.toString());
                } catch (ParseException e2) {
                    try {
                        SimpleDateFormat dfParse = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                        return dfParse.parse(source.toString());
                    } catch (ParseException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
