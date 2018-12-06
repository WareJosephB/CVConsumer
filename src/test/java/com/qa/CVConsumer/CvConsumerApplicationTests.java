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
		Mockito.when(producer.produce(cv)).thenReturn("${SuccessfullyQueued.message}");
		Mockito.when(producer.produce(allCVs)).thenReturn("${SuccessfullyQueued.message}");

		Request findRequest = new Request();
		findRequest.setcv_id(1l);
		findRequest.setType(requestType.READ);

		Request badFindRequest = new Request();
		badFindRequest.setcv_id(11l);
		badFindRequest.setType(requestType.READ);

		Request deleteRequest = new Request();
		deleteRequest.setcv_id(1l);
		deleteRequest.setType(requestType.DELETE);

		Request badDeleteRequest = new Request();
		badDeleteRequest.setcv_id(11l);
		badDeleteRequest.setType(requestType.DELETE);

		Request updateRequest = new Request();
		updateRequest.setCv(cv2);
		updateRequest.setcv_id(1l);
		updateRequest.setType(requestType.UPDATE);

		Request badUpdateRequest = new Request();
		badUpdateRequest.setCv(cv2);
		badUpdateRequest.setcv_id(11l);
		badUpdateRequest.setType(requestType.UPDATE);

		Request findAllRequest = new Request();
		findAllRequest.setType(requestType.READALL);

		Request addRequest = new Request();
		addRequest.setType(requestType.CREATE);
		addRequest.setCv(cv2);

		Request badAddRequest = new Request();
		addRequest.setType(requestType.CREATE);

		Request malformedRequest = new Request();

		assertEquals("${SuccessfullyQueued.message}", service.parse(findRequest));
		assertEquals("${CVNotFound.message}", service.parse(badFindRequest));

		assertEquals("${CVDeleted.message}", service.parse(deleteRequest));
		assertEquals("${CVNotFound.message}", service.parse(badDeleteRequest));

		assertEquals("${CVUpdated.message}", service.parse(updateRequest));
		assertEquals("${CVNotFound.message}", service.parse(badUpdateRequest));

		assertEquals("${CVAdded.message}", service.parse(addRequest));
		assertEquals("${MalformedRequest.message}", service.parse(badAddRequest));

		assertEquals("${SuccessfullyQueued.message}", service.parse(findAllRequest));
		assertEquals("${MalformedRequest.message}", service.parse(malformedRequest));

	}
}
