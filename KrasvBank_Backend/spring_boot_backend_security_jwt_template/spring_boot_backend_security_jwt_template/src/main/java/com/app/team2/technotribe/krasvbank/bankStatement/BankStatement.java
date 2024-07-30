package com.app.team2.technotribe.krasvbank.bankStatement;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.EmailDetails;
import com.app.team2.technotribe.krasvbank.entity.Transaction;
import com.app.team2.technotribe.krasvbank.entity.User;
import com.app.team2.technotribe.krasvbank.repository.TransactionRepository;
import com.app.team2.technotribe.krasvbank.repository.UserRepository;
import com.app.team2.technotribe.krasvbank.service.impl.EmailService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankStatement {

	private static final Logger log = Logger.getLogger(BankStatement.class.getName());
	private static final String FILE = "D:\\MyStatement.pdf";
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	EmailService emailService;
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
	    LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
	    LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
	    List<Transaction> transactionList = transactionRepository.findAll().stream()
	            .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
	            .filter(transaction -> !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
	            .toList();


//		User user = userRepository.findByAccountNumber(accountNumber);
//		String customerName = user.getName();
//
//		Rectangle statementSize = new Rectangle(PageSize.A4);
//		Document document = new Document(statementSize);
//		log.info("Setting size of document");
//		try {
//			OutputStream outputStream = new FileOutputStream(FILE);
//			PdfWriter.getInstance(document, outputStream);
//			document.open();
//
//			PdfPTable bankInfoTable = new PdfPTable(1);
//			PdfPCell bankName = new PdfPCell(new Phrase("Krasv Bank"));
//			bankName.setBorder(0);
//			bankName.setBackgroundColor(BaseColor.RED);
//			bankName.setPadding(20f);
//
//			PdfPCell bankAddress = new PdfPCell(new Phrase("72, Maharashtra"));
//			bankAddress.setBorder(0);
//			bankInfoTable.addCell(bankName);
//			bankInfoTable.addCell(bankAddress);
//
//			PdfPTable statementInfo = new PdfPTable(2);
//			PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
//			customerInfo.setBorder(0);
//			PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
//			statement.setBorder(0);
//			PdfPCell stopDate = new PdfPCell(new Phrase("End Date" + endDate));
//			stopDate.setBorder(0);
//			PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
//			name.setBorder(0);
//			PdfPCell space = new PdfPCell();
//			space.setBorder(0);
//			PdfPCell address = new PdfPCell(new Phrase("Customer Address " + user.getAddress()));
//			space.setBorder(0);
//
//			PdfPTable transactionsTable = new PdfPTable(4);
//			PdfPCell date = new PdfPCell(new Phrase("DATE"));
//			date.setBackgroundColor(BaseColor.RED);
//			date.setBorder(0);
//			PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
//			transactionType.setBackgroundColor(BaseColor.RED);
//			transactionType.setBorder(0);
//			PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
//			transactionAmount.setBackgroundColor(BaseColor.RED);
//			transactionAmount.setBorder(0);
//			PdfPCell status = new PdfPCell(new Phrase("STATUS"));
//			status.setBackgroundColor(BaseColor.RED);
//			status.setBorder(0);
//
//			transactionsTable.addCell(date);
//			transactionsTable.addCell(transactionType);
//			transactionsTable.addCell(transactionAmount);
//			transactionsTable.addCell(status);
//
//			transactionList.forEach(transaction -> {
//				transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
//				transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
//				transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
//				transactionsTable.addCell(new Phrase(transaction.getStatus()));
//			});
//
//			statementInfo.addCell(customerInfo);
//			statementInfo.addCell(statement);
//			statementInfo.addCell(endDate);
//			statementInfo.addCell(name);
//			statementInfo.addCell(space);
//			statementInfo.addCell(address);
//
//			document.add(bankInfoTable);
//			document.add(statementInfo);
//			document.add(transactionsTable);
//
//			document.close();
//			
//			EmailDetails emailDetails=EmailDetails.builder()
//					.recipient(user.getEmail())
//					.subject("ACCOUNT STATEMENT")
//					.messageBody("Kindly find your requested account statement attached!")
//					.attachment(FILE)
//					.build();
//			
//			emailService.sendEmailWithAttachment(emailDetails);
//
//		} catch (FileNotFoundException | DocumentException e) {
//			log.log(Level.SEVERE, "Error creating PDF", e);
//		}

		return transactionList;
	}

}
