package com.app.team2.technotribe.krasvbank.bankStatementPdfGenerator;

import java.io.File;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.app.team2.technotribe.krasvbank.dto.EmailDetailsDto;

import lombok.extern.slf4j.Slf4j;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import com.app.team2.technotribe.krasvbank.dto.EmailDetails;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Autowired(required = false)
//	@Override
//	public void sendEmailAlert(EmailDetailsDto emailDetails) {
//		// TODO Auto-generated method stub
//		try {
//			SimpleMailMessage mailMessage = new SimpleMailMessage();
//			mailMessage.setFrom(senderEmail);
//			mailMessage.setTo(emailDetails.getRecipient());
//			mailMessage.setText(emailDetails.getMessageBody());
//			mailMessage.setSubject(emailDetails.getSubject());
//
//			javaMailSender.send(mailMessage);
//			System.out.println("Mail send successfully");
//
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public void sendEmailWithAttachment(EmailDetailsDto emailDetails) {
		MimeMessage mimeMessage= javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper =new MimeMessageHelper(mimeMessage,true);
			mimeMessageHelper.setFrom(senderEmail);
			mimeMessageHelper.setTo(emailDetails.getRecipient());
			mimeMessageHelper.setText(emailDetails.getMessageBody());
			mimeMessageHelper.setSubject(emailDetails.getSubject());
			
			FileSystemResource file=new FileSystemResource(new File(emailDetails.getAttachment()));
			mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
			javaMailSender.send(mimeMessage);
			
			log.info(file.getFilename()+"has been sent to user with email "+emailDetails.getRecipient());
			
			
		}catch(MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}

}
