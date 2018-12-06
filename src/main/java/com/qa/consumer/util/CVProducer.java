package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.CV;

@Component
public class CVProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	public String produce(CV cv) {
		jmsTemplate.convertAndSend("${outgoingQueue}", cv);
		return queuedMessage;
	}

	public String produce(Iterable<CV> cvs) {
		jmsTemplate.convertAndSend("${outgoingQueue}", cvs);
		return queuedMessage;
	}

	@Value("${SuccessfullyQueued.message}")
	private String queuedMessage;

}