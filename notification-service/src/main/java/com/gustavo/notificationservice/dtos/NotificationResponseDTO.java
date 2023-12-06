package com.gustavo.notificationservice.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.gustavo.notificationservice.entities.enums.NotificationStatus;

public class NotificationResponseDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private UUID notificationId;
	private UUID userId;
	private String title;
	private String message;
	private LocalDateTime creationDate;
	private NotificationStatus notificationStatus;
	
	public NotificationResponseDTO() {

	}

	public NotificationResponseDTO(UUID notificationId, UUID userId, String title, String message,
			LocalDateTime creationDate, NotificationStatus notificationStatus) {
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

}
