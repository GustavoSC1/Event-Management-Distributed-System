package com.gustavo.eventservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventRequestDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public interface EventView {
		public static interface EventPost {}
		public static interface EventPut {}
	}
	
	@NotEmpty(message="The name field is required", groups = {EventView.EventPost.class, EventView.EventPut.class})
	@Size(min=8, max=150, message="The length must be between 8 and 150 characters", groups = {EventView.EventPost.class, EventView.EventPut.class})
	@JsonView({EventView.EventPost.class, EventView.EventPut.class})
	private String name;
	
	@NotEmpty(message="The description field is required", groups = {EventView.EventPost.class, EventView.EventPut.class})
	@Size(min=20, max=250, message="The length must be between 8 and 3000 characters", groups = {EventView.EventPost.class, EventView.EventPut.class})
	@JsonView({EventView.EventPost.class, EventView.EventPut.class})
	private String description;
	
	@NotEmpty(message="The imageUrl field is required", groups = {EventView.EventPost.class, EventView.EventPut.class})
	@JsonView({EventView.EventPost.class, EventView.EventPut.class})
	private String imageUrl;
	
	@NotNull(message="The registration start date field is required", groups = EventView.EventPost.class)
	@Future(message="Invalid date", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private LocalDateTime registrationStartDate;
	
	@NotNull(message="The registration end date field is required", groups = EventView.EventPost.class)
	@Future(message="Invalid date", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private LocalDateTime registrationEndDate;
	
	@NotNull(message="The start date time field is required", groups = EventView.EventPost.class)
	@Future(message="Invalid date", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private LocalDateTime startDateTime;
	
	@NotNull(message="The end date time field is required", groups = EventView.EventPost.class)
	@Future(message="Invalid date", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private LocalDateTime endDateTime;
	
	@NotEmpty(message="The place field is required", groups = EventView.EventPost.class)
	@Size(min=8, max=150, message="The length must be between 8 and 150 characters", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private String place;
	
	@NotNull(message="The capacity field is required", groups = EventView.EventPost.class)
	@Min(value=1, message="Capacity cannot be less than 1", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private Integer capacity;
	
	@NotNull(message="The capacity field is required", groups = EventView.EventPost.class)
	@DecimalMin(value="0.0", message="Price cannot be negative", groups = EventView.EventPost.class)
	@JsonView(EventView.EventPost.class)
	private Double price;
	
	@NotNull(message="The Creation User ID field is required", groups = EventView.EventPost.class)	
	@JsonView(EventView.EventPost.class)
	private UUID creationUser;
	
	public EventRequestDTO() {

	}

	public EventRequestDTO(String name, String description, String imageUrl, LocalDateTime registrationStartDate, 
			LocalDateTime registrationEndDate, LocalDateTime startDateTime, LocalDateTime endDateTime, 
			String place, Integer capacity, Double price, UUID creationUser) {
		this.name = name;
		this.description = description;
		this.registrationStartDate = registrationStartDate;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public UUID getCreationUser() {
		return creationUser;
	}

	public void setCreationUser(UUID creationUser) {
		this.creationUser = creationUser;
	}

	@Override
	public String toString() {
		return "EventRequestDTO [name=" + name + ", description=" + description + ", imageUrl=" + imageUrl
				+ ", registrationStartDate=" + registrationStartDate + ", registrationEndDate=" + registrationEndDate
				+ ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime + ", place=" + place
				+ ", capacity=" + capacity + ", price=" + price + ", creationUser=" + creationUser + "]";
	}

}
