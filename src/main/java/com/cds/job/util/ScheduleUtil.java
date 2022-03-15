package com.cds.job.util;

import com.alibaba.fastjson.JSON;
import com.cds.job.constant.SchedulerConstants;
import com.cds.job.domain.MyJob;
import org.quartz.*;

/**
 * @author cds
 * @date 2021/12/27 2:01 下午
 */
public class ScheduleUtil {

    /**
     * 创建任务
     * @param scheduler
     * @param job
     * @throws SchedulerException
     */
    public static void createScheduleJob(Scheduler scheduler, MyJob job) throws SchedulerException {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(getClass(job.getJobClassName())).withIdentity(getJobKey(jobId, jobGroup)).build();
        //表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        //按cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup)).withSchedule(cronScheduleBuilder).build();
        //放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(SchedulerConstants.TASK_PROPERTIES, JSON.toJSONString(job));
        //jobDetail.getJobDataMap().put("jobMethodName", job.getJobMethodName());

        if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
            throw new RuntimeException("存在任务!!!");
        }
        scheduler.scheduleJob(jobDetail, trigger);

        System.out.println("生成任务");
    }

    /**
     * 暂停任务
     * @param scheduler
     * @param myJob
     * @throws SchedulerException
     */
    public static void pauseScheduleJob(Scheduler scheduler, MyJob myJob) throws SchedulerException {
        scheduler.pauseJob(getJobKey(myJob.getId(), myJob.getJobGroup()));
    }

    /**
     * 删除任务
     * @param scheduler
     * @param myJob
     * @throws SchedulerException
     */
    public static void deleteScheduleJob(Scheduler scheduler, MyJob myJob) throws SchedulerException {
        scheduler.deleteJob(getJobKey(myJob.getId(), myJob.getJobGroup()));
    }

    /**
     * 唤醒任务
     * @param scheduler
     * @param myJob
     */
    public static void resumeScheduleJob(Scheduler scheduler, MyJob myJob) throws SchedulerException {
        scheduler.resumeJob(getJobKey(myJob.getId(), myJob.getJobGroup()));
    }

    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(SchedulerConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(SchedulerConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    public static Class getClass(String jobClassName) {
        Class cls = null;
        try {
            cls = Class.forName(jobClassName);
            cls.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return cls;
    }

}
