package com.qa.consumer;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
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
	private CVService service;

	@Mock
	private static CVRepository repo;

	private CV cv;
	private Trainee bob;
	private CV cv2;
	private ArrayList<CV> allCVs;

	@Mock
	private static CVProducer producer;

	private String queuedMessage = "File placed on queue succesfully";
	private String malformedMessage = "Queue request malformed";
	private String addMessage = "CV added succesfully";
	private String notFoundMessage = "CV not found";
	private String deletedMessage = "CV deleted succesfully";
	private String updatedMessage = "CV updated succesfully";
	private Request goodRequest;
	private Request badRequest;

	@Before
	public void setUp() {
		goodRequest = new Request();
		goodRequest.setcvIDtoActUpon(1l);
		badRequest = new Request();
		badRequest.setcvIDtoActUpon(11l);
		bob.setFirstName("Bob");
		cv.setCreator(bob);
		cv2.setCreator(bob);
		allCVs.add(cv);
		allCVs.add(cv2);
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());
		Mockito.when(repo.findAll()).thenReturn(allCVs);
		Mockito.when(producer.produce(cv)).thenReturn(queuedMessage);
		Mockito.when(producer.produce(allCVs)).thenReturn(queuedMessage);
	}

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
	public void testFindParse() { // Checks whether requests are parsed correctl
		goodRequest.setType(requestType.READ);
		badRequest.setType(requestType.READ);

		assertEquals(queuedMessage, service.parse(goodRequest));
		assertEquals(notFoundMessage, service.parse(badRequest));

	}

	@Test
	public void testDeleteParse() {
		goodRequest.setType(requestType.DELETE);
		badRequest.setType(requestType.DELETE);

		assertEquals(deletedMessage, service.parse(goodRequest));
		assertEquals(notFoundMessage, service.parse(badRequest));
	}

	@Test
	public void testUpdateParse() {
		goodRequest.setType(requestType.UPDATE);
		badRequest.setType(requestType.UPDATE);

		assertEquals(updatedMessage, service.parse(goodRequest));
		assertEquals(notFoundMessage, service.parse(badRequest));
	}

	@Test
	public void testAddParse() {
		goodRequest.setType(requestType.CREATE);
		goodRequest.setCv(cv2);
		badRequest.setType(requestType.CREATE);

		assertEquals(addMessage, service.parse(goodRequest));
		assertEquals(malformedMessage, service.parse(badRequest));

	}

	@Test
	public void testOtherParse() {
		goodRequest.setType(requestType.READALL);

		assertEquals(queuedMessage, service.parse(goodRequest));
		assertEquals(malformedMessage, service.parse(badRequest));
	}

}
