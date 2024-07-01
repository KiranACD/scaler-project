package com.example.emailservice.consumers;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.emailservice.dtos.EmailEventDTO;
import com.example.emailservice.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailEventConsumer {
    
    private ObjectMapper objectMapper;

    public EmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleEmailEvent(String message) throws JsonMappingException, JsonProcessingException {
    
        EmailEventDTO emailEvent = objectMapper.readValue(message, EmailEventDTO.class);

        String to = emailEvent.getTo();
        String from = emailEvent.getFrom();
        String subject = emailEvent.getSubject();
        String body = emailEvent.getBody();

        Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, "zxinnswlsqnaqmxm");
			}
		};
		Session session = Session.getInstance(props, auth);
		
		EmailUtil.sendEmail(session, to, from, subject, body);
    }
}
