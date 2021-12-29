package com.cds.job.controller;

import com.cds.job.domain.MyJob;
import com.cds.job.service.TestJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author cds
 * @date 2021/12/27 1:38 下午
 */
@RestController
@RequestMapping("/testJob")
public class TestJobController {
    @Autowired
    private TestJobService testJobService;

    @GetMapping("/add")
    public void add() throws Exception {
        testJobService.add(new MyJob());
    }

    @PostMapping("/create")
    public void create(@RequestBody MyJob myJob) {
        try {
            testJobService.create(myJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/changeStatus")
    public void changeStatus(@RequestBody MyJob myJob) throws SchedulerException {
        testJobService.changeStatus(myJob);
    }

    @PostMapping("/edit")
    public void edit(@RequestBody MyJob myJob) throws SchedulerException {
        testJobService.edit(myJob);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody MyJob myJob) throws SchedulerException {
        testJobService.delete(myJob);
    }
}
