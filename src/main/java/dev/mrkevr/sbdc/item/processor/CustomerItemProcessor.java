package dev.mrkevr.sbdc.item.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import dev.mrkevr.sbdc.entity.Customer;
import dev.mrkevr.sbdc.model.CustomerCsvDto;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerItemProcessor implements ItemProcessor<CustomerCsvDto, Customer> {
	
	private final Validator validator;
	
	@Override
	public Customer process(CustomerCsvDto dto) throws Exception {
		
//		Set<ConstraintViolation<CustomerCsvDto>> violations = validator.validate(dto);
//		if(!violations.isEmpty()) {
//			throw new IllegalArgumentException("Invalid argument!!!");
//		}
		
		return Customer.builder()
				.name(dto.getFirstName()+" "+dto.getLastName())
				.phone(dto.getPhone())
				.email(dto.getEmail())
				.balance(dto.getBalance())
				.build();
	}
	
}
