package com.axis.team2.technotribe.krasvbank.dto;

import lombok.Data;

@Data
public class FundTransferRequest {
    private String fromAccount;
    private String toAccount;
    private Double amount;
}

