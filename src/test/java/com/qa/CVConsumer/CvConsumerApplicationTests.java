package com.qa.CVConsumer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.CVConsumer.persistence.domain.CV;
import com.qa.CVConsumer.persistence.domain.Request;
import com.qa.CVConsumer.persistence.domain.Request.requestType;
import com.qa.CVConsumer.persistence.domain.Trainee;
import com.qa.CVConsumer.persistence.repository.CVRepository;
import com.qa.CVConsumer.service.CVService;
import com.qa.CVConsumer.util.CVProducer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CvConsumerApplicationTests {

	@Autowired
	private JmsTemplate jmsTemplate;

	@InjectMocks
	CVService service;

	@Mock
	CVRepository repo;
	
	@Mock
	CVProducer producer;

	@Test
	public void testSendReceive() { // Checks whether correctly pointing to MQ Server and therefore requires one
		jmsTemplate.convertAndSend("testQueue", "Hello World!");
		assertEquals(jmsTemplate.receiveAndConvert("testQueue"), "Hello World!");
	}

	@Test
	public void testReceiveParse() { // Checks whether requests are parsed correctly
		CV cv = new CV();
		Trainee bob = new Trainee();
		bob.setFirstName("Bob");
		cv.setCreator(bob);

		CV cv2 = new CV();
		cv2.setCreator(bob);

		ArrayList<CV> allCVs = new ArrayList<CV>();
		allCVs.add(cv);
		allCVs.add(cv2);

		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());
		Mockito.when(repo.findAll()).thenReturn(allCVs);
		Mockito.when(producer.produce(cv)).thenReturn("${SuccessfullyQueued.message}");
		Mockito.when(producer.produce(allCVs)).thenReturn("${SuccessfullyQueued.message}");

		Request findRequest = new Request();
		findRequest.setCVID(1l);
		findRequest.setType(requestType.READ);

		Request badFindRequest = new Request();
		badFindRequest.setCVID(11l);
		badFindRequest.setType(requestType.READ);

		Request deleteRequest = new Request();
		deleteRequest.setCVID(1l);
		deleteRequest.setType(requestType.DELETE);

		Request badDeleteRequest = new Request();
		badDeleteRequest.setCVID(11l);
		badDeleteRequest.setType(requestType.DELETE);

		Request updateRequest = new Request();
		updateRequest.setCv(cv2);
		updateRequest.setCVID(1l);
		updateRequest.setType(requestType.UPDATE);

		Request badUpdateRequest = new Request();
		badUpdateRequest.setCv(cv2);
		badUpdateRequest.setCVID(11l);
		badUpdateRequest.setType(requestType.UPDATE);

		Request findAllRequest = new Request();
		findAllRequest.setType(requestType.READALL);

		Request addRequest = new Request();
		addRequest.setType(requestType.CREATE);
		addRequest.setCv(cv2);

		Request badAddRequest = new Request();
		addRequest.setType(requestType.CREATE);

		Request malformedRequest = new Request();

		assertEquals(service.parse(findRequest), "${SuccessfullyQueued.message}");
		assertEquals(service.parse(badFindRequest), "${CVNotFound.message}");

		assertEquals(service.parse(deleteRequest), "${CVDeleted.message}");
		assertEquals(service.parse(badDeleteRequest), "${CVNotFound.message}");

		assertEquals(service.parse(updateRequest), "${CVUpdated.message}");
		assertEquals(service.parse(badUpdateRequest), "${CVNotFound.message}");

		assertEquals(service.parse(addRequest), "${CVAdded.message}");
		assertEquals(service.parse(badAddRequest), "${MalformedRequest.message}");

		assertEquals(service.parse(findAllRequest), "${SuccessfullyQueued.message}");
		assertEquals(service.parse(malformedRequest), "${MalformedRequest.message}");

	}
}
