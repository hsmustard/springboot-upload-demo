package com.hsmus.upload.entity;

import java.util.HashMap;

/**
 * @author Mustard <jianzhang@dgut.edu.cn>
 * @date 2021/1/22
 * @time 14:37
 */
public class R extends HashMap<String, Object> {

    public static R ok() {
        return R.ok("success");
    }

    public static R ok(Object message) {
        R r = new R();
        r.put("code", 200);
        r.put("message", message);
        return r;
    }

    public static R error(String message) {
        R r = new R();
        r.put("code", 500);
        r.put("message", message);
        return r;
    }

    public R put(String key, Object val) {
        super.put(key, val);
        return this;
    }
}
