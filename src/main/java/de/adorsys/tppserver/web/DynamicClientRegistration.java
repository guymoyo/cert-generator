package de.adorsys.tppserver.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.client.registration.Auth;
import org.keycloak.client.registration.ClientRegistration;
import org.keycloak.client.registration.ClientRegistrationException;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.adorsys.tppserver.config.EnvConfig;
import de.adorsys.tppserver.domain.Bank;
import de.adorsys.tppserver.service.BankService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author gmo
 */
@RestController
@RequestMapping(produces = { "application/json; charset=UTF-8" })
public class DynamicClientRegistration {
	
	@Autowired
	BankService bankService;
	@Autowired
	EnvConfig envCf;

	@GetMapping("/clientRegistration")
	@ApiOperation(value = "Return the registred Client registed", notes = "Dynamical register an XS2A client to IDP of a known ASPSP ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return the registred Client"),
			@ApiResponse(code = 400, message = "Bad request") })
	public ClientRepresentation registerClient(@RequestParam(value = "bankCode") String bankCode,
			@RequestParam(value = "registrationToken") String registrationToken) {
		
		Bank bank = bankService.getIdpUrl(bankCode);
		 
		List<String> redirectUris = new ArrayList<>();
		redirectUris.add("*");
		
		Map<String, Boolean> access = new HashMap<>();
		access.put("confidential", true);
		
		
		 
		ClientRepresentation client = new ClientRepresentation();
		
		try {
			ClientRegistration registration = ClientRegistration.create()
					.url(bank.getUrlIDP(), bank.getRealm())
					.build();
			registration.auth(Auth.token(registrationToken));
			 client = registration.get("xs2a-client");//check if client exist on idp
		} catch (ClientRegistrationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(client.getClientId()==null){// if client doesnt exist, create new
			client = new ClientRepresentation();
			client.setClientId("xs2a-client");
			client.setName("XS2A Client");
			client.setSecret("secret");
			client.setEnabled(true);
			client.setProtocol("openid-connect");
			client.setStandardFlowEnabled(true);
			client.setRedirectUris(redirectUris);
			client.setWebOrigins(redirectUris);
			client.setAccess(access);
			
			try {
				ClientRegistration registration = ClientRegistration.create()
						.url(bank.getUrlIDP(), bank.getRealm())
						.build();
				client = registration.create(client);
			} catch (ClientRegistrationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
					
		return client;
	}
	
	@GetMapping("/code2Token")
	@ApiOperation(value = "exchange code to token", notes = "exchange code to token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return token"),
			@ApiResponse(code = 400, message = "Bad request") })
	public String code2Token(@RequestParam(value = "code") String code) {
		
		return null;
	}
	
	@GetMapping("/env")
	@ApiOperation(value = "exchange code to token", notes = "exchange code to token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return token"),
			@ApiResponse(code = 400, message = "Bad request") })
	public String env() {
		
		return envCf.getServers().toString();
	}

}
