package com.app.team2.technotribe.krasvbank.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
//	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "transaction_type")
	private String transactionType;
	
	@Column(name = "amount", precision = 19, scale = 2)
	private BigDecimal amount;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "status")
	private String status;
	@CreationTimestamp
	private LocalDate createdAt;
	@UpdateTimestamp
	private LocalDate modifiedAt;


}
