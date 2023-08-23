package dev.mrkevr.sbdc.item.reader;

import java.io.File;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import dev.mrkevr.sbdc.model.CustomerCsvDto;

@Component
@StepScope
public class CustomerItemReader extends FlatFileItemReader<CustomerCsvDto> {
	
	public CustomerItemReader(
			@Value("#{jobParameters[fullPathFileName]}") String fullPathFileName) {
		
		super();
		super.setName("customerCsvItemReader");
		super.setResource(new FileSystemResource(new File(fullPathFileName)));
		super.setLinesToSkip(1);
		setLineMapper(this.lineMapper());
	}
	
	// Configure how each line will be parsed and mapped to different values
	private LineMapper<CustomerCsvDto> lineMapper() {

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("firstName", "lastName", "phone", "email", "balance");
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);

		BeanWrapperFieldSetMapper<CustomerCsvDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(CustomerCsvDto.class);

		DefaultLineMapper<CustomerCsvDto> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
}
