package com.copel.picmicroservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MonitorTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate  restTemplate;

	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		
		String response = this.restTemplate.getForObject("http://localhost:" + port + "/" + ConstantesTest.CONTEXT+  "/monitor",
				String.class);
		assertThat(response).contains("OKOKOK");
	}
}