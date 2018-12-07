package com.qa.consumer.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.consumer.persistence.domain.Request;
import com.qa.consumer.service.CVService;

@Component
public class RequestReceiver {

	@Autowired
	private CVService service;

	@JmsListener(destination = Constants.INCOMING_QUEUE_NAME, containerFactory = "myFactory")
	public void receiveMessage(Request request) {
		service.parse(request);
	}

}