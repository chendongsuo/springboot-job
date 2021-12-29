package com.cds.job.service.impl;

import com.cds.job.constant.SchedulerConstants;
import com.cds.job.domain.MyJob;
import com.cds.job.mapper.MyJobMapper;
import com.cds.job.service.TestJobService;
import com.cds.job.util.ScheduleUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cds
 * @date 2021/12/27 11:43 上午
 */
@Service
public class TestJobServiceImpl implements TestJobService {
    @Qualifier("Scheduler")
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MyJobMapper myJobMapper;

    @Override
    public void add(MyJob myJob) throws Exception {
        MyJob quartz = new MyJob();
        quartz.setJobName("test01");
        quartz.setJobGroup("test");
        quartz.setJobClassName("com.cds.job.task.TestJob");
        quartz.setCronExpression("*/5 * * * * ?");
        quartz.setJobMethodName("firstJob");
        Class cls = Class.forName(quartz.getJobClassName());
        cls.newInstance();
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(quartz.getJobName(), quartz.getJobGroup()).withDescription("我的testJob描述").build();
        jobDetail.getJobDataMap().put("jobMethodName", "firstJob");
        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger" + quartz.getJobName(), quartz.getJobGroup())
                .startNow().withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(MyJob myJob) throws SchedulerException {
        int result = myJobMapper.insert(myJob);
        ScheduleUtil.createScheduleJob(scheduler, myJob);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStatus(MyJob myJob) throws SchedulerException {
        Long jobId = myJob.getId();
        String jobGroup = myJob.getJobGroup();
        String status = myJob.getStatus();
        int update = myJobMapper.update(myJob);
        if (SchedulerConstants.Status.NORMAL.getValue().equals(status)) {
            scheduler.resumeJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        } else if (SchedulerConstants.Status.PAUSE.getValue().equals(status)) {
            scheduler.pauseJob(ScheduleUtil.getJobKey(jobId, jobGroup));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(MyJob myJob) throws SchedulerException {
        MyJob byId = myJobMapper.selectById(myJob.getId());
        int update = myJobMapper.update(myJob);
        //先删除在创建
        scheduler.deleteJob(ScheduleUtil.getJobKey(myJob.getId(), byId.getJobGroup()));
        ScheduleUtil.createScheduleJob(scheduler, myJob);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(MyJob myJob) throws SchedulerException {
        MyJob byId = myJobMapper.selectById(myJob.getId());
        myJobMapper.deleteById(myJob.getId());
        scheduler.deleteJob(ScheduleUtil.getJobKey(myJob.getId(), byId.getJobGroup()));
    }
}
