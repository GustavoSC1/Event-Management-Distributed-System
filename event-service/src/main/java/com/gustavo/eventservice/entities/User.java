package com.gustavo.eventservice.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import com.gustavo.eventservice.entities.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String name;
    
    @Column(length = 20)
    private String cpf;
    
    @Column
    private String imageUrl;
    
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    
    @OneToMany(mappedBy = "creationUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Event> createdEvents = new HashSet<>();
    
    @ManyToMany(mappedBy = "staffUsers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Event> staffEvents = new HashSet<>();
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Ticket> tickets = new HashSet<>();
    
	public User() {

	}

	public User(UUID userId, String name, String cpf, String imageUrl, String email, UserStatus userStatus) {
		this.userId = userId;
		this.name = name;
		this.cpf = cpf;
		this.imageUrl = imageUrl;
		this.email = email;
		this.userStatus = userStatus;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
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
		return Objects.hash(cpf, email, imageUrl, name, userId, userStatus);
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
		return Objects.equals(cpf, other.cpf) && Objects.equals(email, other.email)
				&& Objects.equals(imageUrl, other.imageUrl) && Objects.equals(name, other.name)
				&& Objects.equals(userId, other.userId) && userStatus == other.userStatus;
	}

}
