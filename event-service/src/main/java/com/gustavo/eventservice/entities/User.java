package com.gustavo.eventservice.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USER")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    private UUID userId;
    
	@Column(nullable = false, length = 150)
	private String firstName;
	
	@Column(nullable = false, length = 150)
	private String lastName;
	
	@Column(length = 20)
	private String phone;
    
    @Column(length = 20)
    private String cpf;
    
    @Column
    private String imageUrl;
    
    @Column(nullable = false, unique = true, length = 50)
	private String username;
    
    @Column(nullable = false, unique = true, length = 50)
    private String email;
        
    @OneToMany(mappedBy = "creationUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Event> createdEvents = new HashSet<>();
    
    @ManyToMany(mappedBy = "staffUsers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Event> staffEvents = new HashSet<>();
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Ticket> tickets = new HashSet<>();
    
	public User() {

	}

	public User(UUID userId, String firstName, String lastName, String phone, String cpf, String imageUrl,
			String username, String email) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.username = username;
		this.email = email;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Event> getCreatedEvents() {
		return createdEvents;
	}

	public void setCreatedEvents(Set<Event> createdEvents) {
		this.createdEvents = createdEvents;
	}

	public Set<Event> getStaffEvents() {
		return staffEvents;
	}

	public void setStaffEvents(Set<Event> staffEvents) {
		this.staffEvents = staffEvents;
	}

	public Set<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, createdEvents, email, firstName, imageUrl, lastName, phone, staffEvents, tickets,
				userId, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(createdEvents, other.createdEvents)
				&& Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(imageUrl, other.imageUrl) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(phone, other.phone) && Objects.equals(staffEvents, other.staffEvents)
				&& Objects.equals(tickets, other.tickets) && Objects.equals(userId, other.userId)
				&& Objects.equals(username, other.username);
	}

}
