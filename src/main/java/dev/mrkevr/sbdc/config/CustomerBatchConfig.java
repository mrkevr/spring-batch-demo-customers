package dev.mrkevr.sbdc.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import dev.mrkevr.sbdc.entity.Customer;
import dev.mrkevr.sbdc.exception.skippolicy.CustomerExceptionSkipPolicy;
import dev.mrkevr.sbdc.item.processor.CustomerProcessor;
import dev.mrkevr.sbdc.listener.StepSkipListener;
import dev.mrkevr.sbdc.model.CustomerCsvDto;
import dev.mrkevr.sbdc.repository.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerBatchConfig {

	CustomerRepository customerRepository;
	CustomerProcessor customerProcessor;
	
	StepSkipListener stepSkipListener;
	JobExecutionListener jobExecutionListener;
	
	//Configurations how each line will be parsed and mapped to different values
	private LineMapper<CustomerCsvDto> lineMapper() {
		DefaultLineMapper<CustomerCsvDto> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("firstName", "lastName", "phone", "email", "balance");

		BeanWrapperFieldSetMapper<CustomerCsvDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(CustomerCsvDto.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
	
	@Bean
	FlatFileItemReader<CustomerCsvDto> reader() {
		//Create reader instance
		FlatFileItemReader<CustomerCsvDto> itemReader = new FlatFileItemReader<>();
		itemReader.setName("csvReader");
		
		//Set input file location
		itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
		
		//Set number of lines to skips. Use it if file has header rows.
		itemReader.setLinesToSkip(1);
		
		//Configure how each line will be parsed and mapped to different values
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}

	@Bean
	 RepositoryItemWriter<Customer> writer() {
		RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
		writer.setRepository(customerRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	
	/*
	 * A Step has three main component :
	 * - Reader
	 * - Processor
	 * - Writer
	 */
	@Bean
	Step step1(
			JobRepository jobRepository, 
			PlatformTransactionManager transactionManager) {
		
		
		return new StepBuilder("csv-step", jobRepository)
				.<CustomerCsvDto, Customer>chunk(10, transactionManager)
				.reader(reader())
				.processor(customerProcessor)
				.writer(writer())
				.taskExecutor(taskExecutor())
				.listener(jobExecutionListener)
				.listener(stepSkipListener)
				.faultTolerant()
				.skipPolicy(this.customerSkipPolicy())
				.build();
	}

	@Bean
	Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("importCustomers", jobRepository)
				.flow(step1(jobRepository, transactionManager))
				.end()
				.build();
	}

	@Bean
	TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(10);
		return asyncTaskExecutor;
	}
	
	@Bean
	SkipPolicy customerSkipPolicy() {
		return new CustomerExceptionSkipPolicy();
	}
	
	
}
