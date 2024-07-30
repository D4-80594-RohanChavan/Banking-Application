package com.axis.team2.technotribe.krasvbank.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "transactions")
public class Transaction implements Serializable {

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
    private LocalDateTime createdAt;


}
