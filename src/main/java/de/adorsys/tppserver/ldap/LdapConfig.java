package de.adorsys.tppserver.ldap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LdapConfig {

	
	@Bean
	public LdapContextSource contextSource() {
	    LdapContextSource contextSource = new LdapContextSource();
	     
	    contextSource.setUrl("ldap://openldap:389");
	    contextSource.setBase("dc=example,dc=com");
	    contextSource.setUserDn("cn=Manager,dc=example,dc=com");
	    contextSource.setPassword("admin");
	     
	    return contextSource;
	}
	
	@Bean
	public LdapTemplate ldapTemplate() {
	    return new LdapTemplate(contextSource());
	}
	
}
