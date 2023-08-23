package dev.mrkevr.sbdc.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import dev.mrkevr.sbdc.entity.Customer;
import dev.mrkevr.sbdc.listener.StepSkipListener;
import dev.mrkevr.sbdc.model.CustomerCsvDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerBatchConfig {
	
	ItemProcessor<CustomerCsvDto, Customer> itemProcessor;
	ItemWriter<Customer> itemWriter;
	
	SkipPolicy skipPolicy;
	
	StepSkipListener stepSkipListener;
	JobExecutionListener jobExecutionListener;
	
	@Bean
	Step step1(
			JobRepository jobRepository, 
			PlatformTransactionManager transactionManager,
			ItemReader<CustomerCsvDto> itemReader) {
		
		return new StepBuilder("csv-step", jobRepository)
				.<CustomerCsvDto, Customer>chunk(10, transactionManager)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.taskExecutor(taskExecutor())
				.listener(jobExecutionListener)
				.listener(stepSkipListener)
				.faultTolerant()
				.skipPolicy(skipPolicy)
				.build();
	}

	@Bean
	Job runJob(
			JobRepository jobRepository, 
			PlatformTransactionManager transactionManager, 
			ItemReader<CustomerCsvDto> itemReader) {
		
		return new JobBuilder("importCustomers", jobRepository)
				.flow(step1(jobRepository, transactionManager, itemReader))
				.end()
				.build();
	}

	@Bean
	TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(10);
		return asyncTaskExecutor;
	}
	
}
