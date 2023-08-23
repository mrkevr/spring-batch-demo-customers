package dev.mrkevr.sbdc.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
	
	Partitioner partitioner;
	
	// Automatically defined by Spring
	JobRepository jobRepository;
	PlatformTransactionManager transactionManager;
	
	@Bean
	Step masterStep(
			ItemReader<CustomerCsvDto> itemReader) {
		
		return new StepBuilder("masterStep", jobRepository)
				.partitioner(this.slaveStep_1(itemReader).getName(), partitioner)
				.partitionHandler(this.partitionHandler(itemReader))
				.build();
	}
	
	@Bean
	Step slaveStep_1(
			ItemReader<CustomerCsvDto> itemReader) {
		
		return new StepBuilder("slaveStep_1", jobRepository)
				.<CustomerCsvDto, Customer>chunk(100, transactionManager)
				
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				
				.faultTolerant()
				.skipPolicy(skipPolicy)
				.build();
	}
	
	@Bean
	Job runJob(
			ItemReader<CustomerCsvDto> itemReader) {

		return new JobBuilder("importCustomers", jobRepository)
				.flow(this.slaveStep_1(itemReader))
				.end()
				.build();
	}

	@Bean
    public TaskExecutor taskExecutor() {
		
//		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
//		asyncTaskExecutor.setConcurrencyLimit(10);
//		return asyncTaskExecutor;
		
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(4);
        return taskExecutor;
    }
	
	@Bean
	public PartitionHandler partitionHandler(
			ItemReader<CustomerCsvDto> itemReader) {
		
		TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		taskExecutorPartitionHandler.setGridSize(10);
		taskExecutorPartitionHandler.setTaskExecutor(this.taskExecutor());
		taskExecutorPartitionHandler.setStep(this.slaveStep_1(itemReader));
		return taskExecutorPartitionHandler;
	}
	
}
