package dev.mrkevr.sbdc.processor;

import org.springframework.batch.item.ItemProcessor;

import dev.mrkevr.sbdc.entity.Customer;


public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

	@Override
	public Customer process(Customer customer) throws Exception {
		
		return customer;
	}

}
