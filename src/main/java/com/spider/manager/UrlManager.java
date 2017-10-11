package com.spider.manager;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

/**
 * url调度程序
 * 每天凌晨向url仓库添加url
 */
public class UrlManager {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //开启调度器
            scheduler.start();
            //具体任务对象
            JobDetail jobDetail =new JobDetail("url_manager",Scheduler.DEFAULT_GROUP,UrlJob.class);
            //触发器
            Trigger trigger =new CronTrigger("url_manager",Scheduler.DEFAULT_GROUP,"0 05 17 * * ?");
            //添加调度任务和触发时间
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
