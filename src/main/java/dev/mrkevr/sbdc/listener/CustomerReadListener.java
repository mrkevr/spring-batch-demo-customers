package dev.mrkevr.sbdc.listener;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import dev.mrkevr.sbdc.model.CustomerCsvDto;


@Component
public class CustomerReadListener implements ItemReadListener<CustomerCsvDto> {

	@Override
	public void beforeRead() {
		
	}

	@Override
	public void afterRead(CustomerCsvDto item) {
		
	}

	@Override
	public void onReadError(Exception ex) {
		
	}
	
}
