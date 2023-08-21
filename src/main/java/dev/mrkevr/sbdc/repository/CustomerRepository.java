package dev.mrkevr.sbdc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.mrkevr.sbdc.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
