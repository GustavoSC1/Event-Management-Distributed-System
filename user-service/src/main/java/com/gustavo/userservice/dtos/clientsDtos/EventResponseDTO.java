package com.gustavo.userservice.dtos.clientsDtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.gustavo.userservice.entities.enums.EventStatus;

public class EventResponseDTO extends RepresentationModel<EventResponseDTO> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID eventId;	
	private String name;	
	private String description;	
	private String imageUrl;
	private LocalDateTime creationDate;	
	private LocalDateTime lastUpdateDate;
	private LocalDateTime registrationStartDate;
	private LocalDateTime registrationEndDate;	
	private LocalDateTime startDateTime;	
	private LocalDateTime endDateTime;	
	private String place;	
	private Integer capacity;	
	private Double price;
	private EventStatus eventStatus;
	
	public EventResponseDTO() {

	}
	
	public EventResponseDTO(UUID eventId, String name, String description, String imageUrl, LocalDateTime creationDate,
			LocalDateTime lastUpdateDate, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate,
			LocalDateTime startDateTime, LocalDateTime endDateTime, String place, Integer capacity, Double price,
			EventStatus eventStatus) {
		this.eventId = eventId;
		this.name = name;
		this.description = description;
		this.creationDate = creationDate;
		this.lastUpdateDate = lastUpdateDate;
		this.registrationStartDate = registrationStartDate;
		this.registrationEndDate = registrationEndDate;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.place = place;
		this.capacity = capacity;
		this.price = price;
		this.eventStatus = eventStatus;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public LocalDateTime getRegistrationStartDate() {
		return registrationStartDate;
	}

	public void setRegistrationStartDate(LocalDateTime registrationStartDate) {
		this.registrationStartDate = registrationStartDate;
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

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

}
