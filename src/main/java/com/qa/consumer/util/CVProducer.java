package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.CV;

@Component
public class CVProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	public String produce(CV cv) {
		jmsTemplate.convertAndSend(Constants.OUTGOING_QUEUE_NAME, cv);
		return Constants.CV_QUEUED_MESSAGE;
	}

	public String produce(Iterable<CV> cvs) {
		jmsTemplate.convertAndSend(Constants.OUTGOING_QUEUE_NAME, cvs);
		return Constants.CV_QUEUED_MESSAGE;
	}

}