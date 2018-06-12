package de.adorsys.tppserver.web;


import java.io.StringWriter;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.adorsys.tppserver.domain.CertificateData;
import de.adorsys.tppserver.service.CertificateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/certificate")
public class CertificateCtrl {

	@Autowired
	private CertificateService cerService;
	
	public CertificateCtrl() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	@ApiOperation(value = "Return certificate base 64 encoded", notes = "Create a new certificate ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return new certificate"),
			@ApiResponse(code = 400, message = "Bad request") })
	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.ALL_VALUE)
	public ResponseEntity<String> add(@RequestBody CertificateData certData) throws Exception{
		
		
		X509Certificate cert = cerService.newCertificate(certData);
		
		final StringWriter writer = new StringWriter();
	    final JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
	    pemWriter.writeObject(cert);
	    pemWriter.flush();
	    pemWriter.close();
	    String response = writer.toString();
	    
	    response = response.replaceAll("\n", "").replaceAll("\r", "");
	    
		return new ResponseEntity<String>(response, HttpStatus.CREATED);
		
	}
	
	/*@RequestMapping(value = "/{id}/revoke",
			method = RequestMethod.POST)
	public ResponseEntity<CertificateData> revoke(int id){
		// TODO: Implement
		return null;
	}
	
	@RequestMapping(value = "/{id}/revoke",
			method = RequestMethod.GET)
	public ResponseEntity<String> getRevocationStatus(int id){
		// TODO: Implement
		return null;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ArrayList<CertificateData> getAll(){
		// TODO: Implement
		return null;
	}
	
	@RequestMapping(value = "/{id}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CertificateData> getById(int id){
		// TODO: Implement
		return null;
	}*/
}
