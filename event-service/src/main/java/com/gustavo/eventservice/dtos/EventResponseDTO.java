package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class EventResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID eventId;	
	private String name;	
	private String description;	
	private LocalDateTime creationDate;	
	private LocalDateTime lastUpdateDate;
	private LocalDateTime registrationEndDate;	
	private LocalDateTime startDateTime;	
	private LocalDateTime endDateTime;	
	private String place;	
	private Integer capacity;	
	private Double price;
	
	public EventResponseDTO() {

	}

	public EventResponseDTO(UUID eventId, String name, String description, LocalDateTime creationDate,
			LocalDateTime lastUpdateDate, LocalDateTime registrationEndDate, LocalDateTime startDateTime,
			LocalDateTime endDateTime, String place, Integer capacity, Double price) {
		this.eventId = eventId;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.lastUpdateDate = lastUpdateDate;
		this.registrationEndDate = registrationEndDate;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.place = place;
		this.capacity = capacity;
		this.price = price;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
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

}
