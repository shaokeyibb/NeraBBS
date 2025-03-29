package io.hikarilan.nerabbs.services.comment.configuration;

import io.hikarilan.nerabbs.common.interfaces.Timed;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class QuartzConfiguration implements ApplicationRunner {

    private final ApplicationContext applicationContext;

    private final Scheduler scheduler;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Map.Entry<String, Job> jobEntry : applicationContext.getBeansOfType(Job.class).entrySet()) {
            var jobKey = JobKey.jobKey(jobEntry.getKey(), applicationName);

            if (scheduler.checkExists(jobKey)) {
                continue;
            }

            JobDetail jobDetail = JobBuilder.newJob(jobEntry.getValue().getClass())
                    .withIdentity(jobKey)
                    .build();

            Trigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobEntry.getKey(), applicationName)
                    .withSchedule(SimpleScheduleBuilder.repeatHourlyForever().withIntervalInMinutes(jobEntry.getValue() instanceof Timed ? (int) ((Timed) jobEntry.getValue()).getDuration().toMinutes() : 60).repeatForever())
                    .startNow()
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);
        }
    }
}
