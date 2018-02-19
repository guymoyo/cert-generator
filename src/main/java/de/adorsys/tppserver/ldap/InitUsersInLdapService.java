package de.adorsys.tppserver.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitUsersInLdapService {

	@Autowired
	LdapService ldapService;

	
	public void createUsersInLdap() {

		for (int i = 0; i <= 9999; i++) {
			
			ldapService.create("testuser"+i, "testuser"+i+"@mail.de", "firstname"+i, "lastname"+i, "Password#12");
			System.out.print(i+"-");
		}

	}

}