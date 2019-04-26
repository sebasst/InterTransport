package com.sagatechs.javaeeApp.rest.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sagatechs.adminfaces.starter.model.Car;
import com.sagatechs.javaeeApp.exceptions.BusinessException;

@Path("testsecured")
public class TestSecured {


	@Path("test")
	@GET
	@Produces(javax.ws.rs.core.MediaType.TEXT_HTML)
	public String getHtml() {
		return "<html lang=\"en\"><body><h1>Hello, chch!!</h1></body></html>";
	}
	
	@Path("json")
	@GET
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Car getJson() {
		Car car = new Car();
		car.setId(1);
		car.setModel("Toyota");
		car.setName("Higlander");
		car.setPrice(11.12d);
		return car;
	}
	
	@SuppressWarnings("unused")
	@Path("error")
	@GET
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Car getJsonError() {
		Car car = new Car();
		car.setId(1);
		car.setModel("Toyota");
		car.setName("Higlander");
		car.setPrice(11.12d);
		if(true) {
			throw new BusinessException("Test");
		}
		return car;
	}
}
