package com.cds.job.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cds
 * @date 2021/12/23 6:21 下午
 */
@Data
public class MyJob extends BaseJob implements Serializable {
    private static final long serialVersionUID = 1L;
    private String status;
    private Boolean delFlag;
    private Date createTime;
    private Date updateTime;
}
