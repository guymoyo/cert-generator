package com.cergenerator.service.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import com.nimbusds.jose.util.X509CertUtils;

public class CertificateUtils {

	public static X509Certificate[] getCertificates(String folderName) {

		List<X509Certificate> listCert = new ArrayList<>();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(folderName);
		if(url == null) {
			return new X509Certificate[] {};
		}
		String path = url.getPath();
		File[] files = new File(path).listFiles();

		for (File file : files) {
			
			try {
				byte[] bytesArray = FileUtils.readFileToByteArray(file);
				X509Certificate cert = X509CertUtils.parse(bytesArray);
				if (cert != null) {
					listCert.add(cert);
				}
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}

		}
		return listCert.toArray(new X509Certificate[listCert.size()]);
	}

	public static String getCertificateByName(String filename) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("classpath*:certificates/" + filename);
		String path = url.getPath();
		File file = new File(path);

		if (file.exists()) {

			try {
				byte[] bytesArray = FileUtils.readFileToByteArray(file);
				X509Certificate cert = X509CertUtils.parse(bytesArray);
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
		URL url = loader.getResource("classpath*:certificates/" + filename);
		String path = url.getPath();
		File file = new File(path);

		if (file.exists()) {

			try {
				byte[] bytesArray = FileUtils.readFileToByteArray(file);
				X509Certificate cert = X509CertUtils.parse(bytesArray);
				return cert;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;
	}
	
	public static PrivateKey getKeyFromFile(String keyPath) {
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("classpath*:certificates/" + keyPath);
		
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
