package com.sagatechs.javaeeApp.services.general;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailServiceTest {

	EmailService emailService;
	@Before
	public void setUp() throws Exception {
		this.emailService =new EmailService();
		this.emailService.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		this.emailService.sendEmailMessage("sebassalazart@hotmail.com", "test", "probando3");
	}

}
