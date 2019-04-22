/**
 * 
 */
package com.sagatechs.javaeeApp.rest.security;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author sebas
 *
 */
public class JwtHandlerTest {

	
	UserSecurityService jwtHandler;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.jwtHandler = new UserSecurityService();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.sagatechs.javaeeApp.rest.security.UserSecurityService#generateToken()}.
	 */
	@Test
	public void testGenerateToken() {

		
		
	}
	
	
	@Test
	public void testgenerateKey() {
		
	}

}
