package com.qa.consumer;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
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
import com.qa.consumer.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CvConsumerApplicationTests {

	@Autowired
	private JmsTemplate jmsTemplate;

	@InjectMocks
	private CVService service;

	@Mock
	private CVRepository repo;

	@Mock
	private CVProducer producer;

	private Request goodRequest;
	private Request badRequest;
	private CV cv;
	private Trainee bob;
	private CV cv2;
	private ArrayList<CV> allCVs;

	@Before
	public void setUp() {
		goodRequest = new Request();
		goodRequest.setcvIDtoActUpon(1l);
		badRequest = new Request();
		badRequest.setcvIDtoActUpon(11l);
		
		cv = new CV();
		cv2 = new CV();
		allCVs = new ArrayList<CV>();
		allCVs.add(cv);
		allCVs.add(cv2);

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
	public void traineeTests() {
		bob = new Trainee();
		assertEquals(null, bob.getFirstName());
		assertEquals(null, bob.getEmail());
		assertEquals(null, bob.getLastName());

		bob.setEmail("a@b.com");
		bob.setFirstName("Bob");
		bob.setLastName("Bobbington");

		assertEquals("a@b.com", bob.getEmail());
		assertEquals("Bob", bob.getFirstName());
		assertEquals("Bobbington", bob.getLastName());

	}

	@Test
	public void testFindParse() {
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());
		Mockito.when(producer.produce(cv)).thenReturn(Constants.CV_QUEUED_MESSAGE);

		goodRequest.setType(requestType.READ);
		badRequest.setType(requestType.READ);

		assertEquals(Constants.CV_QUEUED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.CV_NOT_FOUND_MESSAGE, service.parse(badRequest));

	}

	@Test
	public void testDeleteParse() {
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());

		goodRequest.setType(requestType.DELETE);
		badRequest.setType(requestType.DELETE);

		assertEquals(Constants.CV_DELETED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.CV_NOT_FOUND_MESSAGE, service.parse(badRequest));
	}

	@Test
	public void testUpdateParse() {
		Mockito.when(repo.findById(1l)).thenReturn(Optional.of(cv));
		Mockito.when(repo.findById(11l)).thenReturn(Optional.empty());

		goodRequest.setType(requestType.UPDATE);
		goodRequest.setCv(cv2);
		badRequest.setType(requestType.UPDATE);

		assertEquals(Constants.CV_UPDATED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.CV_NOT_FOUND_MESSAGE, service.parse(badRequest));
	}

	@Test
	public void testAddParse() {
		goodRequest.setType(requestType.CREATE);
		goodRequest.setCv(cv2);
		badRequest.setType(requestType.CREATE);

		assertEquals(Constants.CV_ADDED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));

	}

	@Test
	public void testFindAllAndMalformedParse() {
		Mockito.when(repo.findAll()).thenReturn(allCVs);
		Mockito.when(producer.produce(allCVs)).thenReturn(Constants.CV_QUEUED_MESSAGE);

		goodRequest.setType(requestType.READALL);

		assertEquals(Constants.CV_QUEUED_MESSAGE, service.parse(goodRequest));
		assertEquals(Constants.MALFORMED_REQUEST_MESSAGE, service.parse(badRequest));
	}

}
