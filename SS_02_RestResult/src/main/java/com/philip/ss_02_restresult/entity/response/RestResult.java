package com.philip.ss_02_restresult.entity.response;


import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RestResult<T> {
    private int code;
    private T data;
    private String msg;
    private RestResult() {
    }

    public static RestResult success(){
        return new RestResult().setCode(200);
    }

    public static RestResult success(Object data){
        return success().setData(data);
    }

    public static RestResult error(){
        return new RestResult().setCode(500);
    }

    public static RestResult error(String msg){
        return error().setMsg(msg);
    }

    public static RestResult error(int code, String msg){
        return new RestResult().setCode(code).setMsg(msg);
    }

    public static RestResult error(Exception e){
        return error().setMsg(e.getMessage());
    }

    public String toJsonString(){
        return JSON.toJSONString(this);
    }


}
