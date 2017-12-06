package de.adorsys.tppserver.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import de.adorsys.tppserver.domain.Bank;

@Service
public class BankService {

	private Map<String, Bank> bankCode2urlIdpBank = new HashMap<>();

	@PostConstruct
	void init(){

		bankCode2urlIdpBank.put("10000", new Bank("http://192.168.99.102:8080/auth", "demobank"));
		bankCode2urlIdpBank.put("xxxx", new Bank("http://xxxx:8080/auth/", "realm-xxx"));

	}
	
	public Bank getIdpUrl(String bankCode){
		
		return bankCode2urlIdpBank.get("10000");// for demo we only have one known idp.		
		 
	}

}
