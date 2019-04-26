package com.sagatechs.javaeeApp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StringUtils {

	public byte[] encryptPassword(String password)  {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] pass = md.digest(password.getBytes(StandardCharsets.UTF_8));
			return pass;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		

	}

}
