package com.gustavo.eventservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.gustavo.eventservice.entities.enums.EventStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_EVENT")
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private UUID eventId;
    
    @Column(nullable = false, length = 150)
    private String name;
    
    @Column(nullable = false, length= 3000)
    private String description;
    
    @Column
	private String imageUrl;
    
    @Column(nullable = false)
    private LocalDateTime creationDate;
    
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;
    
    @Column(nullable = false)
    private LocalDateTime registrationStartDate;
    
    @Column(nullable = false)
    private LocalDateTime registrationEndDate;
    
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    
    @Column(nullable = false, length = 150)
    private String place;
    
    @Column(nullable = false)
    private Integer capacity;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    
    @ManyToOne(optional = false)
	@JoinColumn(name = "creationUser_id")
    private User creationUser;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_STAFF",
			joinColumns = @JoinColumn(name = "event_id"),
			inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> staffUsers = new HashSet<>();
    
    @OneToMany(mappedBy="event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Ticket> tickets = new HashSet<>();
    
	public Event() {		

	}
	
	public Event(UUID eventId, String name, String description, String imageUrl, LocalDateTime creationDate,
			LocalDateTime lastUpdateDate, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate,
			LocalDateTime startDateTime, LocalDateTime endDateTime, String place, Integer capacity, Double price,
			EventStatus eventStatus, User creationUser) {
		this.eventId = eventId;
		this.name = name;
		this.description = description;
		this.imageUrl = imageUrl;
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
		this.creationUser = creationUser;
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

	public User getCreationUser() {
		return creationUser;
	}

	public void setCreationUser(User creationUser) {
		this.creationUser = creationUser;
	}

	public Set<User> getStaffUsers() {
		return staffUsers;
	}

	public void setStaffUsers(Set<User> staffUsers) {
		this.staffUsers = staffUsers;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, creationDate, creationUser, description, endDateTime, eventId, eventStatus,
				imageUrl, lastUpdateDate, name, place, price, registrationEndDate, registrationStartDate,
				startDateTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return Objects.equals(capacity, other.capacity) && Objects.equals(creationDate, other.creationDate)
				&& Objects.equals(creationUser, other.creationUser) && Objects.equals(description, other.description)
				&& Objects.equals(endDateTime, other.endDateTime) && Objects.equals(eventId, other.eventId)
				&& eventStatus == other.eventStatus && Objects.equals(imageUrl, other.imageUrl)
				&& Objects.equals(lastUpdateDate, other.lastUpdateDate) && Objects.equals(name, other.name)
				&& Objects.equals(place, other.place) && Objects.equals(price, other.price)
				&& Objects.equals(registrationEndDate, other.registrationEndDate)
				&& Objects.equals(registrationStartDate, other.registrationStartDate)
				&& Objects.equals(startDateTime, other.startDateTime);
	}

}
