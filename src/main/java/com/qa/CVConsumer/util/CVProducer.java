package com.qa.CVConsumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.qa.CVConsumer.persistence.domain.CV;

@Component
public class CVProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	public String produce(CV cv) {
		jmsTemplate.convertAndSend("${outgoingQueue}", cv);
		return "${SuccessfullyQueued.message}";
	}

	public String produce(Iterable<CV> cvs) {
		jmsTemplate.convertAndSend("${outgoingQueue}", cvs);
		return "${SuccessfullyQueued.message}";
	}

}