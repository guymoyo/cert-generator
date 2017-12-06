package de.adorsys.tppserver.domain;

import lombok.Data;

@Data
public class Bank {

	private String urlIDP;
	private String realm;
	
	public Bank(String urlIDP, String realm) {
		super();
		this.urlIDP = urlIDP;
		this.realm = realm;
	}
	
	
}
