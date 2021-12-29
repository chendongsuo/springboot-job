package com.cds.job.domain;

import lombok.Data;

/**
 * @author cds
 * @date 2021/12/27 2:05 下午
 */
@Data
public class BaseJob {
    private Long id;
    private Long tenantId;
    private String jobName;
    private String jobGroup;
    private String jobClassName;
    private String cronExpression;
    private String jobMethodName;
}
