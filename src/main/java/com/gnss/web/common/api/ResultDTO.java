package com.gnss.web.common.api;

import com.gnss.web.common.constant.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Description: 统一响应结果</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author menghuan
 * @version 1.0.1
 * @date 2018-5-16
 */
@ApiModel("统一响应结果")
@Data
public class ResultDTO<T> implements Serializable {

    private static final ResultDTO<?> EMPTY = new ResultDTO<>();

    @ApiModelProperty(value = "响应代码", position = 1)
    private int code = ResultCodeEnum.SUCCESS.getCode();

    @ApiModelProperty(value = "响应消息", position = 2)
    private String msg = ResultCodeEnum.SUCCESS.getDesc();

    @ApiModelProperty(value = "数据", position = 3)
    private T data;

    public ResultDTO() {
        super();
    }

    public ResultDTO(T data) {
        super();
        this.data = data;
    }

    public ResultDTO(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = ResultCodeEnum.FAIL.getCode();
    }

    public ResultDTO(ResultCodeEnum resultCodeEnum, String msg) {
        super();
        this.code = resultCodeEnum.getCode();
        this.msg = msg;
    }

    @SuppressWarnings("unchecked")
    public static final <T> ResultDTO<T> empty() {
        return (ResultDTO<T>) EMPTY;
    }

}
