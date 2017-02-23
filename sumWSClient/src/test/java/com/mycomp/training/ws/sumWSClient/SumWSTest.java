package com.mycomp.training.ws.sumWSClient;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.mycomp.training.ws.SumRequest;
import com.mycomp.training.ws.SumResponse;
import com.mycomp.training.ws.SumWS;
import com.mycomp.training.ws.SumWSService;

public class SumWSTest {

	@Test
	public void calculateSumShouldReturnAValidResult() throws MalformedURLException {
		SumWSService service = new SumWSService(new URL("http://localhost:8080/sumws/services/sumService?wsdl"));
		
		SumWS port = service.getSumWSPort();
		SumRequest request = new SumRequest();
		request.setNum1(10);
		request.setNum2(20);
		SumResponse response = port.calculateSum(request);
		
		assertNotNull(response);
		assertEquals(30, response.getResult());
	}
}
