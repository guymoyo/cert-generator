package de.adorsys.tppserver.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Signature Data", value = "SignatureData")
public class SignatureData {

	@ApiModelProperty(value = "private Key", required = true, example = "privateKey")
    @NotNull
	private String privateKey;
	@ApiModelProperty(value = "key ID", required = true, example = "keyID")
    @NotNull
	private String keyID;
	@ApiModelProperty(value = "algorithm", required = true, example = "algorithm")
    @NotNull
	private String algorithm;
	@ApiModelProperty(value = "digest", required = true, example = "digest")
    @NotNull
	private String digest;
	@ApiModelProperty(value = "tpp Transaction ID", required = true, example = "tppTransactionID")
    @NotNull
	private String tppTransactionID;
	@ApiModelProperty(value = "tpp Request ID", required = true, example = "tppRequestID")
    @NotNull
	private String tppRequestID;
	@ApiModelProperty(value = "psuID", required = false, example = "psuID")
	private String psuID;
	@ApiModelProperty(value = "date request", required = true, example = "date")
    @NotNull
	private String timestamp;
	
}
