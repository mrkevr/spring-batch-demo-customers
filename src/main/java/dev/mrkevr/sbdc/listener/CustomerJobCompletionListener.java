package dev.mrkevr.sbdc.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerJobCompletionListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Starting job >>>" + jobExecution);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("Job successful >>>" + jobExecution);
	}

}
