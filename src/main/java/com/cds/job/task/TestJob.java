package com.cds.job.task;

import com.alibaba.fastjson.JSON;
import com.cds.job.constant.SchedulerConstants;
import com.cds.job.domain.MyJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author cds
 * @date 2021/12/27 11:29 上午
 */
@Component("TestJob")
public class TestJob implements Job, Serializable {
    /**
     * 测试定时任务
     */
    public void firstJob() {
        System.out.println("测试定时任务");
    }

    public void secondJob() {
        System.out.println("第二任务::");
    }

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        String jobMethodName = dataMap.getString(SchedulerConstants.TASK_PROPERTIES);
        MyJob myJob = JSON.parseObject(jobMethodName, MyJob.class);
        System.out.println(myJob);
        try {
            TestJob job = new TestJob();
            System.out.println("methodName:::" + myJob.getJobMethodName());
            Method method = job.getClass().getMethod(myJob.getJobMethodName());
            method.invoke(job);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
