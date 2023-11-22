package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="The name field is required")
	@Size(min=8, max=150, message="The length must be between 8 and 150 characters")
	private String name;
	
	@NotEmpty(message="The description field is required")
	@Size(min=20, max=250, message="The length must be between 8 and 3000 characters")
	private String description;
	
	@NotNull(message="The registration end date field is required")
	@Future(message="Invalid date")
	private LocalDateTime registrationEndDate;
	
	@NotNull(message="The start date time field is required")
	@Future(message="Invalid date")
	private LocalDateTime startDateTime;
	
	@NotNull(message="The end date time field is required")
	@Future(message="Invalid date")
	private LocalDateTime endDateTime;
	
	@NotEmpty(message="The place field is required")
	@Size(min=8, max=150, message="The length must be between 8 and 150 characters")
	private String place;
	
	@NotNull(message="The capacity field is required")
	@Min(value=1, message="Capacity cannot be less than 1")
	private Integer capacity;
	
	@NotNull(message="The capacity field is required")
	@DecimalMin(value="0.0", message="Price cannot be negative")
	private Double price;
	
	@NotNull(message="The creation user ID field is required")	
	private UUID creationUser;
	
	public EventRequestDTO() {

	}

	public EventRequestDTO(String name, String description, LocalDateTime registrationEndDate,
			LocalDateTime startDateTime, LocalDateTime endDateTime, String place, Integer capacity, Double price,
			UUID creationUser) {
		super();
		this.name = name;
		this.description = description;
		this.registrationEndDate = registrationEndDate;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.place = place;
		this.capacity = capacity;
		this.price = price;
		this.creationUser = creationUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getRegistrationEndDate() {
		return registrationEndDate;
	}

	public void setRegistrationEndDate(LocalDateTime registrationEndDate) {
		this.registrationEndDate = registrationEndDate;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public UUID getCreationUser() {
		return creationUser;
	}

	public void setCreationUser(UUID creationUser) {
		this.creationUser = creationUser;
	}

}
