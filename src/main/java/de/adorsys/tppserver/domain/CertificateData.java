package de.adorsys.tppserver.domain;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Certificate Data", value = "CertificateData")
public class CertificateData {

	//private String serialNumber;	
	
	@ApiModelProperty(value = "commonName", required = true, example = "domainName")
    @NotNull
	private String commonName;
	
	@ApiModelProperty(value = "organization", required = true, example = "org")
    @NotNull
	private String organization;
	
	@ApiModelProperty(value = "organizationUnit", required = true, example = "ou")
    @NotNull
	private String organizationUnit;
	
	@ApiModelProperty(value = "city", required = true, example = "Nuremberg")
    @NotNull
	private String city;
	
	@ApiModelProperty(value = "state", required = true, example = "Bayern")
    @NotNull
	private String state;
	
	@ApiModelProperty(value = "country", required = true, example = "Germany")
    @NotNull
	private String country;
	
	@ApiModelProperty(value = "validity", required = true, example = "365")
    @NotNull
	private int validity;
	
	@ApiModelProperty(value = "pspAuthorzationNumber", required = true, example = "12345987")
    @NotNull
	private String pspAuthorzationNumber;
	
	@ApiModelProperty(value = "pISP", required = true, example = "true")
	private boolean pISP;
	
	@ApiModelProperty(value = "aISP", required = true, example = "true")
	private boolean aISP;
	
	@ApiModelProperty(value = "pIISP", required = true, example = "true")
	private boolean pIISP;
	
	@ApiModelProperty(value = "pspName", required = true, example = "TPP_test")
    @NotNull
	private String pspName;
	
	@ApiModelProperty(value = "pspAuthorityName", required = true, example = "Auth")
    @NotNull
	private String pspAuthorityName;
	
	@ApiModelProperty(value = "pspAuthorityCountry", required = true, example = "Germany")
    @NotNull
	private String pspAuthorityCountry;
		
	
}
