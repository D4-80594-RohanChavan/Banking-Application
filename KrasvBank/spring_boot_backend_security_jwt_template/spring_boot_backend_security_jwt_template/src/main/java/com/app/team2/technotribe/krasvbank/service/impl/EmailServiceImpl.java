//package com.app.team2.technotribe.krasvbank.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
////import javax.mail.MessagingException;
////import javax.mail.internet.MimeMessage;
////import com.app.team2.technotribe.krasvbank.dto.EmailDetails;
//
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//	
//	@Autowired
//	private JavaMailSender javaMailSender;
//	
//	@Value("${spring.mail.username}")
//	private String senderEmail;
//	
//	@Autowired(required = false)
//	@Override
//	public void sendEmailAlert(EmailDetails emailDetails) {
//		// TODO Auto-generated method stub
////		try {
////			SimpleMailMessage mailMessage=new SimpleMailMessage();
////			mailMessage.setFrom(senderEmail);
////			mailMessage.setTo(emailDetails.getRecipient());
////			mailMessage.setText(emailDetails.getMessageBody());
////			mailMessage.setSubject(emailDetails.getSubject());
////			
////			
////			javaMailSender.send(mailMessage);
////			System.out.println("Mail send successfully");
////			
////		}catch(MessagingException e) {
////			throw new RuntimeException(e);
////		}
//	}
//
//}
