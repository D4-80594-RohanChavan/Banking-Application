package com.app.team2.technotribe.krasvbank.dto;

import com.app.team2.technotribe.krasvbank.entity.UserRole;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter // Lombok's @Getter generates getters for all fields
@JsonSerialize
@JsonDeserialize(builder = UserDto.UserDtoBuilder.class)
@Builder // Lombok's @Builder generates a builder class
public class UserDto {

	private final Long id;
	private final String name;
	private final String gender;
	private final String address;
	private final String stateOfOrigin;
	private final String accountNumber;
	private BigDecimal accountBalance; // Removed from here
	private final String email;
	private final String phoneNumber;
	private final String alternativePhoneNumber;
	private final String status;
	private final UserRole role;
	private final LocalDateTime createdAt;
	private final LocalDateTime modifiedAt;
	private final String aadharCard;
	private final String panCard;

	// Static method to get a builder instance
	public static UserDtoBuilder builder() {
		return new UserDtoBuilder();
	}

	// Inner Builder class with Lombok's @Builder
	public static class UserDtoBuilder {
		private Long id;
		private String name;
		private String gender;
		private String address;
		private String stateOfOrigin;
		private String accountNumber;
		private BigDecimal accountBalance; // Moved here
		private String email;
		private String phoneNumber;
		private String alternativePhoneNumber;
		private String status;
		private UserRole role;
		private LocalDateTime createdAt;
		private LocalDateTime modifiedAt;
		private String aadharCard;
		private String panCard;

		// Default value for accountBalance can be set here if needed

		// Setters for fields in the builder
		public UserDtoBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public UserDtoBuilder name(String name) {
			this.name = name;
			return this;
		}

		public UserDtoBuilder gender(String gender) {
			this.gender = gender;
			return this;
		}

		public UserDtoBuilder address(String address) {
			this.address = address;
			return this;
		}

		public UserDtoBuilder stateOfOrigin(String stateOfOrigin) {
			this.stateOfOrigin = stateOfOrigin;
			return this;
		}

		public UserDtoBuilder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public UserDtoBuilder accountBalance(BigDecimal accountBalance) {
			this.accountBalance = accountBalance;
			return this;
		}

		public UserDtoBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserDtoBuilder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public UserDtoBuilder alternativePhoneNumber(String alternativePhoneNumber) {
			this.alternativePhoneNumber = alternativePhoneNumber;
			return this;
		}

		public UserDtoBuilder status(String status) {
			this.status = status;
			return this;
		}

		public UserDtoBuilder role(UserRole role) {
			this.role = role;
			return this;
		}

		public UserDtoBuilder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public UserDtoBuilder modifiedAt(LocalDateTime modifiedAt) {
			this.modifiedAt = modifiedAt;
			return this;
		}

		public UserDtoBuilder aadharCard(String aadharCard) { // Added method
			this.aadharCard = aadharCard;
			return this;
		}

		public UserDtoBuilder panCard(String panCard) { // Added method
			this.panCard = panCard;
			return this;
		}

		// Build method to instantiate UserDto
		public UserDto build() {
			return new UserDto(this);
		}
	}

	// Private constructor to enforce immutability
	private UserDto(UserDtoBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.gender = builder.gender;
		this.address = builder.address;
		this.stateOfOrigin = builder.stateOfOrigin;
		this.accountNumber = builder.accountNumber;
		this.accountBalance = builder.accountBalance; // Updated to use builder's value
		this.email = builder.email;
		this.phoneNumber = builder.phoneNumber;
		this.alternativePhoneNumber = builder.alternativePhoneNumber;
		this.status = builder.status;
		this.role = builder.role;
		this.createdAt = builder.createdAt;
		this.modifiedAt = builder.modifiedAt;
        this.aadharCard = builder.aadharCard; // Added assignment
        this.panCard = builder.panCard; // Added assignment
	}
}
