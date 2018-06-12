package com.cergenerator.service.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

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
}
