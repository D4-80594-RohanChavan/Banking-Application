package com.axis.team2.technotribe.krasvbank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;



import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	private String email;
	@Column(length = 300, nullable = false)
	private String password;
	private String phoneNumber;
	private String alternativePhoneNumber;
	private String status;

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private UserRole role;
	@CreationTimestamp
	private LocalDateTime createdAt;
	@UpdateTimestamp
	private LocalDateTime modifiedAt;

}
