package com.qa.consumer;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.qa.consumer.persistence.domain.CV;
import com.qa.consumer.persistence.domain.Request;
import com.qa.consumer.persistence.domain.Trainee;
import com.qa.consumer.persistence.domain.Request.requestType;
import com.qa.consumer.persistence.repository.CVRepository;
import com.qa.consumer.service.CVService;
import com.qa.consumer.util.CVProducer;

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
	
	@Value("${SuccessfullyQueued.message}")
	String queuedMessage;
	
	@Value("${MalformedRequest.message}")
	String malformedMessage;
	
	@Value("${CVAdded.message}")
	String addMessage;
	
	@Value("${CVNotFound.message}")
	String notFoundMessage;
	
	@Value("${CVDeleted.message}")
	String deletedMessage;
	
	@Value("${CVUpdated.message}")
	String updatedMessage;
	
	@Value("${SuccessfullyQueued.message}")
	String successMessage;

	@Test
	public void testSendReceive() { // Checks whether correctly pointing to MQ Server and therefore requires one
		jmsTemplate.convertAndSend("testQueue", "Hello World!");
		assertEquals("Hello World!", jmsTemplate.receiveAndConvert("testQueue"));
	}

	@Test
	public void cvTests() {
		CV cv = new CV();
		assertEquals(null, cv.getCreator());

		Trainee bob = new Trainee();
		cv.setCreator(bob);
		assertEquals(bob, cv.getCreator());

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
		Mockito.when(producer.produce(cv)).thenReturn(successMessage);
		Mockito.when(producer.produce(allCVs)).thenReturn(successMessage);

		Request findRequest = new Request();
		findRequest.setcvIDtoActUpon(1l);
		findRequest.setType(requestType.READ);

		Request badFindRequest = new Request();
		badFindRequest.setcvIDtoActUpon(11l);
		badFindRequest.setType(requestType.READ);

		Request deleteRequest = new Request();
		deleteRequest.setcvIDtoActUpon(1l);
		deleteRequest.setType(requestType.DELETE);

		Request badDeleteRequest = new Request();
		badDeleteRequest.setcvIDtoActUpon(11l);
		badDeleteRequest.setType(requestType.DELETE);

		Request updateRequest = new Request();
		updateRequest.setCv(cv2);
		updateRequest.setcvIDtoActUpon(1l);
		updateRequest.setType(requestType.UPDATE);

		Request badUpdateRequest = new Request();
		badUpdateRequest.setCv(cv2);
		badUpdateRequest.setcvIDtoActUpon(11l);
		badUpdateRequest.setType(requestType.UPDATE);

		Request findAllRequest = new Request();
		findAllRequest.setType(requestType.READALL);

		Request addRequest = new Request();
		addRequest.setType(requestType.CREATE);
		addRequest.setCv(cv2);

		Request badAddRequest = new Request();
		addRequest.setType(requestType.CREATE);

		Request malformedRequest = new Request();

		assertEquals(queuedMessage, service.parse(findRequest));
		assertEquals(notFoundMessage, service.parse(badFindRequest));

		assertEquals(deletedMessage, service.parse(deleteRequest));
		assertEquals(notFoundMessage, service.parse(badDeleteRequest));

		assertEquals(updatedMessage, service.parse(updateRequest));
		assertEquals(notFoundMessage, service.parse(badUpdateRequest));

		assertEquals(addMessage, service.parse(addRequest));
		assertEquals(malformedMessage, service.parse(badAddRequest));

		assertEquals(queuedMessage, service.parse(findAllRequest));
		assertEquals(malformedMessage, service.parse(malformedRequest));

	}
}
