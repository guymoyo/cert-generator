package de.adorsys.tppserver.web;


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tomitribe.auth.signatures.Base64;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;

import com.cergenerator.service.helper.CertificateUtils;

import de.adorsys.tppserver.domain.CertificateData;
import de.adorsys.tppserver.domain.CertificateResponse;
import de.adorsys.tppserver.domain.SignatureData;
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
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CertificateResponse> add(@RequestBody CertificateData certData) throws Exception{
		
		
		CertificateResponse certificateResponse = cerService.newCertificate(certData);
	    
		return new ResponseEntity<CertificateResponse>(certificateResponse, HttpStatus.CREATED);
		
	}
	
	@ApiOperation(value = "Return Hash of the message body", notes = "Create digest ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return digest"),
			@ApiResponse(code = 400, message = "Bad request") })
	@RequestMapping(value = "/generateDigest",method = RequestMethod.POST,consumes = "text/plain")
	public ResponseEntity<String> generateDigest(@RequestBody String payload) throws Exception{
		
		String digestHeader = null;
		try {
			byte[] digest = MessageDigest.getInstance("SHA-256").digest(payload.getBytes());
			digestHeader = "SHA-256=" + new String(Base64.encodeBase64(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
			    
		return new ResponseEntity<String>(digestHeader, HttpStatus.CREATED);
		
	}
	
	@ApiOperation(value = "Return signature", notes = "Create signature ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Return signature"),
			@ApiResponse(code = 400, message = "Bad request") })
	@RequestMapping(value = "/generateSignature",method = RequestMethod.POST)
	public ResponseEntity<String> generateSignature(@RequestBody SignatureData signatureData) throws Exception{
		
		String signatureStr = null;
	    String headers = "Digest TPP-Transaction-ID TPP-Request-ID Timestamp";
	    
	    PrivateKey privateKey = CertificateUtils.convertStr2PrivateKey(signatureData.getPrivateKey());

				    
		Map<String, String> headersMap = new HashMap<>();
		
		headersMap.put("Digest", signatureData.getDigest());
		headersMap.put("TPP-Transaction-ID", signatureData.getTppTransactionID());
		headersMap.put("TPP-Request-ID", signatureData.getTppRequestID());
		headersMap.put("Timestamp", signatureData.getTimestamp());
		
		if(!StringUtils.isEmpty(signatureData.getPsuID())){
			
			headersMap.put("PSU-ID", signatureData.getPsuID());
			headers = headers+" PSU-ID";
		}
		
		Signature signature = new Signature(signatureData.getKeyID(), signatureData.getAlgorithm(), null, headers.split(" "));

		Signer signer = new Signer(privateKey, signature);
		Signature signed;
		try {
			signed = signer.sign("method", "uri", headersMap);
		    signatureStr = signed.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new ResponseEntity<String>(signatureStr, HttpStatus.CREATED);
		
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
