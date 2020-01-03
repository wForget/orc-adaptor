package cn.wangz.orc.schema.utils;

import cn.wangz.orc.schema.model.Constants;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wang_zh
 * @date 2020/1/3
 */
public class TypeUtils {

    // Single
    public static Pair<Boolean, String> isSingle(Object object) {
        if (object instanceof Number) {
            // INT
            if (object instanceof Integer) {
                return new Pair<Boolean, String>(true, Constants.INT);
            }
            // BIGINT
            if (object instanceof Long) {
                return new Pair<Boolean, String>(true, Constants.BIGINT);
            }
            // DOUBLE
            if (object instanceof BigDecimal) {
                return new Pair<Boolean, String>(true, Constants.DOUBLE);
            }
        }
        if (object instanceof String) {
            // Date
            Date date = null;
            try {
                date = com.alibaba.fastjson.util.TypeUtils.castToDate(object);
            } catch (Exception e) {
            }
            if (date != null) {
                return new Pair<Boolean, String>(true, Constants.DATE);
            }
            // String
            return new Pair<Boolean, String>(true, Constants.STRING);
        }
        return new Pair<Boolean, String>(false, null);
    }
}
