package com.gustavo.notificationservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import com.gustavo.notificationservice.entities.enums.NotificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_NOTIFICATION")
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationId;
    
    @Column(nullable = false)
    private UUID userId;
    
    @Column(nullable = false, length = 150)
    private String title;
    
    @Column(nullable = false)
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime creationDate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;
    
	public Notification() {
		
	}
	
	public Notification(UUID notificationId, UUID userId, String title, String message, LocalDateTime creationDate,
			NotificationStatus notificationStatus) {
		this.notificationId = notificationId;
		this.userId = userId;
		this.title = title;
		this.message = message;
		this.creationDate = creationDate;
		this.notificationStatus = notificationStatus;
	}

	public UUID getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(UUID notificationId) {
		this.notificationId = notificationId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public NotificationStatus getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(NotificationStatus notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creationDate, message, notificationId, notificationStatus, title, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notification other = (Notification) obj;
		return Objects.equals(creationDate, other.creationDate) && Objects.equals(message, other.message)
				&& Objects.equals(notificationId, other.notificationId)
				&& notificationStatus == other.notificationStatus && Objects.equals(title, other.title)
				&& Objects.equals(userId, other.userId);
	}
	
}
