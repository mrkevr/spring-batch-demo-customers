package dev.mrkevr.sbdc.item.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import dev.mrkevr.sbdc.entity.Customer;
import dev.mrkevr.sbdc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerItemWriter implements ItemWriter<Customer> {

	private final CustomerRepository customerRepo;

	@Override
	public void write(Chunk<? extends Customer> chunk) throws Exception {
		customerRepo.saveAll(chunk.getItems());
	}

}
