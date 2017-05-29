package com.cognitive.newswizard.agent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.cognitive.newswizard.agent.client.FeedGroupClient;
import com.cognitive.newswizard.agent.client.RawFeedEntryClient;
import com.cognitive.newswizard.api.vo.newsfeed.FeedGroupVO;

@Configuration
@EnableScheduling
public class Scheduler implements SchedulingConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
	
	private final FeedGroupClient feedGroupClient;

	private final RawFeedEntryClient rawFeedEntryClient;

	@Autowired
	public Scheduler(final FeedGroupClient feedGroupClient, final RawFeedEntryClient rawFeedEntryClient) {
		this.feedGroupClient = feedGroupClient;
		this.rawFeedEntryClient = rawFeedEntryClient;
	}

	@Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }
	
	@Override
    public void configureTasks(final ScheduledTaskRegistrar taskRegistrar) {
		LOGGER.info("Configuring feed read agensts");
		
		final FeedGroupVO[] feedGroups = feedGroupClient.getAll();
		
		for (FeedGroupVO feedGroup : feedGroups) {
				final FeedReaderAgent agent = new FeedReaderAgent(feedGroup, rawFeedEntryClient);
				
	        taskRegistrar.setScheduler(taskExecutor());
	        taskRegistrar.addTriggerTask(
	                new Runnable() {
	                    @Override public void run() {
	                    	agent.execute();
	                    }
	                },
	                new Trigger() {
	                    @Override public Date nextExecutionTime(TriggerContext triggerContext) {
	                        Calendar nextExecutionTime =  new GregorianCalendar();
	                        Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
	                        nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
	                        if (lastActualExecutionTime == null) {
	                        	// add only 10 seconds
		                        nextExecutionTime.add(Calendar.SECOND, 10);
	                        } else {
	                        	// add configured delay period
		                        nextExecutionTime.add(Calendar.SECOND, feedGroup.getRefreshPeriod().intValue());
	                        }
	                        return nextExecutionTime.getTime();
	                    }
	                }
	            
			);
	        LOGGER.info("Agent reader for group {} configured", feedGroup.getName());
		}
		
    }
}
