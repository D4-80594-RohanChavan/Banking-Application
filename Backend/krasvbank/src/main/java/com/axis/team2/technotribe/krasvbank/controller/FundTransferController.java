package com.axis.team2.technotribe.krasvbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.axis.team2.technotribe.krasvbank.dto.FundTransferRequest;
import com.axis.team2.technotribe.krasvbank.service.FundTransferService;

@RestController
@RequestMapping("/fundTransfer")
public class FundTransferController {

    @Autowired
    private FundTransferService fundTransferService;

    @PostMapping
    public String transferFunds(@RequestBody FundTransferRequest request) {
        return fundTransferService.transferFunds(request);
    }
}