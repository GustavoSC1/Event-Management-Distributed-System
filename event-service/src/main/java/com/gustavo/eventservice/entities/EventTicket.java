package com.gustavo.eventservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_EVENT_TICKET")
public class EventTicket implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID ticketId;
    
    @Column(nullable = false)
    public LocalDateTime registrationDate;
        
    @Column(nullable = false)
    public Boolean isPaid;    
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
    private Event event;
    
	public EventTicket() {
		
	}
	
	public EventTicket(UUID ticketId, Boolean isPaid, LocalDateTime registrationDate) {
		this.ticketId = ticketId;
		this.isPaid = isPaid;
		this.registrationDate = registrationDate;
	}
	
	public UUID getTicketId() {
		return ticketId;
	}

	public void setTicketId(UUID ticketId) {
		this.ticketId = ticketId;
	}

	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isPaid, registrationDate, ticketId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventTicket other = (EventTicket) obj;
		return Objects.equals(isPaid, other.isPaid) && Objects.equals(registrationDate, other.registrationDate)
				&& Objects.equals(ticketId, other.ticketId);
	}

}
