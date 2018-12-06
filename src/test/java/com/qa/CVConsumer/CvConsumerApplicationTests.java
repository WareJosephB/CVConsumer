package com.qa.CVConsumer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CvConsumerApplicationTests {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Test
	public void testSendReceive() { //Checks whether correctly pointing to MQ Server and therefore requires that an MQ server is running
		jmsTemplate.convertAndSend("testQueue", "Hello World!");
		assertEquals(jmsTemplate.receiveAndConvert("testQueue"), "Hello World!");
	}
}