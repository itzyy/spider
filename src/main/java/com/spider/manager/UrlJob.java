package com.spider.manager;

import com.spider.util.RedisUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * 定时任务详细类
 */
public class UrlJob implements Job {

    private RedisUtils redisUtils = new RedisUtils();

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<String> urls = redisUtils.lrange(RedisUtils.start_url, 0, 1);
        // 所有的人口地址会存储到start_url这个list队列中，每天循环从这个队列中取数据，然后添加到url仓库中
        for (String url : urls) {
            redisUtils.add_s(RedisUtils.key, url);
        }
    }

}
