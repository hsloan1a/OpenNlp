package com.mybuild.controller;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class GreetingControllerTest {
	@LocalServerPort
	private int port;
	
	private URL base;
	
	@Autowired
	TestRestTemplate testRestTemplate;
	
	@Before
	public void setUp() throws MalformedURLException{
		this.base = new URL("http://localhost:" + port + "/greeting");
	}
	
	@Test
	public void getHello(){
		ResponseEntity<String> response = testRestTemplate.getForEntity(base.toString(),
				String.class);
		assertThat(response.getBody(), equalTo("Hello, World!"));
	}
	
	
}
