package dev.mrkevr.sbdc.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class GenericEntity {

	@Column(name = "date_created")
	private LocalDate dateCreated;

	@Column(name = "last_updated")
	private LocalDate lastUpdated;

	@PostPersist
	public void postPersist() {
		this.dateCreated = LocalDate.now();
		this.lastUpdated = LocalDate.now();
	}

	@PostUpdate
	public void postUpdate() {
		this.lastUpdated = LocalDate.now();
	}
}
