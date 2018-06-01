package com.p.library.ok;


import com.p.library.A;
import com.p.library.utils.GsonUtil;

import java.util.HashMap;
import java.util.Hashtable;

public class BaseParam extends Hashtable<String, String> {


    private HashMap<String, Object> params = new HashMap<>();

    public BaseParam() {
        String time = System.currentTimeMillis() + "";
        String token = A.getInstance().getToken();
        put("timestamp", time);
        put("token", token);
        try {
            put("signature", S.getString(time, token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public BaseParams(String token) {
//        String time = System.currentTimeMillis() + "";
//        put("timestamp", time);
//        put("token", token);
//        put("signature", SignatureUtils.getString(time, token));
//    }

    /**
     * 一次性把 json 数据放入
     */
    public BaseParam putParams(String json) {
        put("params", json);
        return this;
    }


    /**
     * 加在 params 参数中
     */
    public BaseParam putParam(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public BaseParam commit() {
        put("params", GsonUtil.toJson(params));
        return this;
    }


}
