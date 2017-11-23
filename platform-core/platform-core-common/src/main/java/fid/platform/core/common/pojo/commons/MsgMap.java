package fid.platform.core.common.pojo.commons;

import org.springframework.util.StringUtils;

import java.util.HashMap;


public class MsgMap extends HashMap {
    //返回接口增加数量
    public static String key_num = "num";
    public static String key_data = "data";
    public static String key_code = "code";
    public static String key_operateSuccess = "operateSuccess";
    public static String key_msg = "msg";

    public void setProperty(Boolean operateSuccess, String code) {
        if (operateSuccess != null) {
            this.put(key_operateSuccess, operateSuccess);
        }

        if (!StringUtils.isEmpty(code)) {
            this.put(key_code, code);
        }
    }

    public void doFail() {
        setProperty(false,
                "1");
    }

    public void doSuccess() {
        setProperty(true,
                "0");
    }

    public MsgMap doSuccess(Object data) {
        try {
            this.put(key_operateSuccess, "0");
            this.put(MsgMap.key_data, data);
            this.doSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            this.doFail();
            this.put(MsgMap.key_msg, e.toString());
        }
        return this;
    }

    public MsgMap doFail(Object data) {
        try {
            this.put(key_operateSuccess, "1");
            this.put(MsgMap.key_data, data);
            doFail();
        } catch (Exception e) {
            e.printStackTrace();
            this.doFail();
            this.put(MsgMap.key_msg, e.toString());
        }
        return this;
    }

}
