package com.cds.job.service;

import com.cds.job.domain.MyJob;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * @author cds
 * @date 2021/12/27 11:43 上午
 */
public interface TestJobService {

    void add(MyJob myJob) throws Exception;

    void create(MyJob myJob) throws SchedulerException;

    void changeStatus(MyJob myJob) throws SchedulerException;

    void edit(MyJob myJob) throws SchedulerException;

    void delete(MyJob myJob) throws SchedulerException;
}
