package de.adorsys.tppserver.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Certificate Response", value = "CertificateResponse")
public class CertificateResponse {
	
	private String encodedCert;
	private String privateKey;
	private String keyId;
	private String algorithm;
	

}
