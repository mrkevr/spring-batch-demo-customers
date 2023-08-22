package dev.mrkevr.sbdc.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCsvDto {

	private String firstName;

	private String lastName;

	private String phone;

	private String email;

	@Min(value = 1000, message = "Balance should be between 1000 and 4000")
	@Max(value = 4000, message = "Balance should be between 1000 and 4000")
	private double balance;
}
