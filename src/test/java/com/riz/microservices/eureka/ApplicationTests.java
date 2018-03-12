package com.riz.microservices.eureka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
