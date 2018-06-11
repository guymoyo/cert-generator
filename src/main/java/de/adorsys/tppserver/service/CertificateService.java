package de.adorsys.tppserver.service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cergenerator.service.helper.CertificateGenerator;
import com.cergenerator.service.helper.CertificateUtils;

import de.adorsys.tppserver.domain.CertificateData;
import de.adorsys.tppserver.domain.IssuerData;
import de.adorsys.tppserver.domain.SubjectData;

@Service
public class CertificateService {

//	private static final String BC = BouncyCastleProvider.PROVIDER_NAME;
	
	public CertificateService() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public X509Certificate newCertificate (CertificateData cerData){
		
			SubjectData subjectData = generateSubjectData(cerData);
			
			IssuerData issuerData = generateIssuerData();
			
			CertificateGenerator cg = new CertificateGenerator();
			
			X509Certificate cert = null;
			
			GeneralNames subjectAltNames;
			try {
				subjectAltNames = generateSubjectAltNames(cerData);
				cert = cg.generateCertificate(subjectData, issuerData, subjectAltNames);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("\n===== issuer name =====");
			System.out.println(cert.getIssuerX500Principal().getName());
			System.out.println("\n===== Subject Name =====");
			System.out.println(cert.getSubjectX500Principal().getName());
			System.out.println("\n===== Certifikat =====");
			System.out.println("-------------------------------------------------------");
			System.out.println(cert);
			System.out.println("-------------------------------------------------------");
		
			return cert;
	}
	
	private GeneralNames generateSubjectAltNames(CertificateData cerData) throws IOException {
		
		ArrayList<GeneralName> namesList = new ArrayList<>();
		
		if(cerData.isAISP()) {
			ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.2.3.1"); // some arbitrary non-existent OID number
			DERSequence seq = new DERSequence(new ASN1Encodable[] { oid, new ASN1Integer(1) });
			namesList.add(new GeneralName(GeneralName.otherName, seq));
		}else {
			ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.2.3.1"); // some arbitrary non-existent OID number
			DERSequence seq = new DERSequence(new ASN1Encodable[] { oid, new ASN1Integer(0) });
			namesList.add(new GeneralName(GeneralName.otherName, seq));
		}
		
		if(cerData.isPISP()) {
			ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.2.3.2"); // some arbitrary non-existent OID number
			DERSequence seq = new DERSequence(new ASN1Encodable[] { oid, new ASN1Integer(1) });
			namesList.add(new GeneralName(GeneralName.otherName, seq));
		}else {
			ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.2.3.2"); // some arbitrary non-existent OID number
			DERSequence seq = new DERSequence(new ASN1Encodable[] { oid, new ASN1Integer(0) });
			namesList.add(new GeneralName(GeneralName.otherName, seq));
		}
		
		if(cerData.isPIISP()) {
			ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.2.3.3"); // some arbitrary non-existent OID number
			DERSequence seq = new DERSequence(new ASN1Encodable[] { oid, new ASN1Integer(1) });
			namesList.add(new GeneralName(GeneralName.otherName, seq));
		}else {
			ASN1ObjectIdentifier oid = new ASN1ObjectIdentifier("1.2.3.3"); // some arbitrary non-existent OID number
			DERSequence seq = new DERSequence(new ASN1Encodable[] { oid, new ASN1Integer(0) });
			namesList.add(new GeneralName(GeneralName.otherName, seq));
		}
			
		GeneralNames subjectAltNames = GeneralNames.getInstance(new DERSequence((GeneralName[]) namesList.toArray(new GeneralName[] {})));

		return subjectAltNames;
	}

	private SubjectData generateSubjectData(CertificateData cerData) {
		KeyPair keyPairSubject = generateKeyPair(2048, "RSA");
		
		//SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date(System.currentTimeMillis());
		Date endDate = new Date(System.currentTimeMillis() + (cerData.getValidity() * 86400000));

		Random rand = new Random();
		String serialNumber = Integer.toString(rand.nextInt(Integer.MAX_VALUE));
//		String serialNumber = UUID.randomUUID().toString();
		//String serialNumber = "12345";
		
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, cerData.getCommonName());
		builder.addRDN(BCStyle.O, cerData.getOrganization());
		builder.addRDN(BCStyle.OU, cerData.getOrganizationUnit());
		builder.addRDN(BCStyle.C, cerData.getCountry());
		builder.addRDN(BCStyle.ST, cerData.getState());
		builder.addRDN(BCStyle.L, cerData.getCity());
		
		return new SubjectData(keyPairSubject.getPrivate(), keyPairSubject.getPublic(), builder.build(), serialNumber, startDate, endDate);
	}
	
	private KeyPair generateKeyPair(int keySize, String keyType) {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyType); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(keySize, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}

	private IssuerData generateIssuerData() {
		
		IssuerData issuerData = new IssuerData();
		
		X509Certificate cert = CertificateUtils.getCertificate("MyRootCA.pem");
		System.out.println(cert);
		
		X500Name issuerName;
		try {
			issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			issuerData.setX500name(issuerName);
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrivateKey privateKey = CertificateUtils.getKeyFromFile("MyRootCA.key");
		issuerData.setPrivateKey(privateKey);
		
		return issuerData;
	}
		
}
