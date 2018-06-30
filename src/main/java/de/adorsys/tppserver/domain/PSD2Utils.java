package de.adorsys.tppserver.domain;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.qualified.QCStatement;

public class PSD2Utils {

	
	public static QCStatement psd2QCStatement(){
		return new QCStatement(PSD2QCObjectIdentifiers.id_etsi_psd2_qcStatement);
	}
	
	public static DERSequence psd2QcType(RolesOfPSP rolesOfPSP, NCAName nCAName, NCAId nCAId){
		return new DERSequence(new ASN1Encodable[] { rolesOfPSP, nCAName, nCAId });
	}
}
