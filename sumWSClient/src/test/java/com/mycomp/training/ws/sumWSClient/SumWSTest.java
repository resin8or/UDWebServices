package com.mycomp.training.ws.sumWSClient;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandler;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
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
		
		Client client = ClientProxy.getClient(port);
		Endpoint endpoint = client.getEndpoint();
		
		HashMap<String, Object> outProps = new HashMap<String, Object>();
		
		// Add properties and actions to the Hashmap.
		outProps.put(WSHandlerConstants.ACTION, "UsernameToken Encrypt Signature");
		outProps.put(WSHandlerConstants.USER, "sumuser");
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordCallbackHandler.class.getName());
		
		outProps.put(WSHandlerConstants.ENCRYPTION_USER, "myservicekey");
		outProps.put(WSHandlerConstants.ENC_PROP_FILE, "etc/clientkeystore.properties");
		
		// This is the reverse of encryption
		// sign using the client sides private key
		// server will use the public key
		outProps.put(WSHandlerConstants.SIGNATURE_USER, "myclientkey");
		outProps.put(WSHandlerConstants.SIG_PROP_FILE, "etc/clientkeystore.properties");
		
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(
				outProps);
		
		// Add interceptor to the list of interceptor.
	    // Invoked when the SOAP message is sent to the server.
		endpoint.getOutInterceptors().add(wssOut);
		
		// Decrypt when the message comes back from the server to the client
		HashMap<String, Object> inProps = new HashMap<>();
		inProps.put(WSHandlerConstants.ACTION, "Encrypt Signature");
		inProps.put(WSHandlerConstants.DEC_PROP_FILE, "etc/clientkeystore.properties");
		// Access the private key to decrypt the message
		inProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordCallbackHandler.class.getName());
		inProps.put(WSHandlerConstants.SIG_PROP_FILE, "etc/clientkeystore.properties");

		
		WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);
		endpoint.getInInterceptors().add(wssIn);
		
		SumRequest request = new SumRequest();
		request.setNum1(10);
		request.setNum2(20);
		SumResponse response = port.calculateSum(request);
		
		assertNotNull(response);
		assertEquals(30, response.getResult());
	}
	
}
