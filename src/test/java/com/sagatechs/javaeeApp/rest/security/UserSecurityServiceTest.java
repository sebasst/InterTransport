package com.sagatechs.javaeeApp.rest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserSecurityServiceTest {

	UserSecurityService userSecurityService;
	@Before
	public void setUp() throws Exception {
		this.userSecurityService = new UserSecurityService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void generateRandomCodeTest() {
		String code = this.userSecurityService.generateRandomCode();
		System.out.println(code);
		try {
			@SuppressWarnings("unused")
			int result = Integer.parseInt(code);
			assertEquals("no es de cuatro dígitos", 4, code.length());
		} catch (NumberFormatException e) {
			fail("No es un entero: "+code);
		}
	}

}
