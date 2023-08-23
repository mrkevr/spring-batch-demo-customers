package dev.mrkevr.sbdc.item.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import dev.mrkevr.sbdc.model.CustomerCsvDto;

@Component
public class CustomerItemReader extends FlatFileItemReader<CustomerCsvDto> {
	
	public CustomerItemReader() {
		super();
		super.setName("customerCsvReader");
		super.setResource(new FileSystemResource("src/main/resources/customers.csv"));
		super.setLinesToSkip(1);
		
		
		
		
		
		setLineMapper(this.lineMapper());
	}
	
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
}
