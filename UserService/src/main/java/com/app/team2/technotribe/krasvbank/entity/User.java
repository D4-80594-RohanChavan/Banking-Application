package com.app.team2.technotribe.krasvbank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String gender;
	private String address;
	private String stateOfOrigin;
	private String accountNumber;
	private BigDecimal accountBalance;
	@Column(length = 30, unique = true)
	@Email(message = "Email should be valid")
	private String email;
	@Column(length = 300, nullable = false)
	@Size(min = 4, message = "Password should have at least 4 characters")
	private String password;
	@Pattern(regexp = "[6-9]\\d{9}", message = "Phone number should be 10 digits and start with 6, 7, 8, or 9")
	@Column(nullable = false)
	private String phoneNumber;
	@Pattern(regexp = "[6-9]\\d{9}", message = "Phone number should be 10 digits and start with 6, 7, 8, or 9")
	@Column(nullable = false)
//	private String a;
	private String alternativePhoneNumber;
	private String status;
	private String aadharCard;
	private String panCard;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private UserRole role;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime modifiedAt;

}
