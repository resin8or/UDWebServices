package com.mycomp.training.ws;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.mycomp.training.ws.dto.SumRequest;
import com.mycomp.training.ws.dto.SumResponse;

@WebService(name="SumWS")
public interface SumWS {

	@WebResult(name="SumResponse")
	SumResponse calculateSum(@WebParam SumRequest request);
	
}
