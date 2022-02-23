package com.wangjf.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:公共返回对象
 * @author: Joker
 * @time: 2022/1/3 20:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String msg;
    private Object obj;

    /**
     * @description:成功返回结果
     * @param msg
            * @return: com.wangjf.server.pojo.RespBean
            * @author: Joker
            * @time: 2022/1/3 20:23
     */
    public static RespBean success(String msg){
        return new RespBean(200,msg,null);
    }

    /**
     * @description:成功返回结果
     * @param msg
 * @param obj
            * @return: com.wangjf.server.pojo.RespBean
            * @author: Joker
            * @time: 2022/1/3 20:24
     */
    public static RespBean success(String msg, Object obj){
        return new RespBean(200,msg,obj);
    }

    /**
     * @description: 失败返回结果
     * @param msg
            * @return: com.wangjf.server.pojo.RespBean
            * @author: Joker
            * @time: 2022/1/3 20:25
     */
    public static RespBean error(String msg){
        return new RespBean(500,msg,null);
    }

    /**
     * @description:失败返回结果
     * @param msg
 * @param obj
            * @return: com.wangjf.server.pojo.RespBean
            * @author: Joker
            * @time: 2022/1/3 20:26
     */
    public static RespBean error(String msg,Object obj){
        return new RespBean(500,msg,obj);
    }
}
