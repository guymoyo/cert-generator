package de.adorsys.tppserver.ldap;

import java.util.List;

import javax.naming.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

@Service
public class LdapService {

	    @Autowired
	    private LdapTemplate ldapTemplate;

	  

	    public List<String> search(final String username) {
	        return ldapTemplate.search(
	          "dc=example,dc=com",
	          "uid=" + username,
	          (AttributesMapper<String>) attrs -> (String) attrs
	          .get("uid")
	          .get());
	    }

	    public void create(final String username, final String email, 
	    		final String firstname, final String lastname, final String password ) {
	    	
	        Name dn = LdapNameBuilder
	          .newInstance()   
	          .add("uid", username)
	          .build();
	        DirContextAdapter context = new DirContextAdapter(dn);

	        context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
	        context.setAttributeValue("uid", username);
	        context.setAttributeValue("cn", firstname);
	        context.setAttributeValue("sn", lastname);
	        context.setAttributeValue("mail", email);
	        context.setAttributeValue("userPassword", password);
	        
	        ldapTemplate.bind(context);
	    }

	    public void modify(final String username, final String password) {
	        Name dn = LdapNameBuilder
	          .newInstance()
	          .add("uid", username)
	          .add("userPassword", password)
	
	          .build();
	        DirContextOperations context = ldapTemplate.lookupContext(dn);

	        context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
	        
	        context.setAttributeValue("userPassword", password);

	        ldapTemplate.modifyAttributes(context);
	    }
}