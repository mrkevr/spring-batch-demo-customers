package dev.mrkevr.sbdc.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import dev.mrkevr.sbdc.entity.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StepSkipListener implements SkipListener<Customer, Number> {

	@Override
	public void onSkipInRead(Throwable t) {
		log.info("Failure while reading : {}", t.getMessage());
	}

	@Override
	public void onSkipInProcess(Customer item, Throwable t) {
		log.info("Failure while processing : {}", t.getMessage());
	}

	@Override
	public void onSkipInWrite(Number item, Throwable t) {
		log.info("Failure while writing : {} on {}", t.getMessage(), item);
	}
}
