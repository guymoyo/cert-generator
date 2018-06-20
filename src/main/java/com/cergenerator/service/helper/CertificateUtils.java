package com.cergenerator.service.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import com.nimbusds.jose.util.X509CertUtils;

public class CertificateUtils {


	public static String getCertificateByName(String filename) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream("certificates/" + filename);
		
		if (is != null) {
			try {
				byte[] bytes = IOUtils.toByteArray(is);
				X509Certificate cert = X509CertUtils.parse(bytes);
				String encodeCert = X509CertUtils.toPEMString(cert);
				return encodeCert;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;
	}
	
	public static X509Certificate getCertificate(String filename) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream("certificates/" + filename);
		
		if (is != null) {
			try {
				byte[] bytes = IOUtils.toByteArray(is);
				X509Certificate cert = X509CertUtils.parse(bytes);
				return cert;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;
	}
	
	public static PrivateKey getKeyFromFile(String keyPath) {
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("certificates/" + keyPath);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		
		try {
			Security.addProvider(new BouncyCastleProvider());
			PEMParser pp = new PEMParser(br);
			PEMKeyPair pemKeyPair = (PEMKeyPair) pp.readObject();
			KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
			pp.close();
			return kp.getPrivate();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*public static PrivateKey convert2(String privateKey){
		
		InputStream stream = new ByteArrayInputStream(privateKey.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		
		String temp = null;
        try {
			while((temp = br.readLine()) != null){
			    System.out.println(temp);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  
		
		try {
			Security.addProvider(new BouncyCastleProvider());
			PEMParser pp = new PEMParser(br);
			System.out.println(pp.toString());
			PEMKeyPair pemKeyPair = (PEMKeyPair) pp.readObject();
			
			KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
			pp.close();
			return kp.getPrivate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	*/
	public static PrivateKey convertStr2PrivateKey(String privateKey) {
		
		Security.addProvider(new BouncyCastleProvider());
	    KeyFactory factory = null;
	    PrivateKey key = null;
	    byte[] privateKeyFileBytes = privateKey.getBytes();
		
		String KeyString = new String(privateKeyFileBytes);

	    KeyString = KeyString.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "");
	    KeyString = KeyString.replaceAll("-----END RSA PRIVATE KEY-----", "");
	    KeyString = KeyString.replaceAll("[\n\r]", "");
	    KeyString = KeyString.trim();


	    byte[] encoded = Base64.getDecoder().decode(KeyString);

	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

	    try {
	        factory = KeyFactory.getInstance("RSA");
	        key = factory.generatePrivate(keySpec);
	    } catch (NoSuchAlgorithmException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (InvalidKeySpecException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    
	    return key;
	}
}
