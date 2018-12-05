package com.qa.CVConsumer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qa.CVConsumer.persistence.domain.Request;
import com.qa.CVConsumer.service.CVService;

@Component
public class RequestReceiver {

	@Autowired
	private CVService service;

	@JmsListener(destination = "${incomingQueueName}", containerFactory = "myFactory")
	public void receiveMessage(Request request) {
		service.parse(request);
	}

}