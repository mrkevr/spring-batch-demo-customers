package dev.mrkevr.sbdc.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.mrkevr.sbdc.entity.Customer;
import dev.mrkevr.sbdc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerRepository customerRepository;

	@GetMapping("/{id}")
	ResponseEntity<?> getById(@PathVariable Integer id) {
		Optional<Customer> optional = customerRepository.findById(id);
		return optional.isPresent() ? 
				ResponseEntity.ok(optional.get()) : 
				ResponseEntity.notFound().build();
	}
}
