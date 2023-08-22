package dev.mrkevr.sbdc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customers")
public class Customer extends GenericEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String phone;

	private String email;

	private double balance;

	@Override
	public String toString() {
		return "Customers [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", balance="
				+ balance + ", getDateCreated()=" + getDateCreated() + ", getLastUpdated()=" + getLastUpdated() + "]";
	}
}
